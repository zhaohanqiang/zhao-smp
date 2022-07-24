package jp.co.seamaple.controller;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.WorkSchedule;
import jp.co.seamaple.form.WorkScheduleForm;
import jp.co.seamaple.repository.WorkScheduleRepository;
import jp.co.seamaple.service.MailService;
import jp.co.seamaple.service.UserManageService;
import jp.co.seamaple.service.WorkscheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/workschedule")
public class WorkscheduleController {

    private final WorkscheduleService wsService;
    private final WorkScheduleRepository wsRep;
    private final UserManageService userManageService;
    private final SessionData session;
    private final MailService mailService;

    @Autowired
    public WorkscheduleController(WorkscheduleService wsService, WorkScheduleRepository wsRep,
                                  UserManageService userManageService, MailService mailService,
                                  SessionData session) {
        this.wsService = wsService;
        this.wsRep = wsRep;
        this.userManageService = userManageService;
        this.session = session;
        this.mailService=mailService;
    }

    // 申請画面表示
    @PostMapping(value = "/app")
    public String getworkscheduleApp(@RequestParam(name = "errorFlag", required = false) String errorFlag,
                                     @ModelAttribute WorkScheduleForm workScheduleForm, Model model) {
        // SQLチェックエラー表示
        if ("SQLCheckError".equals(errorFlag)) {
            model.addAttribute("errorMessage", "その年月は既に提出されています。");
        }
        // ファイルサイズチェックエラー表示
        if ("sizeCheckError".equals(errorFlag)) {
            model.addAttribute("errorMessage", "ファイルのサイズは10MB以下で提出してください");
        }
        // 提出ファイル拡張子エラー表示
        if ("extentionError".equals(errorFlag)) {
            model.addAttribute("errorMessage", "提出ファイルの拡張子が正しくありません。");
        }

        model.addAttribute("usersList", userManageService.getUserList());
        return "workschedule/workscheduleApp";
    }

    // 登録処理
    @PostMapping(value = "/insert")
    public String insertWsApp(
            @Validated @ModelAttribute WorkScheduleForm wsForm,
            BindingResult result) throws IOException, MaxUploadSizeExceededException {
        if (result.hasErrors()) {
            return "workschedule/workscheduleApp";
        }

        // SQL存在チェック
        if ("admin".equals(session.getRoleId())) {
            WorkSchedule ws = wsRep.getByUserIdAndMonthYear(wsForm.getUserId(),
                    wsForm.getMonthYear());
            if (ws != null) {
                return "forward:/workschedule/app?errorFlag=SQLCheckError";
            }
        } else {
            WorkSchedule ws = wsRep.getByUserIdAndMonthYear(session.getUserId(),
                    wsForm.getMonthYear());
            if (ws != null) {
                return "forward:/workschedule/app?errorFlag=SQLCheckError";
            }
        }

        // ファイルサイズチェック10MB以下
        int size = Math.toIntExact(wsForm.getFileContent().getSize());
        if (size > 10485760) {
            return "forward:/workschedule/app?errorFlag=sizeCheckError";
        }

        String fileName = StringUtils.cleanPath(wsForm.getFileContent().getOriginalFilename());
        if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png")
                || fileName.endsWith(".pdf") || fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            wsService.insertWs(wsForm.getFileContent(), wsForm);
            String subject = "勤務表提出のご連絡";
            String content = session.getUserName() + "さんが勤務表を提出しました。";
            //収信メールアドレス
            mailService.sendMail("sheneilianluo@seamaple.co.jp", subject, content);
            return "forward:/workschedule/list";
        } else {
            // 拡張子が正しくない場合
            return "forward:/workschedule/app?errorFlag=extentionError";
        }
    }

    // 一覧表示
    @PostMapping(value = "/list")
    public String getWsList(@RequestParam(name = "monthYear", required = false) String monthYear,
                            Model model) {

        List<WorkSchedule> wsLists = new ArrayList<WorkSchedule>();

        if ("admin".equals(session.getRoleId())) {
            if (monthYear != null) {
                wsLists = wsRep.getByMonthYear(monthYear);
                model.addAttribute("monthYear", monthYear);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                String now = sdf.format(new Date());
                wsLists = wsRep.getByMonthYear(now);
                model.addAttribute("monthYear", now);
            }
        } else if ("general".equals(session.getRoleId())) {
            wsLists = wsRep.getListByUserIdOrderByMonthYearDesc(session.getUserId());
        }
        model.addAttribute("wsLists", wsLists);

        return "workschedule/workscheduleList";
    }

    // ファイルダウンロード
    @PostMapping(value = "/dl/{id}")
    public void dlWsFile(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        wsService.dlFile(id, response);
    }

    // ファイル削除
    @PostMapping(value = "/delete/{id}")
    public String deleteFile(@PathVariable Integer id) throws Exception {
        wsService.deleteFile(id);
        return "forward:/workschedule/list";
    }

    // ファイルブラウザ表示
    @PostMapping(value = "/view/{id}")
    public String viewWsFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {

        // ファイルデータを取得
        Optional<WorkSchedule> result = wsRep.findById(id);

        // データ存在チェック
        if (!result.isPresent()) {
            throw new IOException("該当ファイルが見つかりませんでした。 ID=" + id);
        }
        WorkSchedule ws = result.get();

        // ファイル形式判別
        if (ws.getFileName().endsWith(".jpeg") || ws.getFileName().endsWith(".jpg")
                || ws.getFileName().endsWith(".png")) {

            response.setContentType("image/jpeg, image/jpg, image/png");
            response.getOutputStream().write(ws.getFileContent());
            response.getOutputStream().close();

        } else if (ws.getFileName().endsWith(".pdf")) {

            response.setHeader("Pragma", "");
            response.addHeader("Cache-Control", "");
            response.addHeader("Content-Disposition", "inline;");
            response.setContentType("application/pdf");
            response.getOutputStream().write(ws.getFileContent());

            OutputStream out = response.getOutputStream();
            out.flush();
        } else {

            // 上記ファイル形式に該当しない場合、DLする。
            return "forward:/workschedule/dl/" + id;
        }
        return null;
    }
}
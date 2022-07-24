package jp.co.seamaple.service;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.User;
import jp.co.seamaple.entity.WorkSchedule;
import jp.co.seamaple.form.WorkScheduleForm;
import jp.co.seamaple.repository.UserRepository;
import jp.co.seamaple.repository.WorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class WorkscheduleService {

    public final WorkScheduleRepository wsRep;
    public final UserRepository userRep;
    public final SessionData session;

    @Autowired
    public WorkscheduleService(WorkScheduleRepository wsRep, UserRepository userRep, SessionData session) {
        this.wsRep = wsRep;
        this.userRep = userRep;
        this.session = session;
    }

    /**
     * 勤務表データ登録処理
     *
     * @param multipartFile
     * @param wsForm
     * @return
     * @throws IOException
     */
    @Transactional
    public WorkSchedule insertWs(MultipartFile multipartFile, WorkScheduleForm wsForm)
            throws IOException, MaxUploadSizeExceededException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        WorkSchedule ws = new WorkSchedule();

        // 管理者の場合、選択された社員IDの挿入
        if ("admin".equals(session.getRoleId())) {
            ws.setUserId(wsForm.getUserId());
            User user = userRep.getById(wsForm.getUserId());
            ws.setUserName(user.getUserName());
        } else {
            ws.setUserId(session.getUserId());
            ws.setUserName(session.getUserName());
        }

        ws.setWorkHour(wsForm.getWorkHour());
        ws.setFileName(fileName);
        ws.setFileContent(multipartFile.getBytes());
        ws.setFileSize(this.getByteSize(multipartFile.getSize()));
        ws.setMonthYear(wsForm.getMonthYear());
        ws.setWeekendsWork(wsForm.getWeekendsWork());
        ws.setMemo(wsForm.getMemo());
        ws.setRegisteredDate(new Date());
        return wsRep.save(ws);
    }

    /**
     * 勤務表データダウンロード処理
     *
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @Transactional
    public WorkSchedule dlFile(Integer id, HttpServletResponse response) throws Exception {

        Optional<WorkSchedule> result = wsRep.findById(id);
        if (!result.isPresent()) {
            throw new Exception("該当ファイルが見つかりませんでした。 ID=" + id);
        }
        WorkSchedule ws = result.get();

        // 日本語文字化け対策
        final String CONTENT_DISPOSITION_FORMAT = "attachment; filename=\"%s\"; filename*=UTF-8''%s";
        String fileName = ws.getFileName();
        String headerValue = String.format(CONTENT_DISPOSITION_FORMAT,
                fileName,
                UriUtils.encode(fileName, StandardCharsets.UTF_8.name()));
        response.setHeader("Content-Disposition", "attachment; filename=" + headerValue);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(ws.getFileContent());
        outputStream.close();
        return ws;
    }

    /**
     * 勤務表ファイル削除 管理者用
     *
     * @param id
     */
    @Transactional
    public void deleteFile(Integer id) {
        WorkSchedule deleteWs = wsRep.getById(id);
        wsRep.delete(deleteWs);
    }

    /**
     * 勤務表データサイズ表示バイト設定
     *
     * @param size
     * @return
     */
    public String getByteSize(long size) {
        // 1024， B ， 1024， 3
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 1024 ， 1024， KB
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // MB ， 1 ，
            // ， 100
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            // GB ， 1024
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

}

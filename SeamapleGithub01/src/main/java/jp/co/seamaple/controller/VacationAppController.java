package jp.co.seamaple.controller;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.VacationAppType;
import jp.co.seamaple.entity.VacationDays;
import jp.co.seamaple.form.VacationappForm;
import jp.co.seamaple.service.MailService;
import jp.co.seamaple.service.VacationAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class VacationAppController {

    private final VacationAppService vacationAppService;
    private final SessionData sessionData;
    private final MailService mailService;

    @Autowired
    public VacationAppController(VacationAppService vacationAppService,MailService mailService, SessionData sessionData) {
        this.vacationAppService = vacationAppService;
        this.mailService=mailService;
        this.sessionData = sessionData;
    }

    // 休暇申請内容入力画面
    @PostMapping(value = "/vacationApp")
    public String getVacationAppPage(@RequestParam(name = "errorFlag", required = false) String errorFlag,
                                     @ModelAttribute VacationappForm vacationappForm, Model model) {

        VacationDays vacationDays = this.vacationAppService.getVacationDays(sessionData.getUserId());
        Date today = new Date();
        String errorMessage = "errorMessage";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 有給計算
        if ("general".equals(sessionData.getRoleId()) && today.after(vacationDays.getPaidVacationAddDate())) {
            vacationAppService.caliculatePaidVacation(sessionData.getUserId());
        }

        // 有給表示
        int allPaidVacation = (vacationDays.getLastPaidVacation() + vacationDays.getPreviousPaidVacation());
        model.addAttribute("allPaidVacation", allPaidVacation);
        model.addAttribute("summerVacation", vacationDays.getSummerVacation());

        if (vacationDays.getPaidVacationAddCount() > 0) {
            model.addAttribute("lostVacationNum", vacationDays.getLastPaidVacation());
            model.addAttribute("lostVacationDate", sdf.format(vacationAppService.getLostLastVacationDate()));
        } else if (vacationDays.getPaidVacationAddCount() > 1) {
            model.addAttribute("lostVacationNum", vacationDays.getPreviousPaidVacation());
            model.addAttribute("lostVacationDate", sdf.format(vacationDays.getPaidVacationAddDate()));
        }

        // 休暇申請種別DB検索
        List<VacationAppType> vacationAppType = vacationAppService.findAll();
        model.addAttribute("vacationAppTypes", vacationAppType);

        // 休暇日数チェックエラー表示
        if ("numError".equals(errorFlag)) {
            model.addAttribute(errorMessage, "該当の休暇残数がありません");
        }
        // 休暇期間チェックエラー表示
        if ("dateError".equals(errorFlag)) {
            model.addAttribute(errorMessage, "該当の休暇期間内ではありません");
        }

        return "vacationapp/vacationApp";
    }

    // 休暇申請内容登録
    @PostMapping("/vacationApp/insert")
    public String regVacationApp(@Validated @ModelAttribute VacationappForm vacationappForm, BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            VacationDays vacationDays = this.vacationAppService.getVacationDays(sessionData.getUserId());
            int allPaidVacation = (vacationDays.getLastPaidVacation() + vacationDays.getPreviousPaidVacation());
            model.addAttribute("allPaidVacation", allPaidVacation);

            // 夏季休暇表示
            model.addAttribute("summerVacation", vacationDays.getSummerVacation());

            // 休暇申請種別DB検索
            List<VacationAppType> vacationAppType = vacationAppService.findAll();
            model.addAttribute("vacationAppTypes", vacationAppType);
            return "vacationapp/vacationApp";
        }

        VacationDays vacationDays = vacationAppService.getVacationDays(sessionData.getUserId());

        if ("有給休暇".equals(vacationappForm.getVacationAppType())) {

            // 有給残数チェック
            if (vacationDays.getLastPaidVacation() + vacationDays.getPreviousPaidVacation() == 0) {
                return "forward:/vacationApp?errorFlag=numError";
            }

            // 失効日チェック
            // 前回の有給の失効日取得
            Date lostPreviousVacationDate = vacationDays.getPaidVacationAddDate();
            // 最近の有給の失効日取得(前回有給失効日 + 1年)
            Date lostLastVacationDate = vacationAppService.getLostLastVacationDate();

            // 最近の有給のみ有で最近の有給失効日より申請日が後の場合エラー
            if (vacationDays.getPreviousPaidVacation() == 0
                    && vacationappForm.getVacationappDate().after(lostLastVacationDate)) {
                return "forward:/vacationApp?errorFlag=dateError";
                // 前回の有給のみ有で前回の失効日より申請日が後の場合エラー
            } else if (vacationDays.getLastPaidVacation() == 0
                    && vacationappForm.getVacationappDate().after(lostPreviousVacationDate)) {
                return "forward:/vacationApp?errorFlag=dateError";
                // 最近・前回どちらの有給も有で最近の失効日より申請日が後の場合エラー
            } else if (vacationDays.getLastPaidVacation() != 0
                    && vacationappForm.getVacationappDate().after(lostLastVacationDate))
                return "forward:/vacationApp?errorFlag=dateError";
                // エラーでない場合保存
            else {
                this.vacationAppService.saveVacationAppForm(vacationappForm);
            }
        } else if ("夏季休暇".equals(vacationappForm.getVacationAppType())) {

            // 夏季休暇残数チェック
            if (vacationDays.getSummerVacation() == 0) {
                return "forward:/vacationApp?errorFlag=numError";
            }

            // 夏季休暇期間チェック(7/1-9/30)
            if (vacationappForm.getVacationappDate().after(vacationAppService.getStartSummerVacationDate())
                    && vacationappForm.getVacationappDate().before(vacationAppService.getFinalSummerVacationDate())) {
                this.vacationAppService.saveVacationAppForm(vacationappForm);
            } else {
                return "forward:/vacationApp?errorFlag=dateError";
            }

        } else {
            // 他の休暇種別
            this.vacationAppService.saveVacationAppForm(vacationappForm);
        }
        String subject = "有給休暇申請のご連絡";
        String content = sessionData.getUserName() + "さんが有給休暇を申請しました。";
        //収信メールアドレス
        mailService.sendMail("sheneilianluo@seamaple.co.jp", subject, content);

        return "forward:/vacationAppList";
    }

}
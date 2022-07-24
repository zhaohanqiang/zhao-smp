package jp.co.seamaple.controller;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.VacationApp;
import jp.co.seamaple.entity.VacationDays;
import jp.co.seamaple.form.SearchForm;
import jp.co.seamaple.form.VacationAppApprovalForm;
import jp.co.seamaple.service.VacationAppListService;
import jp.co.seamaple.service.VacationAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 休憩申請一覧情報 Controller
 */
@Controller
public class VacationAppListController {

    private final VacationAppListService vacationAppListService;
    private final VacationAppService vacationAppService;
    private final SessionData sessionData;

    @Autowired
    public VacationAppListController(VacationAppListService vacationAppListService,
                                     VacationAppService vacationAppService, SessionData sessionData) {
        this.vacationAppListService = vacationAppListService;
        this.vacationAppService = vacationAppService;
        this.sessionData = sessionData;
    }

    // 空文字をnull変換
    @InitBinder
    public void initbinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * 休暇申請一覧画面を表示
     */
    @PostMapping(value = "/vacationAppList")
    public String searchApp(@ModelAttribute SearchForm searchForm, HttpSession session, Model model) {

        VacationDays vacationDays = this.vacationAppService.getVacationDays(sessionData.getUserId());
        Date today = new Date();

        // 夏季休暇計算
        if ("admin".equals(sessionData.getRoleId()) && today.after(vacationDays.getSummerVacationAddDate())) {
            this.vacationAppListService.caliculateSummerVacation(sessionData.getUserId());
            this.vacationAppListService.setSummerVacationAddDate();
        }

        // 夏季休暇削除
        if ("admin".equals(sessionData.getRoleId()) && today.after(vacationAppService.getFinalSummerVacationDate())) {
            this.vacationAppListService.deleteSummerVacation();
        }

        // 検索表示＆初期全件検索表示
        List<VacationApp> vp = vacationAppListService.getVpList(searchForm);
        model.addAttribute("vacationAppList", vp);

        // 更新後の初期値に検索した申請日をセット
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (searchForm.getSearchStartDate() != null) {
            String formatedSDate = sdf.format(searchForm.getSearchStartDate());
            model.addAttribute("searchStartDate", formatedSDate);
        }
        if (searchForm.getSearchEndDate() != null) {
            String formatedEndDate = sdf.format(searchForm.getSearchEndDate());
            model.addAttribute("searchEndDate", formatedEndDate);
        }
        // 更新後の初期値に検索したステータスをセット
        model.addAttribute("statuses", vacationAppListService.getAppStatus());

        return "vacationapp/vacationAppList";
    }

    // 承認ステータス更新
    @PostMapping(value = "/vacationAppList/approval")
    public String updateStatus(@RequestParam(name = "paidVacationType", required = false) String updateFlag,
                               VacationAppApprovalForm vacationAppApprovalForm) {
        vacationAppListService.updateStatus(updateFlag, vacationAppApprovalForm);

        return "forward:/vacationAppList";
    }

    @PostMapping(value = "/vacationAppList/approvalAll")
    public String updateStatusAll() {
        vacationAppListService.updateStatusAll();
        return "forward:/vacationAppList";
    }
}

package jp.co.seamaple.service;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.VacationApp;
import jp.co.seamaple.entity.VacationDays;
import jp.co.seamaple.form.SearchForm;
import jp.co.seamaple.form.VacationAppApprovalForm;
import jp.co.seamaple.repository.VacationAppRepository;
import jp.co.seamaple.repository.VacationDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;

@Service
public class VacationAppListService {

    public final VacationAppRepository vacationAppRepository;
    public final VacationDaysRepository vacationDaysRepository;
    public final VacationAppService vacationAppService;
    public final SessionData sessionData;

    @Autowired
    public VacationAppListService(VacationAppRepository vacationAppRepository,
                                  VacationDaysRepository vacationDaysRepository, VacationAppService vacationAppService,
                                  SessionData sessionData) {
        this.vacationAppRepository = vacationAppRepository;
        this.vacationDaysRepository = vacationDaysRepository;
        this.vacationAppService = vacationAppService;
        this.sessionData = sessionData;
    }

    /**
     * 有給検索一覧
     *
     * @param sForm
     * @return
     */
    public List<VacationApp> getVpList(SearchForm sForm) {
        List<VacationApp> vpList = new ArrayList<VacationApp>();
        // 管理者用 全件検索・表示
        if ("admin".equals(sessionData.getRoleId())) {
            vpList = vacationAppRepository.getByUserIdAndUserNameAndstartDateAndEndDateAndAppStatus(
                    sForm.getSearchUserId(),
                    sForm.getSearchUserName(), sForm.getSearchStartDate(), sForm.getSearchEndDate(),
                    sForm.getSearchAppStatus());
            // 社員用 ログインユーザー情報のみ検索・表示
        } else {
            vpList = vacationAppRepository.getByUserIdAndUserNameAndstartDateAndEndDateAndAppStatus(
                    sessionData.getUserId(),
                    sForm.getSearchUserName(), sForm.getSearchStartDate(), sForm.getSearchEndDate(),
                    sForm.getSearchAppStatus());
        }
        return vpList;
    }

    /**
     * ステータス更新
     *
     * @param updateFlag
     * @param vacationAppApprovalForm
     * @return
     */
    @Transactional
    public VacationApp updateStatus(String updateFlag,
                                    @ModelAttribute("VacationAppApprovalForm") VacationAppApprovalForm vacationAppApprovalForm) {
        VacationApp vacationApp = vacationAppRepository.getById(vacationAppApprovalForm.getVacationappNo());
        vacationApp.setAppStatus(vacationAppApprovalForm.getAppStatus());
        vacationApp.setUpdateDate(new Date()); // 更新日
        vacationApp.setApproverName(sessionData.getUserName());
        VacationDays vacationDays = vacationDaysRepository.getById(vacationApp.getUserId());

        // 有給否認時、+1戻し
        if ("dismiss".equals(updateFlag) && "有給休暇".equals(vacationApp.getVacationAppType())) {
            if (vacationApp.getPaidVacationType() == 0) {
                vacationDays.setLastPaidVacation(vacationDays.getLastPaidVacation() + 1);
            } else if (vacationApp.getPaidVacationType() == 1) {
                vacationDays.setPreviousPaidVacation(vacationDays.getPreviousPaidVacation() + 1);
            }
        }

        // 夏季休暇否認時、+1戻し
        if ("dismiss".equals(updateFlag) && "夏季休暇".equals(vacationApp.getVacationAppType())) {
            vacationDays.setSummerVacation(vacationDays.getSummerVacation() + 1);
        }
        return vacationAppRepository.save(vacationApp);
    }

    /**
     * 有給一括承認
     */
    @Transactional
    public void updateStatusAll() {
        List<VacationApp> unapprovalList = vacationAppRepository.findALLByAppStatus(0);
        for (VacationApp list : unapprovalList) {
            list.setAppStatus(1);
            list.setApproverName(sessionData.getUserName());
            list.setUpdateDate(new Date());
        }
        vacationAppRepository.saveAll(unapprovalList);
    }

    /**
     * 承認ステータスドロップダウン
     */
    public Map<Integer, String> getAppStatus() {
        Map<Integer, String> statuses = new LinkedHashMap<Integer, String>(); // 格納された順に出力
        statuses.put(0, "申請中");
        statuses.put(1, "承認");
        statuses.put(2, "否認");

        return statuses;
    }

    /**
     * 夏季休暇付与日計算
     *
     * @return 夏季休暇付与日
     */
    @Transactional
    public VacationDays caliculateSummerVacation(String userId) {

        VacationDays vacationDays = vacationAppService.getVacationDays(userId);
        Date summerVacationAddDate = vacationDays.getSummerVacationAddDate();

        // 夏季休暇付与日を来年にセット
        Calendar calendarSummerVacation = Calendar.getInstance();
        calendarSummerVacation.setTime(summerVacationAddDate);
        calendarSummerVacation.add(Calendar.YEAR, 1);
        Date convertedSummerVacationAddDate = calendarSummerVacation.getTime();
        vacationDays.setSummerVacationAddDate(convertedSummerVacationAddDate);

        return this.vacationDaysRepository.save(vacationDays);
    }

    /**
     * 夏季休暇付与
     *
     * @param userId
     * @return 夏季休暇付与
     */
    @Transactional
    public List<VacationDays> setSummerVacationAddDate() {
        // 全ユーザー夏季休暇2日セット
        List<VacationDays> allUserVacationDays = this.vacationDaysRepository.findAll();
        int i = 0;
        while (i < allUserVacationDays.size()) {
            allUserVacationDays.get(i).setSummerVacation(2);
            i++;
        }
        return this.vacationDaysRepository.saveAll(allUserVacationDays);
    }

    /**
     * 夏季休暇失効
     *
     * @return 夏季休暇失効
     */
    @Transactional
    public List<VacationDays> deleteSummerVacation() {
        List<VacationDays> allUserVacationDays = this.vacationDaysRepository.findAll();
        int i = 0;
        while (i < allUserVacationDays.size()) {
            allUserVacationDays.get(i).setSummerVacation(0);
            i++;
        }
        return this.vacationDaysRepository.saveAll(allUserVacationDays);
    }

}

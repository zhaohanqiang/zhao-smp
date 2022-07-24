package jp.co.seamaple.service;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.VacationApp;
import jp.co.seamaple.entity.VacationAppType;
import jp.co.seamaple.entity.VacationDays;
import jp.co.seamaple.form.VacationappForm;
import jp.co.seamaple.repository.UserRepository;
import jp.co.seamaple.repository.VacationAppRepository;
import jp.co.seamaple.repository.VacationAppTypeRepository;
import jp.co.seamaple.repository.VacationDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 休憩申請情報 Service
 */
@Service
public class VacationAppService {

    public final VacationAppRepository vacationAppRepository;
    public final VacationAppTypeRepository vacationAppTypeRepository;
    public final UserRepository userRepository;
    public final VacationDaysRepository vacationDaysRepository;
    public final SessionData sessionData;

    @Autowired
    public VacationAppService(VacationAppRepository vacationAppRepository,
                              VacationAppTypeRepository vacationAppTypeRepository, UserRepository userRepository,
                              VacationDaysRepository vacationDaysRepository, SessionData sessionData) {
        this.vacationAppRepository = vacationAppRepository;
        this.vacationAppTypeRepository = vacationAppTypeRepository;
        this.userRepository = userRepository;
        this.vacationDaysRepository = vacationDaysRepository;
        this.sessionData = sessionData;
    }

    /**
     * 休暇申請情報 全検索
     *
     * @return 検索結果
     */
    public List<VacationApp> searchAll() {
        return vacationAppRepository.findAll();
    }

    /**
     * 休暇申請種別情報 取得
     *
     * @return 休暇種別結果
     */
    public List<VacationAppType> findAll() {
        return vacationAppTypeRepository.findAll();
    }

    /**
     * 休暇日数情報取得
     *
     * @return 休暇日数情報取得
     */
    public VacationDays getVacationDays(String userId) {
        return vacationDaysRepository.getById(userId);
    }

    /**
     * 勤続年数による有給数 取得
     *
     * @return 勤続年数による有給数結果
     */
    public Map<Integer, Integer> createLengthOfServiceMap() {
        Map<Integer, Integer> lengthOfServiceMap = new HashMap<>();
        lengthOfServiceMap.put(0, 10); // 勤続年数 0.5年
        lengthOfServiceMap.put(1, 11); // 勤続年数 1.5年
        lengthOfServiceMap.put(2, 12); // 勤続年数 2.5年
        lengthOfServiceMap.put(3, 14); // 勤続年数 3.5年
        lengthOfServiceMap.put(4, 16); // 勤続年数 4.5年
        lengthOfServiceMap.put(5, 18); // 勤続年数 5.5年
        lengthOfServiceMap.put(6, 20); // 勤続年数 6.5年以上
        return lengthOfServiceMap;
    }

    /**
     * 有給付与計算
     *
     * @return 有給付与＆失効計算結果
     */
    @Transactional
    public VacationDays caliculatePaidVacation(String userId) {

        VacationDays vacationDays = this.getVacationDays(userId);
        int paidVacationAddCount = vacationDays.getPaidVacationAddCount();
        Map<Integer, Integer> lengthOfServiceMap = this.createLengthOfServiceMap();

        // 現状の新しい有給データを古い有給データにセット
        vacationDays.setPreviousPaidVacation(vacationDays.getLastPaidVacation());

        // 今回の有給を新しい有給データにセット
        // 付与回数7回以上の場合全て、有給日数20日を付与
        if (paidVacationAddCount > 6) {
            vacationDays.setLastPaidVacation(lengthOfServiceMap.get(6));
            // それ以外は付与回数に応じた有給日数を付与
        } else {
            vacationDays.setLastPaidVacation(lengthOfServiceMap.get(paidVacationAddCount));
        }

        // 有給付与日を+1年
        Calendar calendarPaidAddDate = Calendar.getInstance();
        calendarPaidAddDate.setTime(vacationDays.getPaidVacationAddDate());
        calendarPaidAddDate.add(Calendar.YEAR, 1);
        Date convertedPaidVacationAddDate = calendarPaidAddDate.getTime();
        vacationDays.setPaidVacationAddDate(convertedPaidVacationAddDate);

        // 有給付与回数を+1回
        vacationDays.setPaidVacationAddCount(vacationDays.getPaidVacationAddCount() + 1);

        return this.vacationDaysRepository.save(vacationDays);
    }

    /**
     * 最新有給失効日取得
     *
     * @return 最新有給失効日
     */
    public Date getLostLastVacationDate() {
        VacationDays vacationDays = this.getVacationDays(sessionData.getUserId());
        Calendar calendarLostPreviousDay = Calendar.getInstance();
        calendarLostPreviousDay.setTime(vacationDays.getPaidVacationAddDate());
        calendarLostPreviousDay.add(Calendar.YEAR, 1);
        return calendarLostPreviousDay.getTime();
    }

    /**
     * 夏季休暇開始日取得
     *
     * @return 夏季休暇開始日
     */
    public Date getStartSummerVacationDate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.MONTH, 5); // 月は0からカウント
        startDate.set(Calendar.DATE, 30);
        return startDate.getTime();
    }

    /**
     * 夏季休暇最終日取得
     *
     * @return 夏季休暇最終日
     */
    public Date getFinalSummerVacationDate() {
        Calendar finalDate = Calendar.getInstance();
        finalDate.set(Calendar.MONTH, 8); // 月は0からカウント
        finalDate.set(Calendar.DATE, 30);
        return finalDate.getTime();
    }

    /**
     * 休暇申請情報フォーム 送信
     *
     * @return 休暇申請結果
     */
    @Transactional
    public VacationApp saveVacationAppForm(VacationappForm vacationappForm) {
        VacationDays vacationDays = this.getVacationDays(sessionData.getUserId());
        VacationApp vacationApp = new VacationApp();
        Date vacationAppDate = vacationappForm.getVacationappDate();
        String vacationType = vacationappForm.getVacationAppType();

        // 有給休暇処理
        if ("有給休暇".equals(vacationType)) {
            int lastPaidVacation = vacationDays.getLastPaidVacation();
            int previousPaidVacation = vacationDays.getPreviousPaidVacation();

            // 失効日チェック
            Date lostPreviousVacationDate = vacationDays.getPaidVacationAddDate();

            // 有給計算マイナス処理&有給休暇タイプセット
            if (previousPaidVacation != 0 && vacationAppDate.before(lostPreviousVacationDate)) {
                vacationDays.setPreviousPaidVacation(previousPaidVacation - 1);
                vacationApp.setPaidVacationType(1); // 最新の有給タイプにセット
            } else {
                vacationDays.setLastPaidVacation(lastPaidVacation - 1);
                vacationApp.setPaidVacationType(0); // 前回の有給タイプにセット
            }
        }

        // 夏季休暇処理
        if ("夏季休暇".equals(vacationType)) {

            // 夏季休暇マイナス処理
            vacationDays.setSummerVacation(vacationDays.getSummerVacation() - 1);
        }

        // 申請フォーム情報セット
        vacationApp.setVacationAppType(vacationappForm.getVacationAppType());
        vacationApp.setVacationappDate(vacationappForm.getVacationappDate());
        vacationApp.setVacationappComment(vacationappForm.getVacationappComment());
        vacationApp.setAppStatus(0);
        vacationApp.setCreateDate(new Date());
        vacationApp.setUserId(sessionData.getUserId());
        vacationApp.setUserName(sessionData.getUserName());

        return this.vacationAppRepository.save(vacationApp);
    }
}

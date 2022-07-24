package jp.co.seamaple.service;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.TransportCost;
import jp.co.seamaple.form.TransportCostListForm;
import jp.co.seamaple.repository.TransportCostRepository;
import jp.co.seamaple.repository.TransportCostRepository.TotalCostList;
import jp.co.seamaple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransportCostService {

    public final SessionData sessionData;
    public final TransportCostRepository transportCostRepository;
    public final UserRepository userRepository;

    @Autowired
    public TransportCostService(SessionData sessionData, TransportCostRepository transportCostRepository,
                                UserRepository userRepository) {
        this.sessionData = sessionData;
        this.transportCostRepository = transportCostRepository;
        this.userRepository = userRepository;
    }

    /**
     * 交通費申請情報確定
     *
     * @param transportCostListForm
     */
    @Transactional
    public void insertFeeList(TransportCostListForm transportCostListForm) {

        transportCostRepository.deleteAll(getListByDateAndUserId(transportCostListForm));
        for (int i = 0; i < transportCostListForm.getCost().length; i++) {

            TransportCost transportCost = new TransportCost();
            transportCost.setUserId(sessionData.getUserId());
            transportCost.setUserName(sessionData.getUserName());
            transportCost.setType(transportCostListForm.getType()[i]);
            transportCost.setMonthYear(transportCostListForm.getMonthYear()[i]);
            transportCost.setDay(transportCostListForm.getDay()[i]);
            transportCost.setDeparture(transportCostListForm.getDeparture()[i]);
            transportCost.setArrival(transportCostListForm.getArrival()[i]);
            transportCost.setCost(transportCostListForm.getCost()[i]);
            transportCost.setReason(transportCostListForm.getReason()[i]);
            transportCost.setRegTime(new Timestamp(System.currentTimeMillis()));
            transportCost.setDisabled(true);
            transportCostRepository.save(transportCost);
        }
    }

    /**
     * 交通費一時保存
     *
     * @param transportCostListForm
     */
    @Transactional
    public void inserttmpList(TransportCostListForm transportCostListForm) {

        transportCostRepository.deleteAll(getListByDateAndUserId(transportCostListForm));

        for (int i = 0; i < transportCostListForm.getCost().length; i++) {

            TransportCost transportCost = new TransportCost();
            transportCost.setUserId(sessionData.getUserId());
            transportCost.setUserName(sessionData.getUserName());
            transportCost.setType(transportCostListForm.getType()[i]);
            transportCost.setMonthYear(transportCostListForm.getMonthYear()[i]);
            transportCost.setDay(transportCostListForm.getDay()[i]);
            transportCost.setDeparture(transportCostListForm.getDeparture()[i]);
            transportCost.setArrival(transportCostListForm.getArrival()[i]);
            transportCost.setCost(transportCostListForm.getCost()[i]);
            transportCost.setReason(transportCostListForm.getReason()[i]);
            transportCost.setRegTime(new Timestamp(System.currentTimeMillis()));
            transportCostRepository.save(transportCost);
        }
    }

    /**
     * 交通費在宅時申請
     *
     * @param monthYear
     */
    @Transactional
    public void insertFeeAtHome(String monthYear) {

        transportCostRepository.deleteAll(getListByDateAndUserIdAtHome(monthYear, sessionData.getUserId()));

        TransportCost transportCost = new TransportCost();
        transportCost.setUserId(sessionData.getUserId());
        transportCost.setUserName(sessionData.getUserName());
        transportCost.setType(3);
        transportCost.setMonthYear(monthYear);
        transportCost.setDay(null);
        transportCost.setDeparture(null);
        transportCost.setArrival(null);
        transportCost.setCost("0");
        transportCost.setReason("在宅勤務申請済");
        transportCost.setRegTime(new Timestamp(System.currentTimeMillis()));
        transportCost.setDisabled(true);
        transportCostRepository.save(transportCost);
    }

    // 年月検索データ取得
    public List<TotalCostList> getTotalCostList(String monthYear) {
        return transportCostRepository.getByDate(monthYear);
    }

    
    // 年検索データ取得
    public List<TotalCostList> getTotalCostList1(String monthYear) {

        return transportCostRepository.findByMonthYearLike(monthYear);
    }



    // 全削除データ取得
    public List<TransportCost> getListByDateAndUserId(TransportCostListForm transportCostListForm) {
        return transportCostRepository.getByDateAndUserId(transportCostListForm.getMonthYear()[0],
                sessionData.getUserId());
    }

    // 全削除データ取得在宅用
    public List<TransportCost> getListByDateAndUserIdAtHome(String monthYear, String userId) {
        return transportCostRepository.getByDateAndUserId(monthYear, userId);
    }

    /**
     * 交通費種類ドロップダウン
     */
    public Map<Integer, String> getTypes() {
        Map<Integer, String> types = new LinkedHashMap<Integer, String>();
        types.put(0, "定期");
        types.put(1, "片道");
        types.put(2, "往復");
        types.put(3, "在宅");
        return types;
    }

    /**
     * 交通費種類ドロップダウン
     */
    public Map<Integer, String> getDays() {
        Map<Integer, String> days = new LinkedHashMap<Integer, String>();

        for (int i = 1; i < 32; i++) {
            days.put(i, i + "日");
        }
        return days;
    }

    /**
     * 交通費確定ステータス解除
     *
     * @param monthYear
     * @param userId
     */
    @Transactional
    public void updateDisabledStatus(String monthYear, String userId) {
        List<TransportCost> costList = transportCostRepository.getByDateAndUserId(monthYear, userId);

        // 在宅解除の場合、在宅のチェックを外す。金額理由に空文字挿入
        if (costList.get(0).getType() == 3) {
            costList.get(0).setType(0);
            costList.get(0).setCost("");
            costList.get(0).setReason("");
        }

        for (int i = 0; i < costList.size(); i++) {
            costList.get(i).setDisabled(null);
            transportCostRepository.saveAll(costList);
        }
    }
}

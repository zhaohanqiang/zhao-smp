package jp.co.seamaple.repository;

import jp.co.seamaple.entity.VacationApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 休憩申請情報 Repository
 */
@Repository
public interface VacationAppRepository extends JpaRepository<VacationApp, Integer> {

    // 検索リスト
    @Query(value = "SELECT * FROM m_vacationapp WHERE (:id IS NULL OR user_id = :id)"
            + "AND (:name IS NULL OR user_name LIKE :name%)"
            + "AND (:startDate IS NULL OR vacationapp_date >= :startDate)"
            + "AND (:endDate IS NULL OR vacationapp_date <= :endDate)"
            + "AND (:status IS NULL OR app_status = :status)"
            + "ORDER BY create_date DESC, app_status ASC", nativeQuery = true)
    public List<VacationApp> getByUserIdAndUserNameAndstartDateAndEndDateAndAppStatus(@Param("id") String userId,
                                                                                      @Param("name") String userName, @Param("startDate") Date startDate,
                                                                                      @Param("endDate") Date endDate, @Param("status") Integer status);

    // 有給ステータス"申請中"全データ取得
    public List<VacationApp> findALLByAppStatus(int appStatus);
}
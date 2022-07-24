package jp.co.seamaple.repository;

import jp.co.seamaple.entity.TransportCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportCostRepository extends JpaRepository<TransportCost, Integer> {

    // 月別ユーザー一覧
    @Query(value = "SELECT t.user_id as userId, t.user_name as userName, t.month_year as monthYear, SUM(t.cost) as cost"
            + " FROM m_transportcost t INNER JOIN m_user u"
            + " WHERE month_year = ?1 AND t.disabled = 1 AND t.user_id = u.user_id AND u.role_id != 'admin'"
            + " GROUP BY (t.user_id) ORDER BY u.user_id ASC", nativeQuery = true)
    List<TotalCostList> getByDate(String monthYear);

    // 年別ユーザー一覧

    @Query(value = "SELECT t.user_id as userId, t.user_name as userName, t.month_year as monthYear,  cost"
            + " FROM m_transportcost t INNER JOIN m_user u"
            + " WHERE t.month_year like %?1%  AND t.disabled = 1 AND t.user_id = u.user_id  AND u.role_id  != 'admin'"
            + " ORDER BY u.user_id", nativeQuery = true)
    List<TotalCostList> findByMonthYearLike(String monthYear);

    // 申請交通費表示
    @Query(value = "SELECT * FROM m_transportcost WHERE month_year = ?1 AND user_id = ?2", nativeQuery = true)
    List<TransportCost> getByDateAndUserId(String monthYear, String userId);

    // 交通費 月の合計金額
    @Query(value = "SELECT SUM(cost) FROM m_transportcost WHERE month_year = ?1 AND user_id = ?2", nativeQuery = true)
    Integer getTotalByDateAndUserId(String monthYear, String userId);

    // 合計金額リスト
    public static interface TotalCostList {
        public String getUserId();

        public String getUserName();

        public String getMonthYear();

        public Integer getCost();
    }
}

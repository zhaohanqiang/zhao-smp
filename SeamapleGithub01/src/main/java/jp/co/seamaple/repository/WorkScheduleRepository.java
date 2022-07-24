package jp.co.seamaple.repository;

import jp.co.seamaple.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {

    @Query(value = "SELECT * FROM m_work_schedule WHERE month_year = ?1 ORDER BY user_id ASC", nativeQuery = true)
    List<WorkSchedule> getByMonthYear(String monthYear);

    List<WorkSchedule> getListByUserIdOrderByMonthYearDesc(String userId);

    WorkSchedule getByUserIdAndMonthYear(String userId, String monthYear);
}
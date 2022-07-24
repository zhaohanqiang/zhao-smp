package jp.co.seamaple.repository;

import jp.co.seamaple.entity.VacationDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationDaysRepository extends JpaRepository<VacationDays, String> {

}

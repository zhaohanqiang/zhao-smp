package jp.co.seamaple.repository;

import jp.co.seamaple.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

        @Query(value = "SELECT * FROM m_user u JOIN m_vacation_days v ON v.user_id = u.user_id JOIN t_user_role r ON r.user_id = u.user_id"
                        + " WHERE r.role_id != 'admin' ORDER BY u.user_id", nativeQuery = true)
        public List<User> getUserList();

        @Query(value = "SELECT u.user_id FROM m_user u JOIN m_vacation_days v ON v.user_id = u.user_id JOIN t_user_role r ON r.user_id = u.user_id"
                        + " WHERE r.role_id != 'admin' ORDER BY u.user_id", nativeQuery = true)
        public List getUserId();

}

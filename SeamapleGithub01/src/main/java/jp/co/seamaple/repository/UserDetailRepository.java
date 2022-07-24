package jp.co.seamaple.repository;

import jp.co.seamaple.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, String> {

    @Query(value = "select * from m_usermore  where um_id = ?1", nativeQuery = true)
    public UserDetail findByUserid(String userId);

    @Transactional
    @Modifying
    @Query(value = "delete from m_usermore  where um_id  = ?1", nativeQuery = true)

    public void umdelete(String userId);

    @Query(value = "select um_sex from m_usermore  where um_id = ?1", nativeQuery = true)
    String findUserSex(String userId);

    @Query(value = "SELECT um_id FROM m_usermore ", nativeQuery = true)
    List getUserList();

    @Query(value = "SELECT um_id FROM m_usermore   where um_id = ?1", nativeQuery = true)
    List getUserIdList(String userId);
}

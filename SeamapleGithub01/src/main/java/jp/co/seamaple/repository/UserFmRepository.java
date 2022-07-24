package jp.co.seamaple.repository;

import jp.co.seamaple.entity.UserFm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserFmRepository extends JpaRepository<UserFm, String> {

    // 既存の扶養家族を削除する

    @Transactional
    @Modifying
    @Query(value = "delete from m_userfm  where fm_status=1 and user_fmid = ?1", nativeQuery = true)

    public void fmdelete(String userId);

    @Transactional
    @Modifying
    @Query(value = "delete from m_userfm  where fm_status=0 and user_fmid = ?1", nativeQuery = true)

    public void fydelete(String userId);

    @Query(value = "select * from m_userfm  where fm_status=0 and user_fmid = ?1", nativeQuery = true)
    public UserFm findfy(String userId);

    @Query(value = "select * from m_userfm  where fm_status=1 and user_fmid = ?1", nativeQuery = true)
    public UserFm findkk(String userId);

    @Query(value = "SELECT * FROM m_userfm WHERE user_fmid = ?1 AND fm_status=0", nativeQuery = true)
    List<UserFm> getfyList(String userId);

}

package jp.co.seamaple.service;

import jp.co.seamaple.entity.*;
import jp.co.seamaple.form.UserDetailForm;
import jp.co.seamaple.form.UserFmForm;
import jp.co.seamaple.repository.UserDetailRepository;
import jp.co.seamaple.repository.UserFmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    public final UserDetailRepository userDetailRepository;
    public final UserFmRepository userFmRepository;


    public SessionData sessionData;


    @Autowired
    public UserDetailService(UserDetailRepository userDetailRepository,
     UserFmRepository userFmRepository , SessionData sessionData
  ) {
        this.userDetailRepository = userDetailRepository;
        this.sessionData = sessionData;
        this.userFmRepository=userFmRepository;

    }
    public UserDetail getUser(String userId) {
        return userDetailRepository.findByUserid(userId);
    }
    public UserFm getUserfy(String userId) {
        //find 扶養家族
        return userFmRepository.findfy(userId);
    }
    public UserFm getUserkk(String userId){
        //find 緊急連絡先
        return userFmRepository.findkk(userId);
    }

    //新規入社連絡票登録
    public UserDetail createUserDetail(UserDetailForm userDetailForm,String userId) {
        userDetailRepository.umdelete(userId);
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userId);
        userDetail.setUserName(userDetailForm.getUserName());
        userDetail.setBirthday(userDetailForm.getBirthday());
        userDetail.setSex(userDetailForm.getSex());
        userDetail.setAddress(userDetailForm.getAddress());
        userDetail.setFirstname(userDetailForm.getFirstname());
        userDetail.setLastname(userDetailForm.getLastname());
        userDetail.setFurigana(userDetailForm.getFurigana());
        userDetail.setPhone(userDetailForm.getPhone());
        userDetail.setPostcode(userDetailForm.getPostcode());
        userDetail.setMail(userDetailForm.getMail());
        userDetail.setStay(userDetailForm.getStay());
        userDetail.setTime(userDetailForm.getTime());
        userDetail.setLimit(userDetailForm.getLimit());
        userDetail.setContory(userDetailForm.getContory());
        return userDetailRepository.save(userDetail);
    }

    //緊急連絡先新規登録
    public UserFm createUserFm(UserDetailForm userDetailForm,String userId) {
        UserFm userFm = new UserFm();
        userFm.setUserId(userId);
        userFm.setFm_name(userDetailForm.getFm_name());
        userFm.setFm_relation(userDetailForm.getFm_relation());
        userFm.setFm_postcode(userDetailForm.getFm_postcode());
        userFm.setFm_address(userDetailForm.getFm_address());
        userFm.setFm_tel(userDetailForm.getFm_tel());
        userFm.setFm_phone(userDetailForm.getFm_phone());
        userFm.setFm_status(1);
        return userFmRepository.save(userFm);
    }

    //扶養家族の新規登録
    public void createfy(UserFmForm userFmForm,String userId) {
            UserFm userfm = new UserFm();
            userfm.setUserId(userId);
            userfm.setFm_name(userFmForm.getFm_nameb());
            userfm.setFm_furigana(userFmForm.getFm_furigana());
            userfm.setFm_relation(userFmForm.getFm_relationb());
            userfm.setFm_birth(userFmForm.getFm_birth());
            userfm.setFm_sex(userFmForm.getFm_sex());
            userfm.setFm_job(userFmForm.getFm_job());
            userfm.setFm_money(userFmForm.getFm_money());
            userfm.setFm_address(userFmForm.getFm_addressb());
            userfm.setFm_status(0);
            userFmRepository.save(userfm);
}






    public void fydelete(String userId){
        userFmRepository.fmdelete(userId);
    }
    public void umdelete(String userId){
        userDetailRepository.umdelete(userId);
    }

    public String getUserSex(String userId) {

        return userDetailRepository.findUserSex(userId);
    }
}

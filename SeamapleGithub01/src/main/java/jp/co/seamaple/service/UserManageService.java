package jp.co.seamaple.service;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.User;
import jp.co.seamaple.entity.UserRole;
import jp.co.seamaple.entity.VacationDays;
import jp.co.seamaple.form.ChangePassForm;
import jp.co.seamaple.form.UserEditForm;
import jp.co.seamaple.form.UserRegisterForm;
import jp.co.seamaple.repository.UserRepository;
import jp.co.seamaple.repository.UserRoleRepository;
import jp.co.seamaple.repository.VacationDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserManageService {

    public final UserRepository userRepository;
    public final UserRoleRepository userRoleRepository;
    public final VacationDaysRepository vacationDaysRepository;
    public final SessionData sessionData;

    @Autowired
    public UserManageService(UserRepository userRepository, UserRoleRepository userRoleRepository,
            VacationDaysRepository vacationDaysRepository, SessionData sessionData) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.vacationDaysRepository = vacationDaysRepository;
        this.sessionData = sessionData;
    }

    /**
     * 編集ユーザー取得
     *
     * @param userId
     * @return 編集ユーザー
     */
    public User getUser(String userId) {
        return userRepository.getById(userId);
    }

    /**
     * ユーザーリスト取得
     *
     * @return ユーザーリスト
     */
    public List<User> getUserList() {
        return userRepository.getUserList();
    }

    public List getUserId() {
        return userRepository.getUserId();
    }

    /**
     * @param userId
     * @return ユーザー休暇情報
     */
    public VacationDays getVacationDays(String userId) {
        return vacationDaysRepository.getById(userId);
    }

    /**
     * ユーザー新規登録情報
     *
     * @param userRegisterForm
     * @return ユーザー情報
     */
    public User createUser(UserRegisterForm userRegisterForm) {
        User user = new User();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // ユーザー情報初期設定
        user.setUserId(userRegisterForm.getUserId());
        user.setUserName(userRegisterForm.getUserName());
        user.setPassword(passwordEncoder.encode(userRegisterForm.getPassword()));
        user.setPhoneNumber(userRegisterForm.getPhoneNumber());
        user.setMailAddress(userRegisterForm.getMailAddress());
        user.setEnabled(1);
        user.setRegTime(new Timestamp(System.currentTimeMillis()));
        user.setJoinCompanyDate(userRegisterForm.getJoinCompanyDate());
        user.setRoleId(userRegisterForm.getRoleId());
        user.setVacationDays(createNewVacationdays(userRegisterForm));

        return userRepository.save(user);
    }

    /**
     * 新規ユーザー有給付与日計算
     *
     * @param joinCompanyDate
     * @return 有給付与日
     */
    public Date calNewPaidVacationAddDate(Date joinCompanyDate) {
        Calendar calendarJoinCompanyDate = Calendar.getInstance();
        calendarJoinCompanyDate.setTime(joinCompanyDate);
        calendarJoinCompanyDate.add(Calendar.MONTH, 6);
        return calendarJoinCompanyDate.getTime();
    }

    /**
     * 新規ユーザー有給日数セット
     *
     * @param userRegisterForm
     * @return ユーザー有給情報セット
     */
    public VacationDays createNewVacationdays(UserRegisterForm userRegisterForm) {
        VacationDays vacationDays = new VacationDays();
        vacationDays.setUserId(userRegisterForm.getUserId());
        vacationDays.setUserId(userRegisterForm.getUserId());
        vacationDays.setLastPaidVacation(0);
        vacationDays.setPreviousPaidVacation(0);
        vacationDays.setPaidVacationAddCount(0);
        vacationDays.setPaidVacationAddDate(this.calNewPaidVacationAddDate(userRegisterForm.getJoinCompanyDate()));
        vacationDays.setSummerVacation(0);

        return vacationDays;
    }

    /**
     * 新規ユーザー権限セット
     *
     * @param userRegisterForm
     * @return ユーザー権限情報セット
     */
    public UserRole createUserRole(UserRegisterForm userRegisterForm) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userRegisterForm.getUserId());
        userRole.setRoleId(userRegisterForm.getRoleId());

        return userRoleRepository.save(userRole);
    }

    /**
     * 編集ユーザー情報セット
     *
     * @param userId,userRegisterForm
     * @return 編集ユーザー情報
     */
    public User editUser(String userId, UserEditForm userEditForm) {
        User editUser = userRepository.getById(userId);
        editUser.setUserName(userEditForm.getUserName());
        editUser.setMailAddress(userEditForm.getMailAddress());
        editUser.setPhoneNumber(userEditForm.getPhoneNumber());
        editUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        // 編集ユーザー休暇情報から取得
        editUser.setVacationDays(this.editVacationDays(userEditForm));
        return userRepository.save(editUser);
    }

    /**
     * 編集ユーザー休暇情報
     *
     * @param userRegisterForm
     * @return 編集ユーザー休暇情報
     */
    public VacationDays editVacationDays(UserEditForm userEditForm) {
        VacationDays vacationDays = this.getVacationDays(userEditForm.getUserId());
        vacationDays.setLastPaidVacation(userEditForm.getLastPaidVacation());
        vacationDays.setPreviousPaidVacation(userEditForm.getPreviousPaidVacation());
        vacationDays.setSummerVacation(userEditForm.getSummerVacation());
        return vacationDays;
    }

    /**
     * ユーザー情報削除
     *
     * @param userId
     */
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.getById(userId);
        userRepository.delete(user);
        UserRole userRole = userRoleRepository.getById(userId);
        userRoleRepository.delete(userRole);
    }

    /**
     * ユーザー退職によるロック
     *
     * @param userId
     */
    public void lockUser(String userId, Date retiredDate) {
        User user = userRepository.getById(userId);
        user.setEnabled(0);
        user.setRetiredDate(retiredDate);
        userRepository.save(user);
    }

    /**
     * ユーザーパスワード変更
     *
     * @param changePassForm
     * @return ユーザーパスワード変更結果
     */
    @Transactional
    public User changePass(ChangePassForm changePassForm) {
        User user = userRepository.getById(sessionData.getUserId());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(changePassForm.getNewPass()));
        return userRepository.save(user);
    }

    /**
     * パスワード初期化(初期パスワード="password")
     *
     * @param userId
     * @return パスワード初期化ユーザー情報
     */
    @Transactional
    public User resetPass(String userId) {
        User user = userRepository.getById(userId);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode("password"));
        return userRepository.save(user);
    }
}

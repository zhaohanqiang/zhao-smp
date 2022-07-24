package jp.co.seamaple.controller;

import jp.co.seamaple.entity.User;
import jp.co.seamaple.form.ChangePassForm;
import jp.co.seamaple.form.RetiredForm;
import jp.co.seamaple.form.UserEditForm;
import jp.co.seamaple.form.UserRegisterForm;
import jp.co.seamaple.repository.UserDetailRepository;
import jp.co.seamaple.service.UserDetailService;
import jp.co.seamaple.service.UserManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class UserManageController {

    private final UserManageService userManageService;
    public UserEditForm userEditForm;

    @Autowired
    public UserManageController(UserManageService userManageService, UserDetailService userDetailService,
            UserDetailRepository userDetailRepository) {
        this.userManageService = userManageService;
    }

    // ユーザー一覧画面表示

    @PostMapping("/userList")
    public String getUserList(Model model, UserEditForm userEditForm) {

        List<User> userList = userManageService.getUserList();
        model.addAttribute("userList", userList);
        return "user/userList";
    }

    // ユーザー情報登録画面表示
    @PostMapping(value = "/userRegister")
    public String showUserRegister(@ModelAttribute UserRegisterForm userRegisterForm) {
        return "user/userRegister";
    }

    // ユーザー情報登録処理
    @PostMapping(value = "/userRegister/insert")
    public String createUser(@Validated @ModelAttribute UserRegisterForm userRegisterForm, BindingResult result) {
        if (result.hasErrors()) {
            return "user/userRegister";
        } else {
            userManageService.createUser(userRegisterForm);
            userManageService.createUserRole(userRegisterForm);
            return "forward:/userList";
        }
    }

    // ユーザー情報更新画面表示
    @PostMapping(value = "/userEdit")
    public String getUserEditPage(@RequestParam(name = "userId", required = false) String userId,
            @ModelAttribute UserEditForm userEditForm, BindingResult result, Model model) {
        User editUser = userManageService.getUser(userId);
        // userEditFormに編集ユーザー情報コピー
        BeanUtils.copyProperties(editUser, userEditForm);

        String lastPaidVacation = String.valueOf(editUser.getVacationDays().getLastPaidVacation());
        model.addAttribute("lastPaidVacation", lastPaidVacation);
        String previousPaidVacation = String.valueOf(editUser.getVacationDays().getPreviousPaidVacation());
        model.addAttribute("previousPaidVacation", previousPaidVacation);
        String summerVacation = String.valueOf(editUser.getVacationDays().getSummerVacation());
        model.addAttribute("summerVacation", summerVacation);

        return "user/userEdit";
    }

    // ユーザー情報更新処理
    @PostMapping(value = "userEdit/update")
    public String editUser(@RequestParam(name = "userId", required = false) String userId,
            @Validated @ModelAttribute("userEditForm") UserEditForm userEditForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "forward:/userEdit";
        } else {
            userManageService.editUser(userId, userEditForm);
            return "forward:/userList";
        }
    }

    // ユーザー情報削除
    @PostMapping(value = "/userDelete")
    public String deleteUser(@RequestParam(name = "userId", required = false) String userId) {
        userManageService.deleteUser(userId);
        return "forward:/userList";
    }

    // ユーザーロック
    @PostMapping(value = "/userDisable")
    public String lockUser(RetiredForm retiredForm, @RequestParam(name = "userId", required = false) String userId) {
        userManageService.lockUser(userId, retiredForm.getRetiredDate());
        return "forward:/userList";
    }

    // パスワード変更画面表示
    @PostMapping(value = "/changePass")
    public String getChangePassPage(@RequestParam(name = "errorFlag", required = false) String errorFlag,
            @ModelAttribute ChangePassForm changePassForm, Model model) {
        if ("matchError".equals(errorFlag)) {
            model.addAttribute("errorMessage", "パスワードが一致しません。");
        }
        return "user/changePass";
    }

    // パスワード変更処理
    @PostMapping(value = "/changePass/update")
    public String changePass(
            @Validated(ChangePassForm.GroupValidation.class) @ModelAttribute ChangePassForm changePassForm,
            BindingResult result) {
        if (result.hasErrors()) {
            return "user/changePass";
        }

        // パスワード整合性チェック
        if (!changePassForm.getNewPass().equals(changePassForm.getConfirmedPass())) {
            return "forward:/changePass?errorFlag=matchError";
        } else {
            userManageService.changePass(changePassForm);
            return "/menuSelect";
        }
    }

    // パスワードリセット画面表示
    @PostMapping(value = "/resetPass")
    public String getResetPassPage(Model model) {
        List<User> userList = userManageService.getUserList();
        model.addAttribute("userList", userList);
        return "user/resetPass";
    }

    // パスワードリセット処理
    @PostMapping(value = "/resetPass/reset")
    public String resetPass(@RequestParam(name = "userId", required = true) String userId) {
        userManageService.resetPass(userId);
        return "forward:/userList";
    }

}

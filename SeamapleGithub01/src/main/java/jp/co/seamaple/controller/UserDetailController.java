package jp.co.seamaple.controller;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.UserDetail;
import jp.co.seamaple.entity.UserFm;
import jp.co.seamaple.form.UserDetailForm;
import jp.co.seamaple.form.UserEditForm;
import jp.co.seamaple.form.UserFmForm;
import jp.co.seamaple.repository.UserDetailRepository;
import jp.co.seamaple.repository.UserFmRepository;
import jp.co.seamaple.service.MailService;
import jp.co.seamaple.service.UserDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserDetailController {
    private final UserDetailService userDetailService;
    private final UserDetailRepository userDetailRepository;
    private final UserFmRepository userFmRepository;
    private final MailService mailService;
    private SessionData sessionData;

    @Autowired
    public UserDetailController(UserDetailService userDetailService, UserDetailRepository userDetailRepository,
            UserFmRepository userFmRepository,
            MailService mailService, SessionData sessionData) {
        this.userDetailService = userDetailService;
        this.userDetailRepository = userDetailRepository;
        this.userFmRepository = userFmRepository;
        this.mailService = mailService;
        this.sessionData = sessionData;
    }

    @GetMapping(value = "/contact")
    public String detaili(@ModelAttribute UserDetailForm userDetailForm,
            @ModelAttribute UserFmForm userFmForm,
            Model model) {
        String userId = sessionData.getUserId();
        UserDetail userfm = userDetailService.getUser(userId);

        List<UserFm> getfyList = null;
        if (userFmRepository.getfyList(userId) != null) {
            getfyList = userFmRepository.getfyList(userId);
        }
        model.addAttribute("fyList", getfyList);
        if (userfm != null) {
            model.addAttribute("userSex", userDetailService.getUserSex(userId));
            UserDetail userum = userDetailService.getUser(userId);
            UserFm userkk = userDetailService.getUserkk(userId);
            BeanUtils.copyProperties(userum, userDetailForm);
            BeanUtils.copyProperties(userkk, userDetailForm);

        }
        return "user/contact";
    }

    @PostMapping(value = "/createContact")
    public String createUserDetail(
            @Validated(UserDetailForm.GroupValidation.class) @ModelAttribute UserDetailForm userDetailForm,
            BindingResult bindingResult, @ModelAttribute UserFmForm userFmForm, Model model, HttpServletRequest request,
            HttpServletResponse response,
            String[] sex,
            String[] nameb, String[] furiganab, String[] relataionb, String[] birth, String[] sexb, Integer[] money,
            String[] addressb, String userId) {
        userId = sessionData.getUserId();
        if (userDetailForm.getUserId() != null && sessionData.getRoleId().equals("admin")) {
            userId = userDetailForm.getUserId();
        }
        userDetailForm.setSex(sex[0]);
        userFmRepository.fydelete(userId);
        if (nameb != null) {

            creatfy(userFmForm, request, response, nameb, furiganab, relataionb, birth, sexb, money, addressb, userId,
                    model);
        }
        List<UserFm> getfyList = userFmRepository.getfyList(userId);
        model.addAttribute("fyList", getfyList);
        if (bindingResult.hasErrors()) {
            return "user/contact";
        } else {
            // ユーザー本人の情報登録

            userDetailService.createUserDetail(userDetailForm, userId);
            // 以前のデータを削除
            userFmRepository.fmdelete(userId);
            // 緊急連絡先の情報登録
            userDetailService.createUserFm(userDetailForm, userId);
            // 扶養家族の情報登録

            String subject = "入社連絡票編集のご連絡";
            String content = sessionData.getUserName() + "さんが入社連絡票を編集しました。";
            // 収信メールアドレス
            mailService.sendMail("sheneilianluo@seamaple.co.jp", subject, content);
            if (sessionData.getRoleId().equals("admin")) {
                return "forward:/userList";
            }
            return "user/confirm";
        }
    }

    @PostMapping(value = "/userContactEdit")
    public String userDetailEdit(@Validated @ModelAttribute UserDetailForm userDetailForm, BindingResult bindingResult,
            @ModelAttribute UserFmForm userFmForm, UserEditForm userEditForm, Model model) {
        String userId = userEditForm.getUserId();
        List<UserFm> getfyList = null;

        UserDetail userfm = userDetailService.getUser(userId);
        if (userfm != null) {
            model.addAttribute("userName", userEditForm.getUserName());
            getfyList = userFmRepository.getfyList(userId);

            model.addAttribute("userSex", userDetailService.getUserSex(userId));
            UserDetail userum = userDetailService.getUser(userId);
            UserFm userkk = userDetailService.getUserkk(userId);
            BeanUtils.copyProperties(userum, userDetailForm);
            BeanUtils.copyProperties(userkk, userDetailForm);

        }
        model.addAttribute("fyList", getfyList);

        return "user/contact";
    }

    // 扶養家族の情報を登録
    @ResponseBody
    public String creatfy(UserFmForm userFmForm,
            HttpServletRequest request, HttpServletResponse response,
            String[] nameb, String[] furiganab, String[] relataionb, String[] birth, String[] sexb, Integer[] money,
            String[] addressb,
            String userId, Model model) {
        List<UserFmForm> userfm = new ArrayList<UserFmForm>();
        for (int i = 0; i < nameb.length; i++) {
            userFmForm.setFm_nameb(nameb[i]);
            userFmForm.setFm_furigana(furiganab[i]);
            userFmForm.setFm_relationb(relataionb[i]);
            userFmForm.setFm_birth(birth[i]);
            userFmForm.setFm_sex(sexb[i]);
            userFmForm.setFm_money(money[i]);
            userFmForm.setFm_addressb(addressb[i]);
            userfm.add(userFmForm);
            userDetailService.createfy(userFmForm, userId);
        }
        return "";
    }

    @GetMapping(value = "/confirm")
    public String confirm(@ModelAttribute UserDetailForm userDetailForm,
            @ModelAttribute UserFmForm userFmForm,
            Model model) {
        String userId = sessionData.getUserId();
        // UserDetail userfm = userDetailService.getUser(userId);
        List<UserFm> getfyList = userFmRepository.getfyList(userId);
        model.addAttribute("fyList", getfyList);
        UserDetail userum = userDetailService.getUser(userId);
        UserFm userkk = userDetailService.getUserkk(userId);
        BeanUtils.copyProperties(userum, userDetailForm);
        BeanUtils.copyProperties(userkk, userDetailForm);
        return "confirm";
    }

}
package jp.co.seamaple.controller;

import jp.co.seamaple.entity.SessionData;
import jp.co.seamaple.entity.User;
import jp.co.seamaple.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuSelectController {

    private final UserManageService userManageService;
    private final SessionData sessionData;

    @Autowired
    public MenuSelectController(UserManageService userManageService, SessionData sessionData) {
        this.userManageService = userManageService;
        this.sessionData = sessionData;
    }

    @GetMapping("/menuSelect")
    public String getMenuSelect() {
        return "menuSelect";
    }

    @GetMapping("/set")
    public String setUser() {

        // spring security内のユーザー情報取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // ユーザーエンティティ情報取得
        User user = userManageService.getUser(auth.getName());

        // セッション保存
        sessionData.setUserId(auth.getName());
        sessionData.setUserName(user.getUserName());
        sessionData.setRoleId(user.getRoleId());

        return "redirect:/menuSelect";
    }
}

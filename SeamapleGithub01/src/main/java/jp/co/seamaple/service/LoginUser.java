package jp.co.seamaple.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//ユーザーの認証・認可にかかわるクラス
public class LoginUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;
    private final User user;

    public LoginUser(User user) {
        super(user.getUsername(), user.getPassword(), getAuthorities(user.getAuthorities()));
        this.user = user;
    }

    // ログイン権限、文字列変更
    private static Collection<GrantedAuthority> getAuthorities(Collection<GrantedAuthority> collection) {
        if (collection.equals("ADMIN")) {
            return AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_GENERAL");
        } else {
            return AuthorityUtils.createAuthorityList("ROLE_GENERAL");
        }
    }

    public User getUser() {
        return user;
    }

    // idのgetter
    public String getId() {
        return user.getUsername();
    }
}
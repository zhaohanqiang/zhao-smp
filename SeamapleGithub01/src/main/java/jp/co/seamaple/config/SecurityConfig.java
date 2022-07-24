package jp.co.seamaple.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // ユーザーIDとパスワードを取得するSQL文
    private static final String USER_SQL = "SELECT" + " user_id," + " password," + " enabled" + " FROM" + " m_user"
            + " WHERE" + " user_id = ?";
    // ユーザーのロールを取得するSQL文
    private static final String ROLE_SQL = "SELECT" + " m_user.user_id," + " role.role_name" + " FROM"
            + " m_user INNER JOIN t_user_role AS user_role" + " ON" + " m_user.user_id = user_role.user_id"
            + " INNER JOIN m_role AS role" + " ON" + " user_role.role_id = role.role_id" + " WHERE"
            + " m_user.user_id = ?";
    // データソース
    @Autowired
    private DataSource dataSource;

    // パスワードエンコーダーBean定義
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/login").permitAll().and().authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll() // ログイン前でもアクセス可能なＵＲＬの指定
                // .antMatchers("/admin/**").hasRole("ROLE_GENERAL")//GENERAL権限のユーザーがアクセスできるURLの指定
                // .antMatchers("/admin/**").hasRole("ROLE_ADMIN")//ADMIN権限のユーザーのみアクセスできるURLの指定
                .anyRequest().authenticated(); // ログイン前は他のアドレスにログインできないよう指定

        http.formLogin().loginProcessingUrl("/login") // ログイン処理でpostするURL
                .loginPage("/login") // ログインページのURL
                .failureUrl("/login?error") // ログイン失敗時の遷移先
                .usernameParameter("userId").passwordParameter("password").defaultSuccessUrl("/set", true); // ログイン成功時のURL
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutUrl("/logout")
                // .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID") // ログアウト時キャッシュを削除する処理
                .logoutSuccessUrl("/login"); // ログアウト後に表示するURL
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // ログイン処理時のユーザー情報をDBから取得する
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(USER_SQL) // ユーザーの取得
                .authoritiesByUsernameQuery(ROLE_SQL) // ロールの取得
                .passwordEncoder(passwordEncoder()); // パスワードの複号
    }

}

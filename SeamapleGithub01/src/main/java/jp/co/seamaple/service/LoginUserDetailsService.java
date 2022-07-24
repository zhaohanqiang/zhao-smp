package jp.co.seamaple.service;

import jp.co.seamaple.entity.User;
import jp.co.seamaple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("The requested user is not found.");
        }
        return new LoginUserDetails(user);
    }


}


package com.krekerok.blogapp.configuration.user_details;


import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.repository.AppUserRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with %s username not found.", username)));
        return UserDetailsImpl.build(user);
    }
}

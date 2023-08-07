package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional  //  중요 ~ToMany : default lazy
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("start user detail service");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username : " + username + " not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), covertToSpringAuthorities(user.getAuthorities())
        );

    }

    private Collection<? extends GrantedAuthority> covertToSpringAuthorities(Set<Authority> authorities) {

        log.debug("covertToSpringAuthorities setting start");
        if (authorities != null && !authorities.isEmpty()) {

            log.debug("user has role list");
            authorities.forEach(item -> {
                log.debug("user = {}, role = {}", item.getUsers(), item.getRole());
            });

            return authorities.stream()
                    .map(Authority::getRole)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

}

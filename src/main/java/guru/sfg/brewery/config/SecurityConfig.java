package guru.sfg.brewery.config;


import guru.sfg.brewery.security.SfgPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * The type Security config.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoder.createDelegatingPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authority -> {
                    authority.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find/**", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic()
                .and().csrf().disable()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                    .withUser("spring")
                    .password("{bcrypt}$2a$10$4VDe.dlvi8pBZ5pDs2WP8OjPIbjvgpcAulD.TlOR2iR3PWdnnioSq")
                    .roles("ADMIN")

                .and()
                    .withUser("user")
                    .password("{sha256}965fabb53e8b1060c46852dd3121c88eff5b3cbed80ec003b221ae3caf721dc524df53bb5c94fd36")
                    .roles("USER")

                .and()
                    .withUser("scott")
                    .password("{ldap}{SSHA}Q8ugr4L4yfmTyLVuC0IxyDdp+wvsbPKTA0KH9Q==")
                    .roles("CUSTOMER");
    }

}

package guru.sfg.brewery.config;


import guru.sfg.brewery.security.JpaUserDetailService;
import guru.sfg.brewery.security.SfgPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The type Security config.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));    //  AntPathRequestMatcher ?? ToDO 알아보자
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }   //  헤더 인증 필터 구현 완료.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoder.createDelegatingPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(
                        restHeaderAuthFilter(
                                authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class
                );  //  요청헤더에서 해당 자격 증명을 가져온다.


        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll()  //  h2 console access
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/{beerId}").hasRole("ADMIN")
                    ;
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic()
                .and()
                .csrf().disable();

                //  h2 console access
                http.headers().frameOptions().sameOrigin();
    }


//    @Autowired
//    JpaUserDetailService jpaUserDetailService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(this.jpaUserDetailService)
//                .passwordEncoder(passwordEncoder());
//    }


/*  TODO : 인 메모리 구현 방법
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
*/


}

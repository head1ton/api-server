package com.youwiz.demoapi.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
hasIpAddress(ip) - 접근자의 ip주소가 매칭하는지 확인
hasRole(role) - 역할이 부여된 권한과 일치하는지 확인
hasAnyRole(role) - 부여된 역할 중 일치하는 항목이 있는지 확인
ex) access = "hasAnyRole('ROLE_USER','ROLE_ADMIN')"
permitAll - 모든 접근자를 항상 승인합니다.
denyAll - 모든 사용자의 접근을 거부합니다.
anonymous - 사용자가 익명 사용자인지 확인합니다.
authenticated - 인증된 사용자인지 확인합니다.
rememberMe - 사용자가 remember me를 사용해 인증했는지 확인
fullyAuthenticated - 사용자가 모든 크리덴셜을 갖춘 상태에서 인증했는지 확인.
 */
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)   // annotation으로 권한 설정을 하려면 이걸 활성화해준다. 그리고 auhorizedRequest() 설정은 주석처리하거나 삭제.
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
                .and()
                    .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                        .antMatchers("/*/signin", "/*/signin/**", "/*/signup", "/*/signup/**", "/social/**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                        .antMatchers(HttpMethod.GET, "/exception/**", "helloworld/**", "/actuator/health", "/v1/board/**", "/favicon.ico").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
        //                .antMatchers("/*/users").hasRole("ADMIN")
                        .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣어라.
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}

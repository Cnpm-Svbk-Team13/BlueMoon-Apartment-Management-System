package com.bluemoon.bfms.config;

import com.bluemoon.bfms.service.AuthService;
import com.bluemoon.bfms.security.AuthenticatedPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.List;

/**
 * Cấu hình xác thực và phân quyền phía server cho BlueMoon.
 *
 * <p>Không tin vai trò do request gửi lên: Spring Security lấy role từ
 * {@link AuthService.AuthenticationResult} sau khi Service xác thực tài khoản.
 * Principal của phiên giữ cả ID ổn định và username; các Controller dùng ID
 * để yêu cầu Service kiểm tra lại trạng thái và role hiện tại trong database.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(AuthService authService) {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) {
                HttpServletRequest request = org.springframework.web.context.request.RequestContextHolder
                        .getRequestAttributes() instanceof org.springframework.web.context.request.ServletRequestAttributes attributes
                        ? attributes.getRequest() : null;
                String ipAddress = request == null ? null : request.getRemoteAddr();

                AuthService.AuthenticationResult result = authService.authenticate(
                        authentication.getName(),
                        authentication.getCredentials() == null
                                ? null : authentication.getCredentials().toString(),
                        ipAddress);

                if (result.status() != AuthService.LoginStatus.SUCCESS) {
                    throw new BadCredentialsException(
                            "Tên đăng nhập hoặc mật khẩu không chính xác");
                }

                return new UsernamePasswordAuthenticationToken(
                        // Lưu userId trong principal để đổi username không đổi danh tính actor của phiên.
                        new AuthenticatedPrincipal(result.userId(), result.username()),
                        null,
                        List.of(new SimpleGrantedAuthority(result.role().name())));
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                if (authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                    return;
                }
                if (authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ACCOUNTANT"))) {
                    response.sendRedirect(request.getContextPath() + "/admin/apartments");
                    return;
                }
                if (authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_CASHIER")
                                || authority.getAuthority().equals("ROLE_RESIDENT"))) {
                    response.sendRedirect(request.getContextPath() + "/portal");
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider,
            AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        http.authenticationProvider(authenticationProvider);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/users/change-password").authenticated()
                .requestMatchers("/admin/apartments/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_ACCOUNTANT")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/change-password").authenticated()
                .requestMatchers("/portal/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_ACCOUNTANT", "ROLE_CASHIER", "ROLE_RESIDENT")
                .anyRequest().authenticated());

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error")
                .permitAll());

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID"));

        return http.build();
    }
}

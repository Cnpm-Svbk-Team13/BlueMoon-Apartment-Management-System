package com.bluemoon.bfms.web;

import com.bluemoon.bfms.security.AuthenticatedPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Đặt principal cùng kiểu với principal được tạo bởi luồng đăng nhập thật. */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@WithSecurityContext(factory = WithAuthenticatedAccount.Factory.class)
public @interface WithAuthenticatedAccount {
    long userId();
    String username();
    String[] authorities();

    final class Factory implements WithSecurityContextFactory<WithAuthenticatedAccount> {
        @Override
        public SecurityContext createSecurityContext(WithAuthenticatedAccount annotation) {
            var principal = new AuthenticatedPrincipal(annotation.userId(), annotation.username());
            var authorities = Arrays.stream(annotation.authorities())
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            return context;
        }
    }
}

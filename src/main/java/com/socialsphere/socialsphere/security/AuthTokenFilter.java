package com.socialsphere.socialsphere.security;

import com.socialsphere.socialsphere.constant.CommonConstant;
import com.socialsphere.socialsphere.exception.TokenException;
import com.socialsphere.socialsphere.services.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();

            // Skip JWT processing for public URLs
            if (requestURI.startsWith("/v1/auth/")) {
                filterChain.doFilter(request, response);
                return;
            }
            // Extract the cookie
            String jwt = null;
            if(request.getCookies() != null){
                for(Cookie cookie : request.getCookies()){
                    if(cookie.getName().equals(CommonConstant.ACCESS_TOKEN)){
                        jwt = cookie.getValue();
                    }
                }
            }

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                // TODO on every request we are fetching the user from the database this may impact the performance
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenException tokenException){
            log.error("Exception while validating token", tokenException);
            throw tokenException;
        } catch (Exception e) {
            log.error("Cannot set user authentication: ");
            throw e;
        }
        filterChain.doFilter(request, response);
    }

}
package com.couple.back.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonUtil;
import com.couple.back.common.JwtUtil;
import com.couple.back.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        
        String requestURI = request.getRequestURI();
        if (StringUtils.startsWith(requestURI, "/auth/") || StringUtils.equals(requestURI, "/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String accessToken = CommonUtil.getAccessToken(request.getHeader("Authorization"));

        if (StringUtils.isEmpty(accessToken) || jwtUtil.isTokenExpired(accessToken)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                SecurityContextHolder.clearContext();
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonConstants.TOKEN_EXPIRED_MESSAGE);
            return;
        }

        String email = jwtUtil.extractEmail(accessToken);

        if (StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = jwtUtil.extractUser(accessToken);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getUserRole()));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
    
}

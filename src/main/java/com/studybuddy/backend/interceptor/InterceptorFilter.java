package com.studybuddy.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.studybuddy.backend.config.ShareConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class InterceptorFilter extends OncePerRequestFilter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShareConfig shareConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest rq, HttpServletResponse rp, FilterChain filterChain) throws ServletException, IOException {
        try {
            RequestWrapper rqWrapper = new RequestWrapper(rq, objectMapper, shareConfig);
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(rqWrapper);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(rp);
            filterChain.doFilter(requestWrapper, responseWrapper);

            String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("[RESPONSE BODY]: {}", responseBody);
            responseWrapper.copyBodyToResponse();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

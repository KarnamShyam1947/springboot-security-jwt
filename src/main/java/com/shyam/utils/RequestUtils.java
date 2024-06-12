package com.shyam.utils;

import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
    @SuppressWarnings("null")
    public static void getContextPath() {
        HttpServletRequest request = (HttpServletRequest)RequestContextHolder.getRequestAttributes();
        System.out.println(request.getContextPath());
        System.out.println(request.getServletPath());
    }
}

package com.rybak.andersenhw3.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybak.andersenhw3.dto.ErrorMessage;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class WebUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private WebUtil() {
        throw new UnsupportedOperationException();
    }

    public static String getResolvedPathInfo(HttpServletRequest request) {
        return request.getPathInfo() == null ? "/" : request.getPathInfo();
    }

    public static void sendFailMessage(ServletResponse response, String message, int code) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        PrintWriter out = httpResponse.getWriter();

        httpResponse.setStatus(code);
        httpResponse.setContentType("application/json");

        out.print(objectMapper.writeValueAsString(new ErrorMessage(code, message, Instant.now().toString())));
    }

    public static String getCurrentUserEmail(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("email");
        }
        return null;
    }

}

package com.example.kino_search.filter;

import com.example.kino_search.util.LanguageManager;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LanguageFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        // Получаем параметр языка из запроса
        String lang = request.getParameter("lang");
        if (lang != null) {
            session.setAttribute("lang", lang); // Сохраняем язык в сессии
            LanguageManager.setLocale(lang);    // Устанавливаем текущий язык
        } else if (session.getAttribute("lang") != null) {
            LanguageManager.setLocale((String) session.getAttribute("lang"));
        }

        chain.doFilter(request, response);
    }

}

package com.recipesapi.config;
/* 
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@WebFilter("/*")
public class CustomLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Incapsula la richiesta HTTP per poter leggere il corpo più volte
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest);

        // Leggi il corpo della richiesta
        String requestBody = getRequestBody(requestWrapper);

        // Stampa le informazioni sulla richiesta
        System.out.println("Request Method: " + httpRequest.getMethod());
        System.out.println("Request URL: " + httpRequest.getRequestURL());
        System.out.println("Request Headers:");
        httpRequest.getHeaderNames().asIterator().forEachRemaining(
                headerName -> System.out.println(headerName + ": " + httpRequest.getHeader(headerName)));
        System.out.println("Request Body: " + requestBody);

        // Passa la richiesta al prossimo filtro nella catena
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup
    }

    private String getRequestBody(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}*/

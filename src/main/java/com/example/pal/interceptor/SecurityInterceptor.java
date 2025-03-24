package com.example.pal.interceptor;

import com.example.pal.service.ValidatorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class SecurityInterceptor implements HandlerInterceptor {
  @Autowired private ValidatorsService validatorService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // Log the current user from the request attribute
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      System.out.println("User attempting to access endpoint: " + authentication.getName());
      System.out.println("User authorities: " + authentication.getAuthorities());
    }
    
    return this.validatorService.hasPermission(
        request, request.getRequestURI(), request.getMethod());
  }

  // Lógica a ejecutar durante el manejo de la solicitud por el controlador
  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    // Lógica a ejecutar después de que se haya manejado la solicitud por el controlador
  }

  // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de la
  // vista
  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de
    // la vista
  }
}

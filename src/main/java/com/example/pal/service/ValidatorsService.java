package com.example.pal.service;

import com.example.pal.model.Permission;
import com.example.pal.model.Role;
import com.example.pal.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ValidatorsService {
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserService userService;

  /**
   * Validates if the authenticated user has permission to access the specified URL with the given
   * method.
   *
   * @param request The HTTP request containing authentication information
   * @param url The URL being accessed
   * @param method The HTTP method being used
   * @return true if the user has permission, false otherwise
   */
  @Transactional
  public boolean hasPermission(HttpServletRequest request, String url, String method) {
    final String normalizedUrl = normalizeUrl(url);

    return authenticateUser(request)
        .map(user -> checkUserPermissions(user, normalizedUrl, method))
        .orElse(false);
  }

  /** Normalizes a URL by replacing numeric IDs and MongoDB ObjectIds with placeholders. */
  private String normalizeUrl(String url) {
    return url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
  }

  /**
   * Authenticates a user from the request's Basic Authentication header.
   *
   * @param request The HTTP request
   * @return Optional containing the authenticated User or empty if authentication failed
   */
  private Optional<User> authenticateUser(final HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Basic")) {
      return Optional.empty();
    }

    try {
      String[] credentials = extractCredentials(authHeader);
      if (credentials.length != 2) return Optional.empty();

      String username = credentials[0];
      String password = credentials[1];

      User user = userService.getUserByUsername(username);
      if (user == null) return Optional.empty();

      if (!passwordEncoder.matches(password, user.getPassword())) {
        return Optional.empty();
      }

      return Optional.of(user);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /** Extracts username and password from the Basic Authentication header. */
  private String[] extractCredentials(String authHeader) {
    String encodedCredentials = authHeader.substring(6).trim();
    byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
    return new String(decodedBytes).split(":", 2);
  }

  /** Checks if the user has any role with permission to access the specified URL and method. */
  private boolean checkUserPermissions(User user, String url, String method) {
    Set<Role> roles = user.getRoles();

    if (roles == null || roles.isEmpty()) return false;

    return roles.stream().anyMatch(role -> hasRolePermission(role, url, method));
  }

  /** Checks if a role has permission to access the specified URL and method. */
  private boolean hasRolePermission(Role role, String url, String method) {
    Set<Permission> permissions = role.getPermissions();

    if (permissions == null || permissions.isEmpty()) {
      return false;
    }

    return permissions.stream()
        .anyMatch(permission -> matchesUrlAndMethod(permission, url, method));
  }

  /** Checks if a permission matches the specified URL and method. */
  private boolean matchesUrlAndMethod(Permission permission, String url, String method) {
    return permission.getUrl().equals(url.trim()) && permission.getMethod().equals(method.trim());
  }
}

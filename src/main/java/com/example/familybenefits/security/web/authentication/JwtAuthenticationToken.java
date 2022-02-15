package com.example.familybenefits.security.web.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Реализация {@link Authentication}, основанная на токене формата JWT
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;

  private Object credentials;

  public JwtAuthenticationToken(Object principal, Object credentials) {
    super(null);
    this.credentials = credentials;
    this.principal = principal;
    this.setAuthenticated(false);
  }

  public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.credentials = credentials;
    this.principal = principal;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
  }
}

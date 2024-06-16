package io.nology.postcode_api.security;

public class AuthResponseDTO {
  private final String jwt;

  public AuthResponseDTO(String jwt) {
      this.jwt = jwt;
  }

  public String getJwt() {
      return jwt;
  }
}
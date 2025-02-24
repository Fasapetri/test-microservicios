package com.example.plazoleta.domain.spi;

public interface ISecurityContextPort {

    String getUserAuthenticateRol();

    Long getAuthenticatedUserId();

    String getAuthenticatedUserEmail();

    String getToken();
}

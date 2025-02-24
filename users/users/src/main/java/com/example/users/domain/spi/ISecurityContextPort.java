package com.example.users.domain.spi;

public interface ISecurityContextPort {

    String getUserAuthenticateRol();

    Long getAuthenticatedUserId();

    String getAuthenticatedUserEmail();
}

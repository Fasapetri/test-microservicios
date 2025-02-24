package com.example.users.domain.constants;

public class UserConstants {
    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_PROPIETARIO = "PROPIETARIO";
    public static final String ROLE_EMPLEADO = "EMPLEADO";
    public static final String ROLE_CLIENTE = "CLIENTE";

    // Regex
    public static final String EMAIL_FORMAT_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String ONLY_DIGITS_REGEX  = "\\d+";
    public static final String PHONE_NUMBER_REGEX  = "^\\+?\\d{1,13}$";

    // Reglas de Negocio
    public static final int MIN_ADULT_AGE = 18;

    private UserConstants() {
    }
}

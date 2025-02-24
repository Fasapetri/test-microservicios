package com.example.users.infraestructure.constants;

public class AuthRestControllerConstants {

    // General
    public static final String TAG_NAME = "Autenticación";
    public static final String TAG_DESCRIPTION = "Endpoints para la autenticación y validación de tokens.";

    // Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Códigos de estado HTTP
    public static final String STATUS_OK = "200";
    public static final String STATUS_BAD_REQUEST = "400";
    public static final String STATUS_UNAUTHORIZED = "401";
    public static final String STATUS_SERVER_ERROR = "500";

    // Login
    public static final String LOGIN_SUMMARY = "Autenticación de usuario";
    public static final String LOGIN_DESCRIPTION = "Permite a los usuarios autenticarse y obtener un token de acceso.";
    public static final String LOGIN_REQUEST_DESCRIPTION = "Credenciales del usuario para autenticación.";
    public static final String LOGIN_SUCCESS_RESPONSE = "Inicio de sesión exitoso.";
    public static final String LOGIN_BAD_REQUEST_RESPONSE = "Solicitud inválida, datos incorrectos o incompletos.";
    public static final String LOGIN_UNAUTHORIZED_RESPONSE = "Autenticación fallida, credenciales incorrectas.";
    public static final String LOGIN_SERVER_ERROR_RESPONSE = "Error interno del servidor durante la autenticación.";

    // Validar Token
    public static final String VALIDATE_TOKEN_SUMMARY = "Validar token de autenticación";
    public static final String VALIDATE_TOKEN_DESCRIPTION = "Verifica si un token de autenticación es válido.";
    public static final String VALIDATE_TOKEN_SUCCESS_RESPONSE = "El token es válido.";
    public static final String VALIDATE_TOKEN_BAD_REQUEST_RESPONSE = "Solicitud inválida, token no proporcionado.";
    public static final String VALIDATE_TOKEN_UNAUTHORIZED_RESPONSE = "El token es inválido o ha expirado.";
    public static final String VALIDATE_TOKEN_SERVER_ERROR_RESPONSE = "Error interno del servidor al validar el token.";

    // Invalidar Token
    public static final String INVALIDATE_TOKEN_SUMMARY = "Invalidar token de autenticación";
    public static final String INVALIDATE_TOKEN_DESCRIPTION = "Invalida el token de autenticación actual.";
    public static final String INVALIDATE_TOKEN_SUCCESS = "Token invalidado correctamente.";
    public static final String INVALIDATE_TOKEN_NOT_FOUND = "No se proporcionó un token para invalidar.";

    // Verificar si el Token es inválido
    public static final String CHECK_INVALID_TOKEN_SUMMARY = "Verificar si un token está invalidado";
    public static final String CHECK_INVALID_TOKEN_DESCRIPTION = "Verifica si un token ha sido revocado.";
    public static final String CHECK_INVALID_TOKEN_RESPONSE = "Devuelve `true` si el token es inválido, `false` si es válido.";
    public static final String CHECK_INVALID_TOKEN_BAD_REQUEST = "Solicitud inválida, token no proporcionado.";

}

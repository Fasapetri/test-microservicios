package com.example.users.infraestructure.constants;

public class SecurityContextAdapterConstants {

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String UNKNOWN_USER_ID_TYPE = "Tipo de userId desconocido: ";
    public static final String USER_ID_CLAIM = "userId";
    public static final String ROLE_CLAIM = "role";
    public static final String INVALID_TOKEN = "Token inválido o mal formado ";
    public static final String ERROR_EXTRACT_EMAIL = "Sucedio un error al extraer el email: ";
    public static final String ERROR_EXTRACT_ID = "Error al extraer el userid del token: ";
    public static final String SRC_SECRET_KEY = "N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9";
    public static final int EXPIRATION_TOKEN_MS = 1000;
    public static final int EXPIRATION_TOKEN_SG = 60;
    public static final int EXPIRATION_TOKEN_MIN = 60;
    public static final String PROCESSING_TOKEN_SUCCESS = "Token procesado correctamente. Email extraído: {} ";
    public static final String INVALIDATE_TOKEN_REVOKED = "revoked";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String FORBIDDEN_OPERATION_MESSAGE = "{\"error\": \" No tienes permisos para realizar esta operación.\"}";

}

package com.example.plazoleta.infraestructure.constants;

public class SecurityContextAdapterConstants {

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String UNKNOWN_USER_ID_TYPE = "Tipo de userId desconocido: ";
    public static final String USER_ID_CLAIM = "userId";
    public static final String ROLE_CLAIM = "role";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String FORBIDDEN_OPERATION_MESSAGE = "{\"error\": \" No tienes permisos para realizar esta operación.\"}";

    private SecurityContextAdapterConstants(){}

}

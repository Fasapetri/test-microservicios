package com.example.pedidos.infraestructure.constants;

public class SecurityContextAdapterConstants {

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String UNKNOWN_USER_ID_TYPE = "Tipo de userId desconocido: ";
    public static final String USER_ID_CLAIM = "userId";
    public static final String ROLE_CLAIM = "role";
    public static final String WEBCLIENT_BASEURL_RESTAURANT = "http://localhost:8082";
    public static final String WEBCLIENT_URI_EXISTSRESTAURANT = "/api/restaurant/existsRestaurant/{id}";
    public static final String WEBCLIENT_URI_FIND_DISH_RESTAURANT = "/api/dishes/{id}/dishRestaurant";
    public static final String VALUE_ACCOUNT_SID_TWILIO = "${twilio.account.sid}" ;
    public static final String VALUE_OUTH_TOKEN_TWILIO = "${twilio.auth.token}";
    public static final String VALUE_NUMBER_TWILIO = "${twilio.phone.number}";
    public static final String WEBCLIENT_BASEURL_USER = "http://localhost:8081";
    public static final String WEBCLIENT_URI_FIND_DATA_CLIENT = "/api/users/id/{id}";



}

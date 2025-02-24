package com.example.plazoleta.application.constants;

public class RestaurantRequestConstants {

    // Descripción de la clase
    public static final String DESC_RESTAURANT_REQUEST = "Datos para registrar un nuevo restaurante";

    // Descripciones de los campos
    public static final String DESC_ID = "Identificador del restaurante";
    public static final String DESC_NAME = "Nombre del restaurante";
    public static final String DESC_NIT = "Número de Identificación Tributaria (NIT)";
    public static final String DESC_ADDRESS = "Dirección del restaurante";
    public static final String DESC_PHONE = "Número de teléfono";
    public static final String DESC_URL_LOGO = "URL del logo del restaurante";
    public static final String DESC_ID_PROPRIETARY = "ID del propietario";

    // Ejemplos
    public static final String EXAMPLE_ID = "1";
    public static final String EXAMPLE_NAME = "La Casa de la Pasta";
    public static final String EXAMPLE_NIT = "1234567890";
    public static final String EXAMPLE_ADDRESS = "Calle 123 #45-67";
    public static final String EXAMPLE_PHONE = "+573001234567";
    public static final String EXAMPLE_URL_LOGO = "https://example.com/logo.png";
    public static final String EXAMPLE_ID_PROPRIETARY = "1";

    // Mensajes de validación
    public static final String MSG_NIT_VALIDATION = "El NIT debe tener 10 dígitos numéricos";
    public static final String MSG_PHONE_VALIDATION = "El número de teléfono debe estar en formato internacional, ej: +573001234567";

    // Expresiones Regulares
    public static final String REGEX_NIT = "\\d{10}";
    public static final String REGEX_PHONE = "\\+57\\d{10}";

}


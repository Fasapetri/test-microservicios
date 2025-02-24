package com.example.users.infraestructure.constants;

public class UserRestControllerConstants {
    // General
    public static final String TAG_NAME = "Usuarios";
    public static final String TAG_DESCRIPTION = "Endpoints relacionados con la gestión de usuarios.";

    // Endpoints
    public static final String CREATE_USER_SUMMARY = "Crear un usuario";
    public static final String CREATE_USER_DESCRIPTION = "Crea un nuevo usuario con los datos proporcionados.";

    public static final String FIND_BY_EMAIL_SUMMARY = "Consultar Usuario por email";
    public static final String FIND_BY_EMAIL_DESCRIPTION = "Permite consultar un usuario ingresando su correo.";
    public static final String PARAM_EMAIL = "Email del usuario";
    public static final String PARAM_EMAIL_EXAMPLE = "usuario@example.com";

    public static final String FIND_BY_ID_SUMMARY = "Consultar Usuario por id";
    public static final String FIND_BY_ID_DESCRIPTION = "Permite consultar un usuario ingresando su identificador.";
    public static final String PARAM_ID = "ID del usuario";
    public static final String PARAM_ID_EXAMPLE = "1";

    public static final String UPDATE_USER_SUMMARY = "Actualizar Usuario";
    public static final String UPDATE_USER_DESCRIPTION = "Permite modificar la información de un usuario existente. Se requiere autenticación.";

    public static final String DELETE_USER_SUMMARY = "Eliminar Usuario";
    public static final String DELETE_USER_DESCRIPTION = "Permite eliminar un usuario existente por su ID. Se requiere autenticación con un token válido.";

    // Códigos de estado HTTP
    public static final String STATUS_OK = "200";
    public static final String STATUS_BAD_REQUEST = "400";
    public static final String STATUS_UNAUTHORIZED = "401";
    public static final String STATUS_NOT_FOUND = "404";
    public static final String STATUS_NO_CONTENT = "204";
    public static final String STATUS_SERVER_ERROR = "500";

    // Respuestas API
    public static final String RESPONSE_USER_CREATED = "Usuario creado con éxito.";
    public static final String RESPONSE_USER_FOUND = "Usuario encontrado con éxito.";
    public static final String RESPONSE_USER_UPDATED = "Usuario actualizado con éxito.";
    public static final String RESPONSE_USER_DELETED = "Usuario eliminado con éxito.";

    public static final String RESPONSE_BAD_REQUEST = "Solicitud inválida.";
    public static final String RESPONSE_UNAUTHORIZED = "No autorizado, token inválido o ausente.";
    public static final String RESPONSE_NOT_FOUND = "Usuario no encontrado.";
    public static final String RESPONSE_SERVER_ERROR = "Error interno del servidor.";


}

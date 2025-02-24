package com.example.users.domain.exception;

public enum UserExceptionType {

    INVALID_AGE("El usuario debe ser mayor de edad"),
    INVALID_EMAIL("El correo no tiene un formato válido"),
    INVALID_DOCUMENT("El documento debe ser numérico"),
    INVALID_PHONE("El celular debe tener un máximo de 13 caracteres y puede contener el símbolo +"),
    ROLE_NOT_ALLOWED("No tienes permisos para crear este usuario"),
    GENERAL_ERROR("Error general en la creación del usuario"),
    INVALID_ROL_ADMIN_CREATED_USER("Solo los usuarios con rol PROPIETARIO pueden crear usuarios EMPLEADOS."),
    INVALID_ROL_PROPIETARIO_CREATED_USER("Solo los usuarios con rol ADMIN pueden crear usuarios que no sean EMPLEADOS."),
    USER_NOT_FOUND("No existe el usuario"),
    EMAIL_USER_EXISTS("Ya existe un usuario creado con ese email"),
    USER_NOT_DATA("No existen datos de usuarios registrado");

    private final String message;

    UserExceptionType(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}

package com.example.plazoleta.infraestructure.constants;

public class DishRestControllerConstants {

    public static final String ADD_DISH_SUMMARY = "Agregar un nuevo plato";
    public static final String ADD_DISH_DESCRIPTION = "Este endpoint permite agregar un nuevo plato a un restaurante. Se requiere autenticación.";
    public static final String ADD_DISH_SUCCESS = "Plato creado exitosamente";
    public static final String ADD_DISH_BAD_REQUEST = "Solicitud incorrecta. Datos inválidos";
    public static final String ADD_DISH_UNAUTHORIZED = "No autorizado. Token no proporcionado o inválido";
    public static final String ADD_DISH_INTERNAL_SERVER_ERROR = "Error interno del servidor";

    public static final String UPDATE_DISH_SUMMARY = "Actualizar un plato existente";
    public static final String UPDATE_DISH_DESCRIPTION = "Este endpoint permite actualizar la información de un plato específico dentro de un restaurante. Se requiere autenticación.";
    public static final String UPDATE_DISH_SUCCESS = "Plato actualizado exitosamente";
    public static final String UPDATE_DISH_BAD_REQUEST = "Solicitud incorrecta. Datos inválidos";
    public static final String UPDATE_DISH_UNAUTHORIZED = "No autorizado. Token no proporcionado o inválido";
    public static final String UPDATE_DISH_NOT_FOUND = "Plato no encontrado";
    public static final String UPDATE_DISH_INTERNAL_SERVER_ERROR = "Error interno del servidor";

    public static final String UPDATE_STATUS_DISH_SUMMARY = "Actualizar el estado de un plato";
    public static final String UPDATE_STATUS_DISH_DESCRIPTION = "Este endpoint permite actualizar el estado de un plato dentro del restaurante. Se requiere autenticación.";
    public static final String UPDATE_STATUS_DISH_SUCCESS = "Estado del plato actualizado exitosamente";
    public static final String UPDATE_STATUS_DISH_BAD_REQUEST = "Solicitud incorrecta. Datos inválidos";
    public static final String UPDATE_STATUS_DISH_UNAUTHORIZED = "No autorizado. Token no proporcionado o inválido";
    public static final String UPDATE_STATUS_DISH_NOT_FOUND = "Plato no encontrado";
    public static final String UPDATE_STATUS_DISH_INTERNAL_SERVER_ERROR = "Error interno del servidor";

    public static final String GET_DISH_RESTAURANT_SUMMARY = "Obtener todos los platos de un restaurante";
    public static final String GET_DISH_RESTAURANT_DESCRIPTION = "Este endpoint devuelve una lista de platos que pertenecen a un restaurante específico. Se requiere autenticación.";
    public static final String GET_DISH_RESTAURANT_SUCCESS = "Lista de platos obtenida exitosamente";
    public static final String GET_DISH_RESTAURANT_BAD_REQUEST = "Solicitud incorrecta. Parámetros inválidos";
    public static final String GET_DISH_RESTAURANT_UNAUTHORIZED = "No autorizado. Token no proporcionado o inválido";
    public static final String GET_DISH_RESTAURANT_NOT_FOUND = "Restaurante no encontrado";
    public static final String GET_DISH_RESTAURANT_INTERNAL_SERVER_ERROR = "Error interno del servidor";

    public static final String GET_ALL_DISH_SUMMARY = "Obtener todos los platos";
    public static final String GET_ALL_DISH_DESCRIPTION = "Este endpoint devuelve una lista de todos los platos disponibles en la plataforma. Se requiere autenticación.";
    public static final String GET_ALL_DISH_SUCCESS = "Lista de platos obtenida exitosamente";
    public static final String GET_ALL_DISH_BAD_REQUEST = "Solicitud incorrecta. Parámetros inválidos";
    public static final String GET_ALL_DISH_UNAUTHORIZED = "No autorizado. Token no proporcionado o inválido";
    public static final String GET_ALL_DISH_INTERNAL_SERVER_ERROR = "Error interno del servidor";

    public static final String GET_DISH_BY_ID_SUMMARY = "Obtener un plato por ID";
    public static final String GET_DISH_BY_ID_DESCRIPTION = "Este endpoint permite obtener los detalles de un plato específico a partir de su ID. Se requiere autenticación.";
    public static final String GET_DISH_BY_ID_SUCCESS = "Plato obtenido exitosamente";
    public static final String GET_DISH_BY_ID_BAD_REQUEST = "Solicitud incorrecta. ID inválido";
    public static final String GET_DISH_BY_ID_UNAUTHORIZED = "No autorizado. Token no proporcionado o inválido";
    public static final String GET_DISH_BY_ID_NOT_FOUND = "Plato no encontrado";
    public static final String GET_DISH_BY_ID_INTERNAL_SERVER_ERROR = "Error interno del servidor";

    public static final String DISH_TAG_NAME = "Dish";
    public static final String DISH_TAG_DESCRIPTION = "API para gestionar los platos del restaurante";

    public static final String RESPONSE_CODE_200 = "200";
    public static final String RESPONSE_CODE_400 = "400";
    public static final String RESPONSE_CODE_401 = "401";
    public static final String RESPONSE_CODE_404 = "404";
    public static final String RESPONSE_CODE_500 = "500";

    public static final String PARAMETER_DESCRIPTION_RESTAURANT_ID = "ID del restaurante cuyos platos se desean obtener";
    public static final String GET_DISH_BY_ID_PARAM_DESCRIPTION = "ID único del plato que se desea buscar";
    public static final String UPDATE_DISH_STATUS_PARAM_DESCRIPTION = "ID del plato cuyo estado será actualizado";
    public static final String DISH_REQUEST_BODY_DESCRIPTION = "Datos del plato a registrar";
    public static final String DISH_ID_UPDATE_DESCRIPTION = "ID del plato a actualizar";
    public static final String DISH_DATA_REGISTRATION_DESCRIPTION = "Datos del plato a registrar";

    public static final String BASE_PATH = "/api/dishes";

    public static final String ADD_DISH = "/add-dish";
    public static final String UPDATE_DISH = "/{dishId}/update-dish";
    public static final String UPDATE_DISH_STATUS = "/{dishId}/update-dish-status";
    public static final String GET_DISH_BY_RESTAURANT = "/{id}/dishRestaurant";
    public static final String LIST_ALL_DISHES = "/listAllDish";
    public static final String FIND_DISH_BY_ID = "/{id}/findDish";
    public static final String HAS_AUTHORITY_PROPIETARIO = "hasAuthority('PROPIETARIO')";
    public static final String MEDIA_TYPE_JSON = "application/json";

    public static final String DISH_RESTAURANT_CACHE = "dishRestaurantCache";
    public static final String CACHE_KEY_FIND_DISH_ID = "#findDishId";
    public static final String CACHE_KEY_FIND_RESTAURANT_ID = "#findRestaurantId";

    public static final String DISH_ID = "dishId";
    public static final String ID = "id";

    private DishRestControllerConstants(){}

}

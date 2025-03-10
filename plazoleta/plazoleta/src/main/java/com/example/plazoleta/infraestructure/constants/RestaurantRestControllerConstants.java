package com.example.plazoleta.infraestructure.constants;

public class RestaurantRestControllerConstants {

    public static final String RESTAURANT_API_TAG_NAME = "Restaurant";
    public static final String RESTAURANT_API_DESCRIPTION = "API para la gestión de restaurantes";
    // responseCodes
    public static final String RESPONSE_CODE_200 = "200";
    public static final String RESPONSE_CODE_400 = "400";
    public static final String RESPONSE_CODE_401 = "401";
    public static final String RESPONSE_CODE_500 = "500";
    public static final String RESPONSE_CODE_404 = "404";


    // @Operation
    public static final String REGISTER_RESTAURANT_SUMMARY = "Registrar un nuevo restaurante";
    public static final String REGISTER_RESTAURANT_DESCRIPTION = "Este endpoint permite registrar un nuevo restaurante en el sistema. Se requiere autenticación mediante un token de autorización.";

    // @ApiResponses
    public static final String RESPONSE_CODE_200_DESCRIPTION = "Restaurante registrado exitosamente";
    public static final String RESPONSE_CODE_400_DESCRIPTION = "Solicitud inválida, datos faltantes o incorrectos";
    public static final String RESPONSE_CODE_401_DESCRIPTION = "No autorizado, el token de autenticación es inválido o ha expirado";
    public static final String RESPONSE_CODE_500_DESCRIPTION = "Error interno del servidor";

    public static final String REQUEST_BODY_DESCRIPTION = "Datos del restaurante a registrar";
    public static final boolean REQUEST_BODY_REQUIRED = true;
    public static final String MEDIA_TYPE_JSON = "application/json";

    public static final String OPERATION_LIST_RESTAURANTS_SUMMARY = "Listar todos los restaurantes";
    public static final String OPERATION_LIST_RESTAURANTS_DESCRIPTION = "Este endpoint permite obtener una lista de todos los restaurantes registrados en el sistema.";

    public static final String RESPONSE_DESCRIPTION_200 = "Lista de restaurantes obtenida exitosamente";
    public static final String CHECK_RESTAURANT_EXISTS_DESCRIPTION = "Devuelve `true` si el restaurante existe, `false` en caso contrario";
    public static final String RESTAURANT_NOT_FOUND_DESCRIPTION = "Restaurante no encontrado";
    public static final String VERIFY_RESTAURANT_EXISTENCE_SUMMARY = "Verificar existencia de un restaurante";
    public static final String VERIFY_RESTAURANT_EXISTENCE_DESCRIPTION = "Este endpoint permite verificar si un restaurante existe en el sistema mediante su ID.";
    public static final String PARAMETER_RESTAURANT_ID_DESCRIPTION = "ID del restaurante a verificar";
    public static final String VERIFY_RESTAURANT_BY_NIT_DESCRIPTION = "Este endpoint permite verificar si un restaurante existe en el sistema mediante su NIT.";
    public static final String RESTAURANT_FOUND_DESCRIPTION = "Restaurante encontrado";
    public static final String SEARCH_RESTAURANT_BY_ID_SUMMARY = "Buscar un restaurante por ID";
    public static final String SEARCH_RESTAURANT_BY_ID_DESCRIPTION = "Este endpoint permite obtener la información de un restaurante en base a su identificador único.";
    public static final String GET_ALL_RESTAURANTS_PAGINATED_SUMMARY = "Obtener todos los restaurantes paginados y ordenados";
    public static final String GET_ALL_RESTAURANTS_PAGINATED_DESCRIPTION = "Este endpoint permite obtener una lista de restaurantes con paginación y ordenación ascendente por nombre.";
    public static final String PAGE_DESCRIPTION = "Número de página (empezando desde 0)";
    public static final String PAGE_EXAMPLE = "0";
    public static final String PAGE_NAME = "page";


    public static final String SIZE_DESCRIPTION = "Cantidad de elementos por página";
    public static final String SIZE_EXAMPLE = "10";
    public static final String SIZE_NAME = "size";


    public static final String SORT_DESCRIPTION = "Criterio de ordenación en formato `campo,asc|desc`";
    public static final String SORT_EXAMPLE = "name,asc";
    public static final String SORT_NAME = "sort";
    public static final String GET_RESTAURANTS_SUCCESS_DESCRIPTION = "Lista de restaurantes obtenida exitosamente";
    public static final String PAGINATION_AND_SORTING_DESCRIPTION = "Parámetros de paginación y ordenación";


    public static final String BASE_PATH = "/api/restaurant";

    public static final String ADD_RESTAURANT = "/addRestaurant";
    public static final String LIST_RESTAURANTS = "/listRestaurant";
    public static final String EXISTS_RESTAURANT_BY_ID = "/existsRestaurant/{id}";
    public static final String EXISTS_RESTAURANT_BY_NIT = "/existsNit/{nit}";
    public static final String FIND_RESTAURANT_BY_ID = "/findRestaurant/{id}";
    public static final String LIST_RESTAURANTS_PAGEABLE = "/listPageable";
    public static final String ASIGNED_EMPLEADO_RESTAURANT = "/asignar-empleado-restaurant";
    public static final String PATH_VARIABLE_ID = "id";
    public static final String PATH_VARIABLE_NIT = "nit";
    public static final String HAS_AUTHORITY_ADMIN = "hasAuthority('ADMIN')";
    public static final String HAS_AUTHORITY_PROPIETARIO = "hasAuthority('PROPIETARIO')";

    private RestaurantRestControllerConstants(){}


}

package com.example.plazoleta.domain.validations;

import com.example.plazoleta.domain.constants.PedidoUseCaseConstants;
import com.example.plazoleta.domain.exception.AsignarEmpleadoRestaurantException;
import com.example.plazoleta.domain.exception.AsignarEmpleadoRestaurantExceptionType;
import com.example.plazoleta.domain.model.EmpleadoRestaurant;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IEmpleadoRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IUserClientPort;

public class EmpleadoRestaurantCaseValidation {

    private final IUserClientPort userClientPort;
    private final IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort;


    public EmpleadoRestaurantCaseValidation(IUserClientPort userClientPort,
                                            IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort) {
        this.userClientPort = userClientPort;
        this.empleadoRestaurantPersistencePort = empleadoRestaurantPersistencePort;
    }

    public void validatePropietarioRestaurant(Long userAuthenticatedId, Restaurant foundRestaurant){

        if(!foundRestaurant.getId_proprietary().equals(userAuthenticatedId)){
            throw new AsignarEmpleadoRestaurantException(AsignarEmpleadoRestaurantExceptionType.PROPIETARIO_DOES_NOT_OWN_RESTAURANT);
        }

    }

    public void validateExistsUserById(EmpleadoRestaurant asignarEmpleadoRestaurant){

        if (!userClientPort.existsUserById(asignarEmpleadoRestaurant.getUserId())) {
            throw new AsignarEmpleadoRestaurantException(
                    AsignarEmpleadoRestaurantExceptionType.NOT_EXISTS_USER);
        }
    }

    public void validateExistsUserAssignedToRestaurant(EmpleadoRestaurant asignarEmpleadoRestaurant){

        if (empleadoRestaurantPersistencePort.existsByUserIdAndRestaurantId(
                asignarEmpleadoRestaurant.getUserId(), asignarEmpleadoRestaurant.getRestaurantId())) {
            throw new AsignarEmpleadoRestaurantException(
                    AsignarEmpleadoRestaurantExceptionType.EMPLEADO_ALREADY_ASSIGNED_TO_RESTAURANT);
        }
    }

    public void validateExistsUserAssignedSomeRestaurant(EmpleadoRestaurant asignarEmpleadoRestaurant){

        if (empleadoRestaurantPersistencePort.existsByUserId(
                asignarEmpleadoRestaurant.getUserId())) {
            throw new AsignarEmpleadoRestaurantException(
                    AsignarEmpleadoRestaurantExceptionType.EMPLEADO_ALREADY_ASSIGNED);
        }
    }

    public void validateUserRolEmpleado(EmpleadoRestaurant asignarEmpleadoRestaurant){
        User userAuthenticated = userClientPort.getUserById(asignarEmpleadoRestaurant.getUserId());

        if(!userAuthenticated.getRol().equals(PedidoUseCaseConstants.ROLE_EMPLOYEE)){
            throw new AsignarEmpleadoRestaurantException(AsignarEmpleadoRestaurantExceptionType.USER_NOT_ROL_EMPLEADO);
        }
    }
}

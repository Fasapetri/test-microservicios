package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.api.IEmpleadoRestaurantServicePort;
import com.example.plazoleta.domain.constants.EmpleadoRestaurantConstants;
import com.example.plazoleta.domain.model.EmpleadoRestaurant;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.spi.IEmpleadoRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.ISecurityContextPort;
import com.example.plazoleta.domain.validations.EmpleadoRestaurantCaseValidation;

public class EmpleadoRestaurantUseCase implements IEmpleadoRestaurantServicePort {

    private final IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort;
    private final ISecurityContextPort securityContextPort;
    private final EmpleadoRestaurantCaseValidation empleadoRestaurantCaseValidation;
    private final IRestaurantPersistencePort restaurantPersistencePort;


    public EmpleadoRestaurantUseCase(IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort,
                                     ISecurityContextPort securityContextPort,
                                     EmpleadoRestaurantCaseValidation empleadoRestaurantCaseValidation,
                                     IRestaurantPersistencePort restaurantPersistencePort) {
        this.empleadoRestaurantPersistencePort = empleadoRestaurantPersistencePort;
        this.securityContextPort = securityContextPort;
        this.empleadoRestaurantCaseValidation = empleadoRestaurantCaseValidation;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public String asignarEmpleadoARestaurant(EmpleadoRestaurant asignarEmpleadoRestaurant) {
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Restaurant foundRestaurant = restaurantPersistencePort.findById(asignarEmpleadoRestaurant.getRestaurantId());

        empleadoRestaurantCaseValidation.validatePropietarioRestaurant(userAuthenticatedId, foundRestaurant);
        empleadoRestaurantCaseValidation.validateExistsUserById(asignarEmpleadoRestaurant);
        empleadoRestaurantCaseValidation.validateUserRolEmpleado(asignarEmpleadoRestaurant);
        empleadoRestaurantCaseValidation.validateExistsUserAssignedToRestaurant(asignarEmpleadoRestaurant);
        empleadoRestaurantCaseValidation.validateExistsUserAssignedSomeRestaurant(asignarEmpleadoRestaurant);

         empleadoRestaurantPersistencePort.save(asignarEmpleadoRestaurant);

        return EmpleadoRestaurantConstants.EMPLEADO_SUCCESSFULLY_ASSIGNED_TO_RESTURANT;

    }
}

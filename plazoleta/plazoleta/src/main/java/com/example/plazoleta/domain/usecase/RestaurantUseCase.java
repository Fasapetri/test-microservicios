package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.api.IRestaurantServicePort;
import com.example.plazoleta.domain.exception.RestaurantException;
import com.example.plazoleta.domain.exception.RestaurantExceptionType;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.ISecurityContextPort;
import com.example.plazoleta.domain.spi.IUserClientPort;
import com.example.plazoleta.domain.validations.RestaurantUseCaseValidation;
import com.example.plazoleta.infraestructure.output.jpa.exception.RestaurantEntityException;
import com.example.plazoleta.infraestructure.output.jpa.exception.RestaurantEntityExceptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;
    private final RestaurantUseCaseValidation restaurantUseCaseValidation;

    public RestaurantUseCase(IRestaurantPersistencePort iRestaurantPersistencePort, IUserClientPort iUserClientPort, RestaurantUseCaseValidation restaurantUseCaseValidation) {
        this.restaurantPersistencePort = iRestaurantPersistencePort;
        this.userClientPort = iUserClientPort;
        this.restaurantUseCaseValidation = restaurantUseCaseValidation;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurantToCreate) {

        User foundRestaurantPropietario = userClientPort.getUserById(restaurantToCreate.getId_proprietary());

        restaurantUseCaseValidation.validationCreateRestaurant(foundRestaurantPropietario, restaurantToCreate);

        if(restaurantPersistencePort.existsByNit(restaurantToCreate.getNit())){
            throw new RestaurantException(RestaurantExceptionType.NIT_RESTAURANT_ALREADY_EXISTS);
        }

        return restaurantPersistencePort.saveRestaurant(restaurantToCreate);
    }

    @Override
    public List<Restaurant> getAllrestaurant() {
        return Optional.ofNullable(restaurantPersistencePort.getAllrestaurant())
                .orElseThrow(() -> new RestaurantEntityException(RestaurantEntityExceptionType.RESTAURANT_NOT_DATA));

    }

    @Override
    public boolean existsRestaurant(Long findRestaurantId) {
        return restaurantPersistencePort.existsRestaurant(findRestaurantId);
    }

    @Override
    public boolean existsByNit(String findRestaurantNit) {
        return restaurantPersistencePort.existsByNit(findRestaurantNit);
    }

    @Override
    public Restaurant findById(Long findRestaurantId) {
       return  Optional.ofNullable(restaurantPersistencePort.findById(findRestaurantId))
                .orElseThrow(() -> new RestaurantException(RestaurantExceptionType.RESTAURANT_NOT_FOUND));

    }

    @Override
    public Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable) {
        return Optional.ofNullable(restaurantPersistencePort.findAllByOrderByNameAsc(pageable))
                .orElseThrow(() -> new RestaurantException(RestaurantExceptionType.RESTAURANT_NOT_DATA));

    }
}

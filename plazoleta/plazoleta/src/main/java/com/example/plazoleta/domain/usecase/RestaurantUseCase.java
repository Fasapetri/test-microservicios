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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;
    private final ISecurityContextPort securityContextPort;
    private final RestaurantUseCaseValidation restaurantUseCaseValidation;

    public RestaurantUseCase(IRestaurantPersistencePort iRestaurantPersistencePort, IUserClientPort iUserClientPort, ISecurityContextPort iSecurityContextPort, RestaurantUseCaseValidation restaurantUseCaseValidation) {
        this.restaurantPersistencePort = iRestaurantPersistencePort;
        this.userClientPort = iUserClientPort;
        this.securityContextPort = iSecurityContextPort;
        this.restaurantUseCaseValidation = restaurantUseCaseValidation;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurantToCreate) {
        String userAuthenticatedRol = securityContextPort.getUserAuthenticateRol();

        User foundRestaurantPropietario = userClientPort.getUserById(restaurantToCreate.getId_proprietary());

        if(restaurantPersistencePort.existsByNit(restaurantToCreate.getNit())){
            throw new RestaurantException(RestaurantExceptionType.NIT_RESTAURANT_ALREADY_EXISTS);
        }

        restaurantUseCaseValidation.ValidationCreateRestaurant(userAuthenticatedRol, foundRestaurantPropietario, restaurantToCreate);
        return restaurantPersistencePort.saveRestaurant(restaurantToCreate);
    }

    @Override
    public List<Restaurant> getAllrestaurant() {
        List<Restaurant> listAllRestaurant = restaurantPersistencePort.getAllrestaurant();
        if(listAllRestaurant.isEmpty()){
            throw new RestaurantException(RestaurantExceptionType.RESTAURANT_NOT_DATA);
        }
        return listAllRestaurant;
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
        Restaurant foundRestaurant = restaurantPersistencePort.findById(findRestaurantId);
        if(foundRestaurant == null){
            throw new RestaurantException(RestaurantExceptionType.RESTAURANT_NOT_FOUND);
        }
        return foundRestaurant;
    }

    @Override
    public Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable) {
        Page<Restaurant> paginatorFindAllRestaurant = restaurantPersistencePort.findAllByOrderByNameAsc(pageable);
        if(paginatorFindAllRestaurant.isEmpty()){
            throw new RestaurantException(RestaurantExceptionType.RESTAURANT_NOT_DATA);
        }
        return paginatorFindAllRestaurant;
    }
}

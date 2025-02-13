package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.spi.IJwtServicePort;
import com.example.plazoleta.domain.api.IRestaurantServicePort;
import com.example.plazoleta.domain.exception.RestaurantException;
import com.example.plazoleta.domain.exception.RestaurantExceptionType;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IUserClientPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.regex.Pattern;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort iRestaurantPersistencePort;
    private final IJwtServicePort iJwtServicePort;
    private final IUserClientPort iUserClientPort;

    public RestaurantUseCase(IRestaurantPersistencePort iRestaurantPersistencePort, IJwtServicePort iJwtServicePort, IUserClientPort iUserClientPort) {
        this.iRestaurantPersistencePort = iRestaurantPersistencePort;
        this.iJwtServicePort = iJwtServicePort;
        this.iUserClientPort = iUserClientPort;
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant, String token) {
        User loginUser = iJwtServicePort.validateToken(token);

        if (!"ADMIN".equalsIgnoreCase(loginUser.getRol())) {
            throw new RestaurantException(RestaurantExceptionType.INVALID_ROL_CREATED_RESTAURANT);
        }

        User validateUserPropietario = iUserClientPort.getUserById(restaurant.getId_proprietary(), token);

        if(validateUserPropietario == null || !"PROPIETARIO".equals(validateUserPropietario.getRol())){
            throw new RestaurantException(RestaurantExceptionType.INVALID_ROL_PROPIETARIO);
        }

        if(iRestaurantPersistencePort.existsByNit(restaurant.getNit())){
            throw new RestaurantException(RestaurantExceptionType.NIT_RESTAURANT_ALREADY_EXISTS);
        }

        if(Pattern.matches("^\\d+$", restaurant.getName())){
            throw new RestaurantException(RestaurantExceptionType.INVALID_NAME_RESTAURANT);
        }

        if(!restaurant.getPhone().matches("^\\+?\\d{1,13}$")){
            throw new RestaurantException(RestaurantExceptionType.INVALID_PHONE_RESTAURANT);
        }
        return iRestaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllrestaurant() {
        return iRestaurantPersistencePort.getAllrestaurant();
    }

    @Override
    public boolean existsRestaurant(Long idRestaurant, String token) {
        return iRestaurantPersistencePort.existsRestaurant(idRestaurant);
    }

    @Override
    public boolean existsByNit(String nit) {
        return iRestaurantPersistencePort.existsByNit(nit);
    }

    @Override
    public Restaurant findById(Long idRestaurant) {
        return iRestaurantPersistencePort.findById(idRestaurant);
    }

    @Override
    public Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable) {
        return iRestaurantPersistencePort.findAllByOrderByNameAsc(pageable);
    }
}

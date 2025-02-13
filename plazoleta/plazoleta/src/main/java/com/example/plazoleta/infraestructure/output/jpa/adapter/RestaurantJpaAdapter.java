package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.infraestructure.output.jpa.entity.RestaurantEntity;
import com.example.plazoleta.infraestructure.output.jpa.exception.RestaurantEntityException;
import com.example.plazoleta.infraestructure.output.jpa.exception.RestaurantEntityExceptionType;
import com.example.plazoleta.infraestructure.output.jpa.mapper.RestaurantEntityMapper;
import com.example.plazoleta.infraestructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository iRestaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        if(iRestaurantRepository.existsByNit(restaurant.getNit())){
            throw new RestaurantEntityException(RestaurantEntityExceptionType.EXIST_RESTAURANT);
        }
        RestaurantEntity restaurantEntity = iRestaurantRepository.save(restaurantEntityMapper.toRestaurantEntity(restaurant));
        return restaurantEntityMapper.toRestaurant(restaurantEntity);
    }

    @Override
    public List<Restaurant> getAllrestaurant() {
        List<Restaurant> listRestaurant = restaurantEntityMapper.toListRestaurant(iRestaurantRepository.findAll());
        if(listRestaurant.isEmpty()){
            throw new RestaurantEntityException(RestaurantEntityExceptionType.RESTAURANT_NOT_DATA);
        }
        return listRestaurant;
    }

    @Override
    public boolean existsRestaurant(Long idRestaurant) {
        return iRestaurantRepository.existsById(idRestaurant);
    }

    @Override
    public boolean existsByNit(String nit) {
        return iRestaurantRepository.existsByNit(nit);
    }

    @Override
    public Restaurant findById(Long idRestaurant) {
        return restaurantEntityMapper.toRestaurant(iRestaurantRepository.findById(idRestaurant)
                .orElseThrow(() -> new RestaurantEntityException(RestaurantEntityExceptionType.RESTAURANT_NOT_FOUND)));
    }

    @Override
    public Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable) {
        Page<RestaurantEntity> restaurantEntities = iRestaurantRepository.findAllByOrderByNameAsc(pageable);
        return restaurantEntities.map(restaurantEntityMapper::toRestaurant);
    }
}

package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.RestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.application.mapper.RestaurantMapper;
import com.example.plazoleta.domain.api.IRestaurantServicePort;
import com.example.plazoleta.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler{

    private final IRestaurantServicePort restaurantServicePort;
    private final RestaurantMapper restaurantMapper;


    @Override
    public RestaurantResponse saveRestaurant(RestaurantRequest restaurantToCreate) {
        Restaurant mapperRestaurant = restaurantMapper.toRestaurant(restaurantToCreate);
        restaurantServicePort.saveRestaurant(mapperRestaurant);
        return restaurantMapper.toRestaurantResponse(mapperRestaurant);
    }

    @Override
    public List<RestaurantResponse> getAllrestaurant() {
        return restaurantMapper.toListRestaurantResponse(restaurantServicePort.getAllrestaurant());
    }

    @Override
    public boolean existsRestaurant(Long findRestaurantId) {
        return restaurantServicePort.existsRestaurant(findRestaurantId);
    }

    @Override
    public boolean existsByNit(String findRestaurantNit) {
        return restaurantServicePort.existsByNit(findRestaurantNit);
    }

    @Override
    public RestaurantResponse findById(Long findRestaurantId) {
        return restaurantMapper.toRestaurantResponse(restaurantServicePort.findById(findRestaurantId));
    }

    @Override
    public Page<RestaurantResponse> findAllByOrderByNameAsc(Pageable pageable) {
        return restaurantServicePort.findAllByOrderByNameAsc(pageable)
                .map(restaurantMapper::toRestaurantResponse);
    }
}

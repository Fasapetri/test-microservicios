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

    private final IRestaurantServicePort iRestaurantServicePort;
    private final RestaurantMapper restaurantMapper;


    @Override
    public RestaurantResponse saveRestaurant(RestaurantRequest restaurantRequest, String token) {
        Restaurant restaurant = restaurantMapper.toRestaurant(restaurantRequest);
        iRestaurantServicePort.saveRestaurant(restaurant, token);
        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    @Override
    public List<RestaurantResponse> getAllrestaurant() {
        return restaurantMapper.toListRestaurantResponse(iRestaurantServicePort.getAllrestaurant());
    }

    @Override
    public boolean existsRestaurant(Long idRestaurant, String token) {
        return iRestaurantServicePort.existsRestaurant(idRestaurant, token);
    }

    @Override
    public boolean existsByNit(String nit) {
        return iRestaurantServicePort.existsByNit(nit);
    }

    @Override
    public RestaurantResponse findById(Long idRestaurant) {
        return restaurantMapper.toRestaurantResponse(iRestaurantServicePort.findById(idRestaurant));
    }

    @Override
    public Page<RestaurantResponse> findAllByOrderByNameAsc(Pageable pageable) {
        return iRestaurantServicePort.findAllByOrderByNameAsc(pageable)
                .map(restaurantMapper::toRestaurantResponse);
    }
}

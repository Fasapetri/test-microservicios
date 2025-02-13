package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.RestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantHandler {

    RestaurantResponse saveRestaurant(RestaurantRequest restaurantRequest, String token);

    List<RestaurantResponse> getAllrestaurant();

    boolean existsRestaurant(Long idRestaurant, String token);

    boolean existsByNit(String nit);

    RestaurantResponse findById(Long idRestaurant);

    Page<RestaurantResponse> findAllByOrderByNameAsc(Pageable pageable);
}

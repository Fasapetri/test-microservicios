package com.example.plazoleta.domain.api;

import com.example.plazoleta.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantServicePort {

    Restaurant saveRestaurant(Restaurant restaurant, String token);

    List<Restaurant> getAllrestaurant(String token);

    boolean existsRestaurant(Long idRestaurant, String token);

    boolean existsByNit(String nit);

    Restaurant findById(Long idRestaurant);

    Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable);
}

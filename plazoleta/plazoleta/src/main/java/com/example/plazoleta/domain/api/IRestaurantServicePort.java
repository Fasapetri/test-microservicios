package com.example.plazoleta.domain.api;

import com.example.plazoleta.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantServicePort {

    Restaurant saveRestaurant(Restaurant restaurant);

    List<Restaurant> getAllrestaurant();

    boolean existsRestaurant(Long idRestaurant);

    boolean existsByNit(String nit);

    Restaurant findById(Long idRestaurant);

    Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable);
}

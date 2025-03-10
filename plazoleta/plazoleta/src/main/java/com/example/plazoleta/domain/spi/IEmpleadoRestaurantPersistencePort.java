package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.EmpleadoRestaurant;

public interface IEmpleadoRestaurantPersistencePort {

    boolean existsByUserIdAndRestaurantId(Long findUserId, Long findRestaurantId);

    void save(EmpleadoRestaurant asignarEmpleadoRestaurant);

    boolean existsByUserId(Long findUserId);
}

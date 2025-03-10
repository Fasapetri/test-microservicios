package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.infraestructure.output.jpa.entity.RestaurantEntity;
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
    public Restaurant saveRestaurant(Restaurant restaurantToCreate) {
        RestaurantEntity mapperRestaurantEntity = iRestaurantRepository.save(restaurantEntityMapper.toRestaurantEntity(restaurantToCreate));
        return restaurantEntityMapper.toRestaurant(mapperRestaurantEntity);
    }

    @Override
    public List<Restaurant> getAllrestaurant() {
        return restaurantEntityMapper.toListRestaurant(iRestaurantRepository.findAll());
    }

    @Override
    public boolean existsRestaurant(Long findRestaurantId) {
        return iRestaurantRepository.existsById(findRestaurantId);
    }

    @Override
    public boolean existsByNit(String findRestaurantNit) {
        return iRestaurantRepository.existsByNit(findRestaurantNit);
    }

    @Override
    public Restaurant findById(Long findRestaurantId) {
        return restaurantEntityMapper.toRestaurant(iRestaurantRepository.findById(findRestaurantId).orElse(null));
    }

    @Override
    public Page<Restaurant> findAllByOrderByNameAsc(Pageable pageable) {
        Page<RestaurantEntity> listRestaurantEntities = iRestaurantRepository.findAllByOrderByNameAsc(pageable);
        return listRestaurantEntities.map(restaurantEntityMapper::toRestaurant);
    }

    @Override
    public List<Restaurant> findRestaurantByPropietarioId(Long propietarioId) {
        return restaurantEntityMapper.toListRestaurant(iRestaurantRepository.findByProprietaryId(propietarioId));
    }
}

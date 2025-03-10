package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.EmpleadoRestaurant;
import com.example.plazoleta.domain.spi.IEmpleadoRestaurantPersistencePort;
import com.example.plazoleta.infraestructure.output.jpa.entity.EmpleadoRestaurantEntity;
import com.example.plazoleta.infraestructure.output.jpa.mapper.EmpleadoRestaurantEntityMapper;
import com.example.plazoleta.infraestructure.output.jpa.repository.IEmpleadoRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpladoRestaurantJpaAdapter implements IEmpleadoRestaurantPersistencePort {

    private final IEmpleadoRestaurantRepository empleadoRestaurantRepository;
    private final EmpleadoRestaurantEntityMapper empleadoRestaurantEntityMapper;

    @Override
    public boolean existsByUserIdAndRestaurantId(Long findUserId, Long findRestaurantId) {
        return empleadoRestaurantRepository.existsByUserIdAndRestaurantId(findUserId,
                findRestaurantId);
    }

    @Override
    public void save(EmpleadoRestaurant asignarEmpleadoRestaurant) {
        EmpleadoRestaurantEntity mapperEntity = empleadoRestaurantEntityMapper
                .toEmpleadoRestaurantEntity(asignarEmpleadoRestaurant);
        empleadoRestaurantRepository.save(mapperEntity);
    }

    @Override
    public boolean existsByUserId(Long findUserId) {
        return empleadoRestaurantRepository.existsByUserId(findUserId);
    }
}

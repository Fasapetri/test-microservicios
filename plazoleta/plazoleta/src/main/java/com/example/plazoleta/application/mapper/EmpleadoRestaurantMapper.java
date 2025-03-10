package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.EmpleadoRestaurantRequest;
import com.example.plazoleta.domain.model.EmpleadoRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmpleadoRestaurantMapper {

    EmpleadoRestaurantMapper INSTANCE = Mappers.getMapper(EmpleadoRestaurantMapper.class);

    @Mapping(target = "id", ignore = true)
    EmpleadoRestaurant toEmpleadoRestaurant(EmpleadoRestaurantRequest empleadoRestaurantRequest);
}

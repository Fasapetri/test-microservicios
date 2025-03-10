package com.example.plazoleta.infraestructure.output.jpa.mapper;

import com.example.plazoleta.domain.model.EmpleadoRestaurant;
import com.example.plazoleta.infraestructure.output.jpa.entity.EmpleadoRestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EmpleadoRestaurantEntityMapper {

    EmpleadoRestaurantEntityMapper INSTANCE = Mappers.getMapper(EmpleadoRestaurantEntityMapper.class);

    EmpleadoRestaurantEntity toEmpleadoRestaurantEntity(EmpleadoRestaurant empleadoRestaurant);

}

package com.example.plazoleta.application;

import com.example.plazoleta.domain.Dish;
import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.domain.PlazoletaPort;
import com.example.plazoleta.infraestructure.dto.DishRequest;
import com.example.plazoleta.infraestructure.dto.DishResponse;
import com.example.plazoleta.infraestructure.dto.DishUpdateRequest;
import com.example.plazoleta.infraestructure.repository.DishRepository;
import com.example.plazoleta.infraestructure.user.AuthClient;
import com.example.plazoleta.infraestructure.user.UserClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final PlazoletaPort plazoletaPort;
    private final AuthServiceUser authServiceUser;

    public DishService(DishRepository dishRepository, PlazoletaPort plazoletaPort, AuthServiceUser authServiceUser){
        this.dishRepository = dishRepository;
        this.plazoletaPort = plazoletaPort;
        this.authServiceUser = authServiceUser;
    }

    @Transactional
    public Dish addDish(DishRequest dishRequest, String authenticatedUserRole){

        AuthClient.AuthenticateUser userAuthenticated = authServiceUser.getUserRoleFromToken(authenticatedUserRole);

        if (!"PROPIETARIO".equalsIgnoreCase(userAuthenticated.getRol())) {
            throw new IllegalArgumentException("Solo los usuarios con rol PROPIETARIO pueden registrar platos.");
        }

        Plazoleta plazoleta = plazoletaPort.findById(dishRequest.getId_restaurant())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado."));

        if (!plazoleta.getId_proprietary().equals(userAuthenticated.getId())) {
            throw new IllegalArgumentException("El restaurante no pertenece al propietario autenticado.");
        }

        Dish dish = Dish.builder()
                .name(dishRequest.getName())
                .active(true)
                .category(dishRequest.getCategory())
                .description(dishRequest.getDescription())
                .price(dishRequest.getPrice())
                .url_image(dishRequest.getUrl_image())
                .plazoleta(plazoleta)
                .build();

        return dishRepository.save(dish);
    }

    @Transactional
    public Dish updateDish(Long id_dish, DishUpdateRequest dishUpdateRequest, String authenticatedUserRole){

        AuthClient.AuthenticateUser userAuthenticated = authServiceUser.getUserRoleFromToken(authenticatedUserRole);

        if(!"PROPIETARIO".equalsIgnoreCase(userAuthenticated.getRol())){
            throw new IllegalArgumentException("Solo los usuarios con rol PROPIETARIO pueden actualizar platos.");
        }

        Dish dish = dishRepository.findById(id_dish)
                .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado"));

        Plazoleta restaurant = dish.getPlazoleta();

        if(!restaurant.getId_proprietary().equals(userAuthenticated.getId())){
            throw new IllegalArgumentException("El restaurante no pertenece al propietario");
        }

        if (dishUpdateRequest.getPrice() != null){
            dish.setPrice(dishUpdateRequest.getPrice());
        }
        if (dishUpdateRequest.getDescription() != null){
            dish.setDescription(dishUpdateRequest.getDescription());
        }

        return dishRepository.save(dish);
    }

    @Transactional
    public Dish updateDishStatus(Long dishId, String token){
        AuthClient.AuthenticateUser userAuthenticated = authServiceUser.getUserRoleFromToken(token);

        if(!"PROPIETARIO".equalsIgnoreCase(userAuthenticated.getRol())){
            throw new IllegalArgumentException("Solo lo usuarios con rol propietario pueden modifcar el estado del plato" + userAuthenticated.toString());
        }

        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new IllegalArgumentException("No existe el plato"));

        if (!dish.getPlazoleta().getId_proprietary().equals(userAuthenticated.getId())){
            throw new IllegalArgumentException("El propietario no pertenece a este restaurante, no puede modificar el estado del plato");
        }

        dish.setActive(!dish.getActive());
        return dishRepository.save(dish);
    }

    @Transactional
    public Map<String, Object> obtenerPlatos(Long id_restaurante, String token){
        AuthClient.AuthenticateUser userAuthenticated = authServiceUser.getUserRoleFromToken(token);

        if(!"EMPLEADO".equalsIgnoreCase(userAuthenticated.getRol()) && !"CLIENTE".equalsIgnoreCase(userAuthenticated.getRol())){
            throw new IllegalArgumentException("No tienes permiso para esta acción");
        }

        List<Dish> listaPlatos = dishRepository.findAllByPlazoletaId(id_restaurante);



        return listaPlatos.stream()
                .collect(Collectors.toMap(
                        dish -> String.valueOf(dish.getId()),
                        dish -> new DishResponse(
                                dish.getId(),
                                dish.getName(),
                                dish.getPrice(),
                                dish.getCategory(),
                                dish.getActive(),
                                dish.getDescription(),
                                dish.getUrl_image()
                        )
                ));
    }
}

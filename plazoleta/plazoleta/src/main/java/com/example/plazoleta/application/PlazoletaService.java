package com.example.plazoleta.application;

import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.domain.PlazoletaPort;
import com.example.plazoleta.infraestructure.dto.PlazoletaResponse;
import com.example.plazoleta.infraestructure.user.AuthClient;
import com.example.plazoleta.infraestructure.user.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PlazoletaService {

    private final PlazoletaPort plazoletaPort;
    private final AuthServiceUser authServiceUser;

    @Autowired
    public PlazoletaService(PlazoletaPort plazoletaPort, AuthServiceUser authServiceUser){
        this.plazoletaPort = plazoletaPort;
        this.authServiceUser = authServiceUser;
    }

    public Plazoleta addPlazoleta(Plazoleta plazoleta, String authenticatedUserRole){

        AuthClient.AuthenticateUser userAuthenticated = authServiceUser.getUserRoleFromToken(authenticatedUserRole);

        if (!"ADMIN".equalsIgnoreCase(userAuthenticated.getRol())) {
            throw new IllegalArgumentException("Solo los usuarios con rol ADMIN pueden registrar restaurantes." + userAuthenticated.getRol());
        }

        UserClient.UserResponse userResponse = authServiceUser.userByIdProprietary(plazoleta.getId_proprietary(), authenticatedUserRole);

        if(userResponse == null || !"PROPIETARIO".equals(userResponse.getRol())){
            throw new IllegalArgumentException("El ID del propietario no es valido o no tiene el rol de propietario");
        }

        if(plazoletaPort.existsByNit(plazoleta.getNit())){
            throw new IllegalArgumentException("El NIT ya está registrado");
        }

        if(Pattern.matches("^\\d+$", plazoleta.getName())){
            throw new IllegalArgumentException("El nombre no puede contener solo números");
        }

        if(!plazoleta.getPhone().matches("^\\+?\\d{1,13}$")){
            throw new IllegalArgumentException("El telefono debe contener un maximo de 13 numeros incluido el +");
        }

        return plazoletaPort.save(plazoleta);
    }

    public Page<PlazoletaResponse> getPlazoletas(Pageable pageable, String token){

        AuthClient.AuthenticateUser authenticateUser = authServiceUser.getUserRoleFromToken(token);

        if(!"CLIENTE".equalsIgnoreCase(authenticateUser.getRol())){
            throw new IllegalArgumentException("No tiene permisos para esta acción");
        }

        Page<Plazoleta> plazoletas= plazoletaPort.findAllByOrderByNameAsc(pageable);

        Page<PlazoletaResponse> plazoletaResponses = plazoletas.map(plazoleta -> {
            PlazoletaResponse response = new PlazoletaResponse();
            response.setName(plazoleta.getName());
            response.setUrlLogo(plazoleta.getUrl_Logo());
            return response;
        });
        return plazoletaResponses;
    }

    public boolean existeRestaurante(Long id, String token){
        AuthClient.AuthenticateUser userAuthenticated = authServiceUser.getUserRoleFromToken(token);
        return plazoletaPort.existsById(id);
    }
}

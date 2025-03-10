package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.handler.UserHandler;
import com.example.users.infraestructure.constants.UserRestControllerConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = UserRestControllerConstants.TAG_NAME, description = UserRestControllerConstants.TAG_DESCRIPTION)
public class UserRestController {

    private final UserHandler userHandler;

    public UserRestController(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Operation(summary = UserRestControllerConstants.CREATE_USER_SUMMARY,
            description = UserRestControllerConstants.CREATE_USER_DESCRIPTION)
    @PostMapping("/create-user-propietario")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponse> createUserPropietario(@RequestBody UserRequest userPorpietarioToCreate){
        UserResponse userPropietarioSaved = userHandler.saveUserPropietario(userPorpietarioToCreate);
        return ResponseEntity.ok(userPropietarioSaved);
    }

    @Operation(summary = UserRestControllerConstants.CREATE_USER_SUMMARY,
            description = UserRestControllerConstants.CREATE_USER_DESCRIPTION)
    @PostMapping("/create-user-empleado")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<UserResponse> createUserEmpleado(@RequestBody UserRequest userEmpleadoToCreate){
        UserResponse userEmpleadoSaved = userHandler.saveUserEmpleado(userEmpleadoToCreate);
        return ResponseEntity.ok(userEmpleadoSaved);
    }

    @Operation(summary = UserRestControllerConstants.CREATE_USER_SUMMARY,
            description = UserRestControllerConstants.CREATE_USER_DESCRIPTION)
    @PostMapping("/create-user-cliente")
    public ResponseEntity<UserResponse> createUserCliente(@RequestBody UserRequest userClienteToCreate){
        UserResponse userClienteSaved = userHandler.saveUserCliente(userClienteToCreate);
        return ResponseEntity.ok(userClienteSaved);
    }

    @Operation(summary = UserRestControllerConstants.FIND_BY_ID_SUMMARY,
            description = UserRestControllerConstants.FIND_BY_ID_DESCRIPTION)
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findById(
            @Parameter(name = UserRestControllerConstants.PARAM_ID,
                    example = UserRestControllerConstants.PARAM_ID_EXAMPLE,
                    required = true)
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(userHandler.findByIdUser(id));
    }

    @Operation(summary = UserRestControllerConstants.UPDATE_USER_SUMMARY,
            description = UserRestControllerConstants.UPDATE_USER_DESCRIPTION)
    @PutMapping("/editUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userHandler.updateUser(userRequest));
    }

    @Operation(summary = UserRestControllerConstants.DELETE_USER_SUMMARY,
            description = UserRestControllerConstants.DELETE_USER_DESCRIPTION)
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(name = UserRestControllerConstants.PARAM_ID,
                    example = UserRestControllerConstants.PARAM_ID_EXAMPLE,
                    required = true)
            @PathVariable("id") Long id) {
        userHandler.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsUserById(@PathVariable("id") Long findUserId){
        return ResponseEntity.ok(userHandler.existsUserById(findUserId));
    }
}


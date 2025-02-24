package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.handler.UserHandler;
import com.example.users.infraestructure.constants.UserRestControllerConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest){
        UserResponse user = userHandler.saveUser(userRequest);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = UserRestControllerConstants.FIND_BY_EMAIL_SUMMARY,
            description = UserRestControllerConstants.FIND_BY_EMAIL_DESCRIPTION)
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(
            @Parameter(name = UserRestControllerConstants.PARAM_EMAIL,
                    example = UserRestControllerConstants.PARAM_EMAIL_EXAMPLE,
                    required = true)
            @PathVariable("email") String email) {
        return ResponseEntity.ok(userHandler.findByEmailUser(email));
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
}


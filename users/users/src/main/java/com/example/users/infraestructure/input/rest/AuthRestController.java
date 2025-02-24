package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;
import com.example.users.application.dto.TokenValidationResponse;
import com.example.users.application.handler.AuthHandler;
import com.example.users.infraestructure.constants.AuthRestControllerConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = AuthRestControllerConstants.TAG_NAME, description = AuthRestControllerConstants.TAG_DESCRIPTION)
public class AuthRestController {

    private final AuthHandler authHandler;

    public AuthRestController(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    @Operation(
            summary = AuthRestControllerConstants.LOGIN_SUMMARY,
            description = AuthRestControllerConstants.LOGIN_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_OK,
                    description = AuthRestControllerConstants.LOGIN_SUCCESS_RESPONSE,
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_BAD_REQUEST,
                    description = AuthRestControllerConstants.LOGIN_BAD_REQUEST_RESPONSE,
                    content = @Content),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_UNAUTHORIZED,
                    description = AuthRestControllerConstants.LOGIN_UNAUTHORIZED_RESPONSE,
                    content = @Content),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_SERVER_ERROR,
                    description = AuthRestControllerConstants.LOGIN_SERVER_ERROR_RESPONSE,
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = AuthRestControllerConstants.LOGIN_REQUEST_DESCRIPTION,
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRequest.class))
    ) @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authHandler.login(authRequest));
    }

    @Operation(
            summary = AuthRestControllerConstants.VALIDATE_TOKEN_SUMMARY,
            description = AuthRestControllerConstants.VALIDATE_TOKEN_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_OK,
                    description = AuthRestControllerConstants.VALIDATE_TOKEN_SUCCESS_RESPONSE,
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenValidationResponse.class))}),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_BAD_REQUEST,
                    description = AuthRestControllerConstants.VALIDATE_TOKEN_BAD_REQUEST_RESPONSE,
                    content = @Content),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_UNAUTHORIZED,
                    description = AuthRestControllerConstants.VALIDATE_TOKEN_UNAUTHORIZED_RESPONSE,
                    content = @Content),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_SERVER_ERROR,
                    description = AuthRestControllerConstants.VALIDATE_TOKEN_SERVER_ERROR_RESPONSE,
                    content = @Content)
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader(AuthRestControllerConstants.AUTHORIZATION_HEADER) String token) {
        return ResponseEntity.ok(authHandler.validateToken(token.replace(AuthRestControllerConstants.BEARER_PREFIX, "")));
    }

    @Operation(
            summary = AuthRestControllerConstants.INVALIDATE_TOKEN_SUMMARY,
            description = AuthRestControllerConstants.INVALIDATE_TOKEN_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_OK,
                    description = AuthRestControllerConstants.INVALIDATE_TOKEN_SUCCESS,
                    content = @Content),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_BAD_REQUEST,
                    description = AuthRestControllerConstants.INVALIDATE_TOKEN_NOT_FOUND,
                    content = @Content)
    })
    @PostMapping("/invalidate")
    public ResponseEntity<String> invalidateToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AuthRestControllerConstants.AUTHORIZATION_HEADER);
        String token = authHandler.extractToken(authorizationHeader);

        if (token == null) {
            return ResponseEntity.badRequest().body(AuthRestControllerConstants.INVALIDATE_TOKEN_NOT_FOUND);
        }

        authHandler.logoutUser(token.replace(AuthRestControllerConstants.BEARER_PREFIX, ""));
        return ResponseEntity.ok(AuthRestControllerConstants.INVALIDATE_TOKEN_SUCCESS);
    }

    @Operation(
            summary = AuthRestControllerConstants.CHECK_INVALID_TOKEN_SUMMARY,
            description = AuthRestControllerConstants.CHECK_INVALID_TOKEN_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_OK,
                    description = AuthRestControllerConstants.CHECK_INVALID_TOKEN_RESPONSE,
                    content = @Content),
            @ApiResponse(responseCode = AuthRestControllerConstants.STATUS_BAD_REQUEST,
                    description = AuthRestControllerConstants.CHECK_INVALID_TOKEN_BAD_REQUEST,
                    content = @Content)
    })
    @GetMapping("/is-invalid")
    public ResponseEntity<Boolean> isTokenInvalid(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AuthRestControllerConstants.AUTHORIZATION_HEADER);
        String token = authHandler.extractToken(authorizationHeader);

        if (token == null) {
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(authHandler.isTokenRevoked(token.replace(AuthRestControllerConstants.BEARER_PREFIX, "")));
    }
}

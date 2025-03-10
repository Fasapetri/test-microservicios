package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.handler.UserHandler;
import com.example.users.domain.model.User;
import com.example.users.infraestructure.security.JwtFilter;
import com.example.users.infraestructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserHandler userHandler;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter jwtFilter;


    private UserResponse userResponse;
    private UserRequest userRequest;
    private String token;
    private User userLogin;

    @BeforeEach
    void setUp() {

        userLogin= new User();
        userLogin.setId(1L);
        userLogin.setRol("ADMIN");
        userLogin.setEmail("admin@example.com");

        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsIlJPTEUiOiJBRE1JTiIsIlVzZXJJRCI6MSwiZXhwIjoxNzA5MDY4MDAwLCJpYXQiOjE3MDkwNjQ0MDB9.DyCahh2e5xjcl8psC4L71IRHvPoguv4MKXUblPwmLkQ";

        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setName("Juan");
        userRequest.setPhone("123456789");
        userRequest.setPassword("pass123");
        userRequest.setDocument_number("987654321");
        userRequest.setLast_name("Rodriguez");
        userRequest.setDate_birth(LocalDate.of(1998, 1, 1));

        userResponse = new UserResponse();
        userResponse.setId(2L);
        userResponse.setEmail("test@example.com");
        userResponse.setName("Juan");
        userResponse.setRol("PROPIETARIO");
        userResponse.setPhone("123456789");
        userResponse.setLast_name("Rodriguez");
    }

    @Test
    void testCreateUserPropietarioSuccess() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userLogin, null,
                        List.of(new SimpleGrantedAuthority("ADMIN")))
        );

        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn(token);
        when(userHandler.saveUserPropietario(any(UserRequest.class))).thenReturn(userResponse);

        System.out.println("Mock userResponse " + userResponse);
        MvcResult result = mockMvc.perform(post("/api/users/create-user-propietario")
                        .with(csrf())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"email\": \"test16@example.com\", \n" +
                                " \"password\": \"1234\", \n" +
                                " \"name\": \"Farid 2\", \n" +
                                " \"last_name\": \"Peña 2\", \n" +
                                " \"document_number\": \"5555555\", \n" +
                                " \"phone\": \"+573115211945\", \n" +
                                " \"date_birth\": \"1996-01-01\" \n" +
                "}"))            .andExpect(status().isOk())
                .andExpect(header().exists("Content-Type"))
                .andExpect(header().string("Content-Type", "application/json"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + content);

    }
}
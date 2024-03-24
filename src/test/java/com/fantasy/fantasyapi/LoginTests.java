package com.fantasy.fantasyapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;

import com.fantasy.fantasyapi.mongoServices.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTests 
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testLoginFormPage() throws Exception 
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login.html"));
    }

    @Test
    void testSuccessfulLogin() throws Exception 
    {
        // correct login credentials
        String username = "testRegister";
        String password = "password";

        when(userService.authenticateUser(username, password)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", username)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("loginSuccess.html"));
    }

    @Test
    void testUnsuccessfulLogin() throws Exception 
    {
        
        String username = "testUser";
        String password = "invalidPassword";

        when(userService.authenticateUser(username, password)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", username)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login.html"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));
    }
}

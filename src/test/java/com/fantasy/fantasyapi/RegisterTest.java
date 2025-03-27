// package com.fantasy.fantasyapi;

// import static org.mockito.Mockito.*;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// import com.fantasy.fantasyapi.leagueModels.User;
// import com.fantasy.fantasyapi.mongoServices.UserService;

// @SpringBootTest
// @AutoConfigureMockMvc
// class RegisterTest 
// {
//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private UserService userService;

//     @Test
//     void testSuccessfulRegistration() throws Exception {
//         // Mocking the registration process
//         String username = "testUser";
//         String password = "password";
//         User user = new User(username, password, null, null);

//         // Assuming registration is successful
//         when(userService.addUser(user)).thenReturn(user);

//         // Act & Assert
//         mockMvc.perform(MockMvcRequestBuilders.post("/register/process_register")
//                 .param("username", username)
//                 .param("password", password))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.view().name("registrationSuccess.html"));
//     }
// }

// package com.fantasy.fantasyapi;

// import static org.mockito.Mockito.*;

// import java.util.Optional;

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
// class LoginTests {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private UserService userService;

//     @Test
//     void testLoginFormPage() throws Exception {
//         mockMvc.perform(MockMvcRequestBuilders.get("/login"))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.view().name("login.html"));
//     }

//     @Test
//     void testSuccessfulLogin() throws Exception {
//         // correct login credentials
//         String username = "carterAdmin";
//         String password = "carterAdmin";

//         when(userService.authenticateUser(username, password)).thenReturn(true);
//         when(userService.findByUsername(username)).thenReturn(Optional.of(new User())); // Mock user return

//         mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                 .param("username", username)
//                 .param("password", password))
//                 .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                 .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
//     }

//     @Test
//     void testUnsuccessfulLogin() throws Exception {

//         String username = "incorrect";
//         String password = "incorrect";

//         // Simulate unsuccessful authentication
//         when(userService.authenticateUser(username, password)).thenReturn(false);

//         mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                 .param("username", username)
//                 .param("password", password))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.view().name("error"));
//     }

//     @Test
//     void testLogout() throws Exception {
//         // Perform logout request
//         mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
//                 .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                 .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
//     }
// }

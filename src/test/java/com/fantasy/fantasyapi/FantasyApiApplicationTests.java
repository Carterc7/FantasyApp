package com.fantasy.fantasyapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.fantasy.fantasyapi.controllers.HomeController;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
class FantasyApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testHomePageWithoutAuthenticatedUser() {
		// Setup
		HomeController homeController = new HomeController();
		Model model = Mockito.mock(Model.class);
		HttpSession session = Mockito.mock(HttpSession.class);

		// Mock the session attribute to return null (user not logged in)
		Mockito.when(session.getAttribute("authenticatedUser")).thenReturn(null);

		// Call the method
		String viewName = homeController.showHomePage(session, model);

		assertEquals("home-default.html", viewName, "The home page should render the default template.");
	}

}

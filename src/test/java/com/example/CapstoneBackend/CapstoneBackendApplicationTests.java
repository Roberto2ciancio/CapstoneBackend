package com.example.CapstoneBackend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.config.import=optional:file:env.properties"
})
@Disabled("Disabled until database configuration is properly set up for testing")
class CapstoneBackendApplicationTests {

	@Test
	void contextLoads() {
		// Test that the application context loads successfully
	}

}

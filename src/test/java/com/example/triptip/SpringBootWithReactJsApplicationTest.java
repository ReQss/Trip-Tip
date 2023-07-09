package com.example.triptip;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class SpringBootWithReactJsApplicationTest {

	@Test
	void contextLoads() {
		assertThat(null, equalTo(null));
	}

}

package co.despegar.test.hdash.resources;

import co.despegar.test.hdash.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(value = "test")
@SpringBootTest(classes = Application.class)
@WebAppConfiguration()
public abstract class AbstractResourceTest<T>{
	protected MockMvc mvc;
	protected T endpoint;
	
	@Before
	public void before() {
		initMocks(this);
		endpoint = createEndpoint();
		mvc = standaloneSetup(endpoint).build();
	}
	
	protected abstract T createEndpoint();
}

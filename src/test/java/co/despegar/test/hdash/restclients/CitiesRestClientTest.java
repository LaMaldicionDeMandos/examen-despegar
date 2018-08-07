package co.despegar.test.hdash.restclients;

import co.despegar.test.hdash.model.City;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;

/**
 * Created by boot on 31/05/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class CitiesRestClientTest {
    @Mock
    RestTemplate restTemplate;

    CitiesRestClient client;

    @Before
    public void before() {
        ResponseEntity<List<City>> response = mock(ResponseEntity.class);
        when(response.getBody()).thenReturn(Lists.newArrayList(new City("a", "ar")));
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<City>>() {})))
                .thenReturn(response);
        client = spy(new CitiesRestClient(restTemplate, "lala", "lolo"));
    }

    @Test
    public void should_generate_a_well_formed_request_on_init() throws ExecutionException, InterruptedException {
        client.findAll();
        verify(restTemplate)
                .exchange(eq("lala/cities?snapshot"), eq(GET), any(), eq(new ParameterizedTypeReference<List<City>>() {}));
    }

    @Test
    public void should_get_cached_cities() throws ExecutionException, InterruptedException {
        List<City> cities = client.findAll();
        assertEquals(1, cities.size());
    }
}

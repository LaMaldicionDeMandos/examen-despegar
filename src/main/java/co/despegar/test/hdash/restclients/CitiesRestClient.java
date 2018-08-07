package co.despegar.test.hdash.restclients;

import co.despegar.test.hdash.model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.HttpMethod.GET;

/**
 * Created by boot on 02/03/2018.
 */
@Component
public class CitiesRestClient extends RegionRestClient<City> {
    private final static String REGION = "cities";

    @Autowired
    CitiesRestClient(final RestTemplate restTemplate,
                     @Value("${app.api-host}") final String host,
                     @Value("${app.api-key}") final String secret) {
        super(restTemplate, host, secret);
    }

    @Override
    protected String region() {
        return REGION;
    }

    @Override
    protected ParameterizedTypeReference<List<City>> type() {
        return new ParameterizedTypeReference<List<City>>() {};
    }
}

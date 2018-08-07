package co.despegar.test.hdash.restclients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.http.HttpMethod.GET;

/**
 * Created by boot on 02/03/2018.
 */
@Component
public abstract class RegionRestClient<T> extends RestClient {
    private final static Logger log = LoggerFactory.getLogger(RegionRestClient.class);
    final static String URL_TEMPLATE = "%s/%s?snapshot";
    final ParameterizedTypeReference<List<T>> LIST_TYPE = new ParameterizedTypeReference<List<T>>() {};

    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    private final Future<List<T>> allFuture;

    @Autowired
    RegionRestClient(final RestTemplate restTemplate,
                     @Value("${app.api-host}") final String host,
                     @Value("${app.api-key}") final String secret) {
        super(restTemplate, host, secret);
        allFuture = pool.submit(() -> findAllFirstTime());
    }

    public List<T> findAll() {
        try {
            return allFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String region();
    protected abstract ParameterizedTypeReference<List<T>> type();

    List<T> findAllFirstTime() {
        ResponseEntity<List<T>> restExchange =
                restTemplate.exchange(String.format(URL_TEMPLATE, host, region()), GET, createVoidEntity(), type());
        log.info("get to region {} response status: {}", region(), restExchange.getStatusCodeValue());
        return restExchange.getBody();
    }
}

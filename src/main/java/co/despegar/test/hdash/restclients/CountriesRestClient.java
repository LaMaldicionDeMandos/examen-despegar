package co.despegar.test.hdash.restclients;

import co.despegar.test.hdash.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by boot on 02/03/2018.
 */
@Component
public class CountriesRestClient extends RegionRestClient<Country> {
    private final static String REGION = "countries";

    @Autowired
    CountriesRestClient(final RestTemplate restTemplate,
                        @Value("${app.api-host}") final String host,
                        @Value("${app.api-key}") final String secret) {
        super(restTemplate, host, secret);
    }

    @Override
    protected String region() {
        return REGION;
    }

    @Override
    protected ParameterizedTypeReference<List<Country>> type() {
        return new ParameterizedTypeReference<List<Country>>() {};
    }
}

package co.despegar.test.hdash.restclients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Created by boot on 01/06/2018.
 */
public class RestClient {
    private final static String SECRET_HEADER = "X-ApiKey";
    private final static Logger log = LoggerFactory.getLogger(RestClient.class);

    final String host;
    private final String secret;

    final RestTemplate restTemplate;

    public RestClient(final RestTemplate restTemplate,
                     @Value("${app.api-host}") final String host,
                     @Value("${app.api-key}") final String secret) {
        this.restTemplate = checkNotNull(restTemplate);
        this.host = checkNotNull(host);
        this.secret = checkNotNull(secret);
    }

    protected <T> HttpEntity<T> createEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(SECRET_HEADER, asList(secret));
        headers.put("Accept-Encoding", asList("gzip"));
        log.info("Headers: " + headers.entrySet());
        return new HttpEntity<>(body, headers);
    }

    protected HttpEntity<Void> createVoidEntity() {
        return createEntity(null);
    }
}


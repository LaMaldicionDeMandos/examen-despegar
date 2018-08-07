package co.despegar.test.hdash.restclients;

import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.model.HotelPrice;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.http.HttpMethod.GET;

/**
 * Created by boot on 02/03/2018.
 */
@Component
public class HotelsRestClient extends RestClient {
    private final static Logger log = LoggerFactory.getLogger(HotelsRestClient.class);
    private final static String URL_TEMPLATE = "%s/hotels?limit=%d&offset=%d";
    private final static String URL_PRICES_TEMPLATE = "%s/hotels/prices?hotels=%s&country=ar&include=cheapest";

    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    private final int limit;
    private final int max;
    private final int partitionSize;

    @Autowired
    HotelsRestClient(final RestTemplate restTemplate,
                     @Value("${app.api-host}") final String host,
                     @Value("${app.api-key}") final String secret,
                     @Value("${app.hotels.limit}") final Integer limit,
                     @Value("${app.hotels.max}") final Integer max,
                     @Value("${app.hotels.partition-size}") final Integer partitionSize) {
        super(restTemplate, host, secret);
        this.limit = checkNotNull(limit);
        this.max = checkNotNull(max);
        this.partitionSize = checkNotNull(partitionSize);
    }

    public List<Hotel> findAll() {
        HotelsRestClientResponse response = findFirsts();
        final List<Hotel> hotels = response.getItems();
        int pages = pages(response.pages(limit));
        log.info("Find all hotels in {} pages", pages);
        hotels.addAll(findRestOfHotels(pages));
        return hotels;
    }

    public List<HotelPrice> findPrices(final List<Hotel> hotels) {
        try {
            List<List<Hotel>> partitions = partition(hotels);
            log.info("Find prices to check availability, {} partitions", partitions.size());
            return partitions.stream()
                    .map((list) -> pool.submit(() -> findHotelPrices(list)))
                    .map((future) -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .reduce((all, news) -> {
                        all.addAll(news);
                        return all;
                    }).orElse(Lists.newArrayList());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected List<HotelPrice> findHotelPrices(final List<Hotel> hotels) {
        String hotelIds = Joiner.on(',').join(Iterables.transform(hotels, Hotel::getId));
        ResponseEntity<HotelPriceResponse> restExchange =
                restTemplate.exchange(String.format(URL_PRICES_TEMPLATE, host, hotelIds), GET, createVoidEntity(),
                        HotelPriceResponse.class);
        log.info("get to prices response status: " + restExchange.getStatusCodeValue());
        return restExchange.getBody().getItems();
    }

    List<Hotel> findRestOfHotels(final int pages) {
        List<Hotel> hotels = IntStream
                .range(1, pages)
                .mapToObj((i) -> pool.submit(() -> findAllWithOffset(limit*i)))
                .map((hotelsRestClientResponseFuture) -> {
                    try {
                        return hotelsRestClientResponseFuture.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(HotelsRestClientResponse::getItems)
                .reduce(Lists.newArrayList(), (all, news) -> {
                    all.addAll(news);
                    return all;
                });
        return hotels;
    }

    HotelsRestClientResponse findFirsts() {
        return findAllWithOffset(0);
    }

    HotelsRestClientResponse findAllWithOffset(final long offset) {
        try {
            ResponseEntity<HotelsRestClientResponse> restExchange =
                    restTemplate.exchange(String.format(URL_TEMPLATE, host, limit, offset), GET, createVoidEntity(),
                            HotelsRestClientResponse.class);
            log.info("get to hotels response status: " + restExchange.getStatusCodeValue());
            return restExchange.getBody();
        }catch (HttpClientErrorException e) {
            throw e;
        }
    }

    int pages(final int responsePages) {
        return Math.min(responsePages, max/limit);
    }

    protected List<List<Hotel>> partition(final List<Hotel> hotels) {
        return Lists.partition(hotels, partitionSize);
    }
}

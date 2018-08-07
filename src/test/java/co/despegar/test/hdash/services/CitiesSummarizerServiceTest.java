package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.*;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by boot on 31/05/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class CitiesSummarizerServiceTest {
    @Mock
    HotelsRestClient hotelsService;
    CitiesSummarizerService service;

    @Before
    public void before() {
        this.service = new CitiesSummarizerService(hotelsService);
    }

    @Test
    public void should_summarize_all_cities_and_sort() {
        List<Hotel> hotels = createHotels();
        List<Map.Entry<String, Integer>> result = service.summarize(hotels);

        assertEquals(3, result.size());

        assertEquals("c", result.get(0).getKey());
        assertEquals(valueOf(3), result.get(0).getValue());

        assertEquals("a", result.get(1).getKey());
        assertEquals(valueOf(2), result.get(1).getValue());

        assertEquals("b", result.get(2).getKey());
        assertEquals(valueOf(1), result.get(2).getValue());
    }

    @Test
    public void should_filter_hotels() {
        List<Hotel> hotels = createHotels();
        List<String> cities = Arrays.asList("a", "c");
        List<Hotel> result = service.filter(hotels, cities);

        assertTrue(result.contains(hotels.get(0)));
        assertTrue(result.contains(hotels.get(1)));

        assertFalse(result.contains(hotels.get(2)));

        assertTrue(result.contains(hotels.get(3)));
        assertTrue(result.contains(hotels.get(4)));
        assertTrue(result.contains(hotels.get(5)));
    }

    @Test
    public void should_filter_hotels_and_check_availability() {
        Mockito.when(hotelsService.findPrices(Mockito.anyList())).thenReturn(createPrices());

        List<Hotel> hotels = createHotels();
        List<String> cities = Arrays.asList("a", "c");
        Availability result = service.availability(hotels, cities);

        assertEquals(2, result.getAvailables().size());
        boolean match = Iterables.all(result.getAvailables(), (entry) -> {
            if (entry.getKey().equals("a")) return entry.getValue().equals(1);
            if (entry.getKey().equals("c")) return entry.getValue().equals(2);
            return false;
        });
        assertTrue(match);

        assertEquals(2, result.getUnavailables().size());
        match = Iterables.all(result.getUnavailables(), (entry) ->
            entry.getKey().equals("a") || entry.getKey().equals("c") && entry.getValue().equals(1));
        assertTrue(match);
    }

    private List<Hotel> createHotels() {
        List<Hotel> hotels = Lists.newArrayList(
                new Hotel("1", new Location(new HotelCity("a"))),
                new Hotel("2", new Location(new HotelCity("a"))),
                new Hotel("3", new Location(new HotelCity("b"))),
                new Hotel("4", new Location(new HotelCity("c"))),
                new Hotel("5", new Location(new HotelCity("c"))),
                new Hotel("6", new Location(new HotelCity("c")))
        );
        return hotels;
    }

    private List<HotelPrice> createPrices() {
        List<HotelPrice> hotels = Lists.newArrayList(
                new HotelPrice("1"),
                new HotelPrice("4"),
                new HotelPrice("6")
        );
        return hotels;
    }

}

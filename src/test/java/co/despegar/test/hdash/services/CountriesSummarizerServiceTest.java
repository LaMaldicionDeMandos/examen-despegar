package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.*;
import co.despegar.test.hdash.restclients.CitiesRestClient;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


/**
 * Created by boot on 31/05/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class CountriesSummarizerServiceTest {
    @Mock
    CitiesRestClient citiesService;

    @Mock
    HotelsRestClient hotelsService;

    CountriesSummarizerService service;

    @Before
    public void before() throws ExecutionException, InterruptedException {
        when(citiesService.findAll()).thenReturn(createCities());
        this.service = new CountriesSummarizerService(citiesService, new CitiesSummarizerService(hotelsService), hotelsService);
    }

    @Test
    public void should_summarize_all_countries_and_sort() throws ExecutionException, InterruptedException {
        List<Hotel> hotels = createHotels();
        List<Map.Entry<String, Integer>> result = service.summarize(hotels);

        assertEquals(2, result.size());

        assertEquals("AR", result.get(0).getKey());
        assertEquals(valueOf(4), result.get(0).getValue());

        assertEquals("UY", result.get(1).getKey());
        assertEquals(valueOf(2), result.get(1).getValue());
    }

    @Test
    public void should_filter_hotels() {
        List<Hotel> hotels = createHotels();
        List<String> countries = Arrays.asList("AR");
        List<Hotel> result = service.filter(hotels, countries);

        assertFalse(result.contains(hotels.get(0)));
        assertFalse(result.contains(hotels.get(1)));

        assertTrue(result.contains(hotels.get(2)));
        assertTrue(result.contains(hotels.get(3)));
        assertTrue(result.contains(hotels.get(4)));
        assertTrue(result.contains(hotels.get(5)));
    }

    @Test
    public void should_filter_hotels_and_check_availability() {
        Mockito.when(hotelsService.findPrices(Mockito.anyList())).thenReturn(createPrices());

        List<Hotel> hotels = createHotels();
        List<String> countries = Arrays.asList("AR");
        Availability result = service.availability(hotels, countries);

        assertEquals(1, result.getAvailables().size());
        boolean match = Iterables.all(result.getAvailables(), (entry) ->
                entry.getKey().equals("AR") && entry.getValue().equals(3));
        assertTrue(match);

        assertEquals(1, result.getUnavailables().size());
        match = Iterables.all(result.getUnavailables(), (entry) ->
                entry.getKey().equals("AR") && entry.getValue().equals(1));
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

    private List<City> createCities() {
        List<City> cities = Lists.newArrayList(
                new City("a", "UY"),
                new City("b", "AR"),
                new City("c", "AR")
        );
        return cities;
    }

    private List<HotelPrice> createPrices() {
        List<HotelPrice> hotels = Lists.newArrayList(
                new HotelPrice("3"),
                new HotelPrice("4"),
                new HotelPrice("6")
        );
        return hotels;
    }

}

package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.*;
import co.despegar.test.hdash.restclients.CitiesRestClient;
import co.despegar.test.hdash.restclients.CountriesRestClient;
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
public class ContinentsSummarizerServiceTest {
    @Mock
    CitiesRestClient citiesService;
    @Mock
    CountriesRestClient countriesService;
    @Mock
    HotelsRestClient hotelsService;

    ContinentsSummarizerService service;

    @Before
    public void before() throws ExecutionException, InterruptedException {
        when(citiesService.findAll()).thenReturn(createCities());
        when(countriesService.findAll()).thenReturn(createCountries());
        this.service = new ContinentsSummarizerService(countriesService,
                new CountriesSummarizerService(citiesService, new CitiesSummarizerService(hotelsService), hotelsService),
                hotelsService);
    }

    @Test
    public void should_summarize_all_continents_and_sort() {
        List<Hotel> hotels = createHotels();
        List<Map.Entry<String, Integer>> result = service.summarize(hotels);

        assertEquals(3, result.size());

        assertEquals("AM", result.get(0).getKey());
        assertEquals(valueOf(6), result.get(0).getValue());

        assertEquals("EU", result.get(1).getKey());
        assertEquals(valueOf(5), result.get(1).getValue());

        assertEquals("OC", result.get(2).getKey());
        assertEquals(valueOf(1), result.get(2).getValue());
    }

    @Test
    public void should_filter_hotels() {
        List<Hotel> hotels = createHotels();
        List<String> cities = Arrays.asList("AM", "OC");
        List<Hotel> result = service.filter(hotels, cities);

        assertTrue(result.contains(hotels.get(0)));
        assertTrue(result.contains(hotels.get(1)));
        assertTrue(result.contains(hotels.get(2)));
        assertTrue(result.contains(hotels.get(3)));
        assertTrue(result.contains(hotels.get(4)));
        assertTrue(result.contains(hotels.get(5)));
        assertFalse(result.contains(hotels.get(6)));
        assertFalse(result.contains(hotels.get(7)));
        assertFalse(result.contains(hotels.get(8)));
        assertFalse(result.contains(hotels.get(9)));
        assertFalse(result.contains(hotels.get(10)));
        assertTrue(result.contains(hotels.get(11)));
    }

    @Test
    public void should_filter_hotels_and_check_availability() {
        Mockito.when(hotelsService.findPrices(Mockito.anyList())).thenReturn(createPrices());

        List<Hotel> hotels = createHotels();
        List<String> cities = Arrays.asList("AM", "OC");
        Availability result = service.availability(hotels, cities);

        assertEquals(2, result.getAvailables().size());
        boolean match = Iterables.all(result.getAvailables(), (entry) -> {
            if (entry.getKey().equals("AM")) return entry.getValue().equals(4);
            if (entry.getKey().equals("OC")) return entry.getValue().equals(1);
            return false;
        });
        assertTrue(match);

        assertEquals(1, result.getUnavailables().size());
        match = Iterables.all(result.getUnavailables(), (entry) ->
                entry.getKey().equals("AM") && entry.getValue().equals(2));
        assertTrue(match);
    }

    private List<Hotel> createHotels() {
        List<Hotel> hotels = Lists.newArrayList(
                new Hotel("1", new Location(new HotelCity("a"))),
                new Hotel("2", new Location(new HotelCity("a"))),
                new Hotel("3", new Location(new HotelCity("b"))),
                new Hotel("4", new Location(new HotelCity("c"))),
                new Hotel("5", new Location(new HotelCity("c"))),
                new Hotel("6", new Location(new HotelCity("c"))),
                new Hotel("7", new Location(new HotelCity("d"))),
                new Hotel("8", new Location(new HotelCity("d"))),
                new Hotel("9", new Location(new HotelCity("e"))),
                new Hotel("10", new Location(new HotelCity("e"))),
                new Hotel("11", new Location(new HotelCity("e"))),
                new Hotel("12", new Location(new HotelCity("f")))
        );
        return hotels;
    }



    private List<City> createCities() {
        List<City> cities = Lists.newArrayList(
                new City("a", "UY"),
                new City("b", "AR"),
                new City("c", "AR"),
                new City("d", "UK"),
                new City("e", "UK"),
                new City("f", "AU")
        );
        return cities;
    }

    private List<Country> createCountries() {
        List<Country> countries = Lists.newArrayList(
                new Country("UY", "AM"),
                new Country("AR", "AM"),
                new Country("UK", "EU"),
                new Country("AU", "OC")
        );
        return countries;
    }

    private List<HotelPrice> createPrices() {
        List<HotelPrice> hotels = Lists.newArrayList(
                new HotelPrice("1"),
                new HotelPrice("3"),
                new HotelPrice("4"),
                new HotelPrice("6"),
                new HotelPrice("12")
        );
        return hotels;
    }

}

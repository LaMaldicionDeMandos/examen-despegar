package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.Availability;
import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.model.HotelPrice;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.transformValues;
import static java.util.stream.Collectors.toList;

/**
 * Created by boot on 31/05/2018.
 */
public abstract class SummarizerService {
    private final HotelsRestClient hotelsService;

    public SummarizerService(final HotelsRestClient hotelsService) {
        this.hotelsService = checkNotNull(hotelsService);
    }

    public List<Map.Entry<String, Integer>> summarize(List<Hotel> hotels) {
        Map<String, List<Hotel>> group = group(hotels);
        return summarize(group);
    }

    public Availability availability(final List<Hotel> hotels, final List<String> filter) {
        List<Hotel> filteredHotels = filter(hotels, filter);
        List<HotelPrice> prices = hotelsService.findPrices(filteredHotels);
        Map<AvailabilityState, List<Hotel>> availabilityHotels = groupAvailabilities(filteredHotels, prices);

        List<Map.Entry<String, Integer>> availables = summarize(availabilityHotels.get(AvailabilityState.AVAILABLE));
        List<Map.Entry<String, Integer>> unavailables = summarize(availabilityHotels.get(AvailabilityState.UNAVAILABLE));
        return new Availability(availables, unavailables);
    }

    protected Map<AvailabilityState, List<Hotel>> groupAvailabilities(final List<Hotel> hotels, final List<HotelPrice> prices) {
        Map<AvailabilityState, List<Hotel>> availabilityHotels = Maps.newEnumMap(AvailabilityState.class);
        availabilityHotels.put(AvailabilityState.AVAILABLE, Lists.newArrayList());
        availabilityHotels.put(AvailabilityState.UNAVAILABLE, Lists.newArrayList());
        return hotels.stream().reduce(availabilityHotels, (map, hotel) -> {
            if (Iterables.any(prices, hotelPrice -> hotelPrice.getId().equals(hotel.getId()))) {
                map.get(AvailabilityState.AVAILABLE).add(hotel);
            } else {
                map.get(AvailabilityState.UNAVAILABLE).add(hotel);
            }
            return map;
        }, (map, map2) -> map);
    }

    protected List<Map.Entry<String, Integer>> summarize(final Map<String, List<Hotel>> hotels) {
        Map<String, Integer> results = transformValues(hotels, (h) -> h.size());
        return results.entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).collect(toList());
    }

    protected abstract Map<String, List<Hotel>> group(List<Hotel> hotels);
    protected abstract List<Hotel> filter(List<Hotel> hotels, List<String> filter);

    private enum AvailabilityState {
        AVAILABLE, UNAVAILABLE
    }
}

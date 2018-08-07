package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by boot on 31/05/2018.
 */
@Service
public class CitiesSummarizerService extends SummarizerService {

    @Autowired
    public CitiesSummarizerService(final HotelsRestClient hotelsService) {
        super(hotelsService);
    }

    @Override
    protected Map<String, List<Hotel>> group(List<Hotel> hotels) {
        return hotels.stream().collect(groupingBy(Hotel::getCityId));
    }

    @Override
    protected List<Hotel> filter(final List<Hotel> hotels, final List<String> filter) {
        return hotels.stream().filter(hotel -> filter.contains(hotel.getCityId())).collect(toList());
    }
}

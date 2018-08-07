package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.City;
import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.restclients.CitiesRestClient;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Created by boot on 31/05/2018.
 */
@Service
public class CountriesSummarizerService extends SummarizerService {
    private final CitiesRestClient citiesService;
    private final CitiesSummarizerService citiesSummarizer;

    @Autowired
    public CountriesSummarizerService(final CitiesRestClient citiesService, final CitiesSummarizerService citiesSummarizer,
                                      final HotelsRestClient hotelsService) {
        super(hotelsService);
        this.citiesService = checkNotNull(citiesService);
        this.citiesSummarizer = citiesSummarizer;
    }

    @Override
    protected Map<String, List<Hotel>> group(List<Hotel> hotels) {
        return SummarizeGrouper.group(hotels, citiesSummarizer, citiesService);
    }

    @Override
    protected List<Hotel> filter(List<Hotel> hotels, List<String> filter) {
        List<String> cityIds = citiesService.findAll().stream()
                .filter((city) -> filter.contains(city.getCountryId()))
                .map(City::getId)
                .collect(toList());
        return citiesSummarizer.filter(hotels, cityIds);
    }
}

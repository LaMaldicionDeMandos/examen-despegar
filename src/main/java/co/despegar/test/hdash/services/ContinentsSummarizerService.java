package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.Country;
import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.restclients.CountriesRestClient;
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
public class ContinentsSummarizerService extends SummarizerService {

    private final CountriesRestClient countriesService;
    private final CountriesSummarizerService countriesSummarizerService;

    @Autowired
    public ContinentsSummarizerService(final CountriesRestClient countriesService,
                                       final CountriesSummarizerService countriesSummarizerService,
                                       final HotelsRestClient hotelsService) {
        super(hotelsService);
        this.countriesService = checkNotNull(countriesService);
        this.countriesSummarizerService = checkNotNull(countriesSummarizerService);
    }

    @Override
    protected Map<String, List<Hotel>> group(List<Hotel> hotels) {
        return SummarizeGrouper.group(hotels, countriesSummarizerService, countriesService);
    }

    @Override
    protected List<Hotel> filter(List<Hotel> hotels, List<String> filter) {
        List<String> countryIds = countriesService.findAll().stream()
                .filter((country) -> filter.contains(country.getContinentId()))
                .map(Country::getId)
                .collect(toList());
        return countriesSummarizerService.filter(hotels, countryIds);
    }
}

package co.despegar.test.hdash.resources;

import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.model.Scope;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import co.despegar.test.hdash.services.CitiesSummarizerService;
import co.despegar.test.hdash.services.ContinentsSummarizerService;
import co.despegar.test.hdash.services.CountriesSummarizerService;
import co.despegar.test.hdash.services.SummarizerService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static co.despegar.test.hdash.model.Scope.*;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by boot on 31/05/2018.
 */
public abstract class DashboardResource {
    private final static Logger logger = LoggerFactory.getLogger(DashboardResource.class);
    private final Map<Scope, SummarizerService> summarizers = Maps.newEnumMap(Scope.class);
    private final HotelsRestClient hotelsService;

    @Autowired
    public DashboardResource(final HotelsRestClient hotelsService,
                             final CitiesSummarizerService citiesSummarizerService,
                             final CountriesSummarizerService countriesSummarizerService,
                             final ContinentsSummarizerService continentsSummarizerService) {
        this.hotelsService = checkNotNull(hotelsService);
        this.summarizers.put(CITIES, checkNotNull(citiesSummarizerService));
        this.summarizers.put(COUNTRIES, checkNotNull(countriesSummarizerService));
        this.summarizers.put(CONTINENTS, checkNotNull(continentsSummarizerService));
    }

    protected List<Hotel> getHotels() {
        logger.info("Getting all Hotels");
        return hotelsService.findAll();
    }

    protected Optional<SummarizerService> getService(final String scopeName) {
        Optional<Scope> scope = Scope.fromString(scopeName);
        if (!scope.isPresent()) return Optional.empty();
        return Optional.of(summarizers.get(scope.get()));
    }
}

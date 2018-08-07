package co.despegar.test.hdash.resources;

import co.despegar.test.hdash.model.Availability;
import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.restclients.HotelsRestClient;
import co.despegar.test.hdash.services.CitiesSummarizerService;
import co.despegar.test.hdash.services.ContinentsSummarizerService;
import co.despegar.test.hdash.services.CountriesSummarizerService;
import co.despegar.test.hdash.services.SummarizerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Created by boot on 31/05/2018.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "dashboards/availabilities", produces = APPLICATION_JSON_VALUE)
public class AvailabilityResource extends DashboardResource {
    private final static Logger log = LoggerFactory.getLogger(AvailabilityResource.class);

    @Autowired
    public AvailabilityResource(final HotelsRestClient hotelsService,
                                final CitiesSummarizerService citiesSummarizerService,
                                final CountriesSummarizerService countriesSummarizerService,
                                final ContinentsSummarizerService continentsSummarizerService) {
        super(hotelsService, citiesSummarizerService, countriesSummarizerService, continentsSummarizerService);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Availability> availability(@RequestParam("destinations") final List<String> ids,
                                                     @RequestParam("scope") final String scopeName) {
        log.info("Getting hotels availabilities, scope: {}", scopeName);
        Optional<SummarizerService> service = getService(scopeName);
        if (!service.isPresent()) return badRequest().build();
        List<Hotel> hotels = getHotels();
        return ok(service.get().availability(hotels, ids));
    }
}

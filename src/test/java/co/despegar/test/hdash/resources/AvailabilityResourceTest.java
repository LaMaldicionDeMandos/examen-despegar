package co.despegar.test.hdash.resources;

import co.despegar.test.hdash.restclients.HotelsRestClient;
import co.despegar.test.hdash.services.CitiesSummarizerService;
import co.despegar.test.hdash.services.ContinentsSummarizerService;
import co.despegar.test.hdash.services.CountriesSummarizerService;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AvailabilityResourceTest extends AbstractResourceTest<AvailabilityResource>{

    @Mock
    HotelsRestClient hotelsService;
    @Mock
    CitiesSummarizerService citiesService;
    @Mock
    CountriesSummarizerService countriesService;
    @Mock
    ContinentsSummarizerService continentsService;

    @Override
    protected AvailabilityResource createEndpoint() {
        return spy(new AvailabilityResource(hotelsService, citiesService, countriesService, continentsService));
    }

    @Test
    public void should_call_get_availability_with_valid_scope() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("scope", "cities")
                .param("destinations", "982,123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(endpoint).availability(eq(asList("982", "123")), eq("cities"));
    }

    @Test
    public void should_call_get_availability_with_invalid_scope() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("scope", "saraza")
                .param("destinations", "982,123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(endpoint).availability(eq(asList("982", "123")), eq("saraza"));
    }

    @Test
    public void should_call_get_availability_without_ids() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("scope", "cities")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(endpoint, never()).availability(any(), any());
    }

    @Test
    public void should_call_get_availability_with_not_scope() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("destinations", "982,123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(endpoint, never()).availability(any(), any());
    }

    @Test
    public void should_call_get_availabilitis_with_cities_scope() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("scope", "cities")
                .param("destinations", "982,123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(citiesService).availability(anyList(), anyList());
        verify(countriesService, never()).availability(any(), any());
        verify(continentsService, never()).availability(any(), any());
    }

    @Test
    public void should_call_get_availabilities_with_countries_scope() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("scope", "countries")
                .param("destinations", "982,123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(citiesService, never()).availability(any(), any());
        verify(countriesService).availability(anyList(), anyList());
        verify(continentsService, never()).availability(any(), any());
    }

    @Test
    public void should_call_get_availabilities_with_continents_scope() throws Exception {
        mvc.perform(get("/dashboards/availabilities")
                .param("scope", "continents")
                .param("destinations", "982,123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(citiesService, never()).availability(any(), any());
        verify(countriesService, never()).availability(any(), any());
        verify(continentsService).availability(anyList(), anyList());
    }
}

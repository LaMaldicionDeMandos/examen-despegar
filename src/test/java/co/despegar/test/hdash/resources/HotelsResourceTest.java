package co.despegar.test.hdash.resources;

import co.despegar.test.hdash.restclients.HotelsRestClient;
import co.despegar.test.hdash.services.CitiesSummarizerService;
import co.despegar.test.hdash.services.ContinentsSummarizerService;
import co.despegar.test.hdash.services.CountriesSummarizerService;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HotelsResourceTest extends AbstractResourceTest<HotelsResource>{

    @Mock
    HotelsRestClient hotelsService;
    @Mock
    CitiesSummarizerService citiesService;
    @Mock
    CountriesSummarizerService countriesService;
    @Mock
    ContinentsSummarizerService continentsService;

    @Override
    protected HotelsResource createEndpoint() {
        return spy(new HotelsResource(hotelsService, citiesService, countriesService, continentsService));
    }

    @Test
    public void should_call_get_summarice_with_valid_scope() throws Exception {
        mvc.perform(get("/dashboards/hotels")
                .param("scope", "cities")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(endpoint).summarice("cities");
    }

    @Test
    public void should_call_get_summarice_with_invalid_scope() throws Exception {
        mvc.perform(get("/dashboards/hotels")
                .param("scope", "saraza")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(endpoint).summarice("saraza");
    }

    @Test
    public void should_call_get_summarice_with_not_scope() throws Exception {
        mvc.perform(get("/dashboards/hotels")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(endpoint, never()).summarice(null);
    }

    @Test
    public void should_call_get_summarice_with_cities_scope() throws Exception {
        mvc.perform(get("/dashboards/hotels")
                .param("scope", "cities")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(citiesService).summarize(any());
        verify(countriesService, never()).summarize(any());
        verify(continentsService, never()).summarize(any());
    }

    @Test
    public void should_call_get_summarice_with_countries_scope() throws Exception {
        mvc.perform(get("/dashboards/hotels")
                .param("scope", "countries")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(citiesService, never()).summarize(any());
        verify(countriesService).summarize(any());
        verify(continentsService, never()).summarize(any());
    }

    @Test
    public void should_call_get_summarice_with_continents_scope() throws Exception {
        mvc.perform(get("/dashboards/hotels")
                .param("scope", "continents")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(citiesService, never()).summarize(any());
        verify(countriesService, never()).summarize(any());
        verify(continentsService).summarize(any());
    }
}

package co.despegar.test.hdash.restclients;

import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.model.HotelPrice;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;

/**
 * Created by boot on 31/05/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class HotelsRestClientTest {
    @Mock
    RestTemplate restTemplate;

    HotelsRestClient client;

    @Before
    public void before() {
        client = spy(new HotelsRestClient(restTemplate, "lala", "lolo", 2, 10, 2));
    }

    @Test
    public void should_generate_a_well_formed_request() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(mock(ResponseEntity.class));

        client.findAllWithOffset(4);

        verify(restTemplate)
                .exchange(eq("lala/hotels?limit=2&offset=4"), eq(GET), any(), eq(HotelsRestClientResponse.class));
    }

    @Test
    public void pages_should_return_only_5_pages() {
        int pages = client.pages(100);
        assertEquals(5, pages);
    }

    @Test
    public void pages_should_return_only_4_pages_because_total_is_7() {
        int pages = client.pages(4);
        assertEquals(4, pages);
    }

    @Test
    public void find_rest_of_hotels_should_do_four_more_call() {
        HotelsRestClientResponse res = mock(HotelsRestClientResponse.class);
        when(res.getItems()).thenReturn(Lists.newArrayList(mock(Hotel.class)));
        doReturn(res).when(client).findAllWithOffset(anyLong());

        List<Hotel> hotels = client.findRestOfHotels(5);

        verify(client, Mockito.times(4)).findAllWithOffset(anyLong());
        assertEquals(4, hotels.size());
    }

    @Test
    public void find_rest_of_hotels_should_do_four_more_call_with_offset() {
        HotelsRestClientResponse res = mock(HotelsRestClientResponse.class);
        when(res.getItems()).thenReturn(Lists.newArrayList(mock(Hotel.class)));
        doReturn(res).when(client).findAllWithOffset(anyLong());

        List<Hotel> hotels = client.findRestOfHotels(5);

        verify(client).findAllWithOffset(2);
        verify(client).findAllWithOffset(4);
        verify(client).findAllWithOffset(6);
        verify(client).findAllWithOffset(8);
        verify(client, never()).findAllWithOffset(10);
        verify(client, never()).findAllWithOffset(3);
        assertEquals(4, hotels.size());
    }

    @Test
    public void find_rest_of_hotels_should_do_nothing_if_pages_is_1() {
        HotelsRestClientResponse res = mock(HotelsRestClientResponse.class);
        when(res.getItems()).thenReturn(Lists.newArrayList(mock(Hotel.class)));
        doReturn(res).when(client).findAllWithOffset(anyLong());

        List<Hotel> hotels = client.findRestOfHotels(1);

        verify(client, never()).findAllWithOffset(anyLong());
        assertEquals(0, hotels.size());
    }

    @Test
    public void findAll_should_find_hotels_all_in_once() {
        HotelsRestClientResponse res = mock(HotelsRestClientResponse.class);
        when(res.getItems()).thenReturn(Lists.newArrayList(mock(Hotel.class)));
        when(res.pages(anyInt())).thenReturn(1);
        doReturn(res).when(client).findAllWithOffset(0);

        List<Hotel> hotels = client.findAll();

        verify(client).findAllWithOffset(anyLong());
        assertEquals(1, hotels.size());
    }

    @Test
    public void findAll_should_find_hotels_do_many_calls() {
        HotelsRestClientResponse res = mock(HotelsRestClientResponse.class);
        when(res.getItems()).thenReturn(Lists.newArrayList(mock(Hotel.class)));
        when(res.pages(anyInt())).thenReturn(5);
        doReturn(res).when(client).findAllWithOffset(anyLong());
        doReturn(5).when(client).pages(5);

        List<Hotel> hotels = client.findAll();

        verify(client).findAllWithOffset(0);
        verify(client).findAllWithOffset(2);
        verify(client).findAllWithOffset(4);
        verify(client).findAllWithOffset(6);
        verify(client).findAllWithOffset(8);
        verify(client, never()).findAllWithOffset(10);
        assertEquals(5, hotels.size());
    }

    @Test
    public void find_hotels_price_should_execute_2_call_to_endpoint_with_2_ids_and_last_with_one_id() {
        Hotel hotelMock1 = new Hotel("1", null);
        Hotel hotelMock2 = new Hotel("2", null);
        Hotel hotelMock3 = new Hotel("3", null);
        Hotel hotelMock4 = new Hotel("4", null);
        Hotel hotelMock5 = new Hotel("5", null);

        HotelPrice mock = mock(HotelPrice.class);
        ResponseEntity<HotelPriceResponse> response = mock(ResponseEntity.class);
        when(response.getBody()).thenReturn(new HotelPriceResponse(Lists.newArrayList(mock, mock)));
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(HotelPriceResponse.class)))
                .thenReturn(response);

        client.findPrices(Lists.newArrayList(hotelMock1, hotelMock2, hotelMock3, hotelMock4, hotelMock5));

        verify(restTemplate)
                .exchange(eq("lala/hotels/prices?hotels=1,2&country=ar&include=cheapest"), eq(GET), any(), eq(HotelPriceResponse.class));
        verify(restTemplate)
                .exchange(eq("lala/hotels/prices?hotels=3,4&country=ar&include=cheapest"), eq(GET), any(), eq(HotelPriceResponse.class));
        verify(restTemplate)
                .exchange(eq("lala/hotels/prices?hotels=5&country=ar&include=cheapest"), eq(GET), any(), eq(HotelPriceResponse.class));
    }

    @Test
    public void find_hotels_price_should_return_all_valid_responses_hotel_3_not_returned() {
        Hotel hotelMock1 = new Hotel("1", null);
        Hotel hotelMock2 = new Hotel("2", null);
        Hotel hotelMock3 = new Hotel("3", null);
        Hotel hotelMock4 = new Hotel("4", null);
        Hotel hotelMock5 = new Hotel("5", null);

        HotelPrice mock = mock(HotelPrice.class);
        ResponseEntity<HotelPriceResponse> response = mock(ResponseEntity.class);
        when(response.getBody())
                .thenReturn(new HotelPriceResponse(Lists.newArrayList(mock, mock)))
                .thenReturn(new HotelPriceResponse(Lists.newArrayList(mock)))
                .thenReturn(new HotelPriceResponse(Lists.newArrayList(mock)));
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(HotelPriceResponse.class)))
                .thenReturn(response);

        List<HotelPrice> list = client.findPrices(Lists.newArrayList(hotelMock1, hotelMock2, hotelMock3, hotelMock4, hotelMock5));

        assertEquals(4, list.size());

    }
}

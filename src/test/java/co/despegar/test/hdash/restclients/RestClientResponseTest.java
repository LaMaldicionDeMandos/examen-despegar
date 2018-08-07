package co.despegar.test.hdash.restclients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by boot on 31/05/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class RestClientResponseTest {
    @Test
    public void should_is_complete() {
        Pagination pag = new Pagination(100, 100, 102);
        RestClientResponse response = new RestClientResponse<String>(Arrays.asList("a", "b"), pag);

        assertTrue(response.isComplete());
    }

    @Test
    public void should_is_complete_over() {
        Pagination pag = new Pagination(100, 100, 101);
        RestClientResponse response = new RestClientResponse<String>(Arrays.asList("a", "b"), pag);

        assertTrue(response.isComplete());
    }

    @Test
    public void should_is_not_complete() {
        Pagination pag = new Pagination(100, 100, 103);
        RestClientResponse response = new RestClientResponse<String>(Arrays.asList("a", "b"), pag);

        assertFalse(response.isComplete());
    }

    @Test
    public void pages_should_return_exact_divition_if_size_is_multiple_of_total() {
        Pagination pag = new Pagination(0, 2, 10);
        RestClientResponse response = new RestClientResponse<String>(Arrays.asList("a", "b"), pag);
        assertEquals(5, response.pages(2));
    }

    @Test
    public void pages_should_return_half_plus_one_if_size_is_not_multiple_of_total() {
        Pagination pag = new Pagination(0, 2, 10);
        RestClientResponse response = new RestClientResponse<String>(Arrays.asList("a", "b"), pag);
        assertEquals(4, response.pages(3));
    }
}

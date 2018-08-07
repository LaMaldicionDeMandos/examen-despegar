package co.despegar.test.hdash.restclients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by boot on 31/05/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaginationTest {

    @Test
    public void should_count_fidderence_bettwen_total_and_offset() {
        Pagination pag = new Pagination(100, 100, 1000);
        assertEquals(900, pag.diff());
    }
}

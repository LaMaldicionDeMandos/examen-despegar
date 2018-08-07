package co.despegar.test.hdash.restclients;

import java.util.List;

/**
 * Created by boot on 31/05/2018.
 */
public class RestClientResponse<T> {
    private final List<T> items;
    private final Pagination paging;

    RestClientResponse() {
        this.items = null;
        this.paging = null;
    }

    public RestClientResponse(List<T> items, Pagination paging) {
        this.items = items;
        this.paging = paging;
    }

    public List<T> getItems() {
        return items;
    }

    public Pagination getPaging() {
        return paging;
    }

    public boolean isComplete() {
        return paging.diff() <= items.size();
    }

    public int pages(final int size) {
        return (int) -Math.floorDiv(-paging.getTotal(), size);
    }
}

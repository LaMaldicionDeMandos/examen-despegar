package co.despegar.test.hdash.restclients;

/**
 * Created by boot on 31/05/2018.
 */
public class Pagination {
    private final int offset;
    private final int limit;
    private final long total;

    Pagination() {
        this.offset = 0;
        this.limit = 0;
        this.total = 0;
    }

    public Pagination(int offset, int limit, long total) {
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public long getTotal() {
        return total;
    }

    public long diff() {
        return total - offset;
    }


}

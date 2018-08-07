package co.despegar.test.hdash.restclients;

import co.despegar.test.hdash.model.HotelPrice;

import java.util.List;

/**
 * Created by boot on 04/06/2018.
 */
public class HotelPriceResponse {
    private final List<HotelPrice> items;

    HotelPriceResponse() {
        this.items = null;
    }

    public HotelPriceResponse(List<HotelPrice> items) {
        this.items = items;
    }

    public List<HotelPrice> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotelPriceResponse that = (HotelPriceResponse) o;

        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }
}

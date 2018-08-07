package co.despegar.test.hdash.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by boot on 04/06/2018.
 */
public class HotelPrice {

    @JsonProperty("hotel_id")
    private final String id;

    HotelPrice() {
        this.id = null;
    }

    public HotelPrice(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotelPrice that = (HotelPrice) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

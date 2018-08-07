package co.despegar.test.hdash.model;

/**
 * Created by boot on 02/06/2018.
 */
public class Location {
    private final HotelCity city;

    public Location() {
        this.city = null;
    }

    public Location(HotelCity city) {
        this.city = city;
    }

    public HotelCity getCity() {
        return city;
    }
}

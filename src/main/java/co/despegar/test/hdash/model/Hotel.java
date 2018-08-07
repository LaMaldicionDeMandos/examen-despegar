package co.despegar.test.hdash.model;

/**
 * Created by boot on 01/06/2018.
 */
public class Hotel {
    private final String id;
    private final Location location;

    Hotel() {
        this.id = null;
        this.location = null;
    }

    public Hotel(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getCityId() {
        return location.getCity().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hotel hotel = (Hotel) o;

        return id != null ? id.equals(hotel.id) : hotel.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

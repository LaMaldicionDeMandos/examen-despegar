package co.despegar.test.hdash.model;

/**
 * Created by boot on 02/06/2018.
 */
public class City implements Region {
    private final String id;
    private final String countryId;

    City() {
        this.id = null;
        this.countryId = null;
    }

    public City(String id, String countryId) {
        this.id = id;
        this.countryId = countryId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getParentId() {
        return getCountryId();
    }

    public String getCountryId() {
        return countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return id != null ? id.equals(city.id) : city.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

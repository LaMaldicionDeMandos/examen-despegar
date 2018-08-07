package co.despegar.test.hdash.model;

/**
 * Created by boot on 02/06/2018.
 */
public class Country implements Region {
    private final String id;
    private final String continentId;

    Country() {
        this.id = null;
        this.continentId = null;
    }

    public Country(String id, String continentId) {
        this.id = id;
        this.continentId = continentId;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getParentId() {
        return getContinentId();
    }

    public String getContinentId() {
        return continentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country city = (Country) o;

        return id != null ? id.equals(city.id) : city.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

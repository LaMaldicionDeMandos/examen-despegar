package co.despegar.test.hdash.model;

import java.util.List;
import java.util.Map;

/**
 * Created by boot on 03/06/2018.
 */
public class Availability {
    private final List<Map.Entry<String, Integer>> availables;
    private final List<Map.Entry<String, Integer>> unavailables;

    public Availability(List<Map.Entry<String, Integer>> availables, List<Map.Entry<String, Integer>> unavailables) {
        this.availables = availables;
        this.unavailables = unavailables;
    }

    public List<Map.Entry<String, Integer>> getAvailables() {
        return availables;
    }

    public List<Map.Entry<String, Integer>> getUnavailables() {
        return unavailables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Availability that = (Availability) o;

        if (availables != null ? !availables.equals(that.availables) : that.availables != null) return false;
        return unavailables != null ? unavailables.equals(that.unavailables) : that.unavailables == null;
    }

    @Override
    public int hashCode() {
        int result = availables != null ? availables.hashCode() : 0;
        result = 31 * result + (unavailables != null ? unavailables.hashCode() : 0);
        return result;
    }
}

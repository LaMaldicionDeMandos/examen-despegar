package co.despegar.test.hdash.model;

import java.util.Optional;

/**
 * Created by boot on 31/05/2018.
 */
public enum Scope {
    CITIES("cities"),
    COUNTRIES("countries"),
    CONTINENTS("continents");

    private final String code;
    Scope(final String code) {
        this.code = code;
    }

    public static Optional<Scope> fromString(final String name) {
        try {
            return Optional.of(Scope.valueOf(name.toUpperCase()));
        } catch(RuntimeException e) {
            return Optional.empty();
        }
    }
}

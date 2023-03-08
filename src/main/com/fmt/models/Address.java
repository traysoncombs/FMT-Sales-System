package com.fmt.models;

import java.util.Objects;

/**
 * Class to model an address.
 */
public class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final String country;

    /**
     * Constructs a new address.
     *
     * @param street
     * @param city
     * @param state
     * @param zip
     * @param country
     */
    public Address(String street, String city, String state, String zip, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }

    /**
     * Builds an address from CSV.
     *
     * @param csv CSV string containing an address.
     * @return An address built from the CSV data.
     */
    public static Address fromCSV(String csv) {
        String[] data = csv.split(",");
        return new Address(data[0], data[1], data[2], data[3], data[4]);
    }

    @Override
    public String toString() {
        return String.format("           %s\n", street) +
                String.format("           %s %s %s %s", city, state, zip, country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(city, address.city) && Objects.equals(state, address.state) && Objects.equals(zip, address.zip) && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, zip, country);
    }
}

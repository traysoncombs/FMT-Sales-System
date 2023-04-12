package com.fmt.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Class to model an address.
 */
public class Address implements Persistable {
    private final String street;
    private final String city;
    private final String state;
    private final Integer zip;
    private final String country;

    /**
     * Constructs a new address.
     *
     * @param street String
     * @param city String
     * @param state String
     * @param zip Integer
     * @param country String
     */
    public Address(String street, String city, String state, Integer zip, String country) {
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
     * @param startIndex The index where the address starts.
     * @return An address built from the CSV data.
     */
    public static Address fromCSV(String csv, Integer startIndex) {
        String[] data = csv.split(",");
        return new Address(
                data[startIndex],
                data[startIndex + 1],
                data[startIndex + 2],
                Integer.parseInt(data[startIndex + 3]),
                data[startIndex + 4]
        );
    }

    /**
     * Takes a result set containing the fields:
     * `street`, `city`, `zip_code`, `state`, `country`
     * and returns an address.
     * @param rs ResultSet containing specified fields.
     * @return Address
     * @throws SQLException if ResultSet doesn't contain necessary fields.
     */
    public static Address fromRow(ResultSet rs) throws SQLException {
        return new Address(
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getInt("zip_code"),
                rs.getString("country")
        );
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

    @Override
    public boolean saveToDB() {
        return false;
    }
}

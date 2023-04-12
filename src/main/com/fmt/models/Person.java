package com.fmt.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * A class to model a person.
 */
public class Person implements Persistable {
    private final String personCode;
    private final String lastName;
    private final String firstName;
    private final Address address;
    // Due to a problem with the XML parser this must be an ArrayList,
    // and not a List, otherwise it will not serialize into XML.
    private final ArrayList<String> emails;

    /**
     * Constructs a new person with the associated attributes.
     *
     * @param personCode The unique alphanumeric code that identifies this specific store.
     * @param lastName   The persons last name.
     * @param firstName  The persons first name.
     * @param address    The persons address.
     * @param emails     Any emails associated with the person.
     */
    public Person(String personCode, String lastName, String firstName, Address address, ArrayList<String> emails) {
        this.personCode = personCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        // Emails shouldn't be null, so initialize it if so.
        this.emails = emails == null ?
                new ArrayList<>() : emails;
    }

    /**
     * Builds a person from the CSV string.
     *
     * @param csv The CSV string containing the person's information.
     * @return A person object built from the CSV string.
     */
    public static Person fromCSV(String csv) {
        List<String> data = Arrays.asList(csv.split(","));
        List<String> emails = null;
        Address addr = Address.fromCSV(csv, 3);

        // Any field after the 8th is an email
        if (data.size() > 8) {
            emails = data.subList(8, data.size());
        }

        return new Person(
                data.get(0),
                data.get(1),
                data.get(2),
                addr,
                emails == null ? null : new ArrayList<>(emails)
        );
    }

    /**
     * Takes a ResultSet containing the fields:
     * `person_code`, `last_name`, `first_name`, `street`, `zip_code`, `city`,
     * `state`, `country`, and `emails`.
     * @param rs ResultSet containing specified fields.
     * @return Person
     * @throws SQLException if ResultSet doesn't contain necessary fields.
     */
    public static Person fromRow(ResultSet rs) throws SQLException {
        Address address = Address.fromRow(rs);
        ArrayList<String> emails = null;
        String CSVEmails = rs.getString("emails");

        // If there aren't any emails just pass it as null
        if (CSVEmails != null) {
           emails = new ArrayList<>(Arrays.asList(CSVEmails.split(",")));
        }

        return new Person(
                rs.getString("person_code"),
                rs.getString("last_name"),
                rs.getString("first_name"),
                address,
                emails
        );
    }

    public String getPersonCode() {
        return personCode;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%s: [%s])\n", lastName, firstName, personCode, String.join(", ", emails)) +
                address.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personCode, person.personCode) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(address, person.address) && Objects.equals(emails, person.emails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personCode, firstName, lastName, address, emails);
    }

    @Override
    public boolean saveToDB() {
        return false;
    }
}

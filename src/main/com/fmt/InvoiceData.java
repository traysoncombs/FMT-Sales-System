package com.fmt;

import com.mysql.cj.log.Log;

import java.sql.*;
import java.util.logging.Logger;

public class InvoiceData {

    /**
     * Removes all records from all tables in the database.
     */
    public static void clearDatabase() {
        String[] queries = {"DELETE FROM InvoiceItem", "DELETE FROM Item",
                "DELETE FROM Invoice", "DELETE FROM Store",
                "DELETE FROM Email", "DELETE FROM Person",
                "DELETE FROM Address", "DELETE FROM State",
                "DELETE FROM Country"};

        Connection conn = ConnectionFactory.getConnection();
        try {
            for (String query : queries) {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.execute();
            }
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error clearing database.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Method to add a person record to the database with the provided data.
     *
     * @param personCode
     * @param firstName
     * @param lastName
     * @param street
     * @param city
     * @param state
     * @param zip
     * @param country
     */
    public static void addPerson(String personCode, String firstName, String lastName, String street,
                                 String city, String state, String zip, String country) {
        int address_id = createAddress(street, city, state, zip, country);
        String insert_sql = "INSERT INTO Person (code, first_name, last_name, address_id) VALUES (?, ?, ?, ?)";

        Connection conn = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(insert_sql);
            ps.setString(1, personCode);
            ps.setString(2,  firstName);
            ps.setString(3,  lastName);
            ps.setInt(4, address_id);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            // 1062 is the error for the unique constraint being broken
            // Therefore we can just return because the person already exists.
            if (e.getErrorCode() == 1062) return;
            Logger.getLogger("fmt").severe("Error adding person.");
            throw new RuntimeException(e);
        }


    }

    /**
     * Adds an email record corresponding person record corresponding to the
     * provided <code>personCode</code>
     *
     * @param personCode
     * @param email
     */
    public static void addEmail(String personCode, String email) {
        String insert_sql = "INSERT INTO Email (email, person_id) VALUES (?, (SELECT Person.id FROM Person WHERE Person.code = ? LIMIT 1))";
        String select_sql = "SELECT * FROM Email WHERE email=? AND person_id = (SELECT Person.id FROM Person WHERE Person.code = ? LIMIT 1)";
        Connection conn = ConnectionFactory.getConnection();
        try {
            // Check if email is already associated with person.
            PreparedStatement ps = conn.prepareStatement(select_sql);
            ps.setString(1, email);
            ps.setString(2, personCode);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if(rs.next()) {
                conn.close();
                return;
            }

            // Insert email
            ps = conn.prepareStatement(insert_sql);
            ps.setString(1, email);
            ps.setString(2, personCode);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error adding email.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a store record to the database managed by the person identified by the
     * given code.
     *
     * @param storeCode
     * @param managerCode
     * @param street
     * @param city
     * @param state
     * @param zip
     * @param country
     */
    public static void addStore(String storeCode, String managerCode, String street, String city, String state,
                                String zip, String country) {
        int address_id = createAddress(street, city, state, zip, country);
        String insert_sql = "INSERT INTO Store (code, manager_id, address_id) VALUES (?, (SELECT Person.id FROM Person WHERE Person.code = ?), ?)";
        Connection conn = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(insert_sql);
            ps.setString(1, storeCode);
            ps.setString(2,  managerCode);
            ps.setInt(3,  address_id);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            // 1062 is the error for the unique constraint being broken
            // Therefore we can just return because the person already exists.
            if (e.getErrorCode() == 1062) return;
            Logger.getLogger("fmt").severe("Error adding store.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds an item, unnecessary fields can be null.
     *
     * @param code
     * @param name
     * @param model
     * @param unit
     * @param unitPrice
     * @param hourlyRate
     * @param discriminator
     */
    private static void addItem(String code, String name, String model, String unit, Double unitPrice, Double hourlyRate, String discriminator) {
        String sql = "INSERT INTO Item (code, name, model, unit, unit_price, hourly_rate, discriminator) VALUES (?,?,?,?,?,?,?)";
        Connection conn = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.setString(2, name);
            ps.setString(3, model);
            ps.setString(4, unit);
            ps.setObject(5, unitPrice);
            ps.setObject(6, hourlyRate);
            ps.setString(7, discriminator);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            // 1062 is the error for the unique constraint being broken
            // Therefore we can just return because the person already exists.
            if (e.getErrorCode() == 1062) return;
            Logger.getLogger("fmt").severe("Error adding item.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a product record to the database with the given <code>code</code>, <code>name</code> and
     * <code>unit</code> and <code>pricePerUnit</code>.
     *
     * @param itemCode
     * @param name
     * @param unit
     * @param pricePerUnit
     */
    public static void addProduct(String code, String name, String unit, double pricePerUnit) {
        addItem(code, name, null, unit, pricePerUnit, null, "P");
    }

    /**
     * Adds an equipment record to the database with the given <code>code</code>,
     * <code>name</code> and <code>modelNumber</code>.
     *
     * @param itemCode
     * @param name
     * @param modelNumber
     */
    public static void addEquipment(String code, String name, String modelNumber) {
        addItem(code, name, modelNumber, null, null, null, "E");

    }

    /**
     * Adds a service record to the database with the given <code>code</code>,
     * <code>name</code> and <code>costPerHour</code>.
     *
     * @param itemCode
     * @param name
     * @param modelNumber
     */
    public static void addService(String code, String name, double costPerHour) {
        addItem(code, name, null, null, null, costPerHour, "S");

    }


    /**
     * Adds an invoice record to the database with the given data.
     *
     * @param invoiceCode
     * @param storeCode
     * @param customerCode
     * @param salesPersonCode
     * @param invoiceDate
     */
    public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode, String invoiceDate) {
        String sql = "INSERT INTO Invoice (code, store_id, customer_id, salesperson_id, invoice_date) VALUES (" +
                        "?, " +
                        "(SELECT Store.id FROM Store WHERE Store.code = ?), " +
                        "(SELECT Person.id FROM Person WHERE Person.code = ?)," +
                        "(SELECT Person.id FROM Person WHERE Person.code = ?)," +
                        "?)";
        Connection conn = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, invoiceCode);
            ps.setString(2, storeCode);
            ps.setString(3, customerCode);
            ps.setString(4, salesPersonCode);
            ps.setString(5, invoiceDate);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            // 1062 is the error for the unique constraint being broken
            // Therefore we can just return because the person already exists.
            if (e.getErrorCode() == 1062) return;
            Logger.getLogger("fmt").severe("Error adding invoice");
            throw new RuntimeException(e);
        }

    }

    /**
     *  Creates an invoice item, unnecessary fields can be null.
     * @param invoiceCode
     * @param itemCode
     * @param quantity
     * @param purchasePrice
     * @param hoursBilled
     * @param fee
     * @param startDate
     * @param endDate
     * @param discriminator
     */
    private static void addInvoiceItem(String invoiceCode, String itemCode, Integer quantity,
                                       Double purchasePrice, Double hoursBilled, Double fee,
                                       String startDate, String endDate, String discriminator) {
        String insert_sql = "INSERT INTO InvoiceItem (invoice_id, item_id, quantity, purchase_price, " +
                "hours_billed, fee, start_date, end_date, discriminator) VALUES (" +
                "(SELECT Invoice.id FROM Invoice WHERE Invoice.code = ?), " +
                "(SELECT Item.id FROM Item WHERE Item.code = ?), " +
                "?, ?, ?, ?, ?, ?, ?)";

        Connection conn = ConnectionFactory.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(insert_sql);
            ps.setString(1, invoiceCode);
            ps.setString(2, itemCode);
            ps.setObject(3, quantity);
            ps.setObject(4, purchasePrice);
            ps.setObject(5, hoursBilled);
            ps.setObject(6, fee);
            ps.setString(7, startDate);
            ps.setString(8, endDate);
            ps.setString(9, discriminator);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error adding invoice item.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a particular product (identified by <code>itemCode</code>)
     * to a particular invoice (identified by <code>invoiceCode</code>) with the
     * specified quantity.
     *
     * @param invoiceCode
     * @param itemCode
     * @param quantity
     */
    public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {
        String select_sql = "SELECT id, quantity FROM InvoiceItem WHERE " +
                "invoice_id = (SELECT Invoice.id FROM Invoice WHERE Invoice.code = ?) AND " +
                "item_id = (SELECT Item.id FROM Item WHERE Item.code = ?)";
        String update_sql = "UPDATE InvoiceItem SET quantity = quantity + ? WHERE id = ?";

        Connection conn = ConnectionFactory.getConnection();

        try {
            // First check if the same item is already on the invoice
            // if it is simply increment the quantity.
            PreparedStatement ps = conn.prepareStatement(select_sql);
            ps.setString(1, invoiceCode);
            ps.setString(2, itemCode);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                ps = conn.prepareStatement(update_sql);
                ps.setInt(1, quantity);
                ps.setInt(2, rs.getInt("id"));
                ps.execute();
                conn.close();
                return;
            }

            addInvoiceItem(invoiceCode, itemCode, quantity,
                    null, null, null,
                    null, null, "P");
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error adding product to invoice.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds a particular equipment <i>purchase</i> (identified by <code>itemCode</code>) to a
     * particular invoice (identified by <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
     *
     * @param invoiceCode
     * @param itemCode
     * @param purchasePrice
     */
    public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
        addInvoiceItem(invoiceCode, itemCode, null, purchasePrice, null, null, null, null, "PE");
    }

    /**
     * Adds a particular equipment <i>lease</i> (identified by <code>itemCode</code>) to a
     * particular invoice (identified by <code>invoiceCode</code>) with the given 30-day
     * <code>periodFee</code> and <code>beginDate/endDate</code>.
     *
     * @param invoiceCode
     * @param itemCode
     * @param amount
     */
    public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate, String endDate) {
        addInvoiceItem(invoiceCode, itemCode, null, null, null, periodFee, beginDate, endDate, "L");
    }

    /**
     * Adds a particular service (identified by <code>itemCode</code>) to a
     * particular invoice (identified by <code>invoiceCode</code>) with the
     * specified number of hours.
     *
     * @param invoiceCode
     * @param itemCode
     * @param billedHours
     */
    public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
        String select_sql = "SELECT id, hours_billed FROM InvoiceItem WHERE " +
                "invoice_id = (SELECT Invoice.id FROM Invoice WHERE Invoice.code = ?) AND " +
                "item_id = (SELECT Item.id FROM Item WHERE Item.code = ?)";
        String update_sql = "UPDATE InvoiceItem SET hours_billed = hours_billed + ? WHERE id = ?";

        Connection conn = ConnectionFactory.getConnection();

        try {
            // First check if the same item is already on the invoice
            // if it is simply increment the billed hours.
            PreparedStatement ps = conn.prepareStatement(select_sql);
            ps.setString(1, invoiceCode);
            ps.setString(2, itemCode);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                ps = conn.prepareStatement(update_sql);
                ps.setDouble(1, billedHours);
                ps.setInt(2, rs.getInt("id"));
                ps.execute();
                conn.close();
                return;
            }

            addInvoiceItem(invoiceCode, itemCode, null,
                    null, billedHours, null,
                    null, null, "S");
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error adding service to invoice.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Creates or gets a rows id.
     *
     * @param name
     * @param selectSql
     * @param insertSql
     * @return
     */
    private static int createOrGet(String name, String selectSql, String insertSql) {
        int ret;
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(selectSql);

            ps.setString(1, name);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();

            if (!rs.next()) { // State hasn't been created yet.
                ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.execute();

                ResultSet keys = ps.getGeneratedKeys();
                keys.next();

                ret = keys.getInt(1);
            } else {
                ret = rs.getInt("id");
            }

            conn.close();
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error inserting state.");
            throw new RuntimeException(e);
        }

        return ret;
    }

    /**
     * Creates or gets a states id.
     *
     * @param name
     * @return
     */
    private static int createOrGetState(String name) {
        String select_sql = "SELECT id FROM State WHERE name = ?";
        String insert_sql = "INSERT INTO State (name) VALUES (?)";
        return createOrGet(name, select_sql, insert_sql);

    }

    /**
     * Creates or gets a countries id.
     *
     * @param name
     * @return
     */
    private static int createOrGetCountry(String name) {
        String select_sql = "SELECT id FROM Country WHERE name = ?";
        String insert_sql = "INSERT INTO Country (name) VALUES (?)";
        return createOrGet(name, select_sql, insert_sql);
    }

    /**
     * Creates an address if one doesn't already exist, otherwise it returns the id.
     *
     * @param street
     * @param city
     * @param state
     * @param zip
     * @param country
     * @return
     */
    private static int createAddress(String street, String city, String state,
                                      String zip, String country) {
        int state_id = createOrGetState(state);
        int country_id = createOrGetCountry(country);
        int ret;
        String address_insert_sql = "INSERT INTO Address (street, city, state_id, zip_code, country_id) VALUES (?,?,?,?,?)";
        String address_exists_sql = "SELECT id FROM Address WHERE street = ? AND city = ? AND state_id = ? AND zip_code = ? AND country_id = ?";

        try {
            Connection conn = ConnectionFactory.getConnection();
            // Check if address already exists
            PreparedStatement ps = conn.prepareStatement(address_exists_sql);
            ps.setString(1, street);
            ps.setString(2, city);
            ps.setInt(3, state_id);
            ps.setInt(4, Integer.parseInt(zip));
            ps.setInt(5, country_id);

            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                ret = rs.getInt("id");
                conn.close();
                return ret;
            } // Address already exists

            // Insert address as it doesn't exist
            ps = conn.prepareStatement(address_insert_sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, street);
            ps.setString(2, city);
            ps.setInt(3, state_id);
            ps.setInt(4, Integer.parseInt(zip));
            ps.setInt(5, country_id);

            ps.execute();
            rs = ps.getGeneratedKeys();
            rs.next();
            ret = rs.getInt(1);
            conn.close();
            return ret;
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error creating address.");
            throw new RuntimeException(e);
        }
    }
}


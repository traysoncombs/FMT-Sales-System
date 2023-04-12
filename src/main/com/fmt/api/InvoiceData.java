package com.fmt.api;

public class InvoiceData {

    /**
     * Removes all records from all tables in the database.
     */
    public static void clearDatabase() {
        //TODO: implement

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
        //TODO: implement

    }

    /**
     * Adds an email record corresponding person record corresponding to the
     * provided <code>personCode</code>
     *
     * @param personCode
     * @param email
     */
    public static void addEmail(String personCode, String email) {
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

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
        //TODO: implement

    }

}

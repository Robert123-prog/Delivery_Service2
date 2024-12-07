/**
 * Represents a store in the delivery management system.
 * This class implements the HasID interface and manages store information
 * including store details and associated deposits.
 *
 * Relationships:
 * - Store - Deposit: Association (deposits list initialized in constructor)
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class Store implements HasID {
    /** Unique identifier for the store */
    private final Integer storeID;

    /** Name of the store */
    private String name;

    /** Physical address of the store */
    private String address;

    /** Contact information for the store */
    private String contact;

    /** List of deposits associated with this store */
    private List<Deposit> deposits;

    /**
     * Constructs a new Store with the specified details.
     * Initializes an empty list of deposits.
     *
     * @param storeID Unique identifier for the store
     * @param name Name of the store
     * @param address Physical address of the store
     * @param contact Contact information for the store
     */
    public Store(Integer storeID, String name, String address, String contact) {
        this.storeID = storeID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.deposits = new ArrayList<>();
    }

    /**
     * Returns the store's unique identifier.
     *
     * @return The store's ID
     */
    public int getStoreID() {
        return storeID;
    }

    /**
     * Returns the store's name.
     *
     * @return The name of the store
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the store's name.
     *
     * @param name New name for the store
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the store's address.
     *
     * @return The physical address of the store
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the store's address.
     *
     * @param address New address for the store
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the store's contact information.
     *
     * @return The contact information for the store
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the store's contact information.
     *
     * @param contact New contact information for the store
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Returns the list of deposits associated with this store.
     *
     * @return List of store's deposits
     */
    public List<Deposit> getDeposits() {
        return deposits;
    }

    /**
     * Adds a new deposit to the store's deposit list.
     *
     * @param deposit Deposit to be added to the store's deposits
     */
    public void addDeposit(Deposit deposit) {
        deposits.add(deposit);
    }

    /**
     * Returns a string representation of the Store object.
     * Includes store ID, name, address, contact information, and associated deposits.
     *
     * @return String representation of the store
     */
    @Override
    public String toString() {
        return "storeID=" + storeID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", deposits=" + deposits +
                '}';
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "storeID, name, address, contact";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "%d, '%s', '%s', '%s'",
                storeID,
                name,
                address,
                contact
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "name = '%s', address = '%s', contact = '%s'",
                name,
                address,
                contact
        );
    }

    /**
     * Implementation of HasID interface.
     * Returns the store's unique identifier.
     *
     * @return The store's ID
     */
    @Override
    public Integer getId() {
        return storeID;
    }

    /**
     * Serializes the Store object into a CSV string.
     *
     * @return A CSV string representing the state of the Store object
     */
    public String toCsv() {
        StringBuilder serializedDeposits = new StringBuilder();

        // Serialize deposits list
        for (Deposit deposit : deposits) {
            serializedDeposits.append(deposit.toCsv()).append(";");
        }

        // Remove trailing semicolon if there are any deposits
        if (serializedDeposits.length() > 0) {
            serializedDeposits.deleteCharAt(serializedDeposits.length() - 1);
        }

        // Return the serialized store data
        return storeID + "," +
                name + "," +
                address + "," +
                contact + "," +
                serializedDeposits.toString();
    }

    /**
     * Deserializes a CSV string into a Store object.
     *
     * @param csvLine A CSV string containing the serialized data of the Store
     * @return A new Store object created from the CSV string
     */
    public static Store fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", 5); // Split into 5 parts: ID, name, address, contact, deposits

        Integer storeID = Integer.parseInt(parts[0]);
        String name = parts[1];
        String address = parts[2];
        String contact = parts[3];
        String depositsString = parts[4]; // Serialized deposits

        // Create a new Store object
        Store store = new Store(storeID, name, address, contact);

        // Parse deposits from the depositsString
        if (!depositsString.isEmpty()) {
            String[] depositParts = depositsString.split(";");
            for (String depositString : depositParts) {
                Deposit deposit = Deposit.fromCsv(depositString);
                store.addDeposit(deposit);
            }
        }
        return store;
    }
}
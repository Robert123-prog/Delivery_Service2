package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deposit location for storing packages.
 * This class implements the HasID interface and manages deposit information
 * including its associated store, address, status, and packages.
 * It maintains an aggregation relationship with Package and an association with Store.
 */
public class Deposit implements HasID {
    private final Integer depositID;
    private Integer storeID;
    private String address;
    private String status;
    private List<Packages> packages;

    /**
     * Constructs a new Deposit object.
     * Note:
     * - Deposit-Package: Aggregation relationship
     * - Store-Deposit: Association relationship
     * Initializes an empty list of packages and sets the basic deposit information.
     *
     * @param depositID The unique identifier for the deposit
     * @param address   The physical address of the deposit
     * @param status    The current status of the deposit
     * @param storeID   The ID of the associated store
     */
    public Deposit(int depositID, String address, String status, Integer storeID) {
        this.depositID = depositID;
        this.address = address;
        this.status = status;
        this.storeID = storeID;
        this.packages = new ArrayList<>();
    }

    /**
     * Returns the unique identifier of the deposit.
     *
     * @return The deposit ID
     */
    public Integer getDepositID() {
        return depositID;
    }

    /**
     * Returns the ID of the associated store.
     *
     * @return The store ID
     */
    public Integer getStoreID() {
        return storeID;
    }

    /**
     * Sets the ID of the associated store.
     *
     * @param storeID The new store ID
     */
    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    /**
     * Returns the physical address of the deposit.
     *
     * @return The deposit's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets a new address for the deposit.
     *
     * @param address The new physical address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the current status of the deposit.
     *
     * @return The deposit's status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets a new status for the deposit.
     *
     * @param status The new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the list of packages stored in this deposit.
     *
     * @return List of Packages objects in this deposit
     */
    public List<Packages> getPackages() {
        return packages;
    }

    /**
     * Adds a new package to the deposit.
     *
     * @param packag The Packages object to be added to the deposit
     */
    public void addPackage(Packages packag) {
        packages.add(packag);
    }

    /**
     * Returns a string representation of the Deposit object.
     *
     * @return A string containing the deposit's ID, store ID, address, status, and list of packages
     */
    @Override
    public String toString() {
        return "Deposit{" +
                "depositID=" + depositID +
                ", storeID=" + storeID +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "depositID, storeID, address, status";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "%d, %d, '%s', '%s'",
                depositID,
                storeID,
                address,
                status
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "storeID = %d, address = '%s', status = '%s'",
                storeID,
                address,
                status
        );
    }

    /**
     * Returns the ID of this deposit.
     * Implementation of the HasID interface.
     *
     * @return The deposit ID
     */
    public Integer getId() {
        return depositID;
    }

    /**
     * Serializes the Deposit object into a CSV string.
     *
     * @return A CSV string representing the state of the Deposit object
     */
    public String toCsv() {
        StringBuilder serializedPackages = new StringBuilder();

        // Serialize packages list
        for (Packages packag : packages) {
            serializedPackages.append(packag.toCsv()).append(";");
        }

        // Remove trailing semicolon if there are any packages
        if (serializedPackages.length() > 0) {
            serializedPackages.deleteCharAt(serializedPackages.length() - 1);
        }

        // Return the serialized deposit data
        return depositID + "," +
                storeID + "," +
                address + "," +
                status + "," +
                serializedPackages.toString();
    }

    /**
     * Deserializes a CSV string into a Deposit object.
     *
     * @param csvLine A CSV string containing the serialized data of the Deposit
     * @return A new Deposit object created from the CSV string
     */
    public static Deposit fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", 5); // Split into 5 parts: ID, storeID, address, status, packages

        Integer depositID = Integer.parseInt(parts[0]);
        Integer storeID = Integer.parseInt(parts[1]);
        String address = parts[2];
        String status = parts[3];
        String packagesString = parts[4]; // Serialized packages

        // Create a new Deposit object
        Deposit deposit = new Deposit(depositID, address, status, storeID);

        // Parse packages from the packagesString
        if (!packagesString.isEmpty()) {
            String[] packageParts = packagesString.split(";");
            for (String packageString : packageParts) {
                Packages packag = Packages.fromCsv(packageString);
                deposit.addPackage(packag);
            }
        }

        return deposit;
    }
}
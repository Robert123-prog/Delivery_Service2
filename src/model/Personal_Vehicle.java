package model;

/**
 * Represents a personal vehicle used for transportation purposes.
 * This class extends the Transportation class and includes additional attributes
 * specific to personal vehicles such as extra fees and delivery person assignments.
 */
public class Personal_Vehicle extends Transportation {
    private final Integer personalVehicleID;
    private int extraFee;
    private Integer deliveryPersonID;
    private int capacity;
    private Transportation_Type transportation_type;

    /**
     * Constructs a new Personal_Vehicle object.
     * Note: Delivery_Person has an association relationship and is not initialized in the constructor.
     *
     * @param personalVehicleID   The unique identifier for the personal vehicle
     * @param extraFee           Additional fee associated with using this vehicle
     * @param capacity          The carrying capacity of the vehicle
     * @param transportationType The type of transportation (e.g., car, motorcycle)
     */
    public Personal_Vehicle(Integer personalVehicleID, Integer extraFee, int capacity, Transportation_Type transportationType) {
        super(capacity, transportationType);
        this.personalVehicleID = personalVehicleID;
        this.extraFee = extraFee;
    }

    /**
     * Returns the unique identifier of the personal vehicle.
     *
     * @return The personal vehicle ID
     */
    public int getPersonalVehicleID() {
        return personalVehicleID;
    }

    public void setDeliveryPersonID(Integer deliveryPersonID) {
        this.deliveryPersonID = deliveryPersonID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Transportation_Type getTransportation_type() {
        return transportation_type;
    }

    /**
     * Returns the extra fee associated with this vehicle.
     *
     * @return The extra fee amount
     */

    public int getExtraFee() {
        return extraFee;
    }

    /**
     * Sets the extra fee for using this vehicle.
     *
     * @param extraFee The new extra fee amount
     */
    public void setExtraFee(int extraFee) {
        this.extraFee = extraFee;
    }

    /**
     * Sets the transportation type for this vehicle.
     *
     * @param transportation_type The new transportation type
     */
    public void setTransportation_type(Transportation_Type transportation_type) {
        this.transportation_type = transportation_type;
    }

    /**
     * Returns the ID of the delivery person assigned to this vehicle.
     *
     * @return The delivery person ID, or null if no delivery person is assigned
     */
    public Integer getDeliveryPersonID() {
        return deliveryPersonID;
    }

    /**
     * Assigns a delivery person to this vehicle.
     *
     * @param deliveryPersonID The ID of the delivery person to be assigned
     */
    public void setDeliveryPersonID(int deliveryPersonID) {
        this.deliveryPersonID = deliveryPersonID;
    }

    /**
     * Returns a string representation of the Personal_Vehicle object.
     *
     * @return A string containing the vehicle's ID, extra fee, and assigned delivery person ID
     */
    @Override
    public String toString() {
        return "Personal_Vehicle{" +
                "personalVehicleID=" + personalVehicleID +
                ", extraFee=" + extraFee +
                ", capacity=" + capacity +
                ", transportation_type=" + transportation_type +
                ", deliveryPersonID=" + deliveryPersonID +
                '}';
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "personalVehicleID, extraFee, deliveryPersonID, capacity, transportation_type";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "%d, %d, %d, %d, '%s'",
                personalVehicleID,
                extraFee,
                deliveryPersonID,
                capacity,
                transportation_type != null ? transportation_type.name() : null
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "extraFee = %d, deliveryPersonID = %d, capacity = %d, transportation_type = '%s'",
                extraFee,
                deliveryPersonID,
                capacity,
                transportation_type != null ? transportation_type.name() : null
        );
    }

    /**
     * Returns the ID of this personal vehicle.
     * This method provides an alternative way to access the vehicle's ID.
     *
     * @return The personal vehicle ID
     */
    public Integer getId() {
        return personalVehicleID;
    }

    /**
     * Serializes the Personal_Vehicle object into a CSV string.
     *
     * @return A CSV string representing the state of the Personal_Vehicle object
     */
    public String toCsv() {

        return personalVehicleID + "," +
                extraFee + "," +
                capacity + "," +
                (transportation_type != null ? transportation_type.name() : "null");
    }

    /**
     * Deserializes a CSV string into a Personal_Vehicle object.
     *
     * @param csvLine A CSV string containing the serialized data of the Personal_Vehicle
     * @return A new Personal_Vehicle object created from the CSV string
     */
    public static Personal_Vehicle fromCsv(String csvLine) {
        try {
            String[] parts = csvLine.split(",");

            Integer personalVehicleID = Integer.parseInt(parts[0]);
            Integer extraFee = Integer.parseInt(parts[1]);
            int capacity = Integer.parseInt(parts[2]);

            String transportationTypeString = parts[3].trim();
            Transportation_Type transportationType = Transportation_Type.valueOf(transportationTypeString);

            return new Personal_Vehicle(personalVehicleID, extraFee, capacity, transportationType);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid CSV format: " + csvLine, e);
        }
    }
}
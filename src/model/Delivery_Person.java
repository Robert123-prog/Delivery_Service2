package model;

import java.util.ArrayList;
import java.util.List;

public class Delivery_Person extends Person {
    private Integer deliveryPersonID;
    private boolean verified;
    private String license;
    private List<Delivery> deliveries;
    private Integer personalVehicleId;


    public Delivery_Person(int deliveryPersonID, String phone, String name) {
        this.name=name;
        this.phone=phone;
        this.deliveryPersonID = deliveryPersonID;
        this.deliveries = new ArrayList<>();
    }


    public String getLicense() {
        return license;
    }

    public int getDeliveryPersonID() {
        return deliveryPersonID;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Integer getPersonalVehicleId() {
        return personalVehicleId;
    }

    public void addDelivery(Delivery delivery){
        deliveries.add(delivery);
    }
/*
    public boolean removeDelivery(Delivery delivery) {
        boolean removed = deliveries.remove(delivery);
        return removed;
    }
*/
    public List<Delivery> getDeliveries() {
    return deliveries;
}
    public void setPersonalVehicleId(Integer personalVehicleId) {
        this.personalVehicleId = personalVehicleId;
    }

    /**
     * Generates a string with the column names for SQL statements.
     *
     * @return A comma-separated string of column names.
     */
    public static String getColumns() {
        return "deliveryPersonID, verified, license, personalVehicleId, name, phone";
    }

    /**
     * Generates a string with the values of the instance variables for SQL insertion.
     *
     * @return A formatted string of the instance variable values.
     */
    public String getValues() {
        return String.format(
                "%d, %b, '%s', %d, '%s', '%s'",
                deliveryPersonID,
                verified,
                license,
                personalVehicleId,
                name,
                phone
        );
    }

    /**
     * Generates a string with the column-value pairs for SQL update statements.
     *
     * @return A formatted string for SQL update statements.
     */
    public String getUpdateValues() {
        return String.format(
                "verified = %b, license = '%s', personalVehicleId = %d, name = '%s', phone = '%s'",
                verified,
                license,
                personalVehicleId,
                name,
                phone
        );
    }

    @Override
    public String toString() {
        return "Delivery_Person{" +
                "deliveryPersonID=" + deliveryPersonID +
                ", verified=" + verified +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", personalVehicleId=" + personalVehicleId +
                '}';
    }

    @Override
    public Integer getId() {
        return deliveryPersonID;
    }

    public String toCsv(){
        StringBuilder serializedDeliveries = new StringBuilder();

        for (Delivery delivery: deliveries){
            serializedDeliveries.append(delivery.toCsv()).append(";");
        }

        return deliveryPersonID + "," +
               phone + "," +
               name + "," +
               serializedDeliveries.toString();
    }

    public static Delivery_Person fromCsv(String csvLine){
        String[] parts = csvLine.split(",", 4); // Split into 5 parts: ID, verified, license, deliveries, vehicle ID

        Integer deliveryPersonID = Integer.parseInt(parts[0]);
        String deliveryPersonPhone = parts[1];
        String deliveryPersonName = parts[2];

//        boolean verified = Boolean.parseBoolean(parts[1]);
//        String license = parts[2];
        String deliveriesString = parts[3]; // Serialized deliveries
//        Integer personalVehicleId = Integer.parseInt(parts[4]);

        // Create a new Delivery_Person object
        Delivery_Person deliveryPerson = new Delivery_Person(deliveryPersonID, deliveryPersonPhone, deliveryPersonName); // Phone and name left empty

        // Parse deliveries from the deliveriesString
        if (!deliveriesString.isEmpty()) {
            String[] deliveryParts = deliveriesString.split(";");
            for (String deliveryString : deliveryParts) {
                Delivery delivery = Delivery.fromCsv(deliveryString);
                deliveryPerson.addDelivery(delivery);
            }
        }
        return deliveryPerson;
    }
}


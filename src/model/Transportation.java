package model;

/**
 * The Transportation class serves as an abstract base class for different types of transportation.
 * It contains attributes related to the capacity and type of transportation.
 */
public abstract class Transportation implements HasID {
    private int capacity;
    private Transportation_Type transportationType;

    /**
     * Constructs a Transportation object with the specified capacity and transportation type.
     *
     * @param capacity           the maximum capacity of this transportation
     * @param transportationType the type of transportation (e.g., truck, van, etc.)
     */
    public Transportation(int capacity, Transportation_Type transportationType) {
        this.capacity = capacity;
        this.transportationType = transportationType;
    }
}

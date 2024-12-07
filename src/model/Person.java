package model;

public abstract class Person implements HasID {
    protected String name;
    protected String phone;
    protected Integer id;

    /**
     * Returns the name of the employee.
     *
     * @return The employee's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the employee.
     *
     * @param name The new employee name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the phone number of the employee.
     *
     * @return The employee's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets a new phone number for the employee.
     *
     * @param phone The new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}

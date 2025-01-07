/**
 * Service class responsible for handling business logic related to sellers, stores, deposits, packages, deliveries, customers, and orders.
 */
package service;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.*;
import repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SellerService {
    private final IRepository<Store> storeIRepository;
    private final IRepository<Deposit> depositIRepository;
    private final IRepository<Packages> packageIRepository;
    private final IRepository<Delivery> deliveryIRepository;
    private final IRepository<Customer> customerIRepository;
    private final IRepository<Order> orderIRepository;

    /**
     * Constructor for initializing the service with repositories.
     *
     * @param storeIRepository    Repository for managing stores.
     * @param depositIRepository  Repository for managing deposits.
     * @param packageIRepository  Repository for managing packages.
     * @param deliveryIRepository Repository for managing deliveries.
     * @param customerIRepository Repository for managing customers.
     * @param orderIRepository    Repository for managing orders.
     */
    public SellerService(IRepository<Store> storeIRepository, IRepository<Deposit> depositIRepository, IRepository<Packages> packageIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Customer> customerIRepository, IRepository<Order> orderIRepository) {
        this.storeIRepository = storeIRepository;
        this.depositIRepository = depositIRepository;
        this.packageIRepository = packageIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.customerIRepository = customerIRepository;
        this.orderIRepository = orderIRepository;
    }

    /**
     * Retrieves all stores from the repository.
     *
     * @return List of all stores.
     */
    public List<Store> getStores() {
        return storeIRepository.readAll();
    }

    /**
     * Generates a new unique store ID by finding the maximum existing ID and incrementing it by one.
     *
     * @return A new unique store ID.
     */
    public Integer getNewStoreId() {
        int maxId = 0;
        for (Integer Id : storeIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    /**
     * Registers a new store in the system.
     *
     * @param storeId Unique identifier for the store.
     * @param name    Name of the store.
     * @param address Physical address of the store.
     * @param contact Contact information for the store.
     */
    public void registerStore(Integer storeId, String name, String address, String contact) {
        Store newStore = new Store(storeId, name, address, contact);
        storeIRepository.create(newStore);
    }

    /**
     * Generates a new unique deposit ID by finding the maximum existing ID and incrementing it by one.
     *
     * @return A new unique deposit ID.
     */
    public Integer getNewDepositId() {
        int maxId = 0;
        for (Integer Id : depositIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    /**
     * Registers a new deposit and associates it with a store.
     *
     * @param depositId Unique identifier for the deposit.
     * @param storeId   ID of the store to which the deposit belongs.
     * @param address   Address of the deposit.
     * @param status    Status of the deposit.
     * @throws EntityNotFound if the specified store ID does not exist.
     */
    public void registerDeposit(Integer depositId, Integer storeId, String address, String status) {
        Store store = storeIRepository.get(storeId);
        if (store == null) throw new EntityNotFound("No store found with ID " + storeId);

        Deposit newDeposit = new Deposit(depositId, address, status, storeId);
        depositIRepository.create(newDeposit);

        store.addDeposit(newDeposit);
        storeIRepository.update(store);
    }

    /**
     * Retrieves all deposits from the repository.
     *
     * @return List of all deposits.
     */
    public List<Deposit> getDeposits() {
        return depositIRepository.readAll();
    }

    /**
     * Removes a store and dissociates its deposits.
     *
     * @param storeId The ID of the store to remove.
     * @throws EntityNotFound if the specified store ID does not exist.
     */
    public void removeStore(Integer storeId) {
        Store store = storeIRepository.get(storeId);

        if (store == null) throw new EntityNotFound("No store found with ID " + storeId);

        List<Deposit> deposits = store.getDeposits();
        if (deposits == null) throw new BusinessLogicException("The store has no related deposits");

        for (Deposit deposit : deposits) {
            deposit.setStoreID(0); // Null not supported by fromCsv method
            depositIRepository.update(deposit);
        }

        storeIRepository.delete(storeId);
    }

    /**
     * Removes a deposit from a store.
     *
     * @param storeId   The ID of the store.
     * @param depositId The ID of the deposit to remove.
     * @throws EntityNotFound if the specified store or deposit ID does not exist.
     */
    public void removeDeposit(Integer storeId, Integer depositId) {
        Store store = storeIRepository.get(storeId);
        if (store == null) throw new EntityNotFound("No store found with ID " + storeId);

        store.getDeposits().removeIf(deposit -> Objects.equals(deposit.getDepositID(), depositId));
        depositIRepository.delete(depositId);
        storeIRepository.update(store);
    }

    /**
     * Generates a new unique package ID by finding the maximum existing ID and incrementing it by one.
     *
     * @return A new unique package ID.
     */
    public Integer getNewPackageId() {
        int maxId = 0;
        for (Integer Id : packageIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    /**
     * Creates a new package.
     *
     * @param packageId  Unique identifier for the package.
     * @param cost       Cost of the package.
     * @param weight     Weight of the package.
     * @param dimensions Dimensions of the package.
     */
    public void createPackage(Integer packageId, double cost, double weight, String dimensions) {
        Packages packages = new Packages(packageId, weight, dimensions, cost);
        packageIRepository.create(packages);
    }

    /**
     * Retrieves all packages from the repository.
     *
     * @return List of all packages.
     */
    public List<Packages> getPackages() {
        return packageIRepository.readAll();
    }

    /**
     * Removes a package from the repository.
     *
     * @param packageId The ID of the package to remove.
     * @throws EntityNotFound if the specified package ID does not exist.
     */
    public void removePackage(Integer packageId) {
        Packages packages = packageIRepository.get(packageId);
        if (packages == null) throw new EntityNotFound("No package found for ID " + packageId);
        packageIRepository.delete(packageId);
    }

    /**
     * Filters orders based on their delivery location.
     *
     * @param location The location to filter orders by (case-insensitive).
     *                 If {@code null}, no orders will be returned.
     * @return A list of orders that match the specified location.
     */
    public List<Order> filterDeliveriesByLocation(String location) {
        List<Order> allOrders = orderIRepository.readAll();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (location != null && location.equalsIgnoreCase(order.getLocation())) {
                filteredOrders.add(order);
            }
        }

       // if (filteredOrders.isEmpty()) throw new BusinessLogicException("There are no locations set on the orders");

        return filteredOrders;
    }

    /**
     * Creates a new delivery with the specified delivery ID, orders, and location, and adds it to the repository.
     *
     * @param deliveryID The unique identifier for the new delivery. Must not be {@code null}.
     * @param orders     The list of orders to associate with the delivery.
     *                   If {@code null} or empty, no orders will be added.
     * @param location   The location for the delivery. Can be {@code null}.
     */
    public void createDelivery(Integer deliveryID, List<Order> orders, String location) {
        Delivery delivery = new Delivery(deliveryID);
        deliveryIRepository.create(delivery);
        delivery.setLocation(location);

        if (orders != null) {
            for (Order order : orders) {
                delivery.addOrder(order);
            }
        }
        deliveryIRepository.update(delivery);
    }

    /**
     * Generates a new unique delivery ID by finding the maximum existing ID and incrementing it by one.
     *
     * @return A new unique delivery ID.
     */
    public Integer getNewDeliveryId() {
        int maxId = 0;
        for (Integer Id : deliveryIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }
    public List<Packages> getPackagesFromOrder(Integer orderId) {
        Order order = orderIRepository.get(orderId);
        if (order == null) throw new EntityNotFound("No order found with ID " + orderId);

        return order.getPackages();
    }
}

package service;

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


    public SellerService(IRepository<Store> storeIRepository, IRepository<Deposit> depositIRepository, IRepository<Packages> packageIRepository, IRepository<Delivery> deliveryIRepository, IRepository<Customer> customerIRepository, IRepository<Order> orderIRepository){
        this.storeIRepository = storeIRepository;
        this.depositIRepository = depositIRepository;
        this.packageIRepository = packageIRepository;
        this.deliveryIRepository = deliveryIRepository;
        this.customerIRepository = customerIRepository;
        this.orderIRepository = orderIRepository;
    }

    public List<Store> getStores() {
        return storeIRepository.readAll();
    }

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
     * Registers a new store in the system
     *
     * @param storeId Unique identifier for the store
     * @param name Name of the store
     * @param address Physical address of the store
     * @param contact Contact information for the store
     * @throws IllegalArgumentException if any required field is null or empty
     */
    public void registerStore(Integer storeId, String name, String address, String contact) {
        //TODO ADD VALIDATION EXCEPTION IN UI
//        if (storeId == null || name == null || name.isEmpty() || address == null || address.isEmpty() || contact == null || contact.isEmpty()) {
//            throw new IllegalArgumentException("All fields are required for shop registration.");
//        }
        Store newStore = new Store(storeId, name, address, contact);
        storeIRepository.create(newStore);
    }

    public Integer getNewDepositId() {
        int maxId = 0;
        for (Integer Id : depositIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public void registerDeposit(Integer depositId, Integer storeId, String address, String status) {
        List<Store> stores = storeIRepository.readAll();
        boolean existsStore = false;

        for (Store store: stores){
            if (store.getStoreID() == storeId){
                existsStore = true;
                break;
            }
        }

        if (!existsStore) throw new EntityNotFound("No store found with ID " + storeId);

        //TODO ADD VALIDATION EXCEPTION IN UI
/*
        if (depositId == null || storeId == null ||
                address == null || address.isEmpty() ||
                status == null || status.isEmpty()) {
            throw new IllegalArgumentException("All fields are required for deposit registration.");
        }

 */
        Deposit newDeposit = new Deposit(depositId, address, status, storeId);
        depositIRepository.create(newDeposit);

        Store store = storeIRepository.get(storeId);
        store.addDeposit(newDeposit);
        storeIRepository.update(store);

    }

    public List<Deposit> getDeposits(){
        return depositIRepository.readAll();
    }

    //TODO still not working
    public void removeStore(Integer storeId) {
        List<Store> stores = storeIRepository.readAll();
        boolean existsStore = false;

        for (Store store: stores){
            if (store.getStoreID() == storeId){
                existsStore = true;
                break;
            }
        }

        if (!existsStore) throw new EntityNotFound("No store found with ID " + storeId);

        Store store = storeIRepository.get(storeId);

        // get the deposits associated
        List<Deposit> deposits = store.getDeposits();

        for (Deposit deposit : deposits) {
            //null not supported by the fromCsv method
            deposit.setStoreID(0);
            depositIRepository.update(deposit);
        }

        // Delete the store from the repository
        storeIRepository.delete(storeId);

    }

    public void removeDeposit(Integer storeId, Integer depositId) {
        List<Store> stores = storeIRepository.readAll();
        boolean existsStore = false;

        for (Store store: stores){
            if (store.getStoreID() == storeId){
                existsStore = true;
                break;
            }
        }

        if (!existsStore) throw new EntityNotFound("No store found with ID " + storeId);

        List<Deposit> deposits = depositIRepository.readAll();
        boolean existsDeposit = false;

        for (Deposit deposit: deposits){
            if (Objects.equals(deposit.getDepositID(), depositId)){
                existsDeposit = true;
                break;
            }
        }
        if (!existsDeposit) throw new EntityNotFound("No deposit found with ID " + depositId);


        //Deposit deposit = depositIRepository.get(depositId);
        Store store = storeIRepository.get(storeId);
        store.getDeposits().removeIf(deposit -> Objects.equals(deposit.getDepositID(), depositId));
        depositIRepository.delete(depositId);
        storeIRepository.update(store);
    }

    public Integer getNewPackageId(){
        int maxId = 0;
        for (Integer Id : packageIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }

    public void createPackage(Integer packageId, double cost, double weight, String dimensions){
        Packages packages = new Packages(packageId, weight, dimensions, cost);
        packageIRepository.create(packages);
    }

    public List<Packages> getPackages() {
        return packageIRepository.readAll();
    }

    public void removePackage(Integer packageId){
        List<Packages> packages = packageIRepository.readAll();
        boolean existsPackage = false;

        for (Packages packages1: packages){
            if (Objects.equals(packages1.getPackageID(), packageId)){
                existsPackage = true;
                break;
            }
        }
        if (!existsPackage) throw new EntityNotFound("No package found with ID " + packageId);

        customerIRepository.delete(packageId);
    }

    /**
     * Filters orders based on their delivery location.
     *
     * @param location the location to filter orders by (case-insensitive).
     *                 If {@code null}, no orders will be returned.
     * @return a list of orders that match the specified location.
     */
    public List<Order> filterDeliveriesByLocation(String location) {
        List<Order> allOrders = orderIRepository.readAll();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (location != null && location.equalsIgnoreCase(order.getLocation())) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    /** Creates a new delivery with the specified delivery ID, orders, and location,
     * and adds it to the repository.
     *
     * @param deliveryID the unique identifier for the new delivery. Must not be {@code null}.
     * @param orders     the list of orders to associate with the delivery.
     *                   If {@code null} or empty, no orders will be added.
     * @param location   the location for the delivery. Can be {@code null}.
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

    public Integer getNewDeliveryId() {
        int maxId = 0;
        for (Integer Id : deliveryIRepository.getKeys()) {
            if (Id.compareTo(maxId) > 0) {
                maxId = Id;
            }
        }
        return maxId + 1;
    }



}

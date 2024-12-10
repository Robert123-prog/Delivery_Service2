package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import model.*;
import service.SellerService;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SellerController {
    private final SellerService sellerService;
    
    public SellerController(SellerService sellerService){
        this.sellerService = sellerService;
    }

    public void viewAllStores() {
        StringBuilder output = new StringBuilder("Available Stores:\n");
        sellerService.getStores().forEach(store -> output.append(store.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Creates a new store with the provided details.
     *
     * @param name    the name of the store
     * @param address the address of the store
     * @param contact the contact information for the store
     */
    public void createStore(String name, String address, String contact) {
        Integer id = sellerService.getNewStoreId();
        sellerService.registerStore(id, name, address, contact);
        System.out.println("Registered store: " + name);
    }

    /**
     * Registers a deposit for a specific store.
     *
     * @param storeId        the ID of the store receiving the deposit
     * @param depositAddress the address associated with the deposit
     * @param depositStatus  the status of the deposit
     */
    public void registerDeposit(Integer storeId, String depositAddress, String depositStatus) {
        List<Store> stores = sellerService.getStores();
        boolean existsStore = false;

        for (Store store: stores){
            if (store.getStoreID() == storeId){
                existsStore = true;
                break;
            }
        }

        if (!existsStore) throw new EntityNotFound("No store found with ID " + storeId);

        Integer depositId = sellerService.getNewDepositId();
        sellerService.registerDeposit(depositId, storeId, depositAddress, depositStatus);
        System.out.println("Registered deposit " + depositId + " to store " + storeId);
    }

    /**
     * Displays all available deposits.
     */
    public void viewAllDeposits() {
        StringBuilder output = new StringBuilder("Available Deposits:\n");
        sellerService.getDeposits().forEach(deposit -> output.append(deposit.toString()).append("\n"));
        System.out.println(output);
    }

    /**
     * Deletes a store by its ID.
     *
     * @param storeId the ID of the store to delete
     */
    public void deleteStore(Integer storeId) {
        List<Store> stores = sellerService.getStores();
        boolean existsStore = false;

        for (Store store: stores){
            if (store.getStoreID() == storeId){
                existsStore = true;
                break;
            }
        }

        if (!existsStore) throw new EntityNotFound("No store found with ID " + storeId);


        sellerService.removeStore(storeId);
        System.out.println("Removed store " + storeId);
    }

    /**
     * Deletes a specific deposit from a store.
     *
     * @param storeId   the ID of the store from which to delete the deposit
     * @param depositId the ID of the deposit to delete
     */
    public void deleteDeposit(Integer storeId, Integer depositId) {
        List<Store> stores = sellerService.getStores();
        boolean existsStore = false;

        for (Store store: stores){
            if (store.getStoreID() == storeId){
                existsStore = true;
                break;
            }
        }

        if (!existsStore) throw new EntityNotFound("No store found with ID " + storeId);

        List<Deposit> deposits = sellerService.getDeposits();
        Deposit depositAssignedToStore = null;
        boolean existsDeposit = false;

        for (Deposit deposit: deposits){
            if (Objects.equals(deposit.getDepositID(), depositId)){
                existsDeposit = true;
                depositAssignedToStore = deposit;
                break;
            }
        }

        if (!existsDeposit) throw new EntityNotFound("No deposit found with ID " + depositId);

        if (depositAssignedToStore.getStoreID() == null){
            throw new BusinessLogicException("The desired deposit is not assigned to the desired store!");
        }

        sellerService.removeDeposit(storeId, depositId);
        System.out.println("Removed deposit " + depositId + " from store " + storeId);
    }

    /**
     * Creates a new package with specified attributes.
     *
     * @param cost       cost associated with this package
     * @param weight     weight of this package
     * @param dimensions dimensions description for this package
     */
    public void createPackage(double cost, double weight, String dimensions) {
        Integer packageID = sellerService.getNewPackageId();
        sellerService.createPackage(packageID, cost, weight, dimensions);
    }

    public void viewAllPackages() {
        StringBuilder output = new StringBuilder("All Packages:\n");
        sellerService.getPackages().forEach(packages -> output.append(packages.toString()).append("\n"));
        System.out.println(output);
    }

    public void removePackage(Integer packageId){
        List<Packages> packages = sellerService.getPackages();
        boolean existsPackages = false;

        for (Packages packages1: packages){
            if (Objects.equals(packages1.getPackageID(), packageId)){
                existsPackages = true;
                break;
            }
        }

        if (!existsPackages) throw new EntityNotFound("No package found with ID " + packageId);

        sellerService.removePackage(packageId);
    }

    /**
     * @param location
     */

    public void createDelivery(String location) {
        List<Order> orders = sellerService.filterDeliveriesByLocation(location);
        //String[] statuses = {"processing", "to be shipped", "in hub", "in transit"};
        //Random random = new Random();
        //String status = statuses[random.nextInt(statuses.length)];
        for (Order order : orders) {
            String[] statuses = {"processing", "to be shipped", "in hub", "in transit"};
            Random random = new Random();
            String status = statuses[random.nextInt(statuses.length)];
            order.setStatus(status);
        }
        Integer deliveryId = sellerService.getNewDeliveryId();
        sellerService.createDelivery(deliveryId, orders, location);

    }
    
    
}

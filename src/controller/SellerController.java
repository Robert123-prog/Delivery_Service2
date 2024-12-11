package controller;

import exceptions.BusinessLogicException;
import exceptions.EntityNotFound;
import exceptions.ValidationException;
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
        if (name.isEmpty() || address.isEmpty() || contact.isEmpty()){
            throw new ValidationException("One or more required fields is empty");
        }

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
        if (depositAddress.isEmpty() || depositStatus.isEmpty()){
            throw new ValidationException("One or more required fields is missing");
        }
        try {
            Integer depositId = sellerService.getNewDepositId();
            sellerService.registerDeposit(depositId, storeId, depositAddress, depositStatus);
            System.out.println("Registered deposit " + depositId + " to store " + storeId);
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }
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
        try {
            sellerService.removeStore(storeId);
            System.out.println("Removed store " + storeId);
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }catch (BusinessLogicException e1){
            System.out.println(e1.getMessage());
        }
    }

    /**
     * Deletes a specific deposit from a store.
     *
     * @param storeId   the ID of the store from which to delete the deposit
     * @param depositId the ID of the deposit to delete
     */
    public void deleteDeposit(Integer storeId, Integer depositId) {
        try {
            sellerService.removeDeposit(storeId, depositId);
            System.out.println("Removed deposit " + depositId + " from store " + storeId);
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a new package with specified attributes.
     *
     * @param cost       cost associated with this package
     * @param weight     weight of this package
     * @param dimensions dimensions description for this package
     */
    public void createPackage(double cost, double weight, String dimensions) {
        if (dimensions.isEmpty()){
            throw new ValidationException("One or more required fields is missing");
        }

        Integer packageID = sellerService.getNewPackageId();
        sellerService.createPackage(packageID, cost, weight, dimensions);
    }

    public void viewAllPackages() {
        StringBuilder output = new StringBuilder("All Packages:\n");
        sellerService.getPackages().forEach(packages -> output.append(packages.toString()).append("\n"));
        System.out.println(output);
    }

    public void removePackage(Integer packageId){
        try {
            sellerService.removePackage(packageId);
        }catch (EntityNotFound e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param location
     */

    public void createDelivery(String location) {
        if (location.isEmpty()) throw new ValidationException("The required field is invalid");

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

/**
 * Assignment 7: Vehicle Fleet Management System
 * Demonstrates Inheritance, Fleet Resource Management, and Operational Cost Analysis
 */

import java.util.*;

/**
 * Base Vehicle Class
 */
abstract class Vehicle {
    protected String vehicleId;
    protected String brand;
    protected String model;
    protected int year;
    protected double mileage;
    protected String fuelType;
    protected String currentStatus;
    protected Driver assignedDriver;
    protected double runningCost;
    protected double fuelConsumed;

    // Static variables
    public static int totalVehicles = 0;
    public static double fleetValue = 0;
    public static String companyName = "TransFleet Logistics";
    public static double totalFuelConsumption = 0;

    // Constructor
    public Vehicle(String vehicleId, String brand, String model, int year, double mileage, String fuelType) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.currentStatus = "Available";
        this.runningCost = 0;
        this.fuelConsumed = 0;
        totalVehicles++;
    }

    // Assign Driver
    public void assignDriver(Driver driver) {
        this.assignedDriver = driver;
        driver.assignVehicle(this);
        this.currentStatus = "Assigned";
        System.out.println("Driver " + driver.getDriverName() + " assigned to vehicle " + vehicleId);
    }

    // Schedule Maintenance
    public void scheduleMaintenance() {
        this.currentStatus = "Under Maintenance";
        System.out.println("Vehicle " + vehicleId + " scheduled for maintenance.");
    }

    // Update Mileage
    public void updateMileage(double km, double fuelUsed) {
        this.mileage += km;
        this.fuelConsumed += fuelUsed;
        totalFuelConsumption += fuelUsed;
        this.runningCost += calculateRunningCost();
    }

    // Check if service due (every 10,000 km)
    public boolean checkServiceDue() {
        return mileage % 10000 < 500; // due soon within 500 km
    }

    // Abstract method: Each vehicle type calculates cost differently
    public abstract double calculateRunningCost();

    public String getVehicleType() {
        return this.getClass().getSimpleName();
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public double getRunningCost() {
        return runningCost;
    }

    public double getFuelConsumed() {
        return fuelConsumed;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }
}

/**
 * Car class
 */
class Car extends Vehicle {
    private int passengerCapacity;

    public Car(String vehicleId, String brand, String model, int year, double mileage, String fuelType, int passengerCapacity) {
        super(vehicleId, brand, model, year, mileage, fuelType);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public double calculateRunningCost() {
        return 5.0 * mileage; // Example: ₹5 per km
    }
}

/**
 * Bus class
 */
class Bus extends Vehicle {
    private int seatingCapacity;

    public Bus(String vehicleId, String brand, String model, int year, double mileage, String fuelType, int seatingCapacity) {
        super(vehicleId, brand, model, year, mileage, fuelType);
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public double calculateRunningCost() {
        return 8.0 * mileage; // Example: ₹8 per km
    }
}

/**
 * Truck class
 */
class Truck extends Vehicle {
    private double loadCapacity; // in tons

    public Truck(String vehicleId, String brand, String model, int year, double mileage, String fuelType, double loadCapacity) {
        super(vehicleId, brand, model, year, mileage, fuelType);
        this.loadCapacity = loadCapacity;
    }

    @Override
    public double calculateRunningCost() {
        return 12.0 * mileage; // Example: ₹12 per km
    }
}

/**
 * Driver Class
 */
class Driver {
    private String driverId;
    private String driverName;
    private String licenseType;
    private Vehicle assignedVehicle;
    private int totalTrips;

    public Driver(String driverId, String driverName, String licenseType) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.licenseType = licenseType;
        this.totalTrips = 0;
    }

    public void assignVehicle(Vehicle vehicle) {
        this.assignedVehicle = vehicle;
    }

    public void recordTrip(double distance, double fuelUsed) {
        if (assignedVehicle != null) {
            assignedVehicle.updateMileage(distance, fuelUsed);
            totalTrips++;
            System.out.println(driverName + " completed trip of " + distance + " km in vehicle " + assignedVehicle.getVehicleId());
        } else {
            System.out.println(driverName + " has no vehicle assigned.");
        }
    }

    public String getDriverName() {
        return driverName;
    }

    public int getTotalTrips() {
        return totalTrips;
    }
}

/**
 * Fleet Management System
 */
public class Assignment7_YourName {

    // Static method: Fleet utilization
    public static void getFleetUtilization(Vehicle[] vehicles) {
        System.out.println("\n----- Fleet Utilization Report -----");
        for (Vehicle v : vehicles) {
            System.out.println(v.getVehicleId() + " (" + v.getVehicleType() + ") - Status: " + v.getCurrentStatus() +
                    ", Mileage: " + v.mileage + ", Fuel Consumed: " + v.getFuelConsumed());
        }
    }

    // Static method: Total maintenance cost
    public static double calculateTotalMaintenanceCost(Vehicle[] vehicles) {
        double total = 0;
        for (Vehicle v : vehicles) {
            total += v.getRunningCost() * 0.05; // Assume 5% of running cost as maintenance
        }
        return total;
    }

    // Static method: Vehicles by type
    public static void getVehiclesByType(Vehicle[] vehicles, String type) {
        System.out.println("\n----- Vehicles of Type: " + type + " -----");
        for (Vehicle v : vehicles) {
            if (v.getVehicleType().equalsIgnoreCase(type)) {
                System.out.println(v.getVehicleId() + " - " + v.brand + " " + v.model);
            }
        }
    }

    public static void main(String[] args) {
        // Create vehicles
        Car car1 = new Car("V101", "Toyota", "Corolla", 2020, 12000, "Petrol", 5);
        Bus bus1 = new Bus("V102", "Volvo", "9400", 2018, 50000, "Diesel", 45);
        Truck truck1 = new Truck("V103", "Tata", "Prima", 2019, 80000, "Diesel", 20);

        Vehicle[] fleet = {car1, bus1, truck1};

        // Create drivers
        Driver d1 = new Driver("D201", "Rajesh", "LMV");
        Driver d2 = new Driver("D202", "Amit", "HMV");

        // Assign drivers
        car1.assignDriver(d1);
        truck1.assignDriver(d2);

        // Record trips
        d1.recordTrip(120, 10);
        d2.recordTrip(300, 50);

        // Maintenance check
        for (Vehicle v : fleet) {
            if (v.checkServiceDue()) {
                v.scheduleMaintenance();
            }
        }

        // Reports
        getFleetUtilization(fleet);
        System.out.println("\nTotal Maintenance Cost: ₹" + calculateTotalMaintenanceCost(fleet));
        getVehiclesByType(fleet, "Truck");
    }
}

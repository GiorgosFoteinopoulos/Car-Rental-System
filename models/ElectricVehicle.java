package models;

import enums.VehicleCategory;

public class ElectricVehicle extends Vehicle {

    private double batteryCapacity;
    private int range;

    public ElectricVehicle(String licensePlate, String make, String model, int year, VehicleCategory category,
            double batteryCapacity, int range) {
        super(licensePlate, make, model, year, category);

        if (batteryCapacity <= 0)
            throw new IllegalArgumentException("Battery capacity must be positive");
        if (range <= 0)
            throw new IllegalArgumentException("Range must be positive");

        this.batteryCapacity = batteryCapacity;
        this.range = range;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public int getRange() {
        return range;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        if (batteryCapacity <= 0)
            throw new IllegalArgumentException("Battery capacity must be positive");
        this.batteryCapacity = batteryCapacity;
    }

    public void setRange(int range) {
        if (range <= 0)
            throw new IllegalArgumentException("Range must be positive");
        this.range = range;
    }

    @Override
    public String toString() {
        return String.format("[ELECTRIC]  %s  |  Battery: %.1fkWh  Range: %dkm",
                baseInfo(), batteryCapacity, range);
    }
}

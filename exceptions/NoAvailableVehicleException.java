package exceptions;

public class NoAvailableVehicleException extends Exception {

    private final String vehicleCategory;
    private final int totalInCategory;
    private final String requestedPeriod;

    public NoAvailableVehicleException(String vehicleCategory, int totalInCategory,
            String requestedPeriod) {
        super("No " + vehicleCategory + " vehicles available for " + requestedPeriod
                + ". All " + totalInCategory + " vehicles are currently rented.");
        this.vehicleCategory = vehicleCategory;
        this.totalInCategory = totalInCategory;
        this.requestedPeriod = requestedPeriod;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public int getTotalInCategory() {
        return totalInCategory;
    }

    public String getRequestedPeriod() {
        return requestedPeriod;
    }
}

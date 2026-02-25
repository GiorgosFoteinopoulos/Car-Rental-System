package exceptions;

public class VehicleAlreadyRentedException extends Exception {

    private final String licensePlate;
    private final String currentRenter;
    private final String expectedReturnDate;

    public VehicleAlreadyRentedException(String licensePlate, String currentRenter,
            String expectedReturnDate) {
        super("Vehicle " + licensePlate + " is currently rented. "
                + "Expected return: " + expectedReturnDate);
        this.licensePlate = licensePlate;
        this.currentRenter = currentRenter;
        this.expectedReturnDate = expectedReturnDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getCurrentRenter() {
        return currentRenter;
    }

    public String getExpectedReturnDate() {
        return expectedReturnDate;
    }
}

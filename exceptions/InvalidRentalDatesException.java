package exceptions;

public class InvalidRentalDatesException extends Exception {

    private final String startDate;
    private final String endDate;
    private final String reason;

    public InvalidRentalDatesException(String startDate, String endDate, String reason) {
        super("Invalid rental dates — " + reason
                + " (Start: " + startDate + ", End: " + endDate + ")");
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }
}

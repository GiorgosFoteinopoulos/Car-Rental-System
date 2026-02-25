package exceptions;

public class InvalidRatingException extends Exception {

    private final int attemptedRating;

    public InvalidRatingException(int attemptedRating) {
        super("Rating must be between 1 and 5. You entered: " + attemptedRating);
        this.attemptedRating = attemptedRating;
    }

    public int getAttemptedRating() {
        return attemptedRating;
    }
}

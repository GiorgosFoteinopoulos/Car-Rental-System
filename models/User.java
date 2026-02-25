package models;

public abstract class User {

    private static int nextUserId = 1;

    private final int userId;
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty.");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty.");
        if (role == null || role.isBlank())
            throw new IllegalArgumentException("Role cannot be empty");

        this.userId = nextUserId++;
        this.username = username.trim();
        this.password = password;
        this.role = role.toUpperCase();
    }

    public boolean authenticate(String attemptedPassword) {
        return this.password.equals(attemptedPassword);
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty.");
        this.username = username.trim();
    }

    protected void setPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 4)
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        this.password = newPassword;
    }

    public abstract void displayMenu();

    @Override
    public String toString() {
        return String.format("User[id=%d, username=%s, role=%s]",
                userId, username, role);
    }
}

//Singleton Design Pattern


package application;

public class SessionManager {
    private static int currentUserId;
    private static String currentUserName;  

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    // Add methods to set and get civilian's name
    public static void setCurrentUserName(String userName) {
        currentUserName = userName;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }
}

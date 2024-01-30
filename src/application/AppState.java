package application;

public class AppState {
    private static AppState instance = new AppState();

    private boolean isTestimonyRequested = false;

    private AppState() {}

    public static AppState getInstance() {
        return instance;
    }

    public boolean isTestimonyRequested() {
        return isTestimonyRequested;
    }

    public void setTestimonyRequested(boolean isTestimonyRequested) {
        this.isTestimonyRequested = isTestimonyRequested;
    }
}


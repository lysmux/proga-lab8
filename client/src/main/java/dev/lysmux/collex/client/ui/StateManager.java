package dev.lysmux.collex.client.ui;

import lombok.Data;


@Data
public class StateManager {
    private int userId = -1;

    public boolean isUserLoggedIn() {
        return userId != -1;
    }
    public void logoutUser() {
        userId = -1;
    }
}

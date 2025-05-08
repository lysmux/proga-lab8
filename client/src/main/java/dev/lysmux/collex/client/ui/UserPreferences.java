package dev.lysmux.collex.client.ui;

import java.util.Locale;
import java.util.prefs.Preferences;

public class UserPreferences {
    private static final Preferences preferences = Preferences.userNodeForPackage(UserPreferences.class);

    private static final String CURRENT_THEME = "currentTheme";
    private static final String CURRENT_LOCALE = "currentLocale";
    private static final String SERVER_HOST = "serverHost";
    private static final String SERVER_PORT = "serverPort";

    public Theme getCurrentTheme() {
        return Theme.valueOf(preferences.get(CURRENT_THEME, Theme.LIGHT.name()));
    }

    public void setCurrentTheme(Theme currentTheme) {
        preferences.put(CURRENT_THEME, currentTheme.name());
    }

    public Locale getCurrentLocale() {
        return Locale.forLanguageTag(preferences.get(CURRENT_LOCALE, Locale.getDefault().toLanguageTag()));
    }

    public void setCurrentLocale(Locale currentLocale) {
        preferences.put(CURRENT_LOCALE, currentLocale.toLanguageTag());
    }

    public String getServerHost() {
        return preferences.get(SERVER_HOST, null);
    }

    public void setServerHost(String serverHost) {
        preferences.put(SERVER_HOST, serverHost);
    }

    public Integer getServerPort() {
        int port = preferences.getInt(SERVER_PORT, -1);
        return port != -1 ? port : null;
    }

    public void setServerPort(Integer serverPort) {
        preferences.putInt(SERVER_PORT, serverPort);
    }
}

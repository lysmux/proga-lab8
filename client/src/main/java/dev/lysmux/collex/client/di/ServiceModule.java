package dev.lysmux.collex.client.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import dev.lysmux.collex.client.network.Client;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.UserPreferences;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Client.class).in(Singleton.class);
    }
}

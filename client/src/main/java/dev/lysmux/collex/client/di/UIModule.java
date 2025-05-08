package dev.lysmux.collex.client.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.UserPreferences;

public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StateManager.class).in(Singleton.class);
        bind(UserPreferences.class).in(Singleton.class);
        bind(SceneManager.class).in(Singleton.class);
    }
}

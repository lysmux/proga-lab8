package dev.lysmux.collex.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.logic.service.user.UserService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserService.class).in(Singleton.class);
        bind(CollectionService.class).in(Singleton.class);
    }
}

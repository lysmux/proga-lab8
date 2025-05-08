package dev.lysmux.collex.di;

import com.google.inject.AbstractModule;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.logic.service.user.UserService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserService.class);
        bind(CollectionService.class);
    }
}

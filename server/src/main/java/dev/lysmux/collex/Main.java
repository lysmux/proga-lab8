package dev.lysmux.collex;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.lysmux.collex.config.Config;
import dev.lysmux.collex.di.DatabaseModule;
import dev.lysmux.collex.di.ServiceModule;
import dev.lysmux.collex.presentation.GlobalExceptionHandler;
import dev.lysmux.collex.presentation.UserResolver;
import dev.lysmux.collex.presentation.command.PingCommand;
import dev.lysmux.collex.presentation.command.auth.ChangePasswordCommand;
import dev.lysmux.collex.presentation.command.auth.GetUserInfoCommand;
import dev.lysmux.collex.presentation.command.auth.LoginCommand;
import dev.lysmux.collex.presentation.command.auth.RegisterCommand;
import dev.lysmux.collex.presentation.command.collection.*;
import dev.lysmux.collex.server.ServerConfig;
import dev.lysmux.collex.server.ServerApplication;
import dev.lysmux.collex.server.command.registry.CommandRegistry;

public class Main {
    public static void main(String[] args) {
        Config config = Config.fromEnv();

        Injector injector = Guice.createInjector(
                new ServiceModule(),
                new DatabaseModule(config.database())
        );

        CommandRegistry cmdRegistry = new CommandRegistry() {
            {
                registerCommand(injector.getInstance(PingCommand.class));

                registerCommand(injector.getInstance(ChangePasswordCommand.class));
                registerCommand(injector.getInstance(LoginCommand.class));
                registerCommand(injector.getInstance(RegisterCommand.class));
                registerCommand(injector.getInstance(GetUserInfoCommand.class));

                registerCommand(injector.getInstance(AddCommand.class));
                registerCommand(injector.getInstance(AddIfMaxCommand.class));
                registerCommand(injector.getInstance(AddRandomCommand.class));
                registerCommand(injector.getInstance(ClearCommand.class));
                registerCommand(injector.getInstance(CountGreaterThanMinimalPointCommand.class));
                registerCommand(injector.getInstance(PrintFieldDescendingMinimalPoint.class));
                registerCommand(injector.getInstance(RemoveAnyByMinimalPointCommand.class));
                registerCommand(injector.getInstance(RemoveByIDCommand.class));
                registerCommand(injector.getInstance(RemoveGreaterCommand.class));
                registerCommand(injector.getInstance(RemoveLowerCommand.class));
                registerCommand(injector.getInstance(ShowCommand.class));
                registerCommand(injector.getInstance(UpdateCommand.class));
            }
        };

        ServerConfig serverConfig = ServerConfig.builder()
                .port(config.listenPort())
                .argumentResolver(injector.getInstance(UserResolver.class))
                .exceptionHandler(new GlobalExceptionHandler())
                .build();

        ServerApplication server = new ServerApplication(cmdRegistry, serverConfig);
        server.run();
    }
}
package dev.lysmux.collex.server.command.registry;

import dev.lysmux.collex.server.command.wrapper.CommandWrapper;

import java.util.HashMap;
import java.util.Optional;

public class CommandRegistry {
    private final HashMap<String, CommandWrapper> commands = new HashMap<>();

    public void registerCommand(Object command) {
        CommandWrapper commandWrapper = CommandWrapper.ofCommand(command);
        commands.put(commandWrapper.getName(), commandWrapper);
    }

    public CommandWrapper getCommand(String name) throws CommandNotFoundException {
        return Optional.ofNullable(commands.get(name)).orElseThrow(() -> new CommandNotFoundException(name));
    }
}

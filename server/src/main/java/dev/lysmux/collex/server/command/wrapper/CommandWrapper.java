package dev.lysmux.collex.server.command.wrapper;

import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class CommandWrapper {
    private final Object command;
    private final Method executeMethod;
    private final Command annotation;

    private CommandWrapper(Object command, Method executeMethod, Command annotation) {
        this.command = command;
        this.executeMethod = executeMethod;
        this.annotation = annotation;
    }

    public static CommandWrapper ofCommand(Object command) {
        Class<?> clazz = command.getClass();
        Method[] executeMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals("execute")).toArray(Method[]::new);

        if (!clazz.isAnnotationPresent(Command.class)) {
            throw new NotACommandException("Command must be annotated with @Command");
        }

        if (executeMethods.length != 1) {
            throw new NotACommandException("Command must have exactly one `execute` method");
        }

        if (executeMethods[0].getReturnType() != Response.class) {
            throw new NotACommandException("Command must return a `Response` object");
        }

        return new CommandWrapper(command, executeMethods[0], clazz.getAnnotation(Command.class));
    }

    public String getName() {
        return annotation.name();
    }

    public Parameter[] getArgs() {
        return executeMethod.getParameters();
    }

    public Response<?> execute(Object... args) throws Exception {
        try {
            return (Response<?>) executeMethod.invoke(command, args);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        } catch (IllegalArgumentException e) {
            Class<?>[] requiredArgs = executeMethod.getParameterTypes();
            Class<?>[] providedArgs = Arrays.stream(args)
                    .map(Object::getClass)
                    .toArray(Class<?>[]::new);

            throw new BadCommandArgsException(requiredArgs, providedArgs);
        }
    }
}

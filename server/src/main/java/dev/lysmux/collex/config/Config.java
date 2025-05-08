package dev.lysmux.collex.config;


public record Config(
        int listenPort,
        DbConfig database
) {
    public static Config fromEnv() {
        return new Config(
                Integer.parseInt(System.getenv("LISTEN_PORT")),
                new DbConfig(
                        System.getenv("DATABASE_HOST"),
                        Integer.parseInt(System.getenv("DATABASE_PORT")),
                        System.getenv("DATABASE_USER"),
                        System.getenv("DATABASE_PASSWORD"),
                        System.getenv("DATABASE_NAME")
                )
        );
    }
}

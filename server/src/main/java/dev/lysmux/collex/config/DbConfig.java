package dev.lysmux.collex.config;

public record DbConfig(
        String host,
        int port,
        String user,
        String password,
        String dbName
) {
    public String jdbc() {
        return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
    }
}

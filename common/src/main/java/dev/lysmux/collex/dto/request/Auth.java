package dev.lysmux.collex.dto.request;

import java.io.Serializable;

public record Auth(String login, String password) implements Serializable {
}

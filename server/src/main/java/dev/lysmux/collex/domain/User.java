package dev.lysmux.collex.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;

@Builder
@Data
@Setter(AccessLevel.PRIVATE)
public class User {
    int id;

    String login;

    String hashedPassword;

    @Builder.Default
    LocalDateTime registrationTime = LocalDateTime.now();

    public static UserBuilder builderWithRawPassword(String rawPassword) {
        return User.builder().hashedPassword(hashPassword(rawPassword));
    }

    public void setRawPassword(String password) {
        this.hashedPassword = hashPassword(password);
    }

    public boolean verifyPassword(String password) {
        String hashedPassword = hashPassword(password);
        return this.hashedPassword.equals(hashedPassword);
    }

    private static String hashPassword(String password) {
        return DigestUtils.sha3_224Hex(password);
    }
}

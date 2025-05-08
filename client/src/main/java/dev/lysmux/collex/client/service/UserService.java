package dev.lysmux.collex.client.service;

import com.google.inject.Inject;
import dev.lysmux.collex.client.network.Client;
import dev.lysmux.collex.dto.UserInfo;
import dev.lysmux.collex.dto.request.Auth;
import dev.lysmux.collex.dto.request.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserService {
    private final Client client;

    public int login(String login, String password) {
        client.setAuth(new Auth(login, password));
        Body body = Body.builder().command("login").build();
        Response<Integer> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS
                ? response.getBody().result()
                : -1;
    }

    public int register(String login, String password) {
        client.setAuth(new Auth(login, password));
        Body body = Body.builder()
                .command("register")
                .arg("login", login)
                .arg("password", password)
                .build();
        Response<Integer> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS
                ? response.getBody().result()
                : -1;
    }

    public void logout() {
        client.setAuth(null);
    }

    public void changePassword(String newPassword) {
        Body body = Body.builder()
                .command("changePassword")
                .arg("newPassword", newPassword)
                .build();
        client.sendRequest(body);
    }

    public UserInfo getUserInfo() {
        Body body = Body.builder().command("getUserInfo").build();
        Response<UserInfo> response = client.sendRequest(body);
        return response.getBody().result();
    }
}

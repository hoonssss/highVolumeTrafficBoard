package com.example.boardserver.dto.response;

import com.example.boardserver.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class LoginResponse {

    enum LoginStatus{
        SUCCESS,FAIL,DELETED
    }

    @NonNull
    private LoginStatus status;
    private UserDTO userDto;

    private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

    public static LoginResponse success(UserDTO userDto) {
        return new LoginResponse(LoginStatus.SUCCESS);
    }

}

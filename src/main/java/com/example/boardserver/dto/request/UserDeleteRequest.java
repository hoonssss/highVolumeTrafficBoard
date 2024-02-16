package com.example.boardserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@NonNull
@AllArgsConstructor
@Getter
public class UserDeleteRequest {

    private String userId;
    private String password;

}

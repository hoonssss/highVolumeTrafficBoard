package com.example.boardserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NonNull
public class UserDatePasswordRequest {

    private String beforePassword;
    @Setter
    private String afterPassword;

}

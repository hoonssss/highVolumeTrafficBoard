package com.example.boardserver.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    public boolean hasNullDateBeforeRegister(UserDTO userDto) {
        return userDto.getUserId() == null || userDto.getPassword() == null || userDto.getNickName() == null;
    }

    public enum Status{
        DEFAULT, ADMIN, DELETED
    }

    private Integer id;
    private String userId;
    private String password;
    private String nickName;
    private boolean isAdmin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;

}

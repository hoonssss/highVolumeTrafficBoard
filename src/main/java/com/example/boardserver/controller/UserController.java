package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.aop.LoginCheck.UserType;
import com.example.boardserver.dto.request.UserDatePasswordRequest;
import com.example.boardserver.dto.request.UserDeleteRequest;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.UserDTO.Status;
import com.example.boardserver.dto.request.UserLoginRequest;
import com.example.boardserver.dto.response.LoginResponse;
import com.example.boardserver.dto.response.UserInfoResponse;
import com.example.boardserver.service.impl.UserServiceImpl;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private static ResponseEntity<LoginResponse> response;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody UserDTO userDto) {
        if (userDto.hasNullDateBeforeRegister(userDto)) {
            throw new RuntimeException("회원가입 정보를 확인하세요.");
        }
        userService.register(userDto);
    }

    @PostMapping("/sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {

        ResponseEntity<LoginResponse> responseEntity = null;
        String id = userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();
        UserDTO userInfo = userService.login(id, password);
        LoginResponse loginResponse;

        if (userInfo == null) {
            return HttpStatus.NOT_FOUND;
        } else if (userInfo != null) {
            loginResponse = LoginResponse.success(userInfo);
            if (userInfo.getStatus() == (Status.ADMIN)) {
                SessionUtil.setLoginAdminId(session, id);
            } else {
                SessionUtil.setLoginMemberId(session, id);
                SessionUtil.setLoginMemberId(session, id);
            }

            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error");
        }

        return HttpStatus.OK;
    }

    @GetMapping("/my-info")
    public UserInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) id = SessionUtil.getLoginAdminId(session);
        UserDTO userDto = userService.getUserInfo(id);
        return new UserInfoResponse(userDto);
    }

    @PutMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    @LoginCheck(type = UserType.DEFAULT)
    public ResponseEntity<LoginResponse> updateUserPassword(@RequestParam String accountId, @RequestBody UserDatePasswordRequest userUpdatePasswordRequest,
        HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = accountId;
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();
        if (id == null || beforePassword == null || afterPassword == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        try {
            userService.updatePassword(id, beforePassword, afterPassword);
            UserDTO userInfo = userService.login(id, afterPassword);
            LoginResponse loginResponse = LoginResponse.success(userInfo);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            log.error("updatePassword 실패", e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
    @DeleteMapping
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteRequest userDeleteId, HttpSession session){
        String id = SessionUtil.getLoginMemberId(session);
        if(id==null) id = SessionUtil.getLoginAdminId(session);
        try {
            userService.deleteId(id, userDeleteId.getPassword());
            response = new ResponseEntity<LoginResponse>(HttpStatus.OK);
        }catch (RuntimeException e){
            log.error("deleteId Error {}",e.getMessage() );
            response = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

}

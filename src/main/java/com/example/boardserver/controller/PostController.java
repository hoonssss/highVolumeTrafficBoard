package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.aop.LoginCheck.UserType;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.dto.response.CommonResponse;
import com.example.boardserver.service.impl.PostServiceImpl;
import com.example.boardserver.service.impl.UserServiceImpl;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = UserType.USER)
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(@RequestParam String accountId,
        @RequestBody PostDTO postDto) {
        postService.register(accountId, postDto);
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK, "SUCCESS", "생성 성공", postDto));
    }

    @GetMapping("/my-posts")
    @LoginCheck(type = UserType.USER)
    public ResponseEntity<CommonResponse<List<PostDTO>>> myPostInfo(
        @RequestParam String accountId) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<PostDTO> postDTOList = postService.getMyPosts(memberInfo.getId());
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "조회 성공",
            postDTOList);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("/{postId}")
    @LoginCheck(type = UserType.USER)
    public ResponseEntity<CommonResponse<PostResponse>> updatePosts(@RequestParam String accountId,
        @PathVariable(name = "postId") int postId, @RequestBody PostRequest postRequest) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
            .id(postId)
            .name(postRequest.getName())
            .contents(postRequest.getContents())
            .views(postRequest.getViews())
            .categoryId(postRequest.getCategoryId())
            .userId(postRequest.getUserId())
            .fileId(postRequest.getFiledId())
            .updateTime(new Date())
            .build();
        postService.updatePosts(postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS",
            "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/{postId}")
    @LoginCheck(type = UserType.USER)
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deletePosts(
        @RequestParam String accountId,
        @PathVariable(name = "postId") int postId) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        postService.deletePosts(memberInfo.getId(), postId);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePost",
            postId);
        return ResponseEntity.ok(commonResponse);
    }

    //--Post Response--
    @Getter
    @AllArgsConstructor
    private static class PostResponse {

        private List<PostDTO> postDTOS;
    }

    //--Post Request--
    @Setter
    @Getter
    private static class PostRequest {

        private String name;
        private String contents;
        private int views;
        private int categoryId;
        private int userId;
        private int filedId;
        private Date updateTime;
    }

    @Getter
    @Setter
    private static class PostDeleteRequest {

        private String name;
    }

}

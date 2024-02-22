package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.mapper.CategoryMapper;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.PostService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserProfileMapper userProfileMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        if (memberInfo != null) {
            postDTO.setUserId(memberInfo.getId());
            postDTO.setCreateTime(new Date());
            CategoryDTO categoryId = categoryMapper.findById(postDTO.getCategoryId());
            if (categoryId != null) {
                postMapper.register(postDTO);
            } else {
                log.error("register error [{}]", postDTO);
                throw new RuntimeException("register error: invalid categoryId " + postDTO);
            }
        } else {
            log.error("register error [{}]", postDTO);
            throw new RuntimeException("register error: invalid userId " + postDTO);
        }
    }


    @Override
    public List<PostDTO> getMyPosts(int accountId) {
        List<PostDTO> postDTOS = postMapper.selectMyPosts(accountId);
        return postDTOS;
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if (postDTO != null && postDTO.getId() != 0) {
            postMapper.updatePosts(postDTO);
        } else {
            log.error("update error [{}]", postDTO);
            throw new RuntimeException("update error " + postDTO);
        }
    }

    @Override
    public void deletePosts(int userId, int postId) {
        if (userId != 0 && postId != 0) {
            postMapper.deletePosts(postId);
        } else {
            log.error("delete error [{}]", postId);
            throw new RuntimeException("delete error " + postId);
        }
    }
}

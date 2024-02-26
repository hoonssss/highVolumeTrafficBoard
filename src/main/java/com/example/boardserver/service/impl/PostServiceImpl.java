package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
import com.example.boardserver.dto.UserDTO;
import com.example.boardserver.mapper.CategoryMapper;
import com.example.boardserver.mapper.CommentMapper;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.mapper.TagMapper;
import com.example.boardserver.mapper.UserProfileMapper;
import com.example.boardserver.service.PostService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final UserProfileMapper userProfileMapper;
    private final CategoryMapper categoryMapper;
    private final CommentMapper commentMapper;
    private final TagMapper tagMapper;

    @CacheEvict(value = "getPost", allEntries = true)
    @Override
    public void register(String accountId, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(accountId);
        if (memberInfo != null && categoryMapper.findById(postDTO.getCategoryId()) != null) {
            try {
                postDTO.setUserId(memberInfo.getId());
                postDTO.setCreateTime(new Date());
                postMapper.register(postDTO);
                Integer postId = postDTO.getId();
                //생성된 post 객체 에서 태그 리스트 생성
//            postDTO.getTagDTOList().stream()
//                .map(tagDTO -> {
//                    tagMapper.register(tagDTO);
//                    return tagDTO.getId();
//                })
//                .forEach(tagId -> tagMapper.createPostTag(tagId, postId));
//            for (int i = 0; i < postDTO.getTagDTOList().size(); i++) {
//                TagDTO tagDTO = postDTO.getTagDTOList().get(i);
//                Integer tagId = tagDTO.getId();
//                tagMapper.register(tagDTO);
//                tagMapper.createPostTag(tagId,postId);
//            }
                postDTO.getTagDTOList().forEach(tagDTO -> {
                    tagMapper.register(tagDTO);
                    tagMapper.createPostTag(tagDTO.getId(), postId);
                });
            } catch (RuntimeException e) {
                log.error("register error [{}]", postDTO);
                throw new RuntimeException("register error : " + postDTO);
            }
        } else {
            log.error("register error [{}]", postDTO);
            throw new RuntimeException("register error : " + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyPosts(int accountId) {
        List<PostDTO> postDTOS = null;
        try {
            postMapper.selectMyPosts(accountId);
        } catch (RuntimeException e) {
            log.error("getMyPosts error [{}]", accountId);
            throw new RuntimeException("getMyPosts error : " + accountId);
        }
        return postDTOS;
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if (postDTO != null && postDTO.getId() != 0) {
            try {
                postMapper.updatePosts(postDTO);

            } catch (RuntimeException e) {
                log.error("update error [{}]", postDTO);
                throw new RuntimeException("update error " + postDTO);
            }
        } else {
            log.error("update error [{}]", postDTO);
            throw new RuntimeException("update error " + postDTO);
        }
    }

    @Override
    public void deletePosts(int userId, int postId) {
        if (userId != 0 && postId != 0) {
            try {
                postMapper.deletePosts(postId);
            } catch (RuntimeException e) {
                log.error("delete error [{}]", postId);
                throw new RuntimeException("delete Error " + postId);
            }
        } else {
            log.error("delete error [{}]", postId);
            throw new RuntimeException("delete Error " + postId);
        }
    }

    @Override
    public void registerComment(CommentDTO commentDTO) {
        if (commentDTO.getPostId() != 0) {
            commentMapper.register(commentDTO);
        } else {
            log.error("registerComment Error {}", commentDTO);
            throw new RuntimeException("registerComment Error " + commentDTO);
        }
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        if (commentDTO != null) {
            commentMapper.updateComments(commentDTO);
        } else {
            log.error("updateComment Error {}", commentDTO);
            throw new RuntimeException("updateComment Error " + commentDTO);
        }
    }

    @Override
    public void deleteComment(int userId, int commentId) {
        if (userId != 0 && commentId != 0) {
            commentMapper.deleteComment(commentId);
        } else {
            log.error("deleteComment Error {}", commentId);
            throw new RuntimeException("deleteComment Error : " + commentId);
        }
    }

    @Override
    public void registerTag(TagDTO tagDto) {
        if (tagDto != null) {
            tagMapper.register(tagDto);
        } else {
            log.error("registerTag Error {}", tagDto);
            throw new RuntimeException("registerTag Error : " + tagDto);
        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        if (tagDTO != null) {
            tagMapper.updateTags(tagDTO);
        } else {
            log.error("updateTag Error {}", tagDTO);
            throw new RuntimeException("updateTag Error : " + tagDTO);
        }
    }

    @Override
    public void deleteTag(int userId, int tagId) {
        if (userId != 0 && tagId != 0) {
            tagMapper.deletePostTag(tagId);
        } else {
            log.error("deleteTag Error {}", tagId);
            throw new RuntimeException("deleteTag Error : " + tagId);
        }
    }
}

package com.example.boardserver.service;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
import java.util.List;

public interface PostService {

    void register(String id, PostDTO postDTO);

    List<PostDTO> getMyPosts(int accountId);

    void updatePosts(PostDTO postDTO);

    void deletePosts(int userId, int postId);

    void registerComment(CommentDTO commentDTO);

    void updateComment(CommentDTO commentDTO);

    void deleteComment(int userId, int commentId);

    void registerTag(TagDTO tagDto);

    void updateTag(TagDTO tagDTO);

    void deleteTag(int userId, int tagId);

}

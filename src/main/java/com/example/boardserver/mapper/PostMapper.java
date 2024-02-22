package com.example.boardserver.mapper;

import com.example.boardserver.dto.PostDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    public int register(PostDTO postDTO);

    public List<PostDTO> selectMyPosts(int accountId);

    public void updatePosts(PostDTO postDTO);

    public void deletePosts(int postId);

}

package com.example.boardserver.service;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import java.util.List;

public interface PostSearchService {

    List<PostDTO> getPosts(PostSearchRequest postSearchRequest);

}

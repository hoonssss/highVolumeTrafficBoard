package com.example.boardserver.service.impl;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.mapper.PostSearchMapper;
import com.example.boardserver.service.PostSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;

    @Cacheable(value = "getPosts", key = "'getPosts' + #postSearchRequest.getName() + #postSearchRequest.getCategoryId()")
    @Override
    public List<PostDTO> getPosts(PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOS = null;
        try {
            postDTOS = postSearchMapper.selectPosts(postSearchRequest);
        }catch (RuntimeException e){
            log.error("select Posts Method Error {}", e.getMessage() );
        }
        return postDTOS;
    }
}

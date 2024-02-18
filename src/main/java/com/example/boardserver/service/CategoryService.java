package com.example.boardserver.service;

import com.example.boardserver.dto.CategoryDTO;

public interface CategoryService {

    void register(String accountId, CategoryDTO categoryDTO);
    void update(CategoryDTO categoryDTO);
    void delete(int categoryId);


}

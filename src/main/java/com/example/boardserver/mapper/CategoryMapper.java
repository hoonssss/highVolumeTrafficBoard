package com.example.boardserver.mapper;

import com.example.boardserver.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    public int register(CategoryDTO categoryDTO);

    public void updateCategory(CategoryDTO categoryDTO);

    public void deleteCategory(int categoryId);

    public CategoryDTO findById(int categoryId);

}

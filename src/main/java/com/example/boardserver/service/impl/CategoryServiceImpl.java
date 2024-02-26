package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.mapper.CategoryMapper;
import com.example.boardserver.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if (accountId != null) {
            try {
                categoryMapper.register(categoryDTO);
            } catch (RuntimeException e) {
                log.error("register Error {}", categoryDTO);
                throw new RuntimeException("register Error " + categoryDTO);

            }
        } else {
            log.error("register Error {}", categoryDTO);
            throw new RuntimeException("register Error " + categoryDTO);
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO != null) {
            try {
                categoryMapper.updateCategory(categoryDTO);
            } catch (RuntimeException e) {
                log.error("update Error {} ", categoryDTO);
                throw new RuntimeException("update Error " + categoryDTO);
            }
        } else {
            log.error("update Error {} ", categoryDTO);
            throw new RuntimeException("update Error " + categoryDTO);
        }
    }

    @Override
    public void delete(int categoryId) {
        if (categoryId != 0) {
            try {

                categoryMapper.deleteCategory(categoryId);
            } catch (RuntimeException e) {
                log.error("delete Error {} ", categoryId);
                throw new RuntimeException("delete Error " + categoryId);
            }
        } else {
            log.error("delete Error {} ", categoryId);
            throw new RuntimeException("delete Error " + categoryId);
        }
    }
}

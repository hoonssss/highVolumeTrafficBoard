package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.aop.LoginCheck.UserType;
import com.example.boardserver.dto.CategoryDTO;
import com.example.boardserver.dto.CategoryDTO.SortStatus;
import com.example.boardserver.service.CategoryService;
import com.example.boardserver.service.impl.CategoryServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@Log4j2
public class CategoryController {

    private CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = UserType.ADMIN) //ADMIN category 생성 가능
    public void registerCategory(@RequestParam String accountId, @RequestBody CategoryDTO categoryDTO){
        categoryService.register(accountId,categoryDTO);
    }

    @PatchMapping("/{categoryId}")
    @LoginCheck(type = UserType.ADMIN)
    public void updateCategories(
        @RequestParam String accountId,
        @PathVariable(name = "categoryId") int categoryId,
        @RequestBody CategoryRequest categoryRequest ){
        CategoryDTO categoryDTO = new CategoryDTO(categoryId,categoryRequest.getName(), SortStatus.NEWEST,10,1);
        categoryService.update(categoryDTO);
    }

    @DeleteMapping("/{categoryId}")
    @LoginCheck(type = UserType.ADMIN)
    public void deleteCategories(@RequestParam String accountId, @PathVariable(name = "categoryId") int categoryId){
        categoryService.delete(categoryId);
    }

    // ---request 객체---
    @Getter
    @Setter
    private static class CategoryRequest{
        private int id;
        private String name;
    }
}

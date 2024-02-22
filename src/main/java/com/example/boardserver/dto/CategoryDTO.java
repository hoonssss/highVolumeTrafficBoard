package com.example.boardserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    public enum SortStatus{
        CATEGORIES, NEWEST, OLDEST
    }

    private int id;
    private String name;
    private SortStatus sortStatus;
    private int searchCount;
    private int pagingStartOffset;

}

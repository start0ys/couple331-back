package com.couple.back.dto;

import java.util.List;

import com.couple.back.model.Todo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupedTodos {
    private String day;
    private List<Todo> todos;
}

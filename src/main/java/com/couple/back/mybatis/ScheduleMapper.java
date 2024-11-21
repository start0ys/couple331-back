package com.couple.back.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.couple.back.dto.GroupedTodos;
import com.couple.back.model.Calendar;
import com.couple.back.model.Todo;

@Mapper
public interface ScheduleMapper {
    public void insertCalendar(Calendar calendar);
    public void insertTodo(Todo todo);
    public void updateTodo(Todo todo);
    public void deleteTodo(String id);
    public List<GroupedTodos> selectGroupedTodosByCreateId(Long id);
    public List<Calendar> selectCalendarsByUserId(Long id);
}

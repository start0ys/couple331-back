package com.couple.back.service;

import java.util.List;
import java.util.Map;

import com.couple.back.model.Calendar;
import com.couple.back.model.Todo;

public interface ScheduleService {
    public void registerCalendar(Calendar calendar) throws Exception;
    public void registerTodo(Todo todo) throws Exception;
    public void updateTodo(Todo todo) throws Exception;
    public void deleteTodo(String id) throws Exception;
    public Map<String, List<Todo>> getTodo(Long userId) throws Exception;
    public List<Calendar> getCalender(Long userId) throws Exception;
}

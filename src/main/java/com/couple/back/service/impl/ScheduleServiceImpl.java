package com.couple.back.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.dto.CalendarRequest;
import com.couple.back.dto.GroupedTodos;
import com.couple.back.model.Calendar;
import com.couple.back.model.Todo;
import com.couple.back.mybatis.ScheduleMapper;
import com.couple.back.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService{
    
    @Autowired
    private ScheduleMapper scheduleMapper;

    public List<Calendar> getCalender(Long userId, String type) throws Exception {
        if(userId == null)
            throw new IllegalArgumentException("Parameter is Empty");

        return scheduleMapper.selectCalendars(new CalendarRequest(userId, type));
    }

    public void registerCalendar(Calendar calendar) throws Exception {
        if(calendar == null || StringUtils.isEmpty(calendar.getId()) || calendar.getUserId() == null || calendar.getCreateUserId() == null) 
            throw new IllegalArgumentException("Parameter is Empty");

        scheduleMapper.insertCalendar(calendar);
    }

    public void updateCalendar(String id, Calendar calendar) throws Exception {
        if(calendar == null || StringUtils.isAnyEmpty(id, calendar.getId()) || !StringUtils.equals(id, calendar.getId()))
            throw new IllegalArgumentException("Parameter is Empty");

        scheduleMapper.updateCalendar(calendar);
    }

    public void deleteCalender(String id) throws Exception {
        if(StringUtils.isEmpty(id))
            throw new IllegalArgumentException("Parameter is Empty");

        scheduleMapper.deleteCalender(id);
    }

    public Map<String, List<Todo>> getTodo(Long userId) throws Exception {
        if(userId == null)
            throw new IllegalArgumentException("Parameter is Empty");

        List<GroupedTodos> groupedTodos = scheduleMapper.selectGroupedTodosByCreateId(userId);
        if(groupedTodos == null || groupedTodos.isEmpty())
            return null;

        Map<String, List<Todo>> map = new HashMap<>();

        for(GroupedTodos group : groupedTodos) {
            String day = group.getDay();
            List<Todo> todos = group.getTodos();
            if(StringUtils.isNotEmpty(day) && todos != null && !todos.isEmpty()) {
                map.put(day, todos);
            }
        }

        return map;
    }

    public void registerTodo(Todo todo) throws Exception {
        if(todo == null || StringUtils.isEmpty(todo.getId()) || todo.getCreateUserId() == null) 
            throw new IllegalArgumentException("Parameter is Empty");
            
        scheduleMapper.insertTodo(todo);
    }

    public void updateTodo(Todo todo) throws Exception {
        if(todo == null || StringUtils.isEmpty(todo.getId()))
            throw new IllegalArgumentException("Parameter is Empty");

        scheduleMapper.updateTodo(todo);
    }

    public void deleteTodo(String id) throws Exception {
        if(StringUtils.isEmpty(id))
            throw new IllegalArgumentException("Parameter is Empty");

        scheduleMapper.deleteTodo(id);
    }
}

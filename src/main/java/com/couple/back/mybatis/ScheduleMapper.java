package com.couple.back.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.couple.back.dto.CalendarRequest;
import com.couple.back.dto.GroupedTodos;
import com.couple.back.dto.ScheduleDetailResponse;
import com.couple.back.model.Calendar;
import com.couple.back.model.Todo;

@Mapper
public interface ScheduleMapper {
    public List<Calendar> selectCalendars(CalendarRequest calendarRequest);
    public void insertCalendar(Calendar calendar);
    public void updateCalendar(Calendar calendar);
    public void deleteCalender(String id);
    public List<GroupedTodos> selectGroupedTodosByCreateId(Long id);
    public void insertTodo(Todo todo);
    public void updateTodo(Todo todo);
    public void deleteTodo(String id);
    public List<ScheduleDetailResponse> findDetailScheduleByDay(String day);
}

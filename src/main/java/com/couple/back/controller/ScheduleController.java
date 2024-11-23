package com.couple.back.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.model.Calendar;
import com.couple.back.model.Todo;
import com.couple.back.service.ScheduleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("calendar/{userId}")
    public ResponseEntity<ApiResponse<List<Calendar>>> getCalender(@PathVariable Long userId, @RequestParam(value = "type", required = false, defaultValue="all") String type) {
        try {
            List<Calendar> result = scheduleService.getCalender(userId, type);
            if(result == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : getTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : getTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("calendar/register")
    public ResponseEntity<ApiResponse<String>> registerCalendar(@RequestBody Calendar saveData) {
        try {
            scheduleService.registerCalendar(saveData);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : registerCalendar / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : registerCalendar / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("calendar/{id}")
    public ResponseEntity<ApiResponse<String>> updateCalendar(@PathVariable String id, @RequestBody Calendar saveData) {
        try {
            scheduleService.updateCalendar(id, saveData);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : updateCalendar / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : updateCalendar / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("calendar/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCalender(@PathVariable String id)  {
        try {
            scheduleService.deleteCalender(id);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : deleteCalender / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : deleteCalender / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("todo/{userId}")
    public ResponseEntity<ApiResponse<Map<String, List<Todo>>>> getTodo(@PathVariable Long userId) {
        try {
            Map<String, List<Todo>> result = scheduleService.getTodo(userId);
            if(result == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : getTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : getTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("todo/register")
    public ResponseEntity<ApiResponse<String>> registerTodo(@RequestBody Todo saveData) {
        try {
            scheduleService.registerTodo(saveData);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : registerTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : registerTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("todo")
    public ResponseEntity<ApiResponse<String>> updateTodo(@RequestBody Todo todo)  {
        try {
            scheduleService.updateTodo(todo);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : updateTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : updateTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @DeleteMapping("todo/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTodo(@PathVariable String id)  {
        try {
            scheduleService.deleteTodo(id);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : deleteTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : deleteTodo / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.couple.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.dto.JwtTokenResponse;
import com.couple.back.dto.LoginRequest;
import com.couple.back.dto.MailAuthRequest;
import com.couple.back.exception.DuplicateLoginException;
import com.couple.back.model.User;
import com.couple.back.service.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

	@PostMapping("/sendCode")
    public ResponseEntity<ApiResponse<String>> sendCode(@RequestBody MailAuthRequest mailAuthRequest) throws Exception {
		try{
			ApiResponse<String> result = authService.sendCode(mailAuthRequest);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);   
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/verifyCode")
    public ResponseEntity<ApiResponse<String>> verifyCode(@RequestBody MailAuthRequest mailAuthRequest) throws Exception {
		try{
			ApiResponse<String> result = authService.verifyCode(mailAuthRequest);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);   
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
	@PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> login(@RequestBody LoginRequest loginData) {
		try{
			ApiResponse<JwtTokenResponse> result = authService.loginUser(loginData);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			} 

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (DuplicateLoginException e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.CONFLICT);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @PostMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
		try{
			authService.logout(authorizationHeader);
			return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/refreshAccessToken")
	public ResponseEntity<ApiResponse<JwtTokenResponse>> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
		try{
			ApiResponse<JwtTokenResponse> result = authService.refreshAccessToken(authorizationHeader);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			} 

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/validateToken")
	public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
		try{
			ResultStatus result = authService.validateToken(authorizationHeader);
			if (result == ResultStatus.FAIL) {
				return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.TOKEN_EXPIRED_MESSAGE), HttpStatus.UNAUTHORIZED);
			} 

			return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.TOKEN_EXPIRED_MESSAGE),HttpStatus.UNAUTHORIZED);
		}
	}
}

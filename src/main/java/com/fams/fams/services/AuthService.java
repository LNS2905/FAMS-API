package com.fams.fams.services;

import com.fams.fams.models.payload.dto.LoginDto;
import com.fams.fams.models.payload.dto.UserDto;
import com.fams.fams.models.payload.responseModel.JWTAuthResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    JWTAuthResponse login(LoginDto loginDto);
    UserDto getUserInfo();
}

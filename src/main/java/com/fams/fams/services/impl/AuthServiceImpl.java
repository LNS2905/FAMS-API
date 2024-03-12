package com.fams.fams.services.impl;

import com.fams.fams.models.entities.User;
import com.fams.fams.models.exception.FamsApiException;
import com.fams.fams.models.payload.dto.LoginDto;
import com.fams.fams.models.payload.dto.UserDto;
import com.fams.fams.models.payload.responseModel.JWTAuthResponse;
import com.fams.fams.repositories.UserRepository;
import com.fams.fams.security.JwtTokenProvider;
import com.fams.fams.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public JWTAuthResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new JWTAuthResponse(jwtTokenProvider.generateToken(authentication));
        }catch (AuthenticationException e){
            throw new FamsApiException(HttpStatus.NOT_FOUND, "Username or password not found!");
        }

    }

    @Override
    public UserDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> user = Optional.ofNullable(userRepository.findByUserNameOrEmail(userName, userName)
                    .orElseThrow(() -> new FamsApiException(HttpStatus.NOT_FOUND, "Cannot find user information!")));
        return mapToDto(user);
    }


    private UserDto mapToDto(Optional<User> user){
        UserDto userDto = new UserDto();
        userDto.setFullName(user.get().getFullName());
        userDto.setEmail(user.get().getEmail());
        userDto.setDOB(user.get().getDOB());
        userDto.setAddress(user.get().getAddress());
        userDto.setGender(user.get().getGender());
        userDto.setPhone(user.get().getPhone());
        userDto.setRoleName(user.get().getRole().getRoleName());
        return userDto;
    }
}

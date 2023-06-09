package com.krekerok.blogapp.service;

import com.krekerok.blogapp.dto.request.AppUserRequestDto;

public interface RedisService {

    String registerUser(AppUserRequestDto userCreateDto);

    void verifyUser(String email, String activationCode);

}

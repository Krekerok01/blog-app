package com.krekerok.blogapp.entity;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisUser implements Serializable {

    private String username;

    private String password;

    private String email;

    private Instant createdAt;

    private Instant modifiedAt;

    private Instant timeOfSendingVerificationLink;

    private String activationCode;
}


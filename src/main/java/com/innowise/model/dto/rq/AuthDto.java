package com.innowise.model.dto.rq;

import jakarta.validation.constraints.NotNull;

public record AuthDto(

    @NotNull
    String username,

    @NotNull
    String password
)
{}

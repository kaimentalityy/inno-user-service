package com.innowise.util.dto.rq;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(

    @NotNull
    String username,

    @NotNull
    String password
)
{}

package com.directa24.challenge.dto.response;

public record ResponseDto<T>(T data, String error) { }

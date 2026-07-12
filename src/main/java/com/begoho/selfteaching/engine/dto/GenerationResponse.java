package com.begoho.selfteaching.engine.dto;

public record GenerationResponse(
        boolean success,
        String fileName,
        String generatedCode,
        String lesson
) {
}

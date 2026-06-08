package com.begoho.selfteaching.engine.controller;


import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import com.begoho.selfteaching.engine.dto.GenerationResponse;
import com.begoho.selfteaching.engine.service.CodeGenerationService;
import com.begoho.selfteaching.engine.validator.FileStructureValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/file-structure")
public class FileStructureController {

  //  private final CodeValidationService validationService;
    private final CodeGenerationService generationService;

    private final FileStructureValidator validator;

    public FileStructureController(FileStructureValidator validator, CodeGenerationService generationService) {
        this.validator = validator;
        this.generationService = generationService;
    }


    @PostMapping("/generate")
    public ResponseEntity<?> validateAndGenerate(@RequestBody FileStructureRequest request) {
        Optional<String> validationError = validator.validate(request);
        if (validationError.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", validationError.get()));
        }

        try {
            GenerationResponse response = generationService.generateJavaFile(request);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to generate Java file: " + e.getMessage()));
        }
    }

   /* @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateOnly(@RequestBody FileStructureRequest request) {
        return ResponseEntity.ok(validationService.validate(request));
    }*/
}

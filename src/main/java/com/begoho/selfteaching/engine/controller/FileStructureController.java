package com.begoho.selfteaching.engine.controller;


import com.begoho.selfteaching.engine.dto.FileStructureRequest;
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
    public ResponseEntity<?> validateAndGenerate(@RequestBody FileStructureRequest request) throws IOException {
        Optional<String> validationError = validator.validate(request);
        if (validationError.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", validationError.get()));
        }

        // proceed with generation logic
        try {
            generationService.generateJavaFiles(request);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to generate Java files: " + e.getMessage()));
        }

        return ResponseEntity.ok("response");
    }

   /* @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateOnly(@RequestBody FileStructureRequest request) {
        return ResponseEntity.ok(validationService.validate(request));
    }*/
}
package com.begoho.selfteaching.engine.controller;


import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import com.begoho.selfteaching.engine.validator.FileStructureValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/file-structure")
public class FileStructureController {

  //  private final CodeValidationService validationService;
  //  private final CodeGenerationService generationService;

    private final FileStructureValidator validator;

    public FileStructureController(FileStructureValidator validator) {
        this.validator = validator;
    }


    @PostMapping("/generate")
    public ResponseEntity<?> validateAndGenerate(@RequestBody FileStructureRequest request) {
        Optional<String> validationError = validator.validate(request);
        if (validationError.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", validationError.get()));
        }

        // proceed with generation logic
        return ResponseEntity.ok("response");
    }

   /* @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateOnly(@RequestBody FileStructureRequest request) {
        return ResponseEntity.ok(validationService.validate(request));
    }*/
}
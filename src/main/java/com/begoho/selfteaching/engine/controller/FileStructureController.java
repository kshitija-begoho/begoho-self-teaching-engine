package com.begoho.selfteaching.engine.controller;


import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/file-structure")
public class FileStructureController {

  //  private final CodeValidationService validationService;
  //  private final CodeGenerationService generationService;



    @PostMapping("/generate")
    public ResponseEntity<?> validateAndGenerate(@RequestBody FileStructureRequest request) {
        //ValidationResult validation = validationService.validate(request);
       /* if (!validation.acceptable()) {
            return ResponseEntity.badRequest().body(validation);
        }*/

        /*String generatedSource = generationService.generate(request);
        GenerateResponse response = new GenerateResponse(true, validation.messages(), generatedSource);*/
        return ResponseEntity.ok("response");
    }

   /* @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateOnly(@RequestBody FileStructureRequest request) {
        return ResponseEntity.ok(validationService.validate(request));
    }*/
}
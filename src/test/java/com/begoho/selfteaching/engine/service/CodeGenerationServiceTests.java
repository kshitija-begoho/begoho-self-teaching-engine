package com.begoho.selfteaching.engine.service;

import com.begoho.selfteaching.engine.dto.ClassStructure;
import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import com.begoho.selfteaching.engine.dto.GenerationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CodeGenerationServiceTests {

    @TempDir
    Path outputDirectory;

    @Test
    void generatesAllClassesOnceAndReturnsTheirSource() throws Exception {
        CodeGenerationService service = new CodeGenerationService(outputDirectory);
        FileStructureRequest request = new FileStructureRequest(
                "First.java",
                List.of(
                        new ClassStructure("public", "First", true),
                        new ClassStructure("default", "Helper", null)
                )
        );

        GenerationResponse response = service.generateJavaFile(request);

        assertThat(response.success()).isTrue();
        assertThat(response.fileName()).isEqualTo("First.java");
        assertThat(response.generatedCode())
                .contains("public class First")
                .contains("class Helper")
                .containsOnlyOnce("public class First");
        assertThat(Files.readString(outputDirectory.resolve("First.java")))
                .isEqualTo(response.generatedCode());
    }
}

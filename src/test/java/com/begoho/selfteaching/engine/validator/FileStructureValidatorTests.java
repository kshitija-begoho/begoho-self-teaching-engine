package com.begoho.selfteaching.engine.validator;

import com.begoho.selfteaching.engine.dto.ClassStructure;
import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileStructureValidatorTests {

    private final FileStructureValidator validator = new FileStructureValidator();

    @Test
    void acceptsValidJavaFileStructure() {
        FileStructureRequest request = new FileStructureRequest(
                "First.java",
                List.of(
                        new ClassStructure("public", "First", true),
                        new ClassStructure("default", "Helper", false)
                )
        );

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void rejectsPathTraversalInFileName() {
        FileStructureRequest request = new FileStructureRequest(
                "../First.java",
                List.of(new ClassStructure("public", "First", true))
        );

        assertThat(validator.validate(request))
                .hasValueSatisfying(error -> assertThat(error).contains("simple Java file name"));
    }

    @Test
    void rejectsJavaKeywordAsClassName() {
        FileStructureRequest request = new FileStructureRequest(
                "class.java",
                List.of(new ClassStructure("default", "class", false))
        );

        assertThat(validator.validate(request)).isPresent();
    }

    @Test
    void rejectsEmptyClassList() {
        FileStructureRequest request = new FileStructureRequest("First.java", List.of());

        assertThat(validator.validate(request))
                .hasValue("At least one class structure is required");
    }
}

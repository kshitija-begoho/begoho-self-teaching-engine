package com.begoho.selfteaching.engine.validator;

import com.begoho.selfteaching.engine.dto.ClassStructure;
import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class FileStructureValidator {

    // For top-level classes we only allow 'public' or package-private (represented as empty/default/package-private).
    private static final Set<String> ALLOWED_MODIFIERS = Set.of(
            "public",
            "",
            "default",
            "package-private"
    );

    /**
     * Validates the incoming FileStructureRequest.
     * Returns Optional.empty() when validation passes, otherwise an Optional containing an error message.
     */
    public Optional<String> validate(FileStructureRequest request) {
        if (request == null) {
            return Optional.of("Request body is required");
        }

        if (request.getStructures() == null) {
            return Optional.of("`structures` list is required");
        }

        // Validate access modifiers for each structure
        List<String> invalidEntries = new ArrayList<>();
        for (int i = 0; i < request.getStructures().size(); i++) {
            var s = request.getStructures().get(i);
            if (s == null) {
                invalidEntries.add("structures[" + i + "] is null");
                continue;
            }

            String raw = s.getAccessModifier();
            String norm = raw == null ? "" : raw.trim().toLowerCase();
            if (!ALLOWED_MODIFIERS.contains(norm)) {
                invalidEntries.add("structures[" + i + "].accessModifier='" + raw + "'");
            }

            // ensure className present
            String className = s.getClassName();
            if (className == null || className.isBlank()) {
                invalidEntries.add("structures[" + i + "].className is required and must be non-empty");
            }
        }

        if (!invalidEntries.isEmpty()) {
            return Optional.of("Invalid structure entries: " + String.join("; ", invalidEntries)
                    + ". Allowed accessModifier values for top-level classes: 'public' or package-private (omit modifier / 'default' / 'package-private')");
        }

        long publicCount = request.getStructures().stream()
                .filter(s -> s != null && "public".equalsIgnoreCase(s.getAccessModifier()))
                .count();

        if (publicCount > 1) {
            return Optional.of("Only one public class is allowed per file. Found: " + publicCount);
        }

        // If there is exactly one public class, ensure filename matches <ClassName>.java
        if (publicCount == 1) {
            String fileName = request.getFileName();
            if (fileName == null || fileName.isBlank()) {
                return Optional.of("`fileName` is required when a public class is present and must match the class name (e.g. MyClass.java)");
            }

            Optional<String> publicClassName = request.getStructures().stream()
                    .filter(s -> s != null && "public".equalsIgnoreCase(s.getAccessModifier()))
                    .map(ClassStructure::getClassName)
                    .filter(n -> n != null && !n.isBlank())
                    .findFirst();

            if (publicClassName.isEmpty()) {
                return Optional.of("Public class must have a non-empty `className`");
            }

            String expected = publicClassName.get() + ".java";
            String expectedWithoutJava = publicClassName.get();
            if (!fileName.equals(expected) && !fileName.equals(expectedWithoutJava)) {
                return Optional.of("When a public class is present, `fileName` must match the public class name. Expected: '" + expected + "' but was '" + fileName + "'");
            }
        }

        return Optional.empty();
    }
}

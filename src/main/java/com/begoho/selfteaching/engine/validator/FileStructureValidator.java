package com.begoho.selfteaching.engine.validator;

import com.begoho.selfteaching.engine.dto.ClassStructure;
import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class FileStructureValidator {

    // For top-level classes we only allow 'public' or package-private (represented as empty/default/package-private).
    private static final Set<String> ALLOWED_MODIFIERS = Set.of(
            "public",
            "",
            "default",
            "package-private"
    );
    private static final Pattern JAVA_IDENTIFIER = Pattern.compile("[A-Za-z_$][A-Za-z\\d_$]*");
    private static final Set<String> JAVA_KEYWORDS = Set.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum",
            "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient",
            "try", "void", "volatile", "while", "true", "false", "null", "_",
            "module", "open", "requires", "transitive", "exports", "opens", "to",
            "uses", "provides", "with", "record", "sealed", "permits"
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
        if (request.getStructures().isEmpty()) {
            return Optional.of("At least one class structure is required");
        }

        String fileName = request.getFileName();
        if (fileName == null || fileName.isBlank()) {
            return Optional.of("`fileName` is required");
        }
        if (!isSafeJavaFileName(fileName)) {
            return Optional.of("`fileName` must be a simple Java file name without folders or special characters");
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
            } else if (!JAVA_IDENTIFIER.matcher(className).matches() || JAVA_KEYWORDS.contains(className)) {
                invalidEntries.add("structures[" + i + "].className='" + className + "' is not a valid Java identifier");
            }
        }

        if (!invalidEntries.isEmpty()) {
            return Optional.of("Invalid structure entries: " + String.join("; ", invalidEntries)
                    + ". Allowed accessModifier values for top-level classes: 'public' or package-private (omit modifier / 'default' / 'package-private')");
        }

        long publicCount = request.getStructures().stream()
                .filter(s -> s != null && "public".equalsIgnoreCase(
                        s.getAccessModifier() == null ? "" : s.getAccessModifier().trim()))
                .count();

        if (publicCount > 1) {
            return Optional.of("Only one public class is allowed per file. Found: " + publicCount);
        }

        // If there is exactly one public class, ensure filename matches <ClassName>.java
        if (publicCount == 1) {
            Optional<String> publicClassName = request.getStructures().stream()
                    .filter(s -> s != null && "public".equalsIgnoreCase(
                            s.getAccessModifier() == null ? "" : s.getAccessModifier().trim()))
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

    private boolean isSafeJavaFileName(String fileName) {
        String baseName = fileName.endsWith(".java")
                ? fileName.substring(0, fileName.length() - ".java".length())
                : fileName;
        return JAVA_IDENTIFIER.matcher(baseName).matches()
                && !JAVA_KEYWORDS.contains(baseName);
    }
}

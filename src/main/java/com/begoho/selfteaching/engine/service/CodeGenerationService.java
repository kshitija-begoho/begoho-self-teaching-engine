package com.begoho.selfteaching.engine.service;

import com.begoho.selfteaching.engine.dto.ClassStructure;
import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import com.begoho.selfteaching.engine.dto.GenerationResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CodeGenerationService {

    private static final String LESSON =
            "A Java source file can contain multiple top-level classes, but only one can be public. "
                    + "When a public class exists, the file name must match that class.";
    private final Path outputDirectory;

    public CodeGenerationService() {
        this(Path.of("generated-files"));
    }

    CodeGenerationService(Path outputDirectory) {
        this.outputDirectory = outputDirectory.toAbsolutePath().normalize();
    }

    public GenerationResponse generateJavaFile(FileStructureRequest request) throws IOException {
        Files.createDirectories(outputDirectory);

        String fileName = resolveFileName(request);
        StringBuilder builder = new StringBuilder();
        for (ClassStructure structure : request.getStructures()) {
            builder.append(generateClassContent(structure));
        }

        Path outputFile = outputDirectory.resolve(fileName).normalize();
        if (!outputFile.getParent().equals(outputDirectory)) {
            throw new IOException("Invalid output file name");
        }

        String generatedCode = builder.toString();
        Files.writeString(outputFile, generatedCode, StandardCharsets.UTF_8);
        return new GenerationResponse(true, fileName, generatedCode, LESSON);
    }

    private String generateClassContent(ClassStructure structure) {
        StringBuilder classContent = new StringBuilder();

        String modifier = structure.getAccessModifier() == null
                ? ""
                : structure.getAccessModifier().trim();
        if (modifier.equalsIgnoreCase("public")) {
            classContent.append("public ");
        }
        classContent.append("class ").append(structure.getClassName()).append(" {").append(System.lineSeparator());

        if (Boolean.TRUE.equals(structure.isMain())) {
            classContent.append("    public static void main(String[] args) {").append(System.lineSeparator());
            classContent.append("        System.out.println(\"Hello from ").append(structure.getClassName()).append("!\");").append(System.lineSeparator());
            classContent.append("    }").append(System.lineSeparator());
        }

        classContent.append("}").append(System.lineSeparator());
        return classContent.toString();
    }

    private String resolveFileName(FileStructureRequest request) {
        return request.getStructures().stream()
                .filter(structure -> "public".equalsIgnoreCase(
                        structure.getAccessModifier() == null ? "" : structure.getAccessModifier().trim()))
                .findFirst()
                .map(structure -> structure.getClassName() + ".java")
                .orElseGet(() -> request.getFileName().endsWith(".java")
                        ? request.getFileName()
                        : request.getFileName() + ".java");
    }
}

package com.begoho.selfteaching.engine.service;

import com.begoho.selfteaching.engine.dto.ClassStructure;
import com.begoho.selfteaching.engine.dto.FileStructureRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CodeGenerationService {

    public void generateJavaFiles(FileStructureRequest request) throws IOException {
        String directoryPath = "generated-files/";
        File directory = new File(directoryPath);
        if (!directory.mkdirs() && !directory.exists()) {
            throw new IOException("Failed to create directory: " + directoryPath);
        }

        List<ClassStructure> structures = request.getStructures();
        String fileName = request.getFileName();
        if(!request.getFileName().endsWith(".java")) {
            fileName += ".java";
        }
        StringBuilder builder = new StringBuilder();
        for (ClassStructure structure : structures) {
            if(structure.getAccessModifier().equalsIgnoreCase("public")) {
                fileName = structure.getClassName() + ".java";
            }

             builder.append
                    (generateClassContent(structure));

            generateJavaFile(directoryPath, builder,fileName);
        }
    }

    private void generateJavaFile(String directoryPath, StringBuilder builder, String fileName) {
        File javaFile = new File(directoryPath, fileName);
        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateJavaFile(String directoryPath, ClassStructure structure) throws IOException {

        String fileName = structure.getClassName() + ".java";
        File javaFile = new File(directoryPath, fileName);
        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(generateClassContent(structure));
        }
    }

    private String generateClassContent(ClassStructure structure) {
        StringBuilder classContent = new StringBuilder();

        // Add package declaration if needed
        // classContent.append("package ...;").append(System.lineSeparator());

        // Add class declaration
        if (!structure.getAccessModifier().isEmpty() && !structure.getAccessModifier().equalsIgnoreCase("default")) {
            classContent.append(structure.getAccessModifier()).append(" ");
        }
        classContent.append("class ").append(structure.getClassName()).append(" {").append(System.lineSeparator());

        // Add main method if isMain is true
        if (structure.isMain()) {
            classContent.append("    public static void main(String[] args) {").append(System.lineSeparator());
            classContent.append("        System.out.println(\"Hello from ").append(structure.getClassName()).append("!\");").append(System.lineSeparator());
            classContent.append("    }").append(System.lineSeparator());
        }

        classContent.append("}").append(System.lineSeparator());
        return classContent.toString();
    }
}

package com.begoho.selfteaching.engine.dto;

import java.util.List;

public class FileStructureRequest {

    private String fileName;
    private List<ClassStructure> structures;

    public FileStructureRequest() {
    }

    public FileStructureRequest(String fileName, List<ClassStructure> structures) {
        this.fileName = fileName;
        this.structures = structures;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ClassStructure> getStructures() {
        return structures;
    }

    public void setStructures(List<ClassStructure> structures) {
        this.structures = structures;
    }
}





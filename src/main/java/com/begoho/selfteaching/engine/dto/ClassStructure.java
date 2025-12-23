package com.begoho.selfteaching.engine.dto;

public class ClassStructure {

    private String accessModifier;
    private String className;
    private Boolean isMain; // Use Boolean wrapper to handle null and string mapping

    public ClassStructure() {
    }

    public ClassStructure(String accessModifier, String className) {
        this.accessModifier = accessModifier;
        this.className = className;
    }

    // New constructor including isMain
    public ClassStructure(String accessModifier, String className, Boolean isMain) {
        this.accessModifier = accessModifier;
        this.className = className;
        this.isMain = isMain;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean isMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }
}
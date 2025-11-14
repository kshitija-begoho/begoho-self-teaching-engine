package com.begoho.selfteaching.engine.dto;

public class ClassStructure {

    private String accessModifier;
    private String className;

    public ClassStructure() {
    }

    public ClassStructure(String accessModifier, String className) {
        this.accessModifier = accessModifier;
        this.className = className;
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
}
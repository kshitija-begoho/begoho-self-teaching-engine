package com.begoho.selfteaching.engine.dto;

public class ClassStructure {

    private String accessModifier;
    private String className;
    private boolean isMain; // new field to indicate whether the class contains a main method

    public ClassStructure() {
    }

    public ClassStructure(String accessModifier, String className) {
        this.accessModifier = accessModifier;
        this.className = className;
    }

    // New constructor including isMain
    public ClassStructure(String accessModifier, String className, boolean isMain) {
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

    // Getter follows JavaBean convention for boolean: isX()
    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }
}
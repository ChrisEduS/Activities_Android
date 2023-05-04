package com.example.project5;

public class Type {
    private String typeName;
    private double maxVal;

    public Type (String typeName, double maxVal){
        this.typeName = typeName;
        this.maxVal = maxVal;
    }
    public String getTypeName(){
        return this.typeName;
    }
    public double getMaxVal(){
        return this.maxVal;
    }
    public void setTypeName(String typeName){
        this.typeName = typeName;
    }
    public void setMaxVal(double maxVal){
        this.maxVal = maxVal;
    }
}

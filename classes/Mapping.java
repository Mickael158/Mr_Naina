package etu1904.framework;

public class Mapping{
    private String className;
    private String method;

    public Mapping(String className , String method){
        this.className=className;
        this.method=method;
    }

    public String getClassName(){
        return className;
    }

    public String getMethode(){
        return method;
    }
}
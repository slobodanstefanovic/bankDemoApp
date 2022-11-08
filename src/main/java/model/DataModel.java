package model;

public interface DataModel {

    public DataModel readObjectFromCSVLine(String line);
    
    public String[] readLine(String line);
    
}

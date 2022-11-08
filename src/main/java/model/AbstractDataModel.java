package model;

import config.LoggerConfig;
import java.util.logging.Logger;

public abstract class AbstractDataModel implements DataModel {
    
    private static final Logger LOGGER = LoggerConfig.getLoggerInstance(); 
    private static final String CLASS_NAME = "AbstractDataModel";

    @Override
    public String[] readLine(String line){
        final String SPLIT_BY = ";";
        final String methodName = "readLine()";
        LOGGER.entering(CLASS_NAME, methodName);        
        String[] csvData = line.split(SPLIT_BY);
        LOGGER.exiting(CLASS_NAME, methodName); 
        return csvData;
    }

    @Override
    public abstract DataModel readObjectFromCSVLine(String line);
    
}

package config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerConfig {

    private static Logger instance;
    private static FileHandler handler;

    private LoggerConfig() {

    }

    public static Logger getLoggerInstance() {
        try {
            if (instance == null) {
                instance = Logger.getLogger("LoggerConfig");
                if (handler == null) {
                    String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ConfigReader.readProperty("dateTimePattern")));
                    handler = new FileHandler("/home/dev/Desktop/logs_" + dateTime + ".txt", Long.MAX_VALUE, 1, true);
                }
                handler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord lr) {
                        return "[" + LocalDateTime.now()+ "]  " + lr.getSourceClassName() 
                                + " " + lr.getSourceMethodName() + " " + lr.getMessage() + "\r\n";
                    }
                });
                instance.setLevel(Level.ALL);
                instance.addHandler(handler);
            }
            return instance;
        } catch (IOException ex) {
            Logger.getLogger(LoggerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

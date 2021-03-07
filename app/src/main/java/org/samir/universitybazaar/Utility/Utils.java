package org.samir.universitybazaar.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Samir Shrestha
 * @description A utility class that contains various utility methods to use throughout the application.
 */
public class Utils {

    /**
     * @author Samir Shrestha
     * @return returns the current system date in mm/dd/yyyy format as a string.
     */
    public static String getCurrentDate(){
        //Getting the current system date and formating it to mm/dd/yyyy format.
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }
}

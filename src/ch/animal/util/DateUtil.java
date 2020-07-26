package ch.animal.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Helper functions for handling dates.
 * @author Damian Krebs  
 * @author Stephanie Gloor
 */

public class DateUtil {
	
    /** The date pattern that is used for conversion. Change to your format as you wish. */
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    
    /** The date formatter. */
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern(DATE_PATTERN);
    
    /**
     * Returns the given date as a well formatted string. The above defined 
     * {@link DateUtil#DATE_PATTERN} is used.
     * 
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }
}

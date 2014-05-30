package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public final class ErrorCheckUtils {
	public static final boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	public static final boolean isInteger(String str)
	{
		try {
			int i = Integer.parseInt(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
	
	public static final boolean isDouble(String str)
	{
		try {
			double i = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
	
	// List of all date formats that we want to parse.
    // Add your own format here.
    private static List<SimpleDateFormat>
            dateFormats = new ArrayList<SimpleDateFormat>() {{
            add(new SimpleDateFormat("yyyy-MM-dd"));
            add(new SimpleDateFormat("dd-MM-yyyy"));
        }
    };
 
    /**
     * Convert String with various formats into java.util.Date
     * 
     * @param input
     *            Date as a string
     * @return java.util.Date object if input string is parsed 
     *          successfully else returns null
     */
    public static Date convertToDate(String input) {
        Date date = null;
        if(null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(input);
            } catch (ParseException e) {
                //Shhh.. try other formats
            }
            if (date != null) {
                break;
            }
        }
 
        return date;
    }
}

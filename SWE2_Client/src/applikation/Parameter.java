package applikation;

import java.util.Date;

import javafx.scene.paint.Paint;

public class Parameter {
	
	/* types of preferences */
	private String string = null;
	private Paint paint = null;
	private double doubleValue = 0;
	private Date date = null;
	
	public Parameter(String preference) {
		string = preference;
	}
	
	public Parameter(Date preference) {
		date = preference;
	}
	
	public Parameter(double preference) {
		doubleValue = preference;
	}
	
	public String getStringParameter() {
		return string;
	}
	
	public void setStringParameter(String string) {
		this.string = string;
	}
	
	public Paint getPaintParameter() {
		return paint;
	}
	
	public void setPaintParameter(Paint paint) {
		this.paint = paint;
	}
	
	public double getDoubleParameter() {
		return doubleValue;
	}
	
	public void setDoubleParameter(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	
	public Date getDateParameter() {
		return date;
	}
	
}

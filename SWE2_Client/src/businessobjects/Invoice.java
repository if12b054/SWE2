package businessobjects;

import java.util.Date;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Invoice extends AbstractObject{
	private ObservableList<InvoiceLine> invoiceLines;
	private FloatProperty amount = new SimpleFloatProperty();
	private ObjectProperty<Date> creationDate = new SimpleObjectProperty<Date>();
	private ObjectProperty<Date> dueDate = new SimpleObjectProperty<Date>();
	private StringProperty contact = new SimpleStringProperty();
	private String message, comment;
	private Adress invAdress, delAdress;
	
	public Invoice(
			ObservableList<InvoiceLine> invoiceLines, 
			Date date, 
			Date dueDate, 
			String contact, 
			String message, 
			String comment,
			Adress invAdress, 
			Adress delAdress) {
		this.invoiceLines = invoiceLines;	
		this.creationDate.set(date);
		this.dueDate.set(dueDate);
		this.contact.set(contact);
		this.message = message;
		this.comment = comment;
		this.invAdress = invAdress;
		this.delAdress = delAdress;
		
		for (InvoiceLine r : invoiceLines) {
			amount.set(amount.get()+r.getBrutto());
		}
	}
	
	public final FloatProperty betragProperty() {
		return amount;
	}
	
	public final ObjectProperty<Date> datumProperty() {
		return creationDate;
	}
	
	public final ObjectProperty<Date> faelligkeitProperty() {
		return dueDate;
	}

	public final StringProperty kundeProperty() {
		return contact;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getComment() {
		return comment;
	}
	
	public final void setDatum(Date datum) {
		this.creationDate.set(datum);
	}
	
	public final void setFaelligkeit(Date faelligkeit) {
		this.creationDate.set(faelligkeit);
	}
	
	public final void setKunde(String kunde) {
		this.contact.set(kunde);
	}
	
	public void setNachricht(String message) {
		this.message = message;
	}
	
	public final void setKommentar(String comment) {
		this.comment = comment;
	}
}

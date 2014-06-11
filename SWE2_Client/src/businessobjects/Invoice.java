package businessobjects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Invoice extends AbstractObject{
	/* general data */
	private int id = -1;
	private Contact contact;
	
	/* date stuff */
	private Date todayDate, dueDate;


	private StringProperty todayDateStr = new SimpleStringProperty();
	private StringProperty dueDateStr = new SimpleStringProperty();


	/* invoice data */
	private ObservableList<InvoiceLine> invoiceLines;
	private DoubleProperty amount = new SimpleDoubleProperty();
	private StringProperty contactString = new SimpleStringProperty();
	private String message, comment;
	private Adress invAdress, delAdress;
	private double MWSt;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	public Invoice(
			ObservableList<InvoiceLine> invoiceLines,
			double MWSt,
			Date date, 
			Date dueDate, 
			Contact contact, 
			String message, 
			String comment,
			Adress invAdress, 
			Adress delAdress) {
		this.MWSt = MWSt;
		this.contact = contact;
		this.todayDate = date;
		this.dueDate = dueDate;
		
		if(date ==null){
			this.todayDateStr.set("n.A.");
		}
		else{
			this.todayDateStr.set(format.format(date));
		}
		
		if(dueDate==null){
			this.dueDateStr.set("n.A.");
		}
		else{
			this.dueDateStr.set(format.format(dueDate));
		}
		
		this.setInvoiceLines(invoiceLines);	
		if(contact.getType().equals("Person")) {
			this.contactString.set(contact.getVorname() + ", " + contact.getNachname());
		} else {
			this.contactString.set(contact.getFirma());
		}
		
		this.message = message;
		this.comment = comment;
		this.setInvAdress(invAdress);
		this.setDelAdress(delAdress);
		
		for (InvoiceLine r : invoiceLines) {
			amount.set(amount.get()+r.getBrutto());
		}
		
	}
	
	public final DoubleProperty amountProperty() {
		return amount;
	}
	
	public final StringProperty datumProperty() {
		return todayDateStr;
	}
	
	public final StringProperty dueDateProperty() {
		return dueDateStr;
	}

	public final StringProperty kundeProperty() {
		return contactString;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getComment() {
		return comment;
	}
	
	public final void setTodayDate(Date date) {
		this.todayDate = date;
		this.todayDateStr.set(format.format(date));
	}
	
	public final void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
		this.dueDateStr.set(format.format(dueDate));
	}
	
	public final void setKunde(String kunde) {
		this.contactString.set(kunde);
	}
	
	public void setNachricht(String message) {
		this.message = message;
	}
	
	public final void setKommentar(String comment) {
		this.comment = comment;
	}

	public Adress getInvAdress() {
		return invAdress;
	}

	public void setInvAdress(Adress invAdress) {
		this.invAdress = invAdress;
	}

	public Adress getDelAdress() {
		return delAdress;
	}

	public void setDelAdress(Adress delAdress) {
		this.delAdress = delAdress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ObservableList<InvoiceLine> getInvoiceLines() {
		return (ObservableList<InvoiceLine>) invoiceLines;
	}

	public void setInvoiceLines(ObservableList<InvoiceLine> invoiceLines) {
		this.invoiceLines = invoiceLines;
	}

	public double getMWSt() {
		return MWSt;
	}

	public void setMWSt(double mWSt) {
		MWSt = mWSt;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public String getContactString() {
		return contactString.get();
	}
	
	public Date getDueDate() {
		return dueDate;
	}

	public Date getTodayDate() {
		return todayDate;
	}
	
	public InvoiceData generateInvoiceData(){
		InvoiceData iD = new InvoiceData();
		ArrayList<InvoiceLineData> list = new ArrayList<>();
		
		iD.setId(this.id);
		iD.setContact(this.contact.generateContactData());
		iD.setTodayDate(this.todayDateStr.get());
		iD.setDueDate(this.dueDateStr.get());
		
		for(InvoiceLine il: invoiceLines){
			if(il != null){
				list.add(il.generateInvoiceLineData());
			}
		}
		
		iD.setInvoiceLines(list);
		iD.setAmount(this.amount.get());
		iD.setContactString(this.contactString.get());
		iD.setComment(this.comment);
		iD.setMessage(this.message);
		iD.setDelAdress(this.delAdress);
		iD.setInvAdress(this.invAdress);
		iD.setMWSt(this.MWSt);
		
		return iD;
	}
	
	
}

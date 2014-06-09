package businessobjects;

import java.util.ArrayList;
import java.util.List;

public class InvoiceData extends AbstractObject{
	
	private int id = -1;

	private ContactData contact;
	
	private String todayDate;
	private String dueDate;
	
	private List<InvoiceLineData> invoiceLines;
	private double amount;
	private String contactString;
	private String message, comment;
	private Adress invAdress, delAdress;
	private double MWSt;
	
	public InvoiceData(){
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ContactData getContact() {
		return contact;
	}
	public void setContact(ContactData contact) {
		this.contact = contact;
	}
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<InvoiceLineData> getInvoiceLines() {
		return invoiceLines;
	}
	public void setInvoiceLines(ArrayList<InvoiceLineData> list) {
		this.invoiceLines = list;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getContactString() {
		return contactString;
	}
	public void setContactString(String contactString) {
		this.contactString = contactString;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
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
	public double getMWSt() {
		return MWSt;
	}
	public void setMWSt(double mWSt) {
		MWSt = mWSt;
	}

}

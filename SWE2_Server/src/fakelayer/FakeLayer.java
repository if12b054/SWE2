package fakelayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Parameter;
import businessobjects.Adress;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;

public class FakeLayer {
	
	private ArrayList<Invoice> invoices= new ArrayList<Invoice>();
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private ArrayList<Article> articles = new ArrayList<Article>();
	private ArrayList<Adress> adresses = new ArrayList<Adress>();
	private Contact[] k;
	
	
	public FakeLayer(){
		k = new Contact[6];
		
		k[0] = new Contact("123","ReichAG");
		k[1] = new Contact(k[0], "Peter", "Reich", "Herr", "1966-04-28");
		k[2] = new Contact(k[0], "Markus", "Ebner", "Herr", "1975-12-13");
		k[3] = new Contact(k[0], "Sandra", "Reich", "Frau", "1968-03-01");
		k[4] = new Contact("147","ArmKG");	
		k[5] = new Contact(k[4], "Markus", "Reich", "Herr", "1980-05-05");
		
		k[0].setAdresse("Gutgasse 123", "1220", "Wien", "AUT");
		k[1].setAdresse("Malerstraße 13", "1110", "Wien", "AUT");
		k[2].setAdresse("Gutmannstraße 5", "1020", "Wien", "AUT");
		k[3].setAdresse("Blaustraße 8", "1110", "Wien", "AUT");
		k[4].setAdresse("Vielgasse 232", "1210", "Wien", "AUT");
		k[5].setAdresse("Altermanngasse 123", "1210", "Wien", "AUT");
		
		for(int i=0; i<k.length; i++){
			contacts.add(k[i]);
		}
		
		Article[] a = new Article[4];
		
		a[0] = new Article(0, "Bier", 4.50);
		a[1] = new Article(1, "Wein", 8.00);
		a[2] = new Article(2, "Sekt", 12.00);
		a[3] = new Article(3, "Milch", 2.50);
		
		for(int i=0; i<a.length; i++){
			articles.add(a[i]);
		}
		
		InvoiceLine[] il = new InvoiceLine[8];
		
		il[0] = new InvoiceLine(a[0], 3, 0.15);
		il[1] = new InvoiceLine(a[1], 1, 0.15);
		il[2] = new InvoiceLine(a[2], 1, 0.15);
		il[3] = new InvoiceLine(a[0], 3, 0.25);
		il[4] = new InvoiceLine(a[0], 10, 0.15);
		il[5] = new InvoiceLine(a[1], 3, 0.15);
		il[6] = new InvoiceLine(a[0], 2, 0.15);
		il[7] = new InvoiceLine(a[3], 5, 0.15);
		
		ObservableList<InvoiceLine> ob1 =  FXCollections.observableArrayList();
		ObservableList<InvoiceLine> ob2 =  FXCollections.observableArrayList();
		ObservableList<InvoiceLine> ob3 =  FXCollections.observableArrayList();
		ObservableList<InvoiceLine> ob4 =  FXCollections.observableArrayList();
		
		ob1.add(il[0]);
		ob1.add(il[1]);
		ob1.add(il[2]);
		
		ob2.add(il[3]);
		
		ob3.add(il[4]);
		ob3.add(il[5]);
		
		ob4.add(il[6]);
		ob4.add(il[7]);		
		
		Adress[] ad = new Adress[5];
		
		ad[0] = new Adress("Reichgasse 12", 1220, "Wien", "AUT");
		ad[1] = new Adress("Schöngasse 32", 1220, "Wien", "AUT");
		ad[2] = new Adress("Schlaugasse 89", 1100, "Wien", "AUT");
		ad[3] = new Adress("Schöngasse 123", 1120, "Wien", "AUT");
		ad[4] = new Adress("Kleingasse 312", 1210, "Wien", "AUT");
		
		for(int i=0; i<ad.length; i++){
			adresses.add(ad[i]);
		}
		
		Invoice[] in = new Invoice[4];
		
		in[0] = new Invoice(ob1 , 0.15, new Date(2014,06,11), new Date(2014,06,12), k[1], "Hallo", "Tolles Bier!", ad[3], ad[4]);
		in[1] = new Invoice(ob2 , 0.15, new Date(2014,06,11), new Date(2014,06,15), k[2], "Serwus", "Tolles Bier!", ad[2], ad[1]);
		in[2] = new Invoice(ob1 , 0.15, new Date(2014,06,11), new Date(2014,06,12), k[5], "Hallo", "Schöner Tag heute!", ad[2], ad[2]);
		in[3] = new Invoice(ob1 , 0.15, new Date(2014,06,11), new Date(2014,06,12), k[1], "Hello", "Ich bins nochmal!", ad[3], ad[3]);
		
		for(int i=0; i<in.length; i++){
			invoices.add(in[i]);
		}
	}

	public ObservableList<Contact> searchContact(Vector<Parameter> parms) {
		ObservableList<Contact> cx = FXCollections.observableArrayList();
		
		if(!(parms.get(0).getStringParameter() == null)){
			for(Contact c: contacts){
				if(c.getVorname() != null){
					if(parms.get(0).getStringParameter().equals(c.getVorname())){
						cx.add(c);
					}
				}
			}
		}
		
		if(!(parms.get(1).getStringParameter() == null)){
			for(Contact c: contacts){
				if(c.getNachname() != null){
					if(parms.get(1).getStringParameter().equals(c.getNachname())){
						cx.add(c);
					}
				}
			}
		}
		
		if(!(parms.get(2).getStringParameter() == null)){
			for(Contact c: contacts){
				if(c.getVorname() != null){
					if(parms.get(2).getStringParameter().equals(c.getFirma())){
						cx.add(c);
					}
				}
			}
		}
		return cx;
	}

	public ArrayList<Invoice> searchRechnung(Vector<Parameter> parms) {
		ArrayList<Invoice> ix = new ArrayList<Invoice>();
		
		//Von Datum gesetzt + Bis Datum gesetzt
		if(!(parms.get(0).getDateParameter()==null) && !(parms.get(1).getDateParameter()==null)){
			for(Invoice in: invoices){
				if(in.getDueDate().getDay() < parms.get(0).getDateParameter().getDay() || in.getDueDate().getDay() > parms.get(1).getDateParameter().getDay()){
					ix.add(in);
				}
			}
		}
		
		//Nur Von Datum gesetzt
		if(!(parms.get(0).getDateParameter()==null) && (parms.get(1).getDateParameter()==null)){
			for(Invoice in: invoices){
				if(in.getDueDate().getDay() > parms.get(1).getDateParameter().getDay()){
					ix.add(in);
				}
			}
		}
		
		//Nur Bis Datum  gesetzt
		if((parms.get(0).getDateParameter()==null) && !(parms.get(1).getDateParameter()==null)){
			for(Invoice in: invoices){
				if(in.getDueDate().getDay() < parms.get(0).getDateParameter().getDay()){
					ix.add(in);
				}
			}
		}
		
		ObservableList<InvoiceLine> ilx =  FXCollections.observableArrayList();
		
		//Von Preis gesetzt + Bis Preis gesetzt
		if(!(parms.get(2).getStringParameter() == null) && !(parms.get(3).getStringParameter() == null)){
			for(Invoice in: invoices){
				ilx = in.getInvoiceLines();
				double summe = 0;
				for(int i=0; i<ilx.size(); i++){
					summe = (ilx.get(i).getArticle().getPrice() * ilx.get(i).getMenge())*in.getMWSt();
				}

				if(Double.parseDouble(parms.get(2).getStringParameter()) > summe && summe > Double.parseDouble(parms.get(3).getStringParameter())){
					ix.add(in);
				}
			}
		}
		
		//Nur Von Preis gesetzt
		if(!(parms.get(2).getStringParameter() == null) && (parms.get(3).getStringParameter() == null)){
			for(Invoice in: invoices){
				ilx = in.getInvoiceLines();
				double summe = 0;
				for(int i=0; i<ilx.size(); i++){
					summe = (ilx.get(i).getArticle().getPrice() * ilx.get(i).getMenge())*in.getMWSt();
				}

				if(Double.parseDouble(parms.get(2).getStringParameter()) > summe){
					ix.add(in);
				}
			}
		}
		
		//Nur Bis Preis gesetzt
		if((parms.get(2).getStringParameter() == null) && !(parms.get(3).getStringParameter() == null)){
			for(Invoice in: invoices){
				ilx = in.getInvoiceLines();
				double summe = 0;
				for(int i=0; i<ilx.size(); i++){
					summe = (ilx.get(i).getArticle().getPrice() * ilx.get(i).getMenge())*in.getMWSt();
				}

				if(Double.parseDouble(parms.get(3).getStringParameter()) > summe){
					ix.add(in);
				}
			}
		}
		
		return ix;
	}

	public ObservableList<Article> getArticles() {
		ObservableList<Article> ax = FXCollections.observableArrayList();
		
		for(Article a: articles){
			ax.add(a);
		}
		return ax;
	}

	public ObservableList<Contact> findFirm(Vector<Parameter> parms) {
		ObservableList<Contact> fx = FXCollections.observableArrayList();
		
		for(int i=0; i<contacts.size(); i++){
			Contact k = contacts.get(i);
			if(k.getFirma().equals(parms.get(0).getStringParameter()) && k.getVorname() == null){
				fx.add(k);
			}
		}
		return fx;
	}

	public ObservableList<Contact> findPerson(Vector<Parameter> parms) {
		ObservableList<Contact> fx = FXCollections.observableArrayList();
		
		for(int i=0; i<contacts.size(); i++){
			Contact k = contacts.get(i);
			if(k.getVorname() != null){
				if(k.getVorname().equals(parms.get(0).getStringParameter())){
					fx.add(k);
				}
			}
		}
		
		for(int i=0; i<contacts.size(); i++){
			Contact k = contacts.get(i);
			if(k.getVorname() != null){
				if(k.getNachname().equals(parms.get(0).getStringParameter())){
					fx.add(k);
				}
			}
		}
		
		return fx;
	}
	
	public ArrayList<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(ArrayList<Invoice> invoices) {
		this.invoices = invoices;
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

	public ArrayList<Adress> getAdresses() {
		return adresses;
	}

	public void setAdresses(ArrayList<Adress> adresses) {
		this.adresses = adresses;
	}

	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}
}

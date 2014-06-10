package proxy;

import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class DeserializerClient {
	public ObservableList<Contact> deserializeKontaktSearch(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Contact.class);
		ObservableList<Contact> k = (ObservableList<Contact>) xstream.fromXML(xml);
		return k;
	}
	
	public ObservableList<Invoice> deserializeRechnungSearch(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Invoice.class);
		ObservableList<Invoice> r = (ObservableList<Invoice>) xstream.fromXML(xml);
		return r;
	}
	
	public ObservableList<Article> deserializeArtikelSearch(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Contact.class);
		ObservableList<Article> a = (ObservableList<Article>) xstream.fromXML(xml);
		return a;
	}
}

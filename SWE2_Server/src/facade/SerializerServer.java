package facade;

import java.util.ArrayList;
import java.util.Vector;

import utils.Parameter;
import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class SerializerServer {
	
	public String serializeKontaktSearch(ObservableList<Contact> k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Contact.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public String serializeGetArticle(ObservableList<Article> a) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Article.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(a);
		return xml;
	}
	
	public String serializeRechnung(ArrayList<Invoice> rechnungen) {
		XStream xstream = new XStream(new StaxDriver());

		return xstream.toXML(rechnungen);
	}
}

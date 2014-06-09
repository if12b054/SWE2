package facade;

import javafx.collections.ObservableList;
import businessobjects.Article;
import businessobjects.Contact;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
}

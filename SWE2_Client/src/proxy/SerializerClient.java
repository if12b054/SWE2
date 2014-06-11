package proxy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.ObservableList;
import utils.Parameter;
import businessobjects.Article;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceLine;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class SerializerClient {
	
	public String serializeRechnungSearch(Vector<Parameter> searchParms) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Invoice.class);
		//xstream.processAnnotations(Invoice.class);
		
		String xml = xstream.toXML(searchParms);
		return xml;
		
	}
	
	public String serializeRechnung(Invoice r) {
		XStream xstream = new XStream(new StaxDriver());
		return xstream.toXML(r.generateInvoiceData());
	}
	
	public String serializeArtikelSearch(ObservableList<Article> articles) {
		XStream xstream = new XStream(new StaxDriver());
		xstream.processAnnotations(Article.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(articles);
		return xml;
	}
	
	public String serializeKontakt(Contact k) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Contact.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(k);
		return xml;
	}
	
	public String serializeKontaktSearch(Vector<Parameter> searchParms) {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(Contact.class);
		//xstream.alias("kontakt", Kontakt.class);
		String xml = xstream.toXML(searchParms);
		return xml;
	}
}

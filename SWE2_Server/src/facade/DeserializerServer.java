package facade;

import java.util.Vector;

import utils.Parameter;
import businessobjects.Contact;
import businessobjects.Invoice;
import businessobjects.InvoiceData;

import com.thoughtworks.xstream.XStream;

public class DeserializerServer {
	public Contact deserializeKontakt(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Contact.class);
		Contact k = (Contact)xstream.fromXML(xml);
		return k;
	}
	
	public Vector<Parameter> deserializeVector(String xml) {
		XStream xstream = new XStream();
		xstream.processAnnotations(Vector.class);
		Vector<Parameter> v = (Vector<Parameter>)xstream.fromXML(xml);
		return v;
	}
	
	public InvoiceData deserializeRechnung(String xml) {
		XStream xstream = new XStream();
		InvoiceData r = (InvoiceData)xstream.fromXML(xml);
		return r;
	}
}

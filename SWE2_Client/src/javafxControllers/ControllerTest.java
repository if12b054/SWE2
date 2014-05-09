package javafxControllers;

import static org.junit.Assert.*;

import org.junit.Test;

import businessobjects.Contact;

public class ControllerTest {

	@Test
	public void testKontakt1() {
		Contact k;
		
		k = new Contact("12345", "Beispielfirma");
		assertNotNull(k);
	}
	
	public void testKontakt2(){
		Contact k;
		//new KontaktModel(tfFirma.getText(), tfVname.getText(), tfNname.getText(), tfTitel.getText(), tfGebdatum.getText());
		k = new Contact("Beispielfirma", "Max", "Mustermann", "Herr", "29.03.1934");
		assertNotNull(k);
	}
	
	public void testKontakt3(){
	Contact k;
		
		k = new Contact("12345", null);
		
		String firma = k.getFirma();
		assertNull(firma);
	}
	
	public void testKontakt4(){
		
		Contact k;
		k = new Contact("12345", "Coolefirma");
		
		String firma = k.getFirma();
		assertEquals("Coolefirma", firma);
		
	}

}

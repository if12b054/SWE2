package controllers;

import static org.junit.Assert.*;

import org.junit.Test;

import businessobjects.KontaktModel;

public class ControllerTest {

	@Test
	public void testKontakt1() {
		KontaktModel k;
		
		k = new KontaktModel("12345", "Beispielfirma");
		assertNotNull(k);
	}
	
	public void testKontakt2(){
		KontaktModel k;
		//new KontaktModel(tfFirma.getText(), tfVname.getText(), tfNname.getText(), tfTitel.getText(), tfGebdatum.getText());
		k = new KontaktModel("Beispielfirma", "Max", "Mustermann", "Herr", "29.03.1934");
		assertNotNull(k);
	}
	
	public void testKontakt3(){
	KontaktModel k;
		
		k = new KontaktModel("12345", null);
		
		String firma = k.getFirma();
		assertNull(firma);
	}
	
	public void testKontakt4(){
		
		KontaktModel k;
		k = new KontaktModel("12345", "Coolefirma");
		
		String firma = k.getFirma();
		assertEquals("Coolefirma", firma);
		
	}

}

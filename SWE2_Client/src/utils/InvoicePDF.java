package utils;

import java.io.FileOutputStream;
import java.util.Date;

import businessobjects.Invoice;
import businessobjects.InvoiceLine;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class InvoicePDF {
	private static Invoice invoice;
	
	private static String FILE;
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
      Font.BOLD);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
      Font.BOLD);

  	public InvoicePDF(Invoice invoice) {
	  InvoicePDF.invoice = invoice;
	  FILE = "./invoices/Invoice_" + invoice.datumProperty().get() + "_" + Integer.toString(invoice.getId()) + ".pdf";
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(FILE));
      document.open();
      addMetaData(document);
      addContent(document);
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // iText allows to add metadata to the PDF which can be viewed in your Adobe
  // Reader
  // under File -> Properties
  private static void addMetaData(Document document) {
    document.addTitle("Invoice");
  }

  private static void addContent(Document document) throws DocumentException {
    Anchor anchor = new Anchor("Rechnung vom " + invoice.datumProperty().get(), catFont);
    anchor.setName("Rechnung vom " + invoice.datumProperty().get());

    // Second parameter is the number of the chapter
    Chapter catPart = new Chapter(new Paragraph(anchor), 1);

    Paragraph subPara = new Paragraph("Kunde", subFont);
    Section subCatPart = catPart.addSection(subPara);
    subCatPart.add(new Paragraph(invoice.getContactString()));
    subCatPart.add(new Paragraph(invoice.getContact().getAdresse().get(0)));
    subCatPart.add(new Paragraph(invoice.getContact().getAdresse().get(1) + " " + invoice.getContact().getAdresse().get(2)));
    subCatPart.add(new Paragraph(invoice.getContact().getAdresse().get(3)));
    

    subPara = new Paragraph("Allgemein", subFont);
    subCatPart = catPart.addSection(subPara);
    subCatPart.add(new Paragraph("Fälligkeit: " + invoice.dueDateProperty().get()));
    subCatPart.add(new Paragraph("Nachricht: " + invoice.getMessage()));
    
    subPara = new Paragraph("Rechnung", subFont);
    subCatPart = catPart.addSection(subPara);
    subCatPart.add(new Paragraph(invoice.getMessage()));

    // add a table
    createTable(subCatPart);

    // now add all this to the document
    document.add(catPart);

  }

  private static void createTable(Section subCatPart)
      throws BadElementException {
    PdfPTable table = new PdfPTable(4);

    PdfPCell c1 = new PdfPCell(new Phrase("Artikel"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);
    
    c1 = new PdfPCell(new Phrase("Stück"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);

    c1 = new PdfPCell(new Phrase("Preis/Stück"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);

    c1 = new PdfPCell(new Phrase("Preis(Netto)"));
    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(c1);
    table.setHeaderRows(1);
    
    double total = 0;
    for(InvoiceLine i : invoice.getInvoiceLines()) {
    	table.addCell(i.getArticle().getName());
        table.addCell(Integer.toString(i.getMenge()));
        table.addCell(Double.toString(i.getArticle().getPrice()));
        total += i.getMenge() * i.getArticle().getPrice();
        table.addCell(Double.toString(total));
    }
    table.addCell("");
    table.addCell("");
    table.addCell("Gesamt(Netto)");
    table.addCell(Double.toString(total));
    
    table.addCell("");
    table.addCell("");
    table.addCell("MWSt");
    table.addCell(Double.toString(invoice.getMWSt())+ "%");
    
    table.addCell("");
    table.addCell("");
    table.addCell("Gesamt(Brutto)");
    table.addCell(Double.toString(total * (1+ invoice.getMWSt())));

    subCatPart.add(table);

  }
} 
package carrental.beans.billing;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import carrental.CarRentalConfig;
import carrental.MongoDbConfiguration;
import carrental.model.billing.Claim;
import carrental.model.billing.Invoice;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PrintBean {
	
    public void createPdf(Exchange exchange) throws Exception {
        Invoice invoice=(Invoice) exchange.getIn().getBody();
        File f = generatePDF(invoice);
        //checkDB();
        exchange.getIn().setBody(f);
    }
	
    private File generatePDF(Invoice invoice) throws DocumentException, FileNotFoundException{
    	String RESULT = "mailing/draft/Invoice_"+invoice.getCustomer()+".pdf";

    	Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        
        //add metadata
        addMetaData(document,invoice);
        
        //add title page
        addTitlePage(document,invoice);
        
        //add content
        addContent(document, invoice);
        
        document.close();
     
        File f=new File("mailing/draft/Invoice_"+invoice.getCustomer()+".pdf");
        return f;
    }
    
    private void addMetaData(Document document, Invoice invoice){
  	  	document.addTitle("Invoice (ID:"+invoice.getId()+")");
        document.addSubject("Invoice for "+invoice.getCustomer());
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("FHKME Carrental AG");
        document.addCreator("carrental application");
    }
  
    private void addTitlePage(Document document, Invoice invoice) throws DocumentException{
	  	Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
	  	Paragraph preface = new Paragraph();
	    addEmptyLine(preface, 1);
	    preface.add(new Paragraph("INVOICE",headerFont));
	
	    addEmptyLine(preface, 1);
	    preface.add(new Paragraph("Invoice generated by FHKME Carrental AG" + ", " + new Date()));
	
	    document.add(preface);
	    document.newPage();
    }
    
    private static void addContent(Document document, Invoice invoice) throws DocumentException{
    	Anchor anchor = new Anchor("Calculation of costs");
        anchor.setName("Calculation of costs");

        Chapter chapter = new Chapter(new Paragraph(anchor), 1);

        Paragraph subParagraph= new Paragraph("Travel costs");
	    addEmptyLine(subParagraph, 1);

        Section section = chapter.addSection(subParagraph);
        
        createTravelCostsTable(section,invoice);
        
        Paragraph subParagraph2 = new Paragraph("Service costs");
	    addEmptyLine(subParagraph2, 1);

        Section section2 = chapter.addSection(subParagraph2);
        
        createServiceCostsTable(section2, invoice);

        document.add(chapter);

    }
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
    }
    
    private static void createTravelCostsTable(Section section, Invoice invoice) throws BadElementException {
    	PdfPTable table = new PdfPTable(2);

	    PdfPCell c1 = new PdfPCell(new Phrase("cost category"));
	    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(c1);

	    c1 = new PdfPCell(new Phrase("fees"));
	    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(c1);

	    table.setHeaderRows(1);
	    
	    //Do some useful calculations here
	    table.addCell("Travel costs ");
	    table.addCell("1350.50 EUR");
	    table.addCell("sum taxes");
	    table.addCell("366.88 EUR");
	    table.addCell("total");
	    table.addCell("1717.38 EUR");
	    
	    section.add(table);
    }
    
    private static void createServiceCostsTable(Section section, Invoice invoice) throws BadElementException {
    	    PdfPTable table = new PdfPTable(2);

    	    // t.setBorderColor(BaseColor.GRAY);
    	    // t.setPadding(4);
    	    // t.setSpacing(4);
    	    // t.setBorderWidth(1);

    	    PdfPCell c1 = new PdfPCell(new Phrase("cost category"));
    	    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    	    table.addCell(c1);

    	    c1 = new PdfPCell(new Phrase("fees"));
    	    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    	    table.addCell(c1);

    	    table.setHeaderRows(1);
    	        	    
    	    BigDecimal sumCosts=new BigDecimal(0);
    	    
    	    for(Claim c:invoice.getClaims()){
    	    	table.addCell(c.getClaimType().name()+" service");
    	    	table.addCell(c.getCosts()+" EUR");
    	    	sumCosts=sumCosts.add(c.getCosts());
    	    }
    	    
    	    table.addCell("total");
    	    table.addCell(sumCosts+" EUR");
    	    
    	    section.add(table);
    }
    
    //For testing purposes
    @Autowired
  	private MongoDbConfiguration mongoConfig;
	public void checkDB() throws Exception{
		DB dataBase = mongoConfig.mongo().getDB("carrental");
		DBCollection dbColl = dataBase.getCollection("invoice");
		//dbColl.setObjectClass(Invoice.class);
		//System.out.println("DEBUG OUTPUT: dbColl.find().count(): "+dbColl.count());
		DBCursor dbCursor=dbColl.find();
		System.out.println("dbColl.count(): "+dbColl.count());
		while(dbCursor.hasNext()){
			//Invoice tmp=(Invoice)dbCursor.next();
			DBObject dbObject=dbCursor.next();
			//dbColl.remove(dbObject);
			//System.out.println("dbCursor.next():\ngetId(): "+tmp.getId()+"\ngetCustomer(): "+tmp.getCustomer()+"\ngetAddress(): "+tmp.getAddress());
			System.out.println("DBObject: "+dbObject.toString());
		}
	}
	
	
}

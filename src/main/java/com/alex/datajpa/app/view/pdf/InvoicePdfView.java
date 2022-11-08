package com.alex.datajpa.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.alex.datajpa.app.models.entity.Invoice;
import com.alex.datajpa.app.models.entity.InvoiceItem;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;



// en vex de q muestre el doc html, muestre el pdf
@Component("invoice/ver")   // este name xq es lo q   ver()  retorna en InvoiceController
public class InvoicePdfView extends AbstractPdfView {

    // multilanguage method 1)
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
    private LocaleResolver localeResolver;
	

    

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // model aqui captura los attributes q enviamos en el controller
        Invoice invoice = (Invoice) model.get("invoice");     //invoice asi se guarda en el model en controller


        // // Multilanguage en el pdf 
        Locale locale = localeResolver.resolveLocale(request);
        // method 2) messages   - NO requiere los @Autowired
		MessageSourceAccessor messages = getMessageSourceAccessor();


        PdfPTable table = new PdfPTable(1); // tabla 1 columna

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale)));
        // cell = new PdfPCell(new Phrase(messages.getMessage("text.factura.ver.datos.factura")));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);

        table.addCell(cell);
        table.setSpacingAfter(20);
        table.addCell(invoice.getClient().getName().concat(" ").concat(invoice.getClient().getLastname()));
        table.addCell(invoice.getClient().getEmail());



        PdfPTable table2 = new PdfPTable(1); // tabla 1 columna
        table2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale)));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);

        table2.addCell(cell);
        table2.addCell(messages.getMessage("text.cliente.factura.folio") + ": " + invoice.getId());
        table2.addCell(messages.getMessage("text.cliente.factura.descripcion") + ": " + invoice.getDescription());
        table2.addCell(messages.getMessage("text.cliente.factura.fecha") + ": " + invoice.getCreatedAt());

        document.add(table);
        document.add(table2);



        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float [] {3.5f, 1, 1, 1});
        table3.addCell("Product");
        table3.addCell("Price");
        table3.addCell("Quantity");
        table3.addCell("Total");

        for(InvoiceItem item : invoice.getItems()) {
            table3.addCell(item.getProduct().getName());
            table3.addCell(item.getProduct().getPrice().toString());
            table3.addCell(item.getQuantity().toString());
            table3.addCell(item.calculateAmount().toString());
        }

        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

        table3.addCell(cell);
        table3.addCell(invoice.getTotal().toString());
        document.add(table3);

        
    }
    
}

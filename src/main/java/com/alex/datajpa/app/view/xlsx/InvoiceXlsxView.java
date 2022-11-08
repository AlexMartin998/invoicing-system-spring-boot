package com.alex.datajpa.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.alex.datajpa.app.models.entity.Invoice;
import com.alex.datajpa.app.models.entity.InvoiceItem;



@Component("invoice/ver.xlsx")  // c/component debe ser unico, aqui diferenciamos con la extension  -  .xlsx mismo q en app.properties
public class InvoiceXlsxView extends AbstractXlsxView {


    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        // cambiar filename
        response.setHeader("Content-Disposition", "attachment; filename=\"invoice_view.xlsx\"");

        // // Multilanguage XLSX
        MessageSourceAccessor messages = getMessageSourceAccessor();


        Invoice invoice = (Invoice) model.get("invoice");   // invoice como guarda en el model en controller ver()

        Sheet sheet = workbook.createSheet("Invoice Spring");       // hoja a partir del workbook

        Row row = sheet.createRow(0);   //inicia en 0
        Cell cell = row.createCell(0);
        cell.setCellValue(messages.getMessage("text.factura.ver.datos.cliente"));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(invoice.getClient().getName() + " " + invoice.getClient().getLastname());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(invoice.getClient().getEmail());
        
        // otra forma de crear: directo en cadena  --  4 para q salte 1 y de space
        sheet.createRow(4).createCell(0).setCellValue(messages.getMessage("text.factura.ver.datos.factura"));
        sheet.createRow(5).createCell(0)
                .setCellValue(messages.getMessage("text.cliente.factura.folio") + " " + invoice.getId()); 
        sheet.createRow(6).createCell(0)
                .setCellValue(messages.getMessage("text.cliente.factura.descripcion") + " " + invoice.getDescription());
        sheet.createRow(7).createCell(0)
                .setCellValue(messages.getMessage("text.cliente.factura.fecha") + " " + invoice.getCreatedAt());

        

        // invoice
        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);


        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Product");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Quantity");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);

        int rowNum = 10;    // 10 xq la ultima es la 9
        for (InvoiceItem item : invoice.getItems()) {
            Row fila = sheet.createRow((rowNum++));

            cell = fila.createCell(0);
            cell.setCellValue(item.getProduct().getName());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProduct().getPrice());
            cell.setCellStyle(tbodyStyle);
            
            cell = fila.createCell(2);
            cell.setCellValue(item.getQuantity());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calculateAmount());
            cell.setCellStyle(tbodyStyle);
        }

        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(2).setCellValue("Grand Total: ");
        totalRow.createCell(3).setCellValue(invoice.getTotal());





    }
    
}

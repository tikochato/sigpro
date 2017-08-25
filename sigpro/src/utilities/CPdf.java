package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;

public class CPdf {
	
		public CPdf(){
			
		}
		public String ExportPdf(){
			String path = "";
			try{
				PDDocument doc = new PDDocument();
				PDPage page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
			    
			    
			    PDPageContentStream contentStream = new PDPageContentStream(doc, page);

				// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
				PDFont font = PDType1Font.HELVETICA_BOLD;
				
				contentStream.beginText();
				contentStream.setFont(font, 18);
				//definir x, y
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Públicas");
				contentStream.endText();
				
				contentStream.beginText();
				contentStream.setFont(font, 12);
				//definir x, y
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: Metas de Préstamo");
				contentStream.endText();
				
				float margin = 50;
				// starting y position is whole page height subtracted by top and bottom margin
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				// we want table across whole page width (subtracted by left and right margin ofcourse)
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

				boolean drawContent = true;
				float yStart = yStartNewPage;
				float bottomMargin = 70;
				// y position is your coordinate of top left corner of the table
				float yPosition = 525;

				BaseTable table = new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);

				Row<PDPage> headerRow = table.createRow(15f);
				float distribucion = 100/3;
				float cuarta =15;
				float mitad=distribucion/2;
				float tercera= (20/3)+5/6;
				//nombre - unidad de medida
				Cell<PDPage> cell = headerRow.createCell(25, "Nombre");
				cell = headerRow.createCell(6, "Unidad de Medida");
				
				//cuatrimestres
				cell = headerRow.createCell(cuarta, "Cuatrimestre 1");
				cell = headerRow.createCell(cuarta, "Cuatrimestre 2");
				cell = headerRow.createCell(cuarta, "Cuatrimestre 3");
				
				//totales
				cell = headerRow.createCell(8, "Total Anual");
				cell = headerRow.createCell(8, "Total");
				cell = headerRow.createCell(8, "Meta Final");
				table.addHeaderRow(headerRow);
				
				/*Row<PDPage> row = table.createRow(12);
				cell = row.createCell(30, "Data 1");
				cell = row.createCell(70, "Some value");*/

				table.draw();
			    contentStream.close();
			    path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
				FileOutputStream out = 
						new FileOutputStream(new File(path));
				doc.save(out);
				doc.close();
			}catch(Exception o){
				o.printStackTrace();
			}
			return path;
		}
		
		
		
		private static PDPage addNewPage(PDDocument doc) {
			PDPage page = new PDPage();
			doc.addPage(page);
			return page;
		}
		
}

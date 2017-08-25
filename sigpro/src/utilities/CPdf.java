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
	/**
	 * variables de control de tamaño de celdas
	 * */
	
	private float celda_a=21;
	private float celda_b=(float)6.7;
	private float celda_c=6;
	
	/*
	 * variables de texto
	 * */
	private String titulo ="";
	private String anio="";
	
	/**
	 * Variables del documento
	 */
	private PDDocument doc;
	private PDPage page;
	
	
		public CPdf(String titulo){
			this.titulo=titulo;
			doc = new PDDocument();
			page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
		    doc.addPage( page );
			
		}
		public String ExportPdf(){
			String path = "";
			try{
				
			    
			    
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
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				
				float margin = 50;
				// starting y position is whole page height subtracted by top and bottom margin
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				System.out.println(yStartNewPage);
				// we want table across whole page width (subtracted by left and right margin ofcourse)
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

				boolean drawContent = true;
				float yStart = yStartNewPage;
				float bottomMargin = 70;
				// y position is your coordinate of top left corner of the table
				float yPosition = 525;

				BaseTable table = new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);

				Row<PDPage> headerRow = table.createRow(15f);			
				
				//nombre - unidad de medida
				Cell<PDPage> cell = headerRow.createCell(celda_a, "");
				
				cell = headerRow.createCell(celda_c, "");
				
				//cuatrimestres
				cell = headerRow.createCell(celda_b*2, "Cuatrimestre 1 2017");
				cell = headerRow.createCell(celda_b*2, "Cuatrimestre 2 2017");
				cell = headerRow.createCell(celda_b*2, "Cuatrimestre 3 2017");
				
				//totales
				cell = headerRow.createCell(celda_b*2, "Total Anual");
				cell = headerRow.createCell(celda_b*2, "Total");
				cell = headerRow.createCell(celda_b, "");
				table.addHeaderRow(headerRow);
				
				/**
				 * creando la segunda fila de encabezado
				 */
				//creando fila
				Row<PDPage> row = table.createRow(12);
				 cell = row.createCell(celda_a, "Nombre");
				 cell = row.createCell(celda_c, "Unidad de Medida ");
				//cuatrimestres
				cell = row.createCell(celda_b, "Planificado");
				cell.setFontSize(cell.getFontSize()-1f);
				System.out.println(cell.getFontSize());
				cell = row.createCell(celda_b, "Real");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Planificado");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Real");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Planificado");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Real");
				cell.setFontSize(cell.getFontSize()-1f);
				//totales
				cell = row.createCell(celda_b, "Planificado");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Real");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Planificado");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Real");
				cell.setFontSize(cell.getFontSize()-1f);
				cell = row.createCell(celda_b, "Meta Final");
				for(int i=0; i<18;i++){
					row= agregarFila(table);
				}
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
		
		public Row<PDPage> agregarFila(BaseTable table){
			Row<PDPage> row = table.createRow(12);
			/***/
			Cell<PDPage> cell = row.createCell(celda_a, "ABC...");
			cell.setFontSize(cell.getFontSize()-.5f);
			cell = row.createCell(celda_c, "abc..");
			/***/
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			/***/
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1000");
			cell = row.createCell(celda_b, "1900000");
			return row;
		}
		
		private static PDPage addNewPage(PDDocument doc) {
			PDPage page = new PDPage();
			doc.addPage(page);
			return page;
		}
		
}

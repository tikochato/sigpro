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
		public String ExportPdf(String [][]headers, String [][]datosMetas){
			String path = "";
			try{	
			    String [] cabeceras = new String[headers[0].length];
			    System.arraycopy( headers[0], 0, cabeceras, 0, headers[0].length );;
			   
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

				table.addHeaderRow(agregarCabecera(table,cabeceras));
				
				/**
				 * creando la segunda fila de encabezado
				 */
				//creando fila
				//Cell<PDPage> cell =
				Row<PDPage> row = table.createRow(12);
				row= agregarCabecera_pt2(row, cabeceras);
				//18 filas máximo
				
				for(int i=0; i<datosMetas.length;i++){
					row= agregarFila(table,datosMetas[i]);
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
		
		public Row<PDPage> agregarFila(BaseTable table, String []datos){
			Row<PDPage> row = table.createRow(12);
			double suma_real=0;
			double suma_planificada=0;
			int control =0;
			String entrada="";
				
			for(int i=0;i<datos.length-1;i++){
				String texto="";
				if(datos[i]==null||datos[i].isEmpty()){
					texto="";
					entrada+=i+": ,";
				}else{
					texto=datos[i];
					entrada+=i+": "+texto+",";
					if(i>1){
						if(control==2){
							suma_real=suma_real+Double.parseDouble(datos[i]);
						}else{
							control++;
							suma_planificada=suma_planificada+Double.parseDouble(datos[i]);
						}
					}
					
					
				}
				if(i==0){
					Cell<PDPage> cell = row.createCell(celda_a, texto);
					cell.setFontSize(cell.getFontSize()-.5f);
				}else if(i==1){
					Cell<PDPage>cell = row.createCell(celda_c, texto);
					cell.setFontSize(cell.getFontSize()-.5f);
				}else{
					Cell<PDPage> cell = row.createCell(celda_b, texto);
					cell.setFontSize(cell.getFontSize()-.5f);
				}
			}
			Cell<PDPage> cell = row.createCell(celda_b, suma_planificada+"");
			cell = row.createCell(celda_b, suma_real+"");
			cell = row.createCell(celda_b, suma_planificada+"");
			cell = row.createCell(celda_b, suma_real+"");	
			cell = row.createCell(celda_b, datos[datos.length-1]);
			cell.setFontSize(cell.getFontSize()-.5f);
			return row;
		}
		
		public Row<PDPage> agregarCabecera(BaseTable table,String cabecera[]){
			Row<PDPage> headerRow = table.createRow(12);
			Cell<PDPage> cell = headerRow.createCell(celda_a, "");
			cell = headerRow.createCell(celda_c, "");			
			for(int i =2; i<cabecera.length-1;i++){
				if(!cabecera[i].isEmpty()){
					cell = headerRow.createCell(celda_b*2, cabecera[i]);
				}
			}
			
			cell = headerRow.createCell(celda_b, "");
			
			return headerRow;
			
		}
		public Row<PDPage> agregarCabecera_pt2(Row<PDPage> row,String cabecera[]){
			System.out.println(cabecera.length);
			Cell<PDPage> cell = row.createCell(celda_a, cabecera[0]);
			cell.setFontSize(cell.getFontSize()-1f);
			cell = row.createCell(celda_c, cabecera[1]);	
			cell.setFontSize(cell.getFontSize()-1f);
			int control =1;
			for(int i =0; i<(cabecera.length-3)*2;i++){
				if(control==2){
					control=1;
					cell = row.createCell(celda_b, "Real");
					cell.setFontSize(cell.getFontSize()-1f);
				}else{
					control++;
					cell = row.createCell(celda_b, "Planificado");
					cell.setFontSize(cell.getFontSize()-1f);
				}
				/*
				if(!cabecera[i].isEmpty()){
					cell = row.createCell(celda_b*2, cabecera[i]);
				}*/
			}
			
			cell = row.createCell(celda_b, "Meta Final");
			cell.setFontSize(cell.getFontSize()-1f);
			return row;
			
		}
		
		private static PDPage addNewPage(PDDocument doc) {
			PDPage page = new PDPage();
			doc.addPage(page);
			return page;
		}
		
}

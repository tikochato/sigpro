package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private float font_size=7f;
	/*
	 * variables de texto
	 * */
	private String titulo ="";
	private String anio="";
	private int numero_columnas=13;
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
		public String ExportPdf(String [][]headers, String [][]datosMetas, int visualizacion){
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
				//System.out.println(yStartNewPage);
				// we want table across whole page width (subtracted by left and right margin ofcourse)
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

				boolean drawContent = true;
				float yStart = yStartNewPage;
				float bottomMargin = 70;
				// y position is your coordinate of top left corner of the table
				float yPosition = 525;

				BaseTable table = new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				table.addHeaderRow(agregarCabecera(table,cabeceras));
				
				//imprimir(configurarCabeceras(cabeceras,datosMetas[0]));
				//imprimir(datosMetas);
				List <String[][]>tablas =divTablas(configurarCabeceras(cabeceras,datosMetas[0]),datosMetas,visualizacion);
				System.out.println(tablas.size()+"  -tablas");
				imprimir(tablas.get(0));
				/**
				 * creando la segunda fila de encabezado
				 */
				//creando fila
				Row<PDPage> row = table.createRow(12);
				row= agregarCabecera_pt2(row, datosMetas[0]);
				table.setTableIsBroken(true);
				/*List<Row <PDPage>> li= table.getRows();
				li.get(0).getHeight();*/
				table.addHeaderRow(row);
				
				for(int i=0; i<datosMetas.length;i++){
					row= agregarFila(table,datosMetas[i]);
				}
				table.draw();
				//List <BaseTable> def = getTables(table);
				//int a = table.getHeader().getColCount();
				//System.out.println(a);
			    contentStream.close();
			    path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
				FileOutputStream out = new FileOutputStream(new File(path));
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
				
			for(int i=0;i<datos.length;i++){
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
					cell.setFontSize(font_size);
				}else if(i==1){
					Cell<PDPage>cell = row.createCell(celda_c, texto);
					cell.setFontSize(font_size);
				}else{
					Cell<PDPage> cell = row.createCell(celda_b, texto);
					cell.setFontSize(font_size);
				}
			}
			return row;
		}
		
		public Row<PDPage> agregarCabecera(BaseTable table,String cabecera[]){
			Row<PDPage> headerRow = table.createRow(12);
			Cell<PDPage> cell = headerRow.createCell(celda_a, "");
			cell = headerRow.createCell(celda_c, "");	
			cell.setHeaderCell(true);
			for(int i =2; i<cabecera.length-1;i++){
				if(!cabecera[i].isEmpty()){
					cell = headerRow.createCell(celda_b*2, cabecera[i]);
					cell.setHeaderCell(true);
				}
			}
			
			cell = headerRow.createCell(celda_b, "");
			cell.setHeaderCell(true);
			return headerRow;
			
		}
		public Row<PDPage> agregarCabecera_pt2(Row<PDPage> row,String cabecera[]){
			//System.out.println(cabecera.length);
			Cell<PDPage> cell = row.createCell(celda_a, "Nombre");
			cell.setFontSize(cell.getFontSize()-1f);
			cell = row.createCell(celda_c,"Meta Unidad Medida");	
			cell.setFontSize(cell.getFontSize()-1f);
			int control =1;
			for(int i =0; i<cabecera.length-3;i++){
				if(control==2){
					control=1;
					cell = row.createCell(celda_b, "Real");
					cell.setFontSize(cell.getFontSize()-1f);
				}else{
					control++;
					cell = row.createCell(celda_b, "Planificado");
					cell.setFontSize(cell.getFontSize()-1f);
				}
			}
			
			cell = row.createCell(celda_b, "Meta Final");
			cell.setFontSize(cell.getFontSize()-1f);
			//cell.setHeaderCell(true);
			
			return row;
			
		}
		
		public List<List<Row<PDPage>>> getTables(BaseTable def){
			
			List<List<Row<PDPage>>> ret = new ArrayList<List<Row<PDPage>>>();
			
			/*int num = (int)Math.ceil(((def.getRows().get(0).getColCount()-3)/5)+((def.getRows().get(0).getColCount()-3)% 5)); 
			for(int i=0;i<num;i++){
				BaseTable table = new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
			}*/
			return ret;
		}
		/*private void ChangeSize(){
			celda_a=(float)celda_a/2;
			celda_b=(float)celda_b/2;
			celda_c=(float)celda_b/2;
			font_size=(float)font_size/2;
		}*/
		private String[][] configurarCabeceras(String []cabecera, String []subcabecera){
			String [][]ret=new String [2][subcabecera.length];
			//primera línea.
			ret[0][0]=" ";
			ret[0][1]=" ";
			int cont=2;
			for(int i =2; i<cabecera.length-1;i++){
				if(!cabecera[i].isEmpty()&&cabecera[i].compareTo("null")!=0){
					ret[0][cont]=cabecera[i];
					cont++;
				}
			}
			ret[1][0]="Nombre";
			ret[1][1]="Meta Unidad Medida";
			int control =1;
			//segunda línea
			for(int i =0; i<subcabecera.length-3;i++){
				cont=i;
				if(control==2){
					control=1;
					ret[1][i+2]="Real";
				}else{
					control++;
					ret[1][i+2]="Planificado";
				}
			}
			ret[1][ret[1].length-1]="Meta Final";
			return ret;
		}
		
		void imprimir(String [][]entrada){
			for(int i =0; i<entrada.length;i++){
				String salida="";
				for(int j=0; j<entrada[i].length;j++){
					salida+=entrada[i][j];
					salida+="-";
				}
				System.out.println(salida);
			}
		}
		private List<String[][]> divTablas(String[][]cabecera, String[][]datos, int visualizacion){
			List <String[][]>ret  = new ArrayList<String[][]>();
			int totalFilas= cabecera.length+datos.length;
			int residuo=((datos[0].length-3)% 10);
			if(visualizacion==2){
				int num = (int)Math.ceil(
						((datos[0].length-3)/10)+((datos[0].length-3)% 10)*0.1
						);
				for(int x=0; x<num; x++){
					String [][] tabla_tmp= new String[totalFilas][12];
					int medida= x<num-1? 12: residuo;
					for(int y=0; y<totalFilas;y++){
						if(y<2){
							System.arraycopy(cabecera[y], 12*x, tabla_tmp[y], 0, medida);
						}else{
							System.arraycopy( datos[y-2], 12*x, tabla_tmp[y], 0, medida);
						}
					}
					ret.add(tabla_tmp);
				}
			}
			return ret;
		}
}

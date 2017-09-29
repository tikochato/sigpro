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
	 * variables de control de tama�o de celdas
	 * */
	
	private float celda_a=21;
	private float celda_b=(float)6.7;
	private float celda_c=6;
	private float font_size=7f;
	/*
	 * variables de texto
	 * */
	private String titulo ="";
	float restante;
	private PDDocument doc;
	private PDPage page;
	private int tipo_reporte=0;
	
		public CPdf(String titulo){
			this.titulo=titulo;
			doc = new PDDocument();
			
		}
		public String ExportPdfMetasPrestamo(String [][]headers, String [][]datosMetas, int visualizacion){
			String path = "";
			try{	
			    String [] cabeceras = new String[headers[0].length];
			    System.arraycopy( headers[0], 0, cabeceras, 0, headers[0].length );
				String [][]cabeceras_fixed= configurarCabeceras(cabeceras,datosMetas[0],visualizacion);
				List <String[][]>tablas =divTablas(cabeceras_fixed,datosMetas,visualizacion);
				restante= visualizacion==2? (datosMetas[0].length % 12):(datosMetas[0].length % 13) ;
				PDFont font = PDType1Font.HELVETICA_BOLD;
				List<Float> altura= new ArrayList<Float>();
				for(int x=0;x<tablas.size();x++){
					page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
				    doc.addPage( page );
					PDPageContentStream contentStream = new PDPageContentStream(doc, page);
					
					if(x==0){
						contentStream.beginText();
						contentStream.setFont(font, 18);
						contentStream.newLineAtOffset(50, 550);
						contentStream.showText("Ministerio de Finanzas Publicas");
						contentStream.endText();
						contentStream.beginText();
						contentStream.setFont(font, 12);
						contentStream.newLineAtOffset(50, 530);
						contentStream.showText("Reporte: "+titulo);
						contentStream.endText();
					}
					
					
					float margin = 50;
					float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
					float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

					boolean drawContent = true;
					float bottomMargin = 70;
					
					String[][] tabla_tmp = tablas.get(x);
					BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
					boolean ultimo=x==tablas.size()-1;
					table_x.addHeaderRow(agregarCabecera(table_x,tabla_tmp[0],x,ultimo,visualizacion));
					Row<PDPage> row = table_x.createRow(12);
					
					table_x.addHeaderRow(
							agregarCabecera_pt2(row, tabla_tmp[1],x==0)
					);
					for(int i=2;i<tabla_tmp.length;i++){
						row= agregarFila(table_x,tabla_tmp[i], x==0,x==tablas.size()-1);
					}
					if(x==0){
						altura=obtenerAlturas(table_x);
					}else{
						for(int i=0; i<table_x.getRows().size(); i++){
							table_x.getRows().get(i).setHeight(altura.get(i));
						}
					}
					table_x.draw();
					contentStream.close();
				}
			    path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
				FileOutputStream out = new FileOutputStream(new File(path));
				doc.save(out);
				doc.close();
			}catch(Exception o){
				CLogger.write("1", CPdf.class, o);
			}
			return path;
		}
		
		private List<Float> obtenerAlturas(BaseTable entrada){
			List <Float> ret = new ArrayList<Float>();
			for(int i=0; i<entrada.getRows().size();i++){
				ret.add(entrada.getRows().get(i).getHeight());
			}
			return ret;
			
		}
		public Row<PDPage> agregarFila(BaseTable table, String []datos, boolean primero, boolean ultimo){
			Row<PDPage> row = table.createRow(12);
			
			int tope= ultimo ? (int)restante: datos.length;
			if(tipo_reporte==0){
				int cont=0;
				if(primero){
					Cell<PDPage> cell = row.createCell(celda_a, datos[0]);
					cell.setFontSize(font_size);
					cell = row.createCell(celda_c, datos[1]);
					cell.setFontSize(font_size);
					cont=2;
				}
				for(int i=cont;i<tope;i++){
						Cell<PDPage> cell = row.createCell(celda_b, datos[i]);
						cell.setFontSize(font_size);
					
				}
			}else if(tipo_reporte==1){
				int cont=0;
				if(primero){
					Cell<PDPage> cell = row.createCell(celda_a, datos[0]);
					cell.setFontSize(font_size);
					cont=1;
					
				}
				for(int i=cont;i<tope;i++){
						if(datos[i]!=null){
							Cell<PDPage> cell = row.createCell(celda_b, datos[i]);
							cell.setFontSize(font_size);
						}
				}
			}
			
			return row;
		}
		
		private Row<PDPage> agregarCabecera(BaseTable table,String cabecera[], int posicion, boolean ultimo, int visualizacion){
			Row<PDPage> headerRow = table.createRow(12);
			int corrimiento=0;
			float tam_celda=celda_b;
			Cell<PDPage> cell;
			if(tipo_reporte==0){
				if(posicion==0){
					corrimiento=2;
					cell = headerRow.createCell(celda_a, "");
					cell = headerRow.createCell(celda_c, "");	
				}
				if(visualizacion==2){
					tam_celda=tam_celda*2;
					for(int i =corrimiento; i<cabecera.length;i++){
						if(cabecera[i]!=null&& !cabecera[i].isEmpty()&&i!=cabecera.length-1){
							cell = headerRow.createCell(tam_celda, cabecera[i]);
							cell.setHeaderCell(true);
						}
					}
				}else{
					for(int i =corrimiento; i<cabecera.length;i++){
						if(cabecera[i]!=null&& !cabecera[i].isEmpty()&&i!=cabecera.length){
							cell = headerRow.createCell(tam_celda, cabecera[i]);
							cell.setHeaderCell(true);
						}
					}
				}
				
				if(ultimo){
					cell = headerRow.createCell(celda_b, "");
					cell.setHeaderCell(true);
					cell = headerRow.createCell(celda_b, "");
					cell.setHeaderCell(true);
				}
			}else if(tipo_reporte==1){
				if(posicion==0){
					corrimiento=1;
					cell = headerRow.createCell(celda_a, "");
				}
				if(visualizacion==2){
					tam_celda=(float)13.4;
					for(int i =corrimiento; i<cabecera.length;i++){
						if(cabecera[i]!=null&& !cabecera[i].isEmpty()&&i!=cabecera.length-1){
							cell = headerRow.createCell(tam_celda, cabecera[i]);
							cell.setHeaderCell(true);
						}
					}
				}else{
					for(int i =corrimiento; i<cabecera.length;i++){
						if(cabecera[i]!=null&& !cabecera[i].isEmpty()&&i!=cabecera.length){
							cell = headerRow.createCell(tam_celda, cabecera[i]);
							cell.setHeaderCell(true);
						}
					}
				}
			}
			
			return headerRow;
			
		}
		private Row<PDPage> agregarCabecera_pt2(Row<PDPage> row,String cabecera[],boolean primero){
			Cell<PDPage> cell;
			
			if(tipo_reporte==0){
					int cont=0;
					if(primero){
						cell = row.createCell(celda_a, cabecera[0]);
						cell.setFontSize(cell.getFontSize()-1f);
						cell = row.createCell(celda_c,cabecera[1]);	
						cell.setFontSize(cell.getFontSize()-1f);
						cont=2;
					}
					for(int i =cont; i<cabecera.length;i++){
						if(cabecera[i]!=null&&!cabecera[i].isEmpty()){
							cell = row.createCell(celda_b, cabecera[i]);
							cell.setFontSize(cell.getFontSize()-1f);
						}
						
					}
			}else if(tipo_reporte==1){
				int cont=0;
				if(primero){
					cell = row.createCell(celda_a, cabecera[0]);
					cell.setFontSize(cell.getFontSize()-1f);
					cont++;
				}
				for(int i =cont; i<cabecera.length;i++){
					if(cabecera[i]!=null&&!cabecera[i].isEmpty()){
						cell = row.createCell(celda_b, cabecera[i]);
						cell.setFontSize(cell.getFontSize()-1f);
					}
					
				}
			}
			
			return row;
		}
		
		
		
		private String[][] configurarCabeceras(String []cabecera, String []subcabecera, int visualizacion){
			String [][]ret = new String[2][];
			if(tipo_reporte==0){
				ret=new String [2][subcabecera.length];
				//primera l�nea.
				ret[0][0]=" ";
				ret[0][1]=" ";
				int cont=2;
				for(int i =2; i<cabecera.length-2;i++){
					if(!cabecera[i].isEmpty()&&cabecera[i].compareTo("null")!=0){
						ret[0][cont]=cabecera[i];
						cont++;
					}
				}
				ret[1][0]="Nombre";
				ret[1][1]="Meta Unidad Medida";
				int control =1;
				//segunda l�nea
				if(visualizacion==2){
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
					ret[1][ret[1].length-2]="Meta Final";
					ret[1][ret[1].length-1]="% Avance";
				}else{
					String tipo= visualizacion==0? "planificado" : "Real";
					for(int i =0; i<subcabecera.length-3;i++){
						cont=i;
						ret[1][i+2]=tipo;
						
					}
					ret[1][ret[1].length-2]="Meta Final";
					ret[1][ret[1].length-1]="% Avance";
				}
				
			}else if(tipo_reporte==1){
				ret=new String [2][subcabecera.length];
				ret[0][0]=" ";
				int cont=1;
				for(int i =1; i<cabecera.length-1;i++){
					if(!cabecera[i].isEmpty()&&cabecera[i].compareTo("null")!=0){
						ret[0][cont]=cabecera[i];
						cont++;
					}
				}
				ret[1][0]="Nombre";
				int control =1;
				if(visualizacion==2){
					for(int i =0; i<subcabecera.length-1;i++){
						cont=i;
						if(control==2){
							control=1;
							ret[1][i+1]="Real";
						}else{
							control++;
							ret[1][i+1]="Planificado";
						}
					}
				}else{
					ret[0][subcabecera.length-1]="Total";
					String tipo= visualizacion==0? "planificado" : "Real";
					for(int i =0; i<subcabecera.length;i++){
						cont=i;
						ret[1][i]=tipo;
						
					}
				}				
			}

			return ret;
		}
		
		
		
		private List<String [][]>divTablas(String [][]cabecera, String [][]datos, int visualizacion){
			List <String[][]>ret  = new ArrayList<String[][]>();
			int totalFilas= cabecera.length+datos.length;
			if(tipo_reporte==0){
				if(visualizacion==2){
					int num = (int)Math.ceil(
							((datos[0].length)/12)+((datos[0].length)% 12)*0.1
							);
					int num_col_data=datos[0].length;
					try{
						for(int x=0; x<num; x++){
							int tam_horizontal= num>1? 12: 13;
							String [][] tabla_tmp= new String[totalFilas][tam_horizontal];
							int medida= num_col_data-(12*x)>12? 12: num_col_data-(12*x);
							int tam_cab= x==0? 7: 6;
							int poscab= x==0? 0: (6*x)+1;
							for(int y=0; y<totalFilas;y++){
								if(y==0){
									System.arraycopy(cabecera[y],poscab, tabla_tmp[y], 0, tam_cab);
								}else if(y==1){
									System.arraycopy(cabecera[y], 12*x, tabla_tmp[y], 0, medida);
								}else{
									System.arraycopy( datos[y-2], 12*x, tabla_tmp[y], 0, medida);
								}
							}
							ret.add(tabla_tmp);
						}
						
					}catch(Exception o){
						CLogger.write("2", CPdf.class, o);
					}
				}else{
					int num = (int)Math.ceil(
							((datos[0].length)/13)+((datos[0].length)% 13)*0.1
							);
					int num_col_data=datos[0].length;
					try{
						for(int x=0; x<num; x++){
							int tam_horizontal= 13;
							String [][] tabla_tmp= new String[totalFilas][tam_horizontal];
							int medida= num_col_data-(13*x)>13? 13: num_col_data-(13*x);
							for(int y=0; y<totalFilas;y++){
								if(y<2){
									System.arraycopy(cabecera[y],13*x, tabla_tmp[y], 0, medida);
								}else{
									System.arraycopy( datos[y-2], 13*x, tabla_tmp[y], 0, medida);
								}
							}
							ret.add(tabla_tmp);
						}
						
					}catch(Exception o){
						CLogger.write("3", CPdf.class, o);
					}
				}
			}else if(tipo_reporte==1){
				if(visualizacion==2){
					int num = (int)Math.ceil(
							((datos[0].length)/12)+((datos[0].length)% 12)*0.1
							);
					int num_col_data=datos[0].length;
					try{
						for(int x=0; x<num; x++){
							//int tam_horizontal= 12;
							int tam_horizontal= 12;
							String [][] tabla_tmp= new String[totalFilas][tam_horizontal];
							int medida=num_col_data;
							if(num_col_data>11){
								medida=x==0? 11: num_col_data-((12*x)-1)>12? 12: num_col_data-((12*x)-1);
							}
							
							int tam_cab=6;
							int poscab=6*x;
							int poscol= x==0? 0:(12*x)-1;
							//int corrimiento = x==0? 11
							for(int y=0; y<totalFilas;y++){
								if(y==0){
									System.arraycopy(cabecera[y],poscab, tabla_tmp[y], 0, tam_cab);
								}else if(y==1){
									System.arraycopy(cabecera[y], poscol, tabla_tmp[y], 0, Math.abs(medida));
								}else{
									System.arraycopy( datos[y-2], poscol, tabla_tmp[y], 0,  Math.abs(medida));
								}
							}
							ret.add(tabla_tmp);
						}
						
					}catch(Exception o){
						CLogger.write("4", CPdf.class, o);
					}
				}else{
					int num = (int)Math.ceil(
							((datos[0].length)/13)+((datos[0].length)% 13)*0.1
							);
					int num_col_data=datos[0].length;
					try{
						for(int x=0; x<num; x++){
							int tam_horizontal= 13;
							String [][] tabla_tmp= new String[totalFilas][tam_horizontal];
							int medida= num_col_data-(13*x)>13? 13: num_col_data-(13*x);
							for(int y=0; y<totalFilas;y++){
								if(y<2){
									System.arraycopy(cabecera[y],13*x, tabla_tmp[y], 0, medida);
								}else{
									System.arraycopy( datos[y-2], 13*x, tabla_tmp[y], 0, medida);
								}
							}
							ret.add(tabla_tmp);
						}
						
					}catch(Exception o){
						CLogger.write("5", CPdf.class, o);
					}
				}
			}
			
			
			return ret;
		}
		
		
		
		public String ExportarPdfEjecucionPresupuestaria(String [][]headers, String [][]datosMetas, int visualizacion){
			String path = "";
			tipo_reporte=1;
			try{	
			    String [] cabeceras = new String[headers[0].length];
			    System.arraycopy( headers[0], 0, cabeceras, 0, headers[0].length );
				String [][]cabeceras_fixed= configurarCabeceras(cabeceras,datosMetas[0],visualizacion);
				List <String[][]>tablas =divTablas(cabeceras_fixed,datosMetas,visualizacion);
				restante= visualizacion==2? (datosMetas[0].length % 12):(datosMetas[0].length % 13) ;
				PDFont font = PDType1Font.HELVETICA_BOLD;
				List<Float> altura= new ArrayList<Float>();
				for(int x=0;x<tablas.size();x++){
					page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
				    doc.addPage( page );
					PDPageContentStream contentStream = new PDPageContentStream(doc, page);
					
					if(x==0){
						contentStream.beginText();
						contentStream.setFont(font, 18);
						contentStream.newLineAtOffset(50, 550);
						contentStream.showText("Ministerio de Finanzas Publicas");
						contentStream.endText();
						contentStream.beginText();
						contentStream.setFont(font, 12);
						contentStream.newLineAtOffset(50, 530);
						contentStream.showText("Reporte: "+titulo);
						contentStream.endText();
					}
					
					
					float margin = 50;
					float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
					float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

					boolean drawContent = true;
					float bottomMargin = 70;
					
					String[][] tabla_tmp = tablas.get(x);
					BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
					boolean ultimo=x==tablas.size()-1;
					table_x.addHeaderRow(agregarCabecera(table_x,tabla_tmp[0],x,ultimo,visualizacion));
					Row<PDPage> row = table_x.createRow(12);
					
					table_x.addHeaderRow(
							agregarCabecera_pt2(row, tabla_tmp[1],x==0)
					);
					for(int i=2;i<tabla_tmp.length;i++){
						row= agregarFila(table_x,tabla_tmp[i], x==0,x==tablas.size());
					}
					if(x==0){
						altura=obtenerAlturas(table_x);
					}else{
						for(int i=0; i<table_x.getRows().size(); i++){
							table_x.getRows().get(i).setHeight(altura.get(i));
						}
					}
					table_x.draw();
					contentStream.close();
				}
			    path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
				FileOutputStream out = new FileOutputStream(new File(path));
				doc.save(out);
				doc.close();
			}catch(Exception o){
				CLogger.write("6", CPdf.class, o);
			}
			return path;
		}
		
		public String ExportarPdfAvanceActividades(String [][]headers, String[][]datos, String usuario){
			String path="";
			tipo_reporte=2;
			try{
				PDFont font = PDType1Font.HELVETICA_BOLD;
				page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.beginText();
				contentStream.setFont(font, 18);
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Publicas");
				contentStream.endText();
				contentStream.beginText();
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				float margin = 50;
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
				boolean drawContent = true;
				float bottomMargin = 70;
				BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				Row<PDPage> headerRow = table_x.createRow(12);
				float tam_celda=celda_b*1.5f;
				
				headerRow.createCell((float)(celda_a*1.5),headers[0][0] );
				headerRow.createCell(tam_celda,headers[0][1] );
				headerRow.createCell(tam_celda,headers[0][2] );
				headerRow.createCell(tam_celda,headers[0][3] );
				headerRow.createCell(tam_celda,headers[0][4] );
				headerRow.createCell(tam_celda,headers[0][5] );
				headerRow.createCell(tam_celda,headers[0][6] );
				headerRow.createCell(tam_celda,headers[0][7] );
				table_x.addHeaderRow(headerRow);
				
				for(int i=0; i<datos.length;i++){
					Row<PDPage> row = table_x.createRow(12);
					row.createCell((float)(celda_a*1.5),datos[i][0] );
					row.createCell(tam_celda,datos[i][1] );
					row.createCell(tam_celda,datos[i][2] );
					row.createCell(tam_celda,datos[i][3] );
					row.createCell(tam_celda,datos[i][4] );
					row.createCell(tam_celda,datos[i][5] );
					row.createCell(tam_celda,datos[i][6] );
					row.createCell(tam_celda,datos[i][7] );
				}
				table_x.draw();
				contentStream.close();
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("7", CPdf.class, o);
			}
			
			
			return path;
		}
		
		public String ExportarPdfCargaTrabajo(String [][]headers, String [][]datos, String usuario){
			String path ="";
			tipo_reporte=3;
			try{
				PDFont font = PDType1Font.HELVETICA_BOLD;
				page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.beginText();
				contentStream.setFont(font, 18);
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Publicas");
				contentStream.endText();
				contentStream.beginText();
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				float margin = 50;
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
				boolean drawContent = true;
				float bottomMargin = 70;
				BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				Row<PDPage> headerRow = table_x.createRow(12);
				
				headerRow.createCell((float)(celda_a*2),headers[0][0] );
				headerRow.createCell(celda_b*2,headers[0][1] );
				headerRow.createCell(celda_b*2,headers[0][2] );
				headerRow.createCell(celda_b*2,headers[0][3] );
				headerRow.createCell(celda_b*2,headers[0][4] );
				table_x.addHeaderRow(headerRow);
				
				for(int i=0; i<datos.length;i++){
					Row<PDPage> row = table_x.createRow(12);
					row.createCell((float)(celda_a*2),datos[i][0] );
					row.createCell(celda_b*2,datos[i][1] );
					row.createCell(celda_b*2,datos[i][2] );
					row.createCell(celda_b*2,datos[i][3] );
					row.createCell(celda_b*2,datos[i][4] );
				}
				table_x.draw();
				contentStream.close();
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("8", CPdf.class, o);
			}
			
			
			return path;
		}
		
		public String ExportarPdfAdministracionTransaccional	(String [][]headers, String [][]datos, String usuario){
			String path ="";
			tipo_reporte=4;
			try{
				PDFont font = PDType1Font.HELVETICA_BOLD;
				page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.beginText();
				contentStream.setFont(font, 18);
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Publicas");
				contentStream.endText();
				contentStream.beginText();
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				float margin = 50;
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
				boolean drawContent = true;
				float bottomMargin = 70;
				BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				Row<PDPage> headerRow = table_x.createRow(12);
				float tam_celda=celda_b*2.5f;
				headerRow.createCell((float)(celda_a*2.3f),headers[0][0] );
				headerRow.createCell(tam_celda,headers[0][1] );
				headerRow.createCell(tam_celda,headers[0][2] );
				headerRow.createCell(tam_celda,headers[0][3] );
				table_x.addHeaderRow(headerRow);
				
				for(int i=0; i<datos.length;i++){
					Row<PDPage> row = table_x.createRow(12);
					row.createCell((float)(celda_a*2.3f),datos[i][0] );
					row.createCell(tam_celda,datos[i][1] );
					row.createCell(tam_celda,datos[i][2] );
					row.createCell(tam_celda,datos[i][3] );
				}
				table_x.draw();
				contentStream.close();
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("9", CPdf.class, o);
			}
			
			
			return path;
		}
		
		public String ExportarPdfAdministracionTransaccionalDetalle	(String [][]headers, String [][]datos, String usuario){
			String path ="";
			tipo_reporte=4;
			try{
				PDFont font = PDType1Font.HELVETICA_BOLD;
				page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.beginText();
				contentStream.setFont(font, 18);
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Publicas");
				contentStream.endText();
				contentStream.beginText();
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				float margin = 50;
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
				boolean drawContent = true;
				float bottomMargin = 70;
				BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				Row<PDPage> headerRow = table_x.createRow(12);
				float tam_celda=celda_b*2.0f;
				headerRow.createCell(tam_celda,headers[0][0] );
				headerRow.createCell((float)(celda_a*2.3f),headers[0][1] );
				headerRow.createCell(tam_celda,headers[0][2] );
				headerRow.createCell(tam_celda,headers[0][3] );
				headerRow.createCell(tam_celda,headers[0][4] );
				table_x.addHeaderRow(headerRow);
				
				for(int i=0; i<datos.length;i++){
					Row<PDPage> row = table_x.createRow(12);
					row.createCell(tam_celda,datos[i][0] );
					row.createCell((float)(celda_a*2.3f),datos[i][1] );
					row.createCell(tam_celda,datos[i][2] );
					row.createCell(tam_celda,datos[i][3] );
					row.createCell(tam_celda,datos[i][4] );
				}
				table_x.draw();
				contentStream.close();
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("9", CPdf.class, o);
			}
			
			
			return path;
		}
		
		
		public String exportarMatrizRaci	(String [][]headers, String [][]datos, String usuario){
			String path ="";
			tipo_reporte=5;
			try{
				PDFont font = PDType1Font.HELVETICA_BOLD;
				page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.beginText();
				contentStream.setFont(font, 18);
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Publicas");
				contentStream.endText();
				contentStream.beginText();
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				float margin = 50;
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
				boolean drawContent = true;
				float bottomMargin = 70;
				BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				Row<PDPage> headerRow = table_x.createRow(12);
				float tam_celda=celda_b*1.7f;
				headerRow.createCell((float)(celda_a*2),headers[0][0] );
				if(headers[0].length>1)
					headerRow.createCell(tam_celda,headers[0][1] );
				if(headers[0].length>2)
					headerRow.createCell(tam_celda,headers[0][2] );
				if(headers[0].length>3)
					headerRow.createCell(tam_celda,headers[0][3] );
				if(headers[0].length>4)
					headerRow.createCell(tam_celda,headers[0][4] );
				if(headers[0].length>5)
					headerRow.createCell(tam_celda,headers[0][5] );
				table_x.addHeaderRow(headerRow);
				
				for(int i=0; i<datos.length;i++){
					Row<PDPage> row = table_x.createRow(12);
					row.createCell((float)(celda_a*2),datos[i][0] );
					if(datos[i].length>1)
						row.createCell(tam_celda,datos[i][1] );
					if(datos[i].length>2)
						row.createCell(tam_celda,datos[i][2] );
					if(datos[i].length>3)
						row.createCell(tam_celda,datos[i][3] );
					if(datos[i].length>4)
						row.createCell(tam_celda,datos[i][4] );
					if(datos[i].length>5)
						row.createCell(tam_celda,datos[i][5] );
				}
				table_x.draw();
				contentStream.close();
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("10", CPdf.class, o);
			}
			
			
			return path;
		}
		
		public String exportarDesembolsos(String [][]headers, String [][]datos, String usuario){
			String path ="";
			tipo_reporte=5;
			try{
				PDFont font = PDType1Font.HELVETICA_BOLD;
				page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
			    doc.addPage( page );
				PDPageContentStream contentStream = new PDPageContentStream(doc, page);
				contentStream.beginText();
				contentStream.setFont(font, 18);
				contentStream.newLineAtOffset(50, 550);
				contentStream.showText("Ministerio de Finanzas Publicas");
				contentStream.endText();
				contentStream.beginText();
				contentStream.setFont(font, 12);
				contentStream.newLineAtOffset(50, 530);
				contentStream.showText("Reporte: "+titulo);
				contentStream.endText();
				float margin = 50;
				float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
				float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
				boolean drawContent = true;
				float bottomMargin = 70;
				BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
				Row<PDPage> headerRow = table_x.createRow(12);
				float tam_celda=celda_b*1.05f;
				Cell<PDPage> cell;
				for(int i=0;i<headers[0].length;i++){					
					cell = headerRow.createCell(tam_celda,headers[0][i] );
					cell.setFontSize(7);
				}
				table_x.addHeaderRow(headerRow);
				
				for(int i=0; i<datos.length;i++){
					Row<PDPage> row = table_x.createRow(12);
					for(int j=0; j<datos[i].length;j++){
						cell = row.createCell(tam_celda,datos[i][j] );
						cell.setFontSize(7);
					}
				}
				table_x.draw();
				contentStream.close();
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("11", CPdf.class, o);
			}
			
			
			return path;
		}
		
		public String exportarPlanAdquisiciones(String [][]headers, String [][]datos, String usuario){
			String path ="";
			tipo_reporte=5;
			try{
				int num_col=datos[0].length-1;
			
				PDFont font = PDType1Font.HELVETICA_BOLD;
				int contador=0;
				List<Float> altura= new ArrayList<Float>();
				while(num_col>0){
					page = new PDPage(new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth()));
				    doc.addPage( page );
					PDPageContentStream contentStream = new PDPageContentStream(doc, page);
					if(contador==0){
						contentStream.beginText();
						contentStream.setFont(font, 18);
						contentStream.newLineAtOffset(50, 550);
						contentStream.showText("Ministerio de Finanzas Publicas");
						contentStream.endText();
						contentStream.beginText();
						contentStream.setFont(font, 12);
						contentStream.newLineAtOffset(50, 530);
						contentStream.showText("Reporte: "+titulo);
						contentStream.endText();
					}
					float margin = 50;
					float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
					float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
					boolean drawContent = true;
					float bottomMargin = 70;
					BaseTable table_x= new BaseTable(525, yStartNewPage, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
					Row<PDPage> headerRow = table_x.createRow(12);
					float tam_celda=celda_b*1.05f;
					Cell<PDPage> cell;
					if(contador==0){
						for(int i=0;i<8;i++){
							String st=i<7? " ":headers[0][i];
							tam_celda= i<7? tam_celda: tam_celda*2;
							if(i==0){
								cell = headerRow.createCell(celda_a*2,st );
							}else{
								cell = headerRow.createCell(tam_celda,st );
							}
						}
					}
					else{
						tam_celda=tam_celda*2;
						int control=1;
						for(int i=contador;i<headers[0].length;i++){	
							   if(control==1){
								   cell = headerRow.createCell(tam_celda,headers[0][i] );	
								   control++;
							   }else{
								   control=1;
							   }
						}
					}
					table_x.addHeaderRow(headerRow);
					Row<PDPage> headerRow_ = table_x.createRow(12);
					if(contador==0){
						int control=1;
						tam_celda=celda_b*1.05f;
						for(int i=0;i<9;i++){	
							if(i==0 && i<7){
								cell = headerRow_.createCell(celda_a*2,headers[0][i] );
							}else if(i<7){
								cell = headerRow_.createCell(tam_celda,headers[0][i] );
							}else{
								String etiqueta= control==1? "planificado": "real";
								control= control==1? control+1: 1;
								cell = headerRow_.createCell(tam_celda,etiqueta);
							}
							cell.setFontSize(7);
						}
						table_x.addHeaderRow(headerRow_);
					}
					else{
						int control=1;
						tam_celda=celda_b*1.05f;
						for(int i=contador;i<headers[0].length;i++){								
								String etiqueta= control==1? "planificado": "real";
								control= control==1? control+1: 1;
								cell = headerRow_.createCell(tam_celda,etiqueta);	
								cell.setFontSize(7);
						}
						table_x.addHeaderRow(headerRow_);
					}
					for(int x=0; x<datos.length;x++){
						Row<PDPage> row = table_x.createRow(12);
						if(contador==0){
							tam_celda=celda_b*1.05f;
							for(int i=0;i<9;i++){	
								if(i==0){
									cell = row.createCell(celda_a*2,datos[x][i] );
								}else{
									cell = row.createCell(tam_celda,datos[x][i] );
								}
								cell.setFontSize(7);
							}						
						}
						else{
							tam_celda=celda_b*1.05f;
							for(int i=contador;i<headers[0].length;i++){
									cell = row.createCell(tam_celda,datos[x][i] );	
									cell.setFontSize(7);
							}
						}
					}
					
					if(contador==0){
						altura=obtenerAlturas(table_x);
					}else{
						for(int i=0; i<table_x.getRows().size(); i++){
							table_x.getRows().get(i).setHeight(altura.get(i));
						}
					}
					table_x.draw();
					contentStream.close();
					num_col=contador==0?num_col-9:num_col-7;
					contador=contador==0? 9:contador+7;
				}				
				path = String.join("","/archivos/temporales/temp_",((Long) new Date().getTime()).toString(),".pdf");
					FileOutputStream out = new FileOutputStream(new File(path));
					doc.save(out);
					doc.close();
			}catch(Exception o){
				CLogger.write("12", CPdf.class, o);
			}
			
			
			return path;
		}
			
}

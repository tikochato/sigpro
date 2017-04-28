package servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import com.google.gson.GsonBuilder;


import dao.DocumentosAdjuntosDAO;
import pojo.Documento;

@WebServlet("/SDocumentosAdjuntos")
public class SDocumentosAdjuntos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SDocumentosAdjuntos() {
        super();
    }
    
    class datos {
    	int id;
    	String nombre;
    	String descripcion;
    	String extension;
    	int idTipoObjto;
    	int idObjeto;
    };

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setCharacterEncoding("UTF-8");
    	
		String accion = "";
		String descripcion = "";
		String response_text = "";
		Boolean esNuevo = false;	
		Integer objetoId = 0;
		Integer tipoObjetoId = 0;
		Integer idDocumento = 0;
		try {
			if (request.getContentType() != null && request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 ) {
				List<FileItem> parametros = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem parametro : parametros)
				{
					if (parametro.isFormField()){
						if (parametro.getFieldName().compareTo("accion")==0 && parametro.getString().length()>0){
							accion = parametro.getString();
						}else if (parametro.getFieldName().compareTo("descripcion")==0 && parametro.getString().length()>0){
							descripcion = parametro.getString();
						}else if (parametro.getFieldName().compareTo("esNuevo")==0 && parametro.getString().length()>0){
							esNuevo = Boolean.parseBoolean(parametro.getString());
						}else if (parametro.getFieldName().compareTo("idObjeto")==0 && parametro.getString().length()>0){
							objetoId = Integer.parseInt(parametro.getString());
						}else if(parametro.getFieldName().compareTo("idTipoObjeto")==0 && parametro.getString().length()>0){
							tipoObjetoId = Integer.parseInt(parametro.getString());
						}else if(parametro.getFieldName().compareTo("id")==0 && parametro.getString().length()>0){
							idDocumento = Integer.parseInt(parametro.getString());
						}
					}
				}
				
				if(accion!=null){
					Documento documentoAdjunto;
					Integer result = 0;
					if(accion.equals("agregarDocumento")){
						try {						
							if(esNuevo){
								String directorioTemporal = "/Archivos/Temporales/";
								if (objetoId != 0){
									directorioTemporal = directorioTemporal + objetoId+ "/";
								}
								if (tipoObjetoId != 0){
									directorioTemporal = directorioTemporal + tipoObjetoId + "/";
								}
															
								ArrayList<FileItem> fileItems=new ArrayList<FileItem>();
				
								File documento = null;
								
								for (FileItem parametro : parametros)
							    {
									if (!parametro.isFormField()){
										String nombreDocumento = parametro.getName();
										
										String tipoContenido = parametro.getName().substring(parametro.getName().indexOf('.') + 1);
										documentoAdjunto = new Documento(nombreDocumento,descripcion, tipoContenido, tipoObjetoId,objetoId);
											
										fileItems.add(parametro);
										File directorio = new File(directorioTemporal);
										
										if (!directorio.exists()){
											directorio.mkdirs();
										}
																				
										if(nombreDocumento.lastIndexOf("/") >= 0 ){
											documento = new File( directorio + 
								            nombreDocumento.substring(nombreDocumento.lastIndexOf("/"))) ;
								        }else{
								        	documento = new File( directorio + "/" + nombreDocumento) ;
								        }
										
										if(!documento.exists()){
											result = DocumentosAdjuntosDAO.guardarDocumentoAdjunto(documentoAdjunto);
											if (result > 0){
												parametro.write(documento);
												response_text = "{ \"success\": true, \"existe_archivo\": false, \"id\": \""+ result + "\",\"nombre\": \"" + nombreDocumento + "\",\"extension_archivo\": \"" + tipoContenido + "\" } ";
											}else
												response_text = "{ \"success\": false, \"existe_archivo\": false } ";
										}
										else{										
											response_text = "{ \"success\": false, \"existe_archivo\": true } ";
										}
									}
							    }
							}
							
							
							response.setHeader("Content-Encoding", "gzip");
							response.setCharacterEncoding("UTF-8");
							
							OutputStream output = response.getOutputStream();
							GZIPOutputStream gz = new GZIPOutputStream(output);
					        gz.write(response_text.getBytes("UTF-8"));
					        gz.close();
					        output.close();
						} catch (Throwable e){
							if (result > 0)
								DocumentosAdjuntosDAO.eliminarDocumentoAdjunto(result);
							response_text = "{ \"success\": false }";
						}
					}else if(accion.equals("getDocumentos")){
						try{
							List<Documento> documentos = DocumentosAdjuntosDAO.getDocumentos(objetoId,tipoObjetoId);
							
							response.setHeader("Content-Encoding", "gzip");
							response.setCharacterEncoding("UTF-8");
							
							List <datos> datos_ = new ArrayList<datos>();
							
							for (Documento documento : documentos){
								datos dato = new datos();
								dato.id = documento.getId();
								dato.nombre = documento.getNombre();
								dato.extension = documento.getExtension();
								dato.descripcion = documento.getDescripcion();
								datos_.add(dato);
							}
							
							
							response_text = new GsonBuilder().serializeNulls().create().toJson(datos_);
							response_text = String.join("", "\"documentos\":", response_text);
							response_text = String.join("", "{\"success\":true,", response_text, "}");
							
							
							response.setHeader("Content-Encoding", "gzip");
							response.setCharacterEncoding("UTF-8");
							
							OutputStream output = response.getOutputStream();
							GZIPOutputStream gz = new GZIPOutputStream(output);
					        gz.write(response_text.getBytes("UTF-8"));
					        gz.close();
					        output.close();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}else{
				
				accion = request.getParameter("accion")!=null ? request.getParameter("accion").toString() : "";
				idDocumento = request.getParameter("id")!= null ? Integer.parseInt(request.getParameter("id").toString()) : 0;
				if(accion.equals("getDescarga")){
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					BufferedInputStream fileIn = new BufferedInputStream(null);
					try{
						List<Documento> documento = DocumentosAdjuntosDAO.getDocumentoById(idDocumento);
						String directorioTemporal = "/Archivos/Temporales";
						
						for (Documento doc : documento){
							String filePath = directorioTemporal+"/"+doc.getIdObjeto()+"/"+doc.getIdTipoObjeto()+"/"+doc.getNombre();
							
							File file = new File(filePath);

							fileIn = new BufferedInputStream(new FileInputStream(file));
							
							
							response.setContentType(getServletContext().getMimeType(filePath));
							response.setHeader("Content-Disposition","attachment; filename=\""+ doc.getNombre() + "\"");
							
							if (file.length() <= Integer.MAX_VALUE)
							{
							  response.setContentLength((int)file.length());
							}
							else
							{
							  response.addHeader("Content-Length", Long.toString(file.length()));
							}
							
							response.setHeader("Expires:", "0");
							
							byte [] buffer = new byte[8192];
							for (int length = 0; (length = fileIn.read(buffer)) > 0;){
								out.write(buffer,0,length);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if (out != null)
							out.close();
						if (fileIn != null)
							fileIn.close();
					}
				}else if(accion.equals("eliminarDocumento")){
					List<Documento> documento = DocumentosAdjuntosDAO.eliminarDocumentoAdjunto(idDocumento);
					if (documento.size() > 0){
						String directorioTemporal = "/Archivos/Temporales";
						
						for (Documento doc : documento){
							File file = new File(directorioTemporal+"/"+doc.getIdObjeto()+"/"+doc.getIdTipoObjeto()+"/"+doc.getNombre());
							if (file.exists()){
								file.delete();
								
								response_text = "{\"success\":true}";

								response.setHeader("Content-Encoding", "gzip");
								response.setCharacterEncoding("UTF-8");
								
								OutputStream output = response.getOutputStream();
								GZIPOutputStream gz = new GZIPOutputStream(output);
						        gz.write(response_text.getBytes("UTF-8"));
						        gz.close();
						        output.close();
							}
							else{
								
								DocumentosAdjuntosDAO.guardarDocumentoAdjunto(documento.get(0));
								response_text = String.join("", "{\"success\":false,", response_text, "}");

								response.setHeader("Content-Encoding", "gzip");
								response.setCharacterEncoding("UTF-8");
								
								OutputStream output = response.getOutputStream();
								GZIPOutputStream gz = new GZIPOutputStream(output);
						        gz.write(response_text.getBytes("UTF-8"));
						        gz.close();
						        output.close();
							}
						}
						
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

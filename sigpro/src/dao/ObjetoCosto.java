package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class ObjetoCosto {
	String nombre;
	Integer objeto_id;
	int objeto_tipo;
	Integer nivel;
	DateTime fecha_inicial;
	DateTime fecha_final;
	DateTime fecha_inicial_real;
	DateTime fecha_final_real;
	Integer duracion;
	stanio[] anios; 
	Integer acumulacion_costoid;
	BigDecimal costo;
	BigDecimal totalPagos;
	Integer unidad_ejecutora;
	Integer entidad;
	Integer programa;
	Integer subprograma; 
	Integer proyecto;
	Integer actividad;
	Integer obra;
	Integer renglon;
	Integer geografico;
	String treePath;
	BigDecimal ejecutado = new BigDecimal(0);
	BigDecimal asignado = new BigDecimal(0);
	BigDecimal modificaciones = new BigDecimal(0);
	Integer avance_fisico = 0;
	Integer inversion_nueva = 0;
	transient ObjetoCosto parent;
	transient List<ObjetoCosto> children;
	
	public ObjetoCosto(String nombre, Integer objeto_id, int objeto_tipo, Integer nivel, DateTime fecha_inicial,
			DateTime fecha_final, DateTime fecha_inicial_real, DateTime fecha_final_real, Integer duracion, stanio[] anios, 
			Integer acumulacion_costoid, BigDecimal costo, BigDecimal totalPagos, 
			Integer unidad_ejecutora, Integer entidad, Integer avance_fisico, Integer inversion_nueva,
			Integer programa, Integer subprograma, Integer proyecto, Integer actividad, Integer obra, Integer renglon, 
			Integer geografico, String treePath) {
		super();
		this.nombre = nombre;
		this.objeto_id = objeto_id;
		this.objeto_tipo = objeto_tipo;
		this.nivel = nivel;
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
		this.fecha_inicial_real = fecha_inicial_real;
		this.fecha_final_real = fecha_final_real;
		this.duracion = duracion;
		this.anios = anios;
		this.acumulacion_costoid = acumulacion_costoid;
		this.costo = costo;
		this.totalPagos = totalPagos;
		this.programa = programa;
		this.subprograma = subprograma;
		this.proyecto = proyecto;
		this.actividad = actividad;
		this.obra = obra;
		this.renglon = renglon;
		this.geografico = geografico;
		this.treePath = treePath;
		this.unidad_ejecutora = unidad_ejecutora;
		this.entidad  = entidad;
		this.avance_fisico = avance_fisico;
		this.inversion_nueva = inversion_nueva;
		children = new ArrayList<ObjetoCosto>();
	}

	
	public void inicializarStanio (Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			for(int m=0; m<12; m++){
				temp.mes[m]= new stpresupuesto();
				temp.mes[m].planificado = new BigDecimal(0);
				temp.mes[m].real =  new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
	}
		
	public List<ObjetoCosto> getListado(ObjetoCosto nodo){
		List<ObjetoCosto> lstPrestamo = new ArrayList<>();
		lstPrestamo.add(nodo);
		if(nodo.getChildren()!=null && !nodo.getChildren().isEmpty()){
			for(int h=0; h<nodo.getChildren().size();h++){
				lstPrestamo.addAll((List<ObjetoCosto>)getListado(nodo.getChildren().get(h)));
			}
			
		}
		return lstPrestamo;
	}
	
	public class stpresupuesto{
		public BigDecimal planificado;
		public BigDecimal real;
	}
	
	public class stanio{
		public stpresupuesto[] mes = new stpresupuesto[12];
		public Integer anio;
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getObjeto_id() {
		return objeto_id;
	}

	public void setObjeto_id(Integer objeto_id) {
		this.objeto_id = objeto_id;
	}

	public int getObjeto_tipo() {
		return objeto_tipo;
	}

	public void setObjeto_tipo(int objeto_tipo) {
		this.objeto_tipo = objeto_tipo;
	}

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public DateTime getFecha_inicial() {
		return fecha_inicial;
	}

	public void setFecha_inicial(DateTime fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}

	public DateTime getFecha_final() {
		return fecha_final;
	}

	public void setFecha_final(DateTime fecha_final) {
		this.fecha_final = fecha_final;
	}
	
	public DateTime getFecha_inicial_real() {
		return fecha_inicial_real;
	}


	public void setFecha_inicial_real(DateTime fecha_inicial_real) {
		this.fecha_inicial_real = fecha_inicial_real;
	}


	public DateTime getFecha_final_real() {
		return fecha_final_real;
	}


	public void setFecha_final_real(DateTime fecha_final_real) {
		this.fecha_final_real = fecha_final_real;
	}


	public Integer getDuracion() {
		return duracion;
	}


	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}


	public stanio[] getAnios() {
		return anios;
	}

	public void setAnios(stanio[] anios) {
		this.anios = anios;
	}

	public Integer getAcumulacion_costoid() {
		return acumulacion_costoid;
	}

	public void setAcumulacion_costoid(Integer acumulacion_costoid) {
		this.acumulacion_costoid = acumulacion_costoid;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	
	public BigDecimal getTotalPagos() {
		return totalPagos;
	}

	public void setTotalPagos(BigDecimal totalPagos) {
		this.totalPagos = totalPagos;
	}

	public Integer getPrograma() {
		return programa;
	}

	public void setPrograma(Integer programa) {
		this.programa = programa;
	}

	public Integer getSubprograma() {
		return subprograma;
	}

	public void setSubprograma(Integer subprograma) {
		this.subprograma = subprograma;
	}

	public Integer getProyecto() {
		return proyecto;
	}

	public void setProyecto(Integer proyecto) {
		this.proyecto = proyecto;
	}

	public Integer getActividad() {
		return actividad;
	}

	public void setActividad(Integer actividad) {
		this.actividad = actividad;
	}

	public Integer getObra() {
		return obra;
	}

	public void setObra(Integer obra) {
		this.obra = obra;
	}
	
	public Integer getRenglon() {
		return renglon;
	}

	public void setRenglon(Integer renglon) {
		this.renglon = renglon;
	}
	
	public Integer getGeografico() {
		return geografico;
	}

	public void setGeografico(Integer geografico) {
		this.geografico = geografico;
	}
	
	public String getTreePath(){
		return treePath;
	}
	
	public void setTreePath(String treePath){
		this.treePath = treePath;
	}
	
	public ObjetoCosto getParent() {
		return parent;
	}

	public void setParent(ObjetoCosto parent) {
		this.parent = parent;
	}

	public List<ObjetoCosto> getChildren() {
		return children;
	}

	public void setChildren(List<ObjetoCosto> children) {
		this.children = children;
	}
	
	public void addChildren(ObjetoCosto children) {
		this.children.add(children);
	}


	public BigDecimal getEjecutado() {
		return ejecutado;
	}


	public void setEjecutado(BigDecimal ejecutado) {
		this.ejecutado = ejecutado;
	}


	public BigDecimal getAsignado() {
		return asignado;
	}


	public void setAsignado(BigDecimal asignado) {
		this.asignado = asignado;
	}


	public BigDecimal getModificaciones() {
		return modificaciones;
	}


	public void setModificaciones(BigDecimal modificaciones) {
		this.modificaciones = modificaciones;
	}


	public Integer getAvance_fisico() {
		return avance_fisico;
	}


	public void setAvance_fisico(Integer avance_fisico) {
		this.avance_fisico = avance_fisico;
	}


	public Integer getUnidad_ejecutora() {
		return unidad_ejecutora;
	}


	public void setUnidad_ejecutora(Integer unidad_ejecutora) {
		this.unidad_ejecutora = unidad_ejecutora;
	}


	public Integer getEntidad() {
		return entidad;
	}


	public void setEntidad(Integer entidad) {
		this.entidad = entidad;
	}


	public Integer getInversion_nueva() {
		return inversion_nueva;
	}


	public void setInversion_nueva(Integer inversion_nueva) {
		this.inversion_nueva = inversion_nueva;
	}
	
}

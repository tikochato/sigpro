package dao;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class ObjetoCostoJasper {

	String nombre;
	Integer objeto_id;
	int objeto_tipo;
	Integer nivel;
	DateTime fecha_inicial;
	DateTime fecha_final;
	Integer acumulacion_costoid;
	BigDecimal costo;
	BigDecimal totalPagos;
	Integer programa;
	Integer subprograma; 
	Integer proyecto;
	Integer actividad;
	Integer obra;
	Integer renglon;
	Integer geografico;
	String treePath;
	
	BigDecimal eneroP = new BigDecimal(0);
	BigDecimal febreroP = new BigDecimal(0);
	BigDecimal marzoP = new BigDecimal(0);
	BigDecimal abrilP = new BigDecimal(0);
	BigDecimal mayoP = new BigDecimal(0);
	BigDecimal junioP = new BigDecimal(0);
	BigDecimal julioP = new BigDecimal(0);
	BigDecimal agostoP = new BigDecimal(0);
	BigDecimal septiembreP = new BigDecimal(0);
	BigDecimal octubreP = new BigDecimal(0);
	BigDecimal noviembreP = new BigDecimal(0);
	BigDecimal diciembreP = new BigDecimal(0);
	
	BigDecimal eneroR = new BigDecimal(0);
	BigDecimal febreroR = new BigDecimal(0);
	BigDecimal marzoR = new BigDecimal(0);
	BigDecimal abrilR = new BigDecimal(0);
	BigDecimal mayoR = new BigDecimal(0);
	BigDecimal junioR = new BigDecimal(0);
	BigDecimal julioR = new BigDecimal(0);
	BigDecimal agostoR = new BigDecimal(0);
	BigDecimal septiembreR = new BigDecimal(0);
	BigDecimal octubreR = new BigDecimal(0);
	BigDecimal noviembreR = new BigDecimal(0);
	BigDecimal diciembreR = new BigDecimal(0);
	
	public ObjetoCostoJasper(String nombre, Integer objeto_id, int objeto_tipo, Integer nivel, DateTime fecha_inicial,
			DateTime fecha_final, Integer acumulacion_costoid, BigDecimal costo, BigDecimal totalPagos,
			Integer programa, Integer subprograma, Integer proyecto, Integer actividad, Integer obra, Integer renglon,
			Integer geografico, String treePath, BigDecimal eneroP, BigDecimal febreroP, BigDecimal marzoP,
			BigDecimal abrilP, BigDecimal mayoP, BigDecimal junioP, BigDecimal julioP, BigDecimal agostoP,
			BigDecimal septiembreP, BigDecimal octubreP, BigDecimal noviembreP, BigDecimal diciembreP,
			BigDecimal eneroR, BigDecimal febreroR, BigDecimal marzoR, BigDecimal abrilR, BigDecimal mayoR,
			BigDecimal junioR, BigDecimal julioR, BigDecimal agostoR, BigDecimal septiembreR, BigDecimal octubreR,
			BigDecimal noviembreR, BigDecimal diciembreR) {
		super();
		this.nombre = nombre;
		this.objeto_id = objeto_id;
		this.objeto_tipo = objeto_tipo;
		this.nivel = nivel;
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
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
		this.eneroP = eneroP;
		this.febreroP = febreroP;
		this.marzoP = marzoP;
		this.abrilP = abrilP;
		this.mayoP = mayoP;
		this.junioP = junioP;
		this.julioP = julioP;
		this.agostoP = agostoP;
		this.septiembreP = septiembreP;
		this.octubreP = octubreP;
		this.noviembreP = noviembreP;
		this.diciembreP = diciembreP;
		this.eneroR = eneroR;
		this.febreroR = febreroR;
		this.marzoR = marzoR;
		this.abrilR = abrilR;
		this.mayoR = mayoR;
		this.junioR = junioR;
		this.julioR = julioR;
		this.agostoR = agostoR;
		this.septiembreR = septiembreR;
		this.octubreR = octubreR;
		this.noviembreR = noviembreR;
		this.diciembreR = diciembreR;
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

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public BigDecimal getEneroP() {
		return eneroP;
	}

	public void setEneroP(BigDecimal eneroP) {
		this.eneroP = eneroP;
	}

	public BigDecimal getFebreroP() {
		return febreroP;
	}

	public void setFebreroP(BigDecimal febreroP) {
		this.febreroP = febreroP;
	}

	public BigDecimal getMarzoP() {
		return marzoP;
	}

	public void setMarzoP(BigDecimal marzoP) {
		this.marzoP = marzoP;
	}

	public BigDecimal getAbrilP() {
		return abrilP;
	}

	public void setAbrilP(BigDecimal abrilP) {
		this.abrilP = abrilP;
	}

	public BigDecimal getMayoP() {
		return mayoP;
	}

	public void setMayoP(BigDecimal mayoP) {
		this.mayoP = mayoP;
	}

	public BigDecimal getJunioP() {
		return junioP;
	}

	public void setJunioP(BigDecimal junioP) {
		this.junioP = junioP;
	}

	public BigDecimal getJulioP() {
		return julioP;
	}

	public void setJulioP(BigDecimal julioP) {
		this.julioP = julioP;
	}

	public BigDecimal getAgostoP() {
		return agostoP;
	}

	public void setAgostoP(BigDecimal agostoP) {
		this.agostoP = agostoP;
	}

	public BigDecimal getSeptiembreP() {
		return septiembreP;
	}

	public void setSeptiembreP(BigDecimal septiembreP) {
		this.septiembreP = septiembreP;
	}

	public BigDecimal getOctubreP() {
		return octubreP;
	}

	public void setOctubreP(BigDecimal octubreP) {
		this.octubreP = octubreP;
	}

	public BigDecimal getNoviembreP() {
		return noviembreP;
	}

	public void setNoviembreP(BigDecimal noviembreP) {
		this.noviembreP = noviembreP;
	}

	public BigDecimal getDiciembreP() {
		return diciembreP;
	}

	public void setDiciembreP(BigDecimal diciembreP) {
		this.diciembreP = diciembreP;
	}

	public BigDecimal getEneroR() {
		return eneroR;
	}

	public void setEneroR(BigDecimal eneroR) {
		this.eneroR = eneroR;
	}

	public BigDecimal getFebreroR() {
		return febreroR;
	}

	public void setFebreroR(BigDecimal febreroR) {
		this.febreroR = febreroR;
	}

	public BigDecimal getMarzoR() {
		return marzoR;
	}

	public void setMarzoR(BigDecimal marzoR) {
		this.marzoR = marzoR;
	}

	public BigDecimal getAbrilR() {
		return abrilR;
	}

	public void setAbrilR(BigDecimal abrilR) {
		this.abrilR = abrilR;
	}

	public BigDecimal getMayoR() {
		return mayoR;
	}

	public void setMayoR(BigDecimal mayoR) {
		this.mayoR = mayoR;
	}

	public BigDecimal getJunioR() {
		return junioR;
	}

	public void setJunioR(BigDecimal junioR) {
		this.junioR = junioR;
	}

	public BigDecimal getJulioR() {
		return julioR;
	}

	public void setJulioR(BigDecimal julioR) {
		this.julioR = julioR;
	}

	public BigDecimal getAgostoR() {
		return agostoR;
	}

	public void setAgostoR(BigDecimal agostoR) {
		this.agostoR = agostoR;
	}

	public BigDecimal getSeptiembreR() {
		return septiembreR;
	}

	public void setSeptiembreR(BigDecimal septiembreR) {
		this.septiembreR = septiembreR;
	}

	public BigDecimal getOctubreR() {
		return octubreR;
	}

	public void setOctubreR(BigDecimal octubreR) {
		this.octubreR = octubreR;
	}

	public BigDecimal getNoviembreR() {
		return noviembreR;
	}

	public void setNoviembreR(BigDecimal noviembreR) {
		this.noviembreR = noviembreR;
	}

	public BigDecimal getDiciembreR() {
		return diciembreR;
	}

	public void setDiciembreR(BigDecimal diciembreR) {
		this.diciembreR = diciembreR;
	}
	
}

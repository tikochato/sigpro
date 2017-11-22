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
	DateTime fecha_inicial_real;
	DateTime fecha_final_real;
	Integer duracion;
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
	
	BigDecimal totalP = new BigDecimal(0);
	BigDecimal totalR = new BigDecimal(0);
	
	public ObjetoCostoJasper(String nombre, Integer objeto_id, int objeto_tipo, Integer nivel, DateTime fecha_inicial,
			DateTime fecha_final, DateTime fecha_inicial_real, DateTime fecha_final_real, Integer duracion, 
			Integer acumulacion_costoid, BigDecimal costo, BigDecimal totalPagos,
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
		this.fecha_inicial_real = fecha_inicial_real;
		this.fecha_final_real = fecha_final_real;
		this.duracion = duracion;
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
		this.eneroP = eneroP!=null?eneroP:new BigDecimal(0);
		this.febreroP = febreroP!=null?febreroP:new BigDecimal(0);
		this.marzoP = marzoP!=null?marzoP:new BigDecimal(0);
		this.abrilP = abrilP!=null?abrilP:new BigDecimal(0);
		this.mayoP = mayoP!=null?mayoP:new BigDecimal(0);
		this.junioP = junioP!=null?junioP:new BigDecimal(0);
		this.julioP = julioP!=null?julioP:new BigDecimal(0);
		this.agostoP = agostoP!=null?agostoP:new BigDecimal(0);
		this.septiembreP = septiembreP!=null?septiembreP:new BigDecimal(0);
		this.octubreP = octubreP!=null?octubreP:new BigDecimal(0);
		this.noviembreP = noviembreP!=null?noviembreP:new BigDecimal(0);
		this.diciembreP = diciembreP!=null?diciembreP:new BigDecimal(0);
		this.eneroR = eneroR!=null?eneroR:new BigDecimal(0);
		this.febreroR = febreroR!=null?febreroR:new BigDecimal(0);
		this.marzoR = marzoR!=null?marzoR:new BigDecimal(0);
		this.abrilR = abrilR!=null?abrilR:new BigDecimal(0);
		this.mayoR = mayoR!=null?mayoR:new BigDecimal(0);
		this.junioR = junioR!=null?junioR:new BigDecimal(0);
		this.julioR = julioR!=null?julioR:new BigDecimal(0);
		this.agostoR = agostoR!=null?agostoR:new BigDecimal(0);
		this.septiembreR = septiembreR!=null?septiembreR:new BigDecimal(0);
		this.octubreR = octubreR!=null?octubreR:new BigDecimal(0);
		this.noviembreR = noviembreR!=null?noviembreR:new BigDecimal(0);
		this.diciembreR = diciembreR!=null?diciembreR:new BigDecimal(0);
		
		this.totalP=this.eneroP.add(this.febreroP.add(this.marzoP.add(this.abrilP.add(this.mayoP.add(this.junioP.add(this.julioP.add(this.agostoP.add(this.septiembreP.add(this.octubreP.add(this.noviembreP.add(this.diciembreP)))))))))));
		this.totalR=this.eneroR.add(this.febreroR.add(this.marzoR.add(this.abrilR.add(this.mayoR.add(this.junioR.add(this.julioR.add(this.agostoR.add(this.septiembreR.add(this.octubreR.add(this.noviembreR.add(this.diciembreR)))))))))));
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

	public BigDecimal getTotalP() {
		return totalP;
	}

	public void setTotalP(BigDecimal totalP) {
		this.totalP = totalP;
	}

	public BigDecimal getTotalR() {
		return totalR;
	}

	public void setTotalR(BigDecimal totalR) {
		this.totalR = totalR;
	}
	
}

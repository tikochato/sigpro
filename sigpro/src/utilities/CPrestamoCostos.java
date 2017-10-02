package utilities;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class CPrestamoCostos {
	String nombre;
	Integer objeto_id;
	int objeto_tipo;
	Integer nivel;
	DateTime fecha_inicial;
	DateTime fecha_final;
	stanio[] anios; 
	Integer acumulacion_costoid;
	BigDecimal costo;
	
	public class stpresupuesto{
		public BigDecimal planificado;
		public BigDecimal real;
	}
	
	public class stanio{
		public stpresupuesto[] mes = new stpresupuesto[12];
		public Integer anio;
		
	}

	public CPrestamoCostos(){
		
	}
	
	
	public CPrestamoCostos(String nombre, Integer objeto_id, int objeto_tipo, Integer nivel, DateTime fecha_inicial,
			DateTime fecha_final, stanio[] anios, Integer acumulacion_costoid, BigDecimal costo) {
		super();
		this.nombre = nombre;
		this.objeto_id = objeto_id;
		this.objeto_tipo = objeto_tipo;
		this.nivel = nivel;
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
		this.anios = anios;
		this.acumulacion_costoid = acumulacion_costoid;
		this.costo = costo;
	}
	
	public stanio[] inicializarStanio (Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
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
		return anios;
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
	
	
	
}

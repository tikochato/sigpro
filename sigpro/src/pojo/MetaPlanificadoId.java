package pojo;
// Generated Sep 25, 2017 8:25:07 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MetaPlanificadoId generated by hbm2java
 */
@Embeddable
public class MetaPlanificadoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3361974376338530882L;
	private int metaid;
	private int mes;
	private int ejercicio;

	public MetaPlanificadoId() {
	}

	public MetaPlanificadoId(int metaid, int mes, int ejercicio) {
		this.metaid = metaid;
		this.mes = mes;
		this.ejercicio = ejercicio;
	}

	@Column(name = "metaid", nullable = false)
	public int getMetaid() {
		return this.metaid;
	}

	public void setMetaid(int metaid) {
		this.metaid = metaid;
	}

	@Column(name = "mes", nullable = false)
	public int getMes() {
		return this.mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	@Column(name = "ejercicio", nullable = false)
	public int getEjercicio() {
		return this.ejercicio;
	}

	public void setEjercicio(int ejercicio) {
		this.ejercicio = ejercicio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MetaPlanificadoId))
			return false;
		MetaPlanificadoId castOther = (MetaPlanificadoId) other;

		return (this.getMetaid() == castOther.getMetaid()) && (this.getMes() == castOther.getMes())
				&& (this.getEjercicio() == castOther.getEjercicio());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getMetaid();
		result = 37 * result + this.getMes();
		result = 37 * result + this.getEjercicio();
		return result;
	}

}
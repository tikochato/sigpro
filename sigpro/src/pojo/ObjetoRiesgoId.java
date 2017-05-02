package pojo;
// Generated May 2, 2017 5:32:45 PM by Hibernate Tools 5.2.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ObjetoRiesgoId generated by hbm2java
 */
@Embeddable
public class ObjetoRiesgoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 160266522550641048L;
	private int riesgoid;
	private int objetoId;
	private int objetoTipo;

	public ObjetoRiesgoId() {
	}

	public ObjetoRiesgoId(int riesgoid, int objetoId, int objetoTipo) {
		this.riesgoid = riesgoid;
		this.objetoId = objetoId;
		this.objetoTipo = objetoTipo;
	}

	@Column(name = "riesgoid", nullable = false)
	public int getRiesgoid() {
		return this.riesgoid;
	}

	public void setRiesgoid(int riesgoid) {
		this.riesgoid = riesgoid;
	}

	@Column(name = "objeto_id", nullable = false)
	public int getObjetoId() {
		return this.objetoId;
	}

	public void setObjetoId(int objetoId) {
		this.objetoId = objetoId;
	}

	@Column(name = "objeto_tipo", nullable = false)
	public int getObjetoTipo() {
		return this.objetoTipo;
	}

	public void setObjetoTipo(int objetoTipo) {
		this.objetoTipo = objetoTipo;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ObjetoRiesgoId))
			return false;
		ObjetoRiesgoId castOther = (ObjetoRiesgoId) other;

		return (this.getRiesgoid() == castOther.getRiesgoid()) && (this.getObjetoId() == castOther.getObjetoId())
				&& (this.getObjetoTipo() == castOther.getObjetoTipo());
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRiesgoid();
		result = 37 * result + this.getObjetoId();
		result = 37 * result + this.getObjetoTipo();
		return result;
	}

}

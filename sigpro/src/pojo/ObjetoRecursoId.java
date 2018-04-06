package pojo;
// Generated 5/04/2018 04:15:52 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ObjetoRecursoId generated by hbm2java
 */
@Embeddable
public class ObjetoRecursoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2710107472232022626L;
	private int recursoid;
	private int objetoid;
	private int objetoTipo;

	public ObjetoRecursoId() {
	}

	public ObjetoRecursoId(int recursoid, int objetoid, int objetoTipo) {
		this.recursoid = recursoid;
		this.objetoid = objetoid;
		this.objetoTipo = objetoTipo;
	}

	@Column(name = "recursoid", nullable = false)
	public int getRecursoid() {
		return this.recursoid;
	}

	public void setRecursoid(int recursoid) {
		this.recursoid = recursoid;
	}

	@Column(name = "objetoid", nullable = false)
	public int getObjetoid() {
		return this.objetoid;
	}

	public void setObjetoid(int objetoid) {
		this.objetoid = objetoid;
	}

	@Column(name = "objeto_tipo", nullable = false)
	public int getObjetoTipo() {
		return this.objetoTipo;
	}

	public void setObjetoTipo(int objetoTipo) {
		this.objetoTipo = objetoTipo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ObjetoRecursoId))
			return false;
		ObjetoRecursoId castOther = (ObjetoRecursoId) other;

		return (this.getRecursoid() == castOther.getRecursoid()) && (this.getObjetoid() == castOther.getObjetoid())
				&& (this.getObjetoTipo() == castOther.getObjetoTipo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRecursoid();
		result = 37 * result + this.getObjetoid();
		result = 37 * result + this.getObjetoTipo();
		return result;
	}

}

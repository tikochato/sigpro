package pojo;
// Generated Dec 13, 2017 9:28:15 AM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ProyectoPropiedadValorId generated by hbm2java
 */
@Embeddable
public class ProyectoPropiedadValorId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2825946547113196267L;
	private int proyectoid;
	private int proyectoPropiedadid;

	public ProyectoPropiedadValorId() {
	}

	public ProyectoPropiedadValorId(int proyectoid, int proyectoPropiedadid) {
		this.proyectoid = proyectoid;
		this.proyectoPropiedadid = proyectoPropiedadid;
	}

	@Column(name = "proyectoid", nullable = false)
	public int getProyectoid() {
		return this.proyectoid;
	}

	public void setProyectoid(int proyectoid) {
		this.proyectoid = proyectoid;
	}

	@Column(name = "proyecto_propiedadid", nullable = false)
	public int getProyectoPropiedadid() {
		return this.proyectoPropiedadid;
	}

	public void setProyectoPropiedadid(int proyectoPropiedadid) {
		this.proyectoPropiedadid = proyectoPropiedadid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ProyectoPropiedadValorId))
			return false;
		ProyectoPropiedadValorId castOther = (ProyectoPropiedadValorId) other;

		return (this.getProyectoid() == castOther.getProyectoid())
				&& (this.getProyectoPropiedadid() == castOther.getProyectoPropiedadid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getProyectoid();
		result = 37 * result + this.getProyectoPropiedadid();
		return result;
	}

}

package pojo;
// Generated Dec 28, 2016 1:25:08 PM by Hibernate Tools 5.2.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ActividadPropiedadValorId generated by hbm2java
 */
@Embeddable
public class ActividadPropiedadValorId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1806577738966142949L;
	private int actividadid;
	private int actividadPropiedadid;

	public ActividadPropiedadValorId() {
	}

	public ActividadPropiedadValorId(int actividadid, int actividadPropiedadid) {
		this.actividadid = actividadid;
		this.actividadPropiedadid = actividadPropiedadid;
	}

	@Column(name = "actividadid", nullable = false)
	public int getActividadid() {
		return this.actividadid;
	}

	public void setActividadid(int actividadid) {
		this.actividadid = actividadid;
	}

	@Column(name = "actividad_propiedadid", nullable = false)
	public int getActividadPropiedadid() {
		return this.actividadPropiedadid;
	}

	public void setActividadPropiedadid(int actividadPropiedadid) {
		this.actividadPropiedadid = actividadPropiedadid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ActividadPropiedadValorId))
			return false;
		ActividadPropiedadValorId castOther = (ActividadPropiedadValorId) other;

		return (this.getActividadid() == castOther.getActividadid())
				&& (this.getActividadPropiedadid() == castOther.getActividadPropiedadid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getActividadid();
		result = 37 * result + this.getActividadPropiedadid();
		return result;
	}

}
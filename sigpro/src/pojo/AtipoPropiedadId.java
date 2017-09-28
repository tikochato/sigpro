package pojo;
// Generated Sep 28, 2017 10:43:48 AM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AtipoPropiedadId generated by hbm2java
 */
@Embeddable
public class AtipoPropiedadId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 528264415066561073L;
	private int actividadTipoid;
	private int actividadPropiedadid;

	public AtipoPropiedadId() {
	}

	public AtipoPropiedadId(int actividadTipoid, int actividadPropiedadid) {
		this.actividadTipoid = actividadTipoid;
		this.actividadPropiedadid = actividadPropiedadid;
	}

	@Column(name = "actividad_tipoid", nullable = false)
	public int getActividadTipoid() {
		return this.actividadTipoid;
	}

	public void setActividadTipoid(int actividadTipoid) {
		this.actividadTipoid = actividadTipoid;
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
		if (!(other instanceof AtipoPropiedadId))
			return false;
		AtipoPropiedadId castOther = (AtipoPropiedadId) other;

		return (this.getActividadTipoid() == castOther.getActividadTipoid())
				&& (this.getActividadPropiedadid() == castOther.getActividadPropiedadid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getActividadTipoid();
		result = 37 * result + this.getActividadPropiedadid();
		return result;
	}

}

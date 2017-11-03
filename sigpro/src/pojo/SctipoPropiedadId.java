package pojo;
// Generated Nov 2, 2017 6:40:52 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SctipoPropiedadId generated by hbm2java
 */
@Embeddable
public class SctipoPropiedadId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6241570781987844166L;
	private int subcomponenteTipoid;
	private int subcomponentePropiedadid;

	public SctipoPropiedadId() {
	}

	public SctipoPropiedadId(int subcomponenteTipoid, int subcomponentePropiedadid) {
		this.subcomponenteTipoid = subcomponenteTipoid;
		this.subcomponentePropiedadid = subcomponentePropiedadid;
	}

	@Column(name = "subcomponente_tipoid", nullable = false)
	public int getSubcomponenteTipoid() {
		return this.subcomponenteTipoid;
	}

	public void setSubcomponenteTipoid(int subcomponenteTipoid) {
		this.subcomponenteTipoid = subcomponenteTipoid;
	}

	@Column(name = "subcomponente_propiedadid", nullable = false)
	public int getSubcomponentePropiedadid() {
		return this.subcomponentePropiedadid;
	}

	public void setSubcomponentePropiedadid(int subcomponentePropiedadid) {
		this.subcomponentePropiedadid = subcomponentePropiedadid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SctipoPropiedadId))
			return false;
		SctipoPropiedadId castOther = (SctipoPropiedadId) other;

		return (this.getSubcomponenteTipoid() == castOther.getSubcomponenteTipoid())
				&& (this.getSubcomponentePropiedadid() == castOther.getSubcomponentePropiedadid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSubcomponenteTipoid();
		result = 37 * result + this.getSubcomponentePropiedadid();
		return result;
	}

}

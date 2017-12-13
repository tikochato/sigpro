package pojo;
// Generated Dec 12, 2017 12:13:39 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SubcomponentePropiedadValorId generated by hbm2java
 */
@Embeddable
public class SubcomponentePropiedadValorId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3302496374168046872L;
	private int subcomponenteid;
	private int subcomponentePropiedadid;

	public SubcomponentePropiedadValorId() {
	}

	public SubcomponentePropiedadValorId(int subcomponenteid, int subcomponentePropiedadid) {
		this.subcomponenteid = subcomponenteid;
		this.subcomponentePropiedadid = subcomponentePropiedadid;
	}

	@Column(name = "subcomponenteid", nullable = false)
	public int getSubcomponenteid() {
		return this.subcomponenteid;
	}

	public void setSubcomponenteid(int subcomponenteid) {
		this.subcomponenteid = subcomponenteid;
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
		if (!(other instanceof SubcomponentePropiedadValorId))
			return false;
		SubcomponentePropiedadValorId castOther = (SubcomponentePropiedadValorId) other;

		return (this.getSubcomponenteid() == castOther.getSubcomponenteid())
				&& (this.getSubcomponentePropiedadid() == castOther.getSubcomponentePropiedadid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSubcomponenteid();
		result = 37 * result + this.getSubcomponentePropiedadid();
		return result;
	}

}

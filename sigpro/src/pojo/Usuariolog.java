package pojo;
// Generated 20/12/2016 11:26:44 AM by Hibernate Tools 5.2.0.Beta1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Usuariolog generated by hbm2java
 */
@Entity
@Table(name = "usuariolog", catalog = "sigpro")
public class Usuariolog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5762512060655483094L;
	private UsuariologId id;

	public Usuariolog() {
	}

	public Usuariolog(UsuariologId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "usuario", column = @Column(name = "usuario", nullable = false, length = 32)),
			@AttributeOverride(name = "fecha", column = @Column(name = "fecha", nullable = false, length = 19)) })
	public UsuariologId getId() {
		return this.id;
	}

	public void setId(UsuariologId id) {
		this.id = id;
	}

}
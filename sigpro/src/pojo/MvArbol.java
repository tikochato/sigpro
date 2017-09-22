package pojo;
// Generated Sep 22, 2017 5:37:23 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * MvArbol generated by hbm2java
 */
@Entity
@Table(name = "mv_arbol", catalog = "sipro")
public class MvArbol implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6739285975892030110L;
	private MvArbolId id;

	public MvArbol() {
	}

	public MvArbol(MvArbolId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "prestamo", column = @Column(name = "prestamo", nullable = false)),
			@AttributeOverride(name = "componente", column = @Column(name = "componente", nullable = false)),
			@AttributeOverride(name = "producto", column = @Column(name = "producto", nullable = false)),
			@AttributeOverride(name = "subproducto", column = @Column(name = "subproducto", nullable = false)),
			@AttributeOverride(name = "level", column = @Column(name = "level", nullable = false)),
			@AttributeOverride(name = "actividad", column = @Column(name = "actividad", nullable = false)),
			@AttributeOverride(name = "treelevel", column = @Column(name = "treelevel", nullable = false)),
			@AttributeOverride(name = "treepath", column = @Column(name = "treepath", nullable = false)),
			@AttributeOverride(name = "fechaInicio", column = @Column(name = "fecha_inicio", nullable = false)) })
	public MvArbolId getId() {
		return this.id;
	}

	public void setId(MvArbolId id) {
		this.id = id;
	}

}

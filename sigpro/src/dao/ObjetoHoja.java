package dao;

public class ObjetoHoja {
	Integer objetoTipoHoja;
	Integer objetoTipoPadre;
	Object padre;
	Object hoja;
	
	public ObjetoHoja(Integer objetoTipoHoja, Object hoja, Integer objetoTipoPadre, Object padre){
		super();
		this.objetoTipoHoja = objetoTipoHoja;
		this.hoja = hoja;
		this.objetoTipoPadre = objetoTipoPadre;
		this.padre = padre;
	}
	
	public void setObjetoTipoHoja(Integer objetoTipo) {
		this.objetoTipoHoja = objetoTipo;
	}
	
	public Integer getObjetoTipoHoja(){
		return objetoTipoHoja;
	}
	
	public void setHoja(Object hoja){
		this.hoja = hoja;
	}
	
	public Object getHoja(){
		return hoja;
	}
	
	public void setObjetoTipoPadre(Integer objetoTipoPadre) {
		this.objetoTipoPadre = objetoTipoPadre;
	}
	
	public Integer getObjetoTipoPadre(){
		return objetoTipoPadre;
	}
	
	public void setPadre(Object padre){
		this.padre = padre;
	}
	
	public Object getPadre(){
		return padre;
	}
}

package dao;

import static org.junit.Assert.*;

import java.util.HashSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Test;
import pojo.Desembolso;
import pojo.DesembolsoTipo;
import utilities.CHibernateSession;

public class DesembolsoTipoDAOTest {
	
	@Test
	public void getTotalDesenbolseTipoTest(){
		assertNotNull(DesembolsoTipoDAO.getTotalDesembolsoTipo());
	}
	
	@Test
	public void getDesembolosTipoPorIdTest(){
		assertEquals(DesembolsoTipoDAO.getDesembolosTipoPorId(1).getClass(),DesembolsoTipo.class);
	}
	
	@Test
	public void guardarDesembolsoTipoTest(){
		DesembolsoTipo desembolsoTipo = new DesembolsoTipo("unit_test", "unit_test",1, new HashSet<Desembolso>(0));
		assertNotNull(DesembolsoTipoDAO.guardarDesembolsoTipo(desembolsoTipo));
	}
	
	@Test
	public void eliminarDesembolsoTipoTest(){
		DesembolsoTipo desembolsoTipo = new DesembolsoTipo("unit_test", "unit_test",1, new HashSet<Desembolso>(0));
		assertNotNull(DesembolsoTipoDAO.eliminarDesembolsoTipo(desembolsoTipo));
	}
	
	@Test
	public void  getDesembolsoTiposPagina(){
		assertNotNull(DesembolsoTipoDAO.geDesembolsoTiposPagina(1,1));
	}
	
	@AfterClass
	public static void cleanData(){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DesembolsoTipo> criteria = builder.createQuery(DesembolsoTipo.class);
			Root<DesembolsoTipo> root = criteria.from(DesembolsoTipo.class);
			criteria.where( builder.and(builder.equal(root.get("nombre"), "unit_test")));
			DesembolsoTipo desembolsoTipoTmp =(DesembolsoTipo) session.createQuery( criteria ).getSingleResult();
			session.beginTransaction();
			session.delete(desembolsoTipoTmp);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
}

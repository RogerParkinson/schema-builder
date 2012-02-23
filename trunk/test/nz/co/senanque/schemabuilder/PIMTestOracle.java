/**
 * 
 */
package nz.co.senanque.schemabuilder;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.example.sandbox.Content;
import org.example.sandbox.ContentGroup;
import org.example.sandbox.ContentGroupContent;
import org.example.sandbox.PoPoReln;
import org.example.sandbox.PriceFacet;
import org.example.sandbox.ProdCatProp;
import org.example.sandbox.ProdCatPropGroup;
import org.example.sandbox.ProdCatalog;
import org.example.sandbox.ProdOfferBundleComponent;
import org.example.sandbox.ProdOfferPrice;
import org.example.sandbox.ProductOffering;
import org.example.sandbox.Prop;
import org.example.sandbox.PropGroup;
import org.example.sandbox.PropGroupProp;
import org.example.sandbox.SaAdvisor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scenario includes a PSC in the properties
 * 
 * @author Roger Parkinson
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class PIMTestOracle {
	
	private static Logger logger = LoggerFactory.getLogger(PIMTestOracle.class);
	
	private EntityManager m_entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.m_entityManager = entityManager;
	}
	
	@Test
	public void scenario0() {
		EntityManager em = getEntityManager();
		em.close();
	}

	public EntityManager getEntityManager() {
		return m_entityManager;
	}
}

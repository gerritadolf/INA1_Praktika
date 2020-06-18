package de.ina.p5;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

/**
 * Diese Klasse dient zur Vereinfachung der Nutzung von Hibernate ORM.
 * Sie bietet rudimentäre Funktionen für das Management einer SessionFactory
 * sowie für einfaches Schreiben/Lesen in die/aus der Datenbank.
 * 
 * Diese Klasse ist neu und nur ein einem Projekt getestet.
 * Zudem ist sie nicht auf Laufzeit oder Threadsicherheit optimiert.
 * 
 * Sie dient zu Lehrzwecken und kann nach Belieben verändert werden.
 * Verbesserungsvorschläge und Erweiterungen sind willkommen!
 * 
 * Kompatibel mit Hibernate ORM 5.3, Java 8
 *
 * @author		Martin Schulten
 * @version		0.1
 */
public class PersistenceUtil<BeanType> {

	private static SessionFactory sessionFactory;

	/**
     * Liest Konfiguration aus hibernate.cfg.xml und erstellt damit eine SessionFactory.
     *
     * @return	SessionFactory
     */
	private SessionFactory createSessionFactory() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy(registry);
		}
		return sessionFactory;
	}

    /**
     * Getter für SessionFactory.
     *
     * @return	SessionFactory
     */
	private SessionFactory getSessionFactory() {
		return createSessionFactory();
	}

    /**
     * Schließt SessionFactory. Sollte abschließend aufgerufen werden,
     * da sonst Memory/Resource Leaks entstehen können. 
     */
	private void closeSessionFactory() {
		if (sessionFactory != null)
			sessionFactory.close();
	}

    /**
     * Speichert oder aktualisiert ein Obkjekt in der Datenbank.
     *
     * @param <T>	Typparameter für Bean
     * @param bean	Bean-Instanz
     */
	public void saveOrUpdate(BeanType bean) {
		Session hib_session = getSessionFactory().getCurrentSession();
		hib_session.beginTransaction();
		
		hib_session.saveOrUpdate(bean);
		
		hib_session.getTransaction().commit();
		//hib_session.close();
		//closeSessionFactory();
	}

    /**
     * Lädt alle Beans eines bestimmten Typs aus der Datenbank.
     * <pre>{@code
     * Aufrufbeispiel: List<MyBean> dbresults = hibUtil.<MyBean>obtainAll(MyBean.class);
     * }</pre>
     *
     * @param <BeanType>		Klasse des Beans
     * @param beanTypeClass		Klassenbeschreibungsobjekt .class() der Bean-Klasse
     * @return          		Liste der Beans
     */
	public List<BeanType> obtainAll(Class<BeanType> beanTypeClass) {
		Session hib_session = getSessionFactory().getCurrentSession();
		hib_session.beginTransaction();

//		CriteriaBuilder builder = hib_session.getCriteriaBuilder();
//		CriteriaQuery<BeanType> query = builder.createQuery(beanTypeClass);
//		Root<BeanType> root = query.from(beanTypeClass);
//		query.select(root);
//		List<BeanType> dbresults = hib_session.createQuery(query).getResultList();

		// Alternativ mit HQL
		List<BeanType> dbresults = hib_session.createQuery( "from " + beanTypeClass.getName() ).list();
		
		hib_session.getTransaction().commit();
		hib_session.close();
		closeSessionFactory();
		return dbresults;
	}

    /**
     * Lädt alle Beans eines bestimmten Typs aus der Datenbank, die die "where"-Bedingung erfüllen.
     * <pre>{@code
     * Aufrufbeispiel:  List<MyBean> dbresults =
     * 					hibUtil.<MyBean>obtainWhere(MyBean.class, "vorname", String.class, "Max");
     * }</pre>
     * @param <BeanType>		Klasse des Beans
     * @param beanTypeClass		Klassenbeschreibungsobjekt .class() der Bean-Klasse
     * @param propertyName		Bezeichnung eines Propertys des Beans
     * @param propertyClass		Klassenbeschreibungsobjekt (.class) der Klasse eines Propertys des Beans
     * @param propertyValue		Wert des Propertys des Beans, mit dem selektiert werden soll
     * @return          		Liste der Beans, die die Bedingung erfüllen
     */
	public List<BeanType> obtainWhere(Class<BeanType> beanTypeClass, String propertyName, Class<?> propertyClass, Object propertyValue) {

		Session hib_session = getSessionFactory().getCurrentSession();
		hib_session.beginTransaction();

//		CriteriaBuilder builder = hib_session.getCriteriaBuilder();
//		CriteriaQuery<BeanType> query = builder.createQuery(beanTypeClass);
//		Root<BeanType> root = query.from(beanTypeClass);
//		query.select(root).where(builder.equal(root.get(propertyName), propertyClass.cast(propertyValue)));
//		// Es können weitere Filter angehängt werden .where(...);
//		Query<BeanType> q = hib_session.createQuery(query);
//		List<BeanType> dbresults = q.getResultList();

		// Alternativ mit HQL
		List<BeanType> dbresults = hib_session.createQuery( "from " + beanTypeClass.getName() + " where " + propertyName + " = '" + propertyValue + "'").list();
		
		hib_session.getTransaction().commit();
		hib_session.close();
		closeSessionFactory();
		return dbresults;
	}
}
package com.bhk.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import com.bhk.bean.Student;
import com.bhk.config.HibernateUtil;

class StudentDAOTest {

	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	@Mock
	StudentDAO studentDao;

	@Mock
	HibernateUtil hibernateUtil;

	
	@ParameterizedTest
	@MethodSource("setConfigure")
	void testStudenDAO(SessionFactory sessionFactory) {
		when(hibernateUtil.getSessionFactory()).thenReturn(sessionFactory);

		Student student = new Student(1, "Ajit Sharma");
		assertEquals(true, studentDao.addStudent(student));

	}

	static Stream<Arguments> setConfigure() {
		SessionFactory sessionFactory;
		Configuration configuration = new Configuration();
		// Hibernate settings equivalent to hibernate.cfg.xml's properties
		Properties settings = new Properties();
		settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		settings.put(Environment.URL, "jdbc:h2:mem:testdb");
		settings.put(Environment.USER, "");
		settings.put(Environment.PASS, "");
		settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		settings.put(Environment.SHOW_SQL, "true");
		settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		settings.put(Environment.HBM2DDL_AUTO, "create-drop");
		configuration.setProperties(settings);
		configuration.addAnnotatedClass(Student.class);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return Stream.of(Arguments.of(sessionFactory));
	}

	@BeforeAll
	public static void StudentDAOTest() {
		emf = Persistence.createEntityManagerFactory("mnf-pu-test");
		em = emf.createEntityManager();
		Session session = em.unwrap(Session.class);
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				try {
					File script = new File(getClass().getResource("/data.sql").getFile());
					RunScript.execute(connection, new FileReader(script));
				} catch (FileNotFoundException e) {
					throw new RuntimeException("could not initialize with script");
				}
			}
		});
	}

	@AfterAll
	public static void tearDown() {
		em.clear();
		em.close();
		emf.close();
	}

}

package com.bhk.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import com.bhk.bean.Student;
import com.bhk.config.HibernateUtil;

@TestMethodOrder(OrderAnnotation.class)
class StudentDAOTest {
  
  HibernateUtil hibernateUtil = Mockito.mock(HibernateUtil.class);
  
  StudentDAO studentDao;

  public static SessionFactory sessionFactory = null;

  @BeforeAll
  public static void makeSession() {
    Configuration configuration = new Configuration();
    Properties settings = new Properties();
    settings.put(Environment.DRIVER, "org.h2.Driver");
    settings.put(Environment.URL, "jdbc:h2:mem:test");
    settings.put(Environment.USER, "sa");
    settings.put(Environment.PASS, "");
    settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
    settings.put(Environment.SHOW_SQL, "true");
    settings.put(Environment.HBM2DDL_AUTO, "create-drop");
    configuration.setProperties(settings);
    configuration.addAnnotatedClass(Student.class);
    ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
  }

  @Test
  @Order(1)
  void studenAddTest() {
    when(hibernateUtil.getSessionFactory()).thenReturn(sessionFactory);

    studentDao = new StudentDAO(hibernateUtil);
    Student student = new Student(1, "Ajit Sharma");
    assertEquals(true, studentDao.addStudent(student));
  }

  @Test
  @Order(2)
  void studenGetTest() {
    when(hibernateUtil.getSessionFactory()).thenReturn(sessionFactory);

    studentDao = new StudentDAO(hibernateUtil);
    List<Student> studentList = studentDao.getStudents();
    assertEquals(1, studentList.size());
  }
}

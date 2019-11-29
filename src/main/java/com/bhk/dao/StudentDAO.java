package com.bhk.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.bhk.bean.Student;
import com.bhk.config.HibernateUtil;

public class StudentDAO {
  private Student student;
  private Transaction transaction = null;
  HibernateUtil hibernateUtil;
  
  public StudentDAO(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }

  public boolean addStudent(Student student) {
    try (Session session = hibernateUtil.getSessionFactory().openSession()) {
      // start a transaction
      transaction = session.beginTransaction();
      // save the student object
      session.save(student);
     
      // commit transaction
      transaction.commit();
      return true;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
      return false;
    }
  }

  public List<Student> getStudents() {
    try (Session session = hibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("from Student", Student.class).list();
    }
  }
}

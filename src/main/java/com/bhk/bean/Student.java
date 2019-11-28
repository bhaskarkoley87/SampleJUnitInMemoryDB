package com.bhk.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT")
public class Student implements Serializable 
{   
    private static final long serialVersionUID = -1798070786993154676L;

	@Id
	Integer rollNo;
	String name;
	
	public int getRollNo() {
		return rollNo;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Student(int rollNo, String name) {		
		this.rollNo = rollNo;
		this.name = name;
	}
	
	public Student() {
		
	}
}

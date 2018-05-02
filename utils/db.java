/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.utils;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import sis.config;
import sis.utils.Exceptions.*;
/**
 *
 * @author lucifer
 */
public class db {
    protected Connection con = null;
    protected config c = new config();
        
    private String CreateStudentTableSQL = "CREATE TABLE IF NOT EXISTS students("
            + "id SERIAL PRIMARY KEY,"
            + "name VARCHAR(255) NOT NULL,"
            + "enrollment VARCHAR(12) NOT NULL UNIQUE,"
            + "dob DATE NOT NULL"
            + ");";
    
    protected String AddNewStudentSQL = "INSERT INTO students(name, enrollment, dob) VALUES(?, ?, ?) RETURNING id;";
    
    public boolean connect() throws DatabaseException{
        if(isConnected()){
            return true;
        }
        try{
            con = DriverManager.getConnection(getConnectionString(), c.DatabaseUser, c.DatabasePass);
            System.out.println("Connected to Database!");
            try{
                initialize();
                return true;
            }catch(DatabaseInitializationException e){
                con.close();
                con =  null;
                throw e;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }
    
    private String getConnectionString(){
        return "jdbc:postgresql://"+c.DatabaseHost+":"+c.DatabasePort+"/"+c.DatabaseName;
    }
    private boolean initialize() throws DatabaseInitializationException{
        if(isConnected()){
            try{
                PreparedStatement st = con.prepareStatement(CreateStudentTableSQL);
                st.execute();
                System.out.println("Database Initialized");
                return true;
            }catch(SQLException e){
                System.out.println("Unable to Initialize the database!");
                System.out.println(e.getMessage());
                throw new DatabaseInitializationException();
            }
        }
        return false;
    }
    
    public boolean isConnected(){
        if(con!=null)
            return true;
        else
            return false;
    }
}


class StudentDb extends db{
    
    public int addNewStudent(String name, String enroll, java.util.Date dob) throws DatabaseException{
        try{
            PreparedStatement st = con.prepareStatement(AddNewStudentSQL);
            st.setString(1, name);
            st.setString(2, enroll);
            st.setDate(3, new java.sql.Date(dob.getTime()));
            ResultSet res = st.executeQuery();
            res.next();
            int id = res.getInt("id");
            System.out.println("Inserted Student with id " + id);
            return id;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }
    
    public boolean enrollmentExists(String enroll) throws DatabaseException{
        try{
            PreparedStatement st = con.prepareStatement("SELECT COUNT(id) AS total FROM students WHERE enrollment=?");
            st.setString(1, enroll);
            ResultSet res = st.executeQuery();
            res.next();
            if(res.getInt(1) == 1)
                return true;
            else
                return false;
        }catch(SQLException e){
            throw new DatabaseException(e.getMessage());
        }
    }
    
    public Dictionary getStudentByEnrollment(String enroll) throws EnrollmentNotFoundException, DatabaseException{
        Dictionary student = new Hashtable();
        try{
            PreparedStatement st = con.prepareStatement("SELECT id, name, enrollment, dob FROM students WHERE enrollment = ? LIMIT 1;");
            st.setString(1, enroll);
            
            ResultSet res = st.executeQuery();
            if(res.next()){
                student.put("id", res.getString("id"));
                student.put("name", res.getString("name"));
                student.put("enroll", res.getString("enrollment"));
                student.put("dob", res.getString("dob"));
            }else{
                throw new EnrollmentNotFoundException();
            }
        }catch(SQLException e){
            throw new DatabaseException(e.getMessage());
        }        
        return student;
    }
    
    public ArrayList<Dictionary> getStudents() throws DatabaseException{
        ArrayList<Dictionary> students = new ArrayList<Dictionary>();
        try{
            PreparedStatement st = con.prepareStatement("SELECT id, name, enrollment, dob FROM students;");
            
            ResultSet res = st.executeQuery();
            while(res.next()){
                Dictionary student = new Hashtable();
                student.put("id", res.getString("id"));
                student.put("name", res.getString("name"));
                student.put("enroll", res.getString("enrollment"));
                student.put("dob", res.getString("dob"));
                students.add(student);
            }
        }catch(SQLException e){
            throw new DatabaseException(e.getMessage());
        }        
        return students;
    }
    public ArrayList<Dictionary> getStudentsByName(String name) throws DatabaseException{
        ArrayList<Dictionary> students = new ArrayList<Dictionary>();
        try{
            PreparedStatement st = con.prepareStatement("SELECT id, name, enrollment, dob FROM students WHERE LOWER(name) LIKE ?;");
            st.setString(1, "%"+name.toLowerCase()+"%");
            ResultSet res = st.executeQuery();
            while(res.next()){
                Dictionary student = new Hashtable();
                student.put("id", res.getString("id"));
                student.put("name", res.getString("name"));
                student.put("enroll", res.getString("enrollment"));
                student.put("dob", res.getString("dob"));
                students.add(student);
            }
        }catch(SQLException e){
            throw new DatabaseException(e.getMessage());
        }        
        return students;
    }
    
    public boolean deleteStudentByEnrollment(String enroll) throws DatabaseException{
        try{
            PreparedStatement st = con.prepareStatement("DELETE FROM students WHERE enrollment=?;");
            st.setString(1, enroll);
            st.execute();
            try{
                getStudentByEnrollment(enroll);
                return false;
            } catch (EnrollmentNotFoundException ex) {
                return true;
            }
        }catch(SQLException e){
            throw new DatabaseException(e.getMessage());
        }
    }
    public boolean editStudent(Dictionary student, Dictionary studentNew) throws DatabaseException, EnrollmentExistsException{
        
        try{
            Dictionary currentData = getStudentByEnrollment(studentNew.get("enroll").toString());
            if(currentData.get("id").toString().equals(student.get("id"))){
                throw new EnrollmentNotFoundException();
            }else{
                throw new EnrollmentExistsException();
            }
            
        }catch(EnrollmentNotFoundException ex){
            try{
                PreparedStatement st = con.prepareStatement("UPDATE students SET name=?, enrollment=?, dob=? WHERE id=?;");
            
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
                st.setString(1, studentNew.get("name").toString());
                st.setString(2, studentNew.get("enroll").toString());
                st.setDate(3, new java.sql.Date(sdf.parse(studentNew.get("dob").toString()).getTime()));
                st.setInt(4, Integer.parseInt(studentNew.get("id").toString()));
            
                st.execute();
                return true;
            }catch(SQLException e){
                e.printStackTrace();
                throw new DatabaseException(e.getMessage());
            } catch (ParseException e) {
                return false;
            }
        }
            
    }
}
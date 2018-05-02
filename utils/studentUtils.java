/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.Dictionary;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import sis.utils.Exceptions.*;
/**
 *
 * @author lucifer
 */
public class studentUtils {
    
    public boolean createNewStudent(String name, String enroll, String dob) throws EnrollmentExistsException, DateParsingException, DatabaseInitializationException, DatabaseException{
        StudentDb database = new StudentDb();
        try{
            database.connect();
            if(!database.enrollmentExists(enroll)){
                Date dateofbirth = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
                database.addNewStudent(name, enroll, dateofbirth);
                return true;
            }else{
                    throw new EnrollmentExistsException();
            }
        }catch(ParseException e){
            throw new DateParsingException();
        }catch(DatabaseInitializationException e){
            throw e;
        }catch(DatabaseException e){
            throw e;
        }
    }
    
    public Dictionary searchStudentByEnrollment(String enroll) throws DatabaseException, EnrollmentNotFoundException{
        Dictionary student;
        StudentDb s = new StudentDb();
        try{
            s.connect();
            student = s.getStudentByEnrollment(enroll);
        }catch(EnrollmentNotFoundException e){
            throw e;
        }catch(DatabaseException e){
            throw e;
        }
        return student;
    }
    public ArrayList<Dictionary> getStudents() throws DatabaseException{
        ArrayList<Dictionary> students;
        StudentDb s = new StudentDb();
        try{
            s.connect();
            students = s.getStudents();
            return students;
        }catch(DatabaseException e){
            throw e;
        }
    }
    public ArrayList<Dictionary> searchStudentsByName(String name) throws DatabaseException{
        ArrayList<Dictionary> students;
        StudentDb s = new StudentDb();
        try{
            s.connect();
            students = s.getStudentsByName(name);
            return students;
        }catch(DatabaseException e){
            throw e;
        }
    }
    
    public boolean deleteStudentByEnrollment(String enroll) throws DatabaseException, EnrollmentNotFoundException{
        try{
            searchStudentByEnrollment(enroll);
            
            StudentDb s = new StudentDb();
            
            try{
                s.connect();
                if(s.deleteStudentByEnrollment(enroll)){
                    return true;
                }else{
                    return false;
                }
            }catch(DatabaseException ex){
                throw ex;
            }
        }catch(EnrollmentNotFoundException ex){
            throw ex;
        }
    }
    public boolean editStudent(Dictionary student, Dictionary studentNew) throws DatabaseException, EnrollmentExistsException{
        try{
            StudentDb s = new StudentDb();
            s.connect();
            s.editStudent(student, studentNew);
            return true;
        }catch(DatabaseException ex){
                throw ex;
        }    
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.utils;

import java.util.regex.*;
/**
 *
 * @author lucifer
 */

public class validations {
    String msg = new String("Unknown");
    
    Pattern namePattern = Pattern.compile("[a-zA-Z]{1,}\\s[a-zA-Z]{1,}");
    Pattern nameNotFullPattern = Pattern.compile("^\\s*[a-zA-Z]{1,}(\\s[a-zA-Z]{0,}){0,1}\\s*$");
    Pattern enrollPattern = Pattern.compile("[A-Z][0-9]{10,11}");
    Pattern datePattern = Pattern.compile("^\\s*(3[01]|[12][0-9]|0?[1-9])/(1[012]|0?[1-9])/((?:19|20)\\d{2})\\s*$");
    Pattern dateDbPattern = Pattern.compile("^\\s*((?:19|20)\\d{2})-(1[012]|0?[1-9])-(3[01]|[12][0-9]|0?[1-9])\\s*$");
    public boolean validate(String str, String type){
        if(type == "name"){
            if(str.length() == 0){
                msg = new String("Name is Empty");
                return false;
            }
            if(!namePattern.matcher(str).matches()){
                msg = new String("Only Alphabets and a single space is allowed!");
                return false;
            }
        }
        if(type == "name_not_full"){
            if(str.length() == 0){
                msg = new String("Name is Empty");
                return false;
            }
            if(!nameNotFullPattern.matcher(str).matches()){
                msg = new String("Only Alphabets and a space is allowed!");
                return false;
            }
        }
        if(type == "enroll"){
            if(str.length() == 0){
                msg = new String("Enrollment is Empty");
                return false;
            }
            if(!enrollPattern.matcher(str).matches()){
                msg = new String("Please enter a valid enrollment number");
                return false;
            }
        }
        if(type == "dob"){
            if(str.length() == 0){
                msg = new String("DOB is Empty");
                return false;
            }
            if(!datePattern.matcher(str).matches()){
                msg = new String("Please enter a valid date!");
                return false;
            }
        }
        if(type == "dob-db"){
            if(str.length() == 0){
                msg = new String("DOB is Empty");
                return false;
            }
            if(!dateDbPattern.matcher(str).matches()){
                msg = new String("Please enter a valid date!");
                return false;
            }
        }
        
        return true;
    }
    
    public String getMsg(){
        return msg;
    }
}
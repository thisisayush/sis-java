/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.utils.Exceptions;

/**
 *
 * @author lucifer
 */
public class DatabaseInitializationException extends DatabaseException{
    public DatabaseInitializationException(){msg = "Unable to Initialize Database";}
    public DatabaseInitializationException(String msg){this.msg = msg;}
}
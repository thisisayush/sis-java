/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.gui;
import java.awt.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author lucifer
 */
class MainMenuBar{
    JMenuBar generateMenu(){
        /** Create a JMenuBar **/
        JMenuBar menu = new JMenuBar();
        
        /** Create menus **/
        
        final JMenu fileMenu = new JMenu("File");
        final JMenu queryMenu = new JMenu("Query");
        
        /** Add Items to JMenu **/
        JMenuItem i1 = new JMenuItem("Add Student");
        JMenuItem i2 = new JMenuItem("Student List");
        
        JMenuItem deleteItem = new JMenuItem("Delete a Student");
        JMenuItem editItem = new JMenuItem("Edit a Student");
        
        JMenuItem i3 = new JMenuItem("Exit");
        i3.setActionCommand("exit");
        
        i1.addActionListener(new addStudentListener());
        i2.addActionListener(new studentListListener());
        i3.addActionListener(new ExitListener());
        editItem.addActionListener(new EditStudentListener());
        deleteItem.addActionListener(new DeleteStudentListener());
        /** Create JMenu hierarchy **/
        fileMenu.add(i1);
        
        queryMenu.add(i2);
        queryMenu.add(editItem);
        queryMenu.add(deleteItem);
        
        fileMenu.add(i3);
        
        /** Add JMenu to JMenu bar **/
        menu.add(fileMenu);
        menu.add(queryMenu);
        
        return menu;
    } 
}

class studentListMenuBar{
    private Frame parentFrame;
    
    studentListMenuBar(Frame f){
        parentFrame = f;        
    }
    
    JMenuBar generateMenu(){
        JMenuBar m = new JMenuBar();
        
        JMenu FileMenu = new JMenu("File");
        
        JMenuItem deleteItem = new JMenuItem("Delete a Student");
        JMenuItem editItem = new JMenuItem("Edit a Student");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ExitListener(parentFrame));
        
        editItem.addActionListener(new EditStudentListener());
        deleteItem.addActionListener(new DeleteStudentListener());
        
        FileMenu.add(editItem);
        FileMenu.add(deleteItem);
        FileMenu.add(exitItem);
        
        m.add(FileMenu);
        
        return m;
    }
}

class exitOnlyMenuBar{
    private Frame parentFrame;
    
    exitOnlyMenuBar(Frame f){
        parentFrame = f;        
    }
    
    JMenuBar generateMenu(){
        JMenuBar m = new JMenuBar();
        
        JMenu FileMenu = new JMenu("File");
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ExitListener(parentFrame));
        
        FileMenu.add(exitItem);
        
        m.add(FileMenu);
        
        return m;
    }
}
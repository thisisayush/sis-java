package sis.gui;

import java.awt.*;
import javax.swing.*;

import sis.config;

/**
 *
 * @author lucifer
 */
public class MainWindow extends JFrame {
    config c = new config();
    
    MenuBar menu;
    public MainWindow(){
        setTitle(c.applicationTitle);
        setSize(c.WindowHeight, c.WindowWidth);
        setResizable(c.isResizable);
        setJMenuBar(new MainMenuBar().generateMenu());
        setLayout(new GridLayout(1,1));
        initializeLayout();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void initializeLayout(){
        JPanel j =  new JPanel();
        j.setLayout(new GridLayout(2,2,3,3));
        j.setBackground(Color.white);
        j.setSize(400, 200);
        
        JButton addBtn = new JButton("Add New Student");
        JButton listBtn = new JButton("List All");
        JButton editBtn = new JButton("Edit Student Details");
        JButton delBtn = new JButton("Delete a Student");
        
        addBtn.addActionListener(new addStudentListener());
        listBtn.addActionListener(new studentListListener());
        editBtn.addActionListener(new EditStudentListener());
        delBtn.addActionListener(new DeleteStudentListener());
        
        j.add(addBtn);
        j.add(listBtn);
        j.add(editBtn);
        j.add(delBtn);
        j.setVisible(true);
        add(j);
        
    }
}

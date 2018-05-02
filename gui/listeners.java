/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.gui;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author lucifer
 */
class ExitListener implements ActionListener{
    private Frame parentFrame = null;
    ExitListener(Frame parent){
        parentFrame = parent;
    }
    ExitListener(){}
    @Override
    public void actionPerformed(ActionEvent e){
        if (parentFrame == null)
            System.exit(0);
        else
            parentFrame.dispose();
    }
}

class addStudentListener implements ActionListener{
       
    @Override
    public void actionPerformed(ActionEvent e){
        StudentView sv = new StudentView();
    }
}

class studentListListener implements ActionListener{
    
    @Override
    public void actionPerformed(ActionEvent e){
        StudentsListView sv = new StudentsListView();
    }
}

class EditStudentListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e){
        EditStudentView ev = new EditStudentView();
    }
}

class DeleteStudentListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
        DeleteStudentView dv = new DeleteStudentView();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import sis.config;
import sis.utils.Exceptions.*;
import sis.utils.studentUtils;
import sis.utils.validations;
/**
 *
 * @author lucifer
 */
class StudentView extends JFrame{
    config c = new config();
    SpringLayout layout = new SpringLayout();
    
    final protected String nameFieldText = "Enter Student Name";
    final protected String enrollFieldText = "Enter Enrollment";
    final protected String dobFieldText = "DD/MM/YYYY";
    
    public StudentView(){
        setTitle("Add Student");
        setSize(400, 200);
        setResizable(c.isResizable);
        setLayout(layout);
        setJMenuBar(new exitOnlyMenuBar(this).generateMenu());
        initializeStudentFields();
        setVisible(true);
    }
    
    protected void initializeStudentFields(){
        Frame parent = this;
        JPanel p = new JPanel(new SpringLayout());
        p.setBackground(Color.white);
        JLabel name = new JLabel("Student Name: ");
        JLabel enroll =  new JLabel("Enrollment: ");
        JLabel dob = new JLabel("Date of Birth");
        
        JTextField nameField = new JTextField(nameFieldText);
        nameField.setName("name");
        JTextField enrollField = new JTextField(enrollFieldText);
        enrollField.setName("enroll");
        JTextField dobField = new JTextField(dobFieldText);
        dobField.setName("dob");
        
        JButton submit = new JButton("Submit");
        JButton reset = new JButton("Reset");
        
        name.setLabelFor(nameField);
        enroll.setLabelFor(enrollField);
        dob.setLabelFor(dobField);
        
        submit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                validations v = new validations();
                ErrorBox eb = new ErrorBox();
                if(!v.validate(nameField.getText(), "name")){
                    eb.addMsg(v.getMsg());
                }
                if(!v.validate(enrollField.getText(), "enroll")){
                    eb.addMsg(v.getMsg());
                }
                if(!v.validate(dobField.getText(), "dob")){
                    eb.addMsg(v.getMsg());
                }
                if(eb.is_set){
                    eb.showBox();
                }else{
                    eb.clear();
                    studentUtils s = new studentUtils();
                    
                    try{
                        s.createNewStudent(
                            nameField.getText(),
                            enrollField.getText(),
                            dobField.getText()
                        );
                        eb.setType("success");
                        eb.addMsg("Added Successfully!");
                        eb.showBox();
                        parent.dispose();
                    }catch(EnrollmentExistsException error){
                        eb.addMsg("Enrollment Exists in Database!");
                        eb.showBox();
                    }catch(DateParsingException error){
                        eb.addMsg("Unable to Process Date!");
                        eb.showBox();
                    }catch(DatabaseException error){
                        eb.addMsg("Database Error Occured : " + error.msg);
                        eb.showBox();
                    }
                }
            }
        });
        
        reset.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                nameField.setText(nameFieldText);
                enrollField.setText(enrollFieldText);
                dobField.setText(dobFieldText);
            }
        });
        
        p.add(name);
        p.add(nameField);
        p.add(enroll);
        p.add(enrollField);
        p.add(dob);
        p.add(dobField);
        p.add(reset);
        p.add(submit);
        
        SpringUtilities.makeCompactGrid(p, 4, 2, 6, 6, 5, 5);
        
        setContentPane(p);
    }
}

class StudentsListView extends JFrame{
    config c = new config();
    JPanel mainPanel = new JPanel();
    JPanel headerPanel;
    JScrollPane studentListPanel = new JScrollPane();
    
    final String[] tableColumns = {
            "id",
            "Name",
            "Enrollment Number",
            "Date of Birth"
        };
    
    public StudentsListView(){
        setTitle("Students List");
        setSize(c.WindowHeight, c.WindowWidth);
        setResizable(c.isResizable);
        setJMenuBar(new studentListMenuBar(this).generateMenu());
        setVisible(true);
        mainPanel.setLayout(new GridLayout(2,1));
        initializeLayout();
        studentUtils s = new studentUtils();
        try {
            generateStudentList(s.getStudents());
        } catch (DatabaseException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void initializeLayout(){
        headerPanel = generateHeaderPanel();
        mainPanel.add(headerPanel);
        mainPanel.add(studentListPanel);
        add(mainPanel);
    }
    
    private JPanel generateHeaderPanel(){
        JPanel j = new JPanel();
        
        JLabel searchNameText = new JLabel("Search By Name:");
        searchNameText.setPreferredSize(new Dimension(c.WindowHeight, 20));
        JTextField searchNameField = new JTextField("");
        searchNameField.setPreferredSize(new Dimension(c.WindowHeight, 20));
        searchNameText.setLabelFor(searchNameField);
        
        
        JLabel searchEnrollText = new JLabel("Search By Enrollment:");
        searchEnrollText.setPreferredSize(new Dimension(c.WindowHeight, 20));
        JTextField searchEnrollField = new JTextField("");
        searchEnrollField.setPreferredSize(new Dimension(c.WindowHeight, 20));
        searchEnrollText.setLabelFor(searchEnrollField);
        
        JButton searchBtn = new JButton("Search");
        
        searchBtn.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               ErrorBox eb = new ErrorBox();
               if(searchNameField.getText().length() == 0 && searchEnrollField.getText().length() == 0){
                   eb.addMsg("Please provide either name or enrollment!");
               }else{
                   if(searchEnrollField.getText().length() != 0){
                       validations v = new validations();
                       if(!v.validate(searchEnrollField.getText(),"enroll")){
                           eb.addMsg("Enter a valid enrollment number!" + v.getMsg());
                       }else{
                           studentUtils s = new studentUtils();
                           try {
                               Dictionary res = s.searchStudentByEnrollment(searchEnrollField.getText());
                               generateStudentList(res);
                           } catch (DatabaseException ex) {
                               eb.addMsg("Database Error: " + ex.msg);
                           } catch (EnrollmentNotFoundException ex) {
                               eb.addMsg("Enrollment Not Found!");
                           }
                       }
                   }else{
                       validations v = new validations();
                       if(!v.validate(searchNameField.getText(),"name_not_full")){
                           eb.addMsg("Enter a valid name!" + v.getMsg());
                       }else{
                           studentUtils s = new studentUtils();
                           try {
                               ArrayList<Dictionary> res = s.searchStudentsByName(searchNameField.getText());
                               generateStudentList(res);
                           } catch (DatabaseException ex) {
                               eb.addMsg("Database Error: " + ex.msg);
                           }
                       }
                   }
               }
               if(eb.is_set)
                   eb.showBox();
           } 
        });
        
        j.add(searchNameText);
        j.add(searchNameField);
        j.add(searchEnrollText);
        j.add(searchEnrollField);
        j.add(searchBtn);
        j.setSize(c.WindowWidth, 150);
        j.setLayout(new GridLayout(0,1));
        return j;
    }
    
    private void generateStudentList(Dictionary student){
        
        String[][] data = {
            {
                student.get("id").toString(),
                student.get("name").toString(),
                student.get("enroll").toString(),
                student.get("dob").toString(),
            },
        };
        
        JTable studentTable = new JTable(data, tableColumns);
        
        JScrollPane jp = new JScrollPane(studentTable);
        updateStudentListPanel(jp);
        
    }
    
    private void generateStudentList(ArrayList<Dictionary> students){
        
        String[][] data = new String[students.size()][4];
        
        int i=0;
        for(Dictionary student:students){
            data[i][0] = student.get("id").toString();
            data[i][1] = student.get("name").toString();
            data[i][2] = student.get("enroll").toString();
            data[i][3] = student.get("dob").toString();
            i++;
        }
        
        JTable studentTable = new JTable(data, tableColumns);
        
        JScrollPane jp = new JScrollPane(studentTable);
        updateStudentListPanel(jp);
        
    }
    
    private void updateStudentListPanel(JScrollPane jp){
        mainPanel.remove(studentListPanel);
        studentListPanel = jp;
        studentListPanel.setBounds(0, 170, c.WindowWidth, c.WindowHeight-170);
        mainPanel.add(studentListPanel);
        //mainPanel.revalidate();
        
    }
    
}

class ErrorBox{
    public boolean is_set;
    String[] msg;
    JPanel jp;
    JDialog jd;
    private int currentHeight = 50;
    private Color textColor = Color.white;
    ErrorBox(){
        init();
        jp.setBackground(Color.red);
        textColor = Color.white;
    }
    ErrorBox(String type){
        init();
        setType(type);
    }
    private void init(){
        is_set = false;
        jp = new JPanel();
        jp.setLayout(new FlowLayout());
    }
    public void setType(String type){
        if(type == "success"){
            jp.setBackground(Color.GREEN);
            textColor = Color.BLACK;
        }
    }
    public void showBox(){
        jd = new JDialog();
        jd.add(jp);
        jd.setVisible(true);
        jd.setSize(400,currentHeight);
    }
    public void addMsg(String msg){
        is_set = true;
        JLabel j = new JLabel(msg);
        j.setForeground(textColor);
        currentHeight += 50;
        jp.add(j);
    }
    public void clear(){
       jp.removeAll();
    }
}

class DeleteStudentView extends JFrame{
    
    
    DeleteStudentView(){
        setTitle("Delete a Student");
        setSize(300, 100);
        setVisible(true);
        
        setLayout(new GridLayout(1,1));
        
        JPanel j = new JPanel();
        j.setBackground(Color.white);
        j.setLayout(new GridLayout(3,1));
        j.setSize(300, 100);
        
        JLabel title = new JLabel("Enter Enrollment Number:");
        JTextField enrollField = new JTextField("");
        JButton submit = new JButton("Search");
        
        submit.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                validations v = new validations();
                if(!v.validate(enrollField.getText(), "enroll")){
                    ErrorBox eb = new ErrorBox();
                    eb.addMsg("Please enter a valid enrollment number!");
                    eb.showBox();
                    
                }else{
                    studentUtils s = new studentUtils();
                    try{
                        Dictionary student = s.searchStudentByEnrollment(enrollField.getText());

                        remove(j);
                        add(getDeleteStudentPanel(student));
                        setSize(300, 300);
                        
                    }catch(EnrollmentNotFoundException ex){
                        ErrorBox eb = new ErrorBox();
                        eb.addMsg("Enrollment Not Found! Please Recheck!");
                        eb.showBox();
                    } catch (DatabaseException ex) {
                        ErrorBox eb = new ErrorBox();
                        eb.addMsg("Database Error Occured!");
                        eb.showBox();
                    }
                }
            }
        
        });
        
        j.add(title);
        j.add(enrollField);
        j.add(submit);
        
        add(j);
        
    }
    
    JPanel getDeleteStudentPanel(Dictionary student){
        JPanel jp = new JPanel();
        
        jp.setLayout(new GridLayout(5,1));
        jp.setSize(300, 300);
        
        jp.add(new JLabel("Enrollment: " + student.get("enroll").toString()));
        jp.add(new JLabel("Name: " + student.get("name").toString()));
        jp.add(new JLabel("Date Of Birth: " + student.get("dob").toString()));
        
        JButton deleteBtn = new JButton("Delete");
        JButton cancelBtn = new JButton("Cancel");
        jp.add(deleteBtn);
        jp.add(cancelBtn);
        deleteBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                studentUtils s = new studentUtils();
                try{
                    if(s.deleteStudentByEnrollment(student.get("enroll").toString())){
                        ErrorBox eb = new ErrorBox("success");
                        eb.addMsg("Deleted Successfully!");
                        eb.showBox();
                        dispose();
                    }
                }catch(EnrollmentNotFoundException ex){
                    ErrorBox eb = new ErrorBox();
                    eb.addMsg("Enrollment Number Doesn't exist in database!");
                    eb.showBox();
                }catch(DatabaseException ex){
                    ErrorBox eb = new ErrorBox();
                    eb.addMsg("Database Error Occured: " + ex.getMessage());
                    eb.showBox();
                }
            }
        });
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        jp.setVisible(true);
        return jp;
    }
    
}

class EditStudentView extends JFrame{
    
    
    EditStudentView(){
        setTitle("Edit a Student");
        
        setSize(300, 100);
        setVisible(true);
        
        setLayout(new GridLayout(1,1));
        
        JPanel j = new JPanel();
        j.setBackground(Color.white);
        j.setLayout(new GridLayout(3,1));
        j.setSize(300, 100);
        
        JLabel title = new JLabel("Enter Enrollment Number:");
        JTextField enrollField = new JTextField("");
        JButton submit = new JButton("Search");
        
        submit.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                validations v = new validations();
                if(!v.validate(enrollField.getText(), "enroll")){
                    ErrorBox eb = new ErrorBox();
                    eb.addMsg("Please enter a valid enrollment number!");
                    eb.showBox();
                    
                }else{
                    studentUtils s = new studentUtils();
                    try{
                        Dictionary student = s.searchStudentByEnrollment(enrollField.getText());
                        remove(j);
                        add(getEditStudentPanel(student));
                        setSize(400, 200);
                        
                    }catch(EnrollmentNotFoundException ex){
                        ErrorBox eb = new ErrorBox();
                        eb.addMsg("Enrollment Not Found! Please Recheck!");
                        eb.showBox();
                    } catch (DatabaseException ex) {
                        ErrorBox eb = new ErrorBox();
                        eb.addMsg("Database Error Occured!");
                        eb.showBox();
                    }
                }
            }
        
        });
        
        j.add(title);
        j.add(enrollField);
        j.add(submit);
        
        add(j);
        
    }
    
    JPanel getEditStudentPanel(Dictionary student){
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(4,2));
        jp.setSize(400, 200);
        
        jp.add(new JLabel("Enrollment"));
        JTextField enrollField = new JTextField(student.get("enroll").toString());
        jp.add(enrollField);
        
        jp.add(new JLabel("Name"));
        JTextField nameField = new JTextField(student.get("name").toString());
        jp.add(nameField);
        
        jp.add(new JLabel("Date Of Birth"));
        JTextField dobField = new JTextField(student.get("dob").toString());
        jp.add(dobField);
        
        JButton submitBtn = new JButton("Submit");
        JButton cancelBtn = new JButton("Cancel");
        
        jp.add(submitBtn);
        jp.add(cancelBtn);
        
        submitBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                validations v = new validations();
                ErrorBox eb = new ErrorBox();
                if(!v.validate(nameField.getText(), "name")){
                    eb.addMsg(v.getMsg());
                }
                if(!v.validate(enrollField.getText(), "enroll")){
                    eb.addMsg(v.getMsg());
                }
                if(!v.validate(dobField.getText(), "dob-db")){
                    eb.addMsg(v.getMsg());
                }
                if(eb.is_set){
                    eb.showBox();
                }else{
                    eb.clear();
                    studentUtils s = new studentUtils();

                    Dictionary studentNew = new Hashtable();
                    studentNew.put("name", nameField.getText());
                    studentNew.put("enroll", enrollField.getText());
                    studentNew.put("dob", dobField.getText());
                    studentNew.put("id", student.get("id"));
                    try{
                        if(s.editStudent(student, studentNew)){
                            ErrorBox ebS = new ErrorBox("success");
                            ebS.addMsg("Updated Successfully!");
                            ebS.showBox();
                            dispose();
                        }
                    }catch(DatabaseException ex){
                        eb.addMsg("Database Error Occured: " + ex.getMessage());
                        eb.showBox();
                    } catch (EnrollmentExistsException ex) {
                        eb.addMsg("Enrollment Already in Database!");
                        eb.showBox();
                    }
                }
            }
        });
        
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        
        return jp;
    }
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis;

import sis.gui.MainWindow;

/**
 *
 * @author lucifer
 */
public class Sis {
    public MainWindow mainAppWindow;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Sis s = new Sis();
        s.init();
    }
    public void init(){
        mainAppWindow = new MainWindow();
    }
}

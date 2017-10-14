package com.company;

import com.company.GUI.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menue {

    // Dimension of window / canvas
    int width = 1800;
    int height = 1000;


    public Menue (int width, int height) {
        this.width = width;
        this.height = height;

        // call GUI
        GUI gui = new GUI(this.width, this.height, true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui.myView);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.repaint(100);


        JButton btnloadGame = new JButton("Load Game");
        btnloadGame.setBounds(20,20,100,40);
        btnloadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCompletePlayground(gui);
                frame.getContentPane().remove(btnloadGame);

                // Play yourself
                JButton btnHuman = new JButton("Human Player");
                btnHuman.setBounds(20,20,150,40);
                btnHuman.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // do sth
                    }
                });
                frame.getContentPane().add(btnHuman);

                // Show best Calculation
                JButton btnAI = new JButton("show best AI");
                btnAI.setBounds(220,20,150,40);
                btnAI.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // do sth
                    }
                });
                frame.getContentPane().add(btnAI);

                // Training
                JButton btnTraining = new JButton("train AI");
                btnTraining.setBounds(420,20,150,40);
                btnTraining.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // do sth
                    }
                });
                frame.getContentPane().add(btnTraining);


                frame.repaint(100);
            }
        });
        frame.getContentPane().add(btnloadGame);

        // play myself

        // let computer work

    }

    public void operate () {

    }



    private void showCompletePlayground (GUI gui) {
        // 10 free pixels each side
        // TilesWidthNum / Width = pixels/tile in sqrt
        int tileSide = 190 / (gui.myWidth-20);
        // TilesHeightNum * pixels = HeightReduction
        gui.myHeight = (gui.myHeight) - (8 * tileSide + 20);



    }


}

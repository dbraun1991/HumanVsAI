package com.company;

import com.company.GUI.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {

        // Dimension of window / canvas
        int myWidth = 1800;
        int myHeight = 1000;


        // call GUI
        GUI gui = new GUI(myWidth, myHeight, true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui.myView);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);


        JButton btnloadGame = new JButton("Load Game");
        btnloadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCompletePlayground(gui);
                frame.getContentPane().remove(btnloadGame);
            }
        });
        btnloadGame.setBounds(20,20,100,40);
        frame.getContentPane().add(btnloadGame);

        // play myself

        // let computer work

    }

    private static void showCompletePlayground (GUI gui) {
        // 10 free pixels each side
        // TilesWidthNum / Width = pixels/tile in sqrt
        int tileSide = 190 / (gui.myWidth-20);
        // TilesHeightNum * pixels = HeightReduction
        gui.myHeight = (gui.myHeight) - (8 * tileSide + 20);


    }



}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pos.ui;

import static com.pos.ui.Main.OS_NAME;
import java.awt.Color;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author peter
 */
public class ProgressBar extends javax.swing.JFrame {

    public final int LOGGING_OUT = 1;
    public final int EDITING_PRODUCTS = 2;
    public final int VIEWING_REVENUE = 3;

    /**
     * Creates new form LoggingOutProgress
     */
    public ProgressBar() {
        initComponents();

    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        percentage = new javax.swing.JLabel();
        message = new javax.swing.JLabel();
        header = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(0, 102, 102));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        mainPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 390, 10));

        percentage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        percentage.setForeground(new java.awt.Color(255, 255, 255));
        percentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        percentage.setText("0 %");
        mainPanel.add(percentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 40, -1));

        message.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        message.setForeground(new java.awt.Color(255, 255, 255));
        message.setText("Loading");
        mainPanel.add(message, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 280, 20));

        header.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        header.setForeground(new java.awt.Color(255, 255, 255));
        header.setText("PLEASE WAIT");
        mainPanel.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    // <editor-fold defaultstate="collapsed" desc="Runs the frame.">
    public void run(final JFrame frame, int whatProgress) {

        // Checks if the current operating system is a Windows operating system
        // Windows theme for Windows machines
        // Nimbus theme for non Windows machines
        try {
            if (OS_NAME.startsWith("WINDOWS")) {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
            else {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Thread exit_thread = new Thread(() -> {
            ProgressBar loggingOutProgress = new ProgressBar();

            final Random RANDOM = new Random();
            loggingOutProgress.setVisible(true);

            ManageProducts manageProducts = null;
            ViewRevenue viewRevenue = null;

            try {
                for(int i = 0; i <= 100; i++ ) {
                    Thread.sleep(RANDOM.nextInt(100) + 1);
                    loggingOutProgress.percentage.setText(i + " %");
                    switch(i) {
                        case 5 -> {
                            loggingOutProgress.message.setText("LOADING.");
                            loggingOutProgress.progressBar.setForeground(Color.red);
                        }
                        case 6 -> loggingOutProgress.message.setText("LOADING..");
                        case 7 -> loggingOutProgress.message.setText("LOADING...");
                        case 20 -> {
                            loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "WRAPPING THINGS UP BEFORE EXITING." : (whatProgress == EDITING_PRODUCTS) ? "READING PRODUCTS." : (whatProgress == VIEWING_REVENUE) ? "READING SALES" : "INVALID PROGRESS TYPE");
                            loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "WRAPPING THINGS UP BEFORE EXITING.." : (whatProgress == EDITING_PRODUCTS) ? "READING PRODUCTS.." : (whatProgress == VIEWING_REVENUE) ? "READING SALES." : "INVALID PROGRESS TYPE");
                            loggingOutProgress.progressBar.setForeground(Color.yellow);
                        }
                        case 30 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "WRAPPING THINGS UP BEFORE EXITING..." : (whatProgress == EDITING_PRODUCTS) ? "READING PRODUCTS..." : (whatProgress == VIEWING_REVENUE) ? "READING SALES.." : "INVALID PROGRESS TYPE");
                        case 40 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "WRAPPING THINGS UP BEFORE EXITING...." : (whatProgress == EDITING_PRODUCTS) ? "READING PRODUCTS...." : (whatProgress == VIEWING_REVENUE) ? "READING SALES..." : "INVALID PROGRESS TYPE");
                        case 50 -> {
                            if (whatProgress == EDITING_PRODUCTS) manageProducts = new ManageProducts();
                            if (whatProgress == VIEWING_REVENUE) viewRevenue = new ViewRevenue();
                            loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "WRAPPING THINGS UP BEFORE EXITING....." : (whatProgress == EDITING_PRODUCTS) ? "READING PRODUCTS....." : (whatProgress == VIEWING_REVENUE) ? "READING SALES...." : "INVALID PROGRESS TYPE");
                        }
                        case 90 -> {
                            loggingOutProgress.message.setText("SUCCESS");
                            if (whatProgress == LOGGING_OUT) frame.setVisible(false);
                        }
                        case 94 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "EXITING." : (whatProgress == EDITING_PRODUCTS) ? "OPENING." : (whatProgress == VIEWING_REVENUE) ? "OPENING." : "INVALID PROGRESS TYPE");
                        case 98 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "EXITING.." : (whatProgress == EDITING_PRODUCTS) ? "OPENING.." : (whatProgress == VIEWING_REVENUE) ? "OPENING.." : "INVALID PROGRESS TYPE");
                        case 100 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "EXITING..." : (whatProgress == EDITING_PRODUCTS) ? "OPENING..." : (whatProgress == VIEWING_REVENUE) ? "OPENING..." : "INVALID PROGRESS TYPE");
                    }
                    loggingOutProgress.progressBar.setValue(i);
                }
                if (whatProgress == LOGGING_OUT) System.exit(0);
                else if (whatProgress == EDITING_PRODUCTS || whatProgress == VIEWING_REVENUE) {
                    if (whatProgress == EDITING_PRODUCTS) {
                        this.dispose();
                        assert manageProducts != null;
                        manageProducts.run();
                    }
                    if (whatProgress == VIEWING_REVENUE) {
                        assert false;
                        viewRevenue.run();
                    }
                }
            } catch(InterruptedException interruptedException) {
                JOptionPane.showMessageDialog(null, interruptedException.getMessage());
            }
        });
        exit_thread.setPriority(Thread.MIN_PRIORITY);
        exit_thread.start();
    } // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel header;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel message;
    private javax.swing.JLabel percentage;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}

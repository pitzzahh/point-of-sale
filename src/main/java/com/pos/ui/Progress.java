/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pos.ui;

import javax.swing.UnsupportedLookAndFeelException;
import static com.pos.Main.OS_NAME;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.UIManager;
import com.pos.Main;

/**
 *
 * @author peter
 */
public class Progress extends javax.swing.JFrame {
    public static final int LOGGING_OUT = 1;
    public static final int PRINTING_RECIPT = 2;
    /**
     * Creates new form Logout
     */
    public Progress() {
        initComponents();
        setVisible(true);
    }

    /**
     * Method that stats the progress bar.
     * @param block the code block to wait for.
     * @param progressType the type of progress bar if logging out or printing receipt.
     */
    public void startProgressBar(Runnable block, int progressType) {
        if(progressType == 1) message.setText("LOGGING OUT");
        if(progressType == 2) message.setText("PRINTING RECEIPT");
        
        final Thread TIME_THREAD = new Thread(() -> {
            final int MINIMUM = 0;
            final int MAXIMUM = 10;
            progressBar.setMinimum(MINIMUM);
            progressBar.setMaximum(MAXIMUM);
            progressBar.setValue(0);
            
            for(int i = MINIMUM; i < MAXIMUM; i++) {
                progressBar.setValue(i);
                try {
                    Thread.sleep(getTime(block));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Progress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(progressType == 1) System.exit(0);
        });
        TIME_THREAD.setPriority(Thread.MIN_PRIORITY);
        TIME_THREAD.start();
    }

    /**
     * Method that gets the time the JVM takes to execute a code block.
     * @param block the code block to calculate the time for it to terminate.
     * @return the time taken by a code block before terminating.
     */
    private long getTime(Runnable block) {
        long start = System.nanoTime();
        block.run();
        long end = System.nanoTime();
        return (long) ((end - start) / 1.0e9);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoutPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        message = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        logoutPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        logoutPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 27, 210, 18));

        message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoutPanel.add(message, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 140, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Progress().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel logoutPanel;
    private javax.swing.JLabel message;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}

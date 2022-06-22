/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pos;

import javax.swing.UnsupportedLookAndFeelException;
import static com.pos.ui.Main.OS_NAME;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.*;
import java.util.Random;
import com.pos.ui.Main;

/**
 *
 * @author peter
 */
public class App extends javax.swing.JFrame {

    /**
     * Creates new form Progress
     */
    public App() {
        initComponents();
        Toolkit.getDefaultToolkit().getImage(getClass().getResource("src/main/resources/ico.png"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        message = new javax.swing.JLabel();
        percentage = new javax.swing.JLabel();
        headerPanel = new javax.swing.JPanel();
        header = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(0, 102, 102));
        mainPanel.setForeground(new java.awt.Color(153, 0, 51));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        progressBar.setBackground(new java.awt.Color(153, 204, 0));
        progressBar.setForeground(new java.awt.Color(0, 0, 255));
        mainPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 450, 20));

        message.setBackground(new java.awt.Color(255, 255, 255));
        message.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        message.setForeground(new java.awt.Color(255, 255, 255));
        message.setText("Loading");
        mainPanel.add(message, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 270, 30));

        percentage.setBackground(new java.awt.Color(255, 255, 255));
        percentage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        percentage.setForeground(new java.awt.Color(255, 255, 255));
        percentage.setText("100 %");
        mainPanel.add(percentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 130, 50, 30));

        headerPanel.setBackground(new java.awt.Color(102, 102, 0));
        headerPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        header.setFont(new java.awt.Font("Consolas", 1, 36)); // NOI18N
        header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        header.setText("POINT OF SALE");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(header))
        );

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 310, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Starting point.
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
        
        App app = new App();
        final Random RANDOM = new Random();
        app.setVisible(true);
        final int START = 0;
        final int END = 100;
        Main MAIN = null;
        try {
            for(int i = 0; i <= 100; i++ ) {
                Thread.sleep(RANDOM.nextInt(100) + 1);
                app.percentage.setText(i + " %");
                switch(i) {
                    case 5 -> {
                        app.message.setText("LOADING.");
                        app.progressBar.setBackground(Color.red);
                    }
                    case 6 -> app.message.setText("LOADING..");
                    case 7 -> app.message.setText("LOADING...");
                    case 20 -> {
                        app.message.setText("INITIALIZING POINT OF SALES.");
                        app.progressBar.setBackground(Color.yellow);
                    }
                    case 30 -> app.message.setText("INITIALIZING POINT OF SALES..");
                    case 35 -> app.message.setText("INITIALIZING POINT OF SALES...");
                    case 40 -> app.message.setText("CONNECTING TO DATABASE.");
                    case 45 -> app.message.setText("CONNECTING TO DATABASE..");
                    case 50 -> app.message.setText("CONNECTING TO DATABASE...");
                    case 64 -> {
                        app.message.setText("CONNECTED SUCCESSFULLY");
                        app.progressBar.setBackground(Color.blue);
                    }
                    case 70 -> app.message.setText("READING PRODUCTS LIST.");
                    case 72 -> {
                        app.message.setText("READING PRODUCTS LIST.");
                        i+=new Random().nextInt(4) + 1;
                    }
                    case 80 -> {
                        app.message.setText("READING PRODUCTS LIST..");
                        MAIN = new Main();
                    }
                    case 90 -> app.message.setText("SUCCESS");
                    case 94 -> app.message.setText("OPENING POINT OF SALES.");
                    case 98 -> app.message.setText("OPENING POINT OF SALES..");
                    case 100 -> app.message.setText("OPENING POINT OF SALES...");
                }
                app.progressBar.setValue(i);
            }
            app.dispose();

            assert MAIN != null;
            MAIN.run();
        } catch(InterruptedException interruptedException) {
            JOptionPane.showMessageDialog(null, interruptedException.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel header;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel message;
    private javax.swing.JLabel percentage;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}

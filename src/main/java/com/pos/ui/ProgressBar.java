package com.pos.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.imageio.ImageIO;
import java.util.Random;
import javax.swing.*;
import java.net.URL;
import java.awt.*;

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
        FlatDarkLaf.setup();
        initComponents();
        setIcon();
    }

    // <editor-fold defaultstate="collapsed" desc="Method that sets the icon for this frame.">//
    private void setIcon() {
        try {
            Image img = ImageIO.read(new URL("https://github.com/pitzzahh/point-of-sale/blob/220ccaa9681f18faa17a76b38ed6d91764303c5b/src/main/resources/loading.png?raw=true"));
            setIconImage(new ImageIcon(img).getImage());
        }
        catch(Exception ignored) {}
    } // </editor-fold>//

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(0, 102, 102));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        progressBar.setForeground(new java.awt.Color(244, 0, 0));
        mainPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 390, 10));

        percentage.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14)); // NOI18N
        percentage.setForeground(new java.awt.Color(255, 255, 255));
        percentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        percentage.setText("0 %");
        mainPanel.add(percentage, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 40, -1));

        message.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14)); // NOI18N
        message.setForeground(new java.awt.Color(255, 255, 255));
        message.setText("Loading");
        mainPanel.add(message, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 280, 20));

        header.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 24)); // NOI18N
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

        try {

            UIManager.setLookAndFeel(new FlatDarkLaf());

        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        final Thread PROGRESS_BAR_THREAD = new Thread(() -> {
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
                            loggingOutProgress.progressBar.setForeground(Color.blue);
                        }
                        case 94 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "EXITING." : (whatProgress == EDITING_PRODUCTS) ? "OPENING." : (whatProgress == VIEWING_REVENUE) ? "OPENING." : "INVALID PROGRESS TYPE");
                        case 98 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "EXITING.." : (whatProgress == EDITING_PRODUCTS) ? "OPENING.." : (whatProgress == VIEWING_REVENUE) ? "OPENING.." : "INVALID PROGRESS TYPE");
                        case 100 -> loggingOutProgress.message.setText((whatProgress == LOGGING_OUT) ? "EXITING..." : (whatProgress == EDITING_PRODUCTS) ? "OPENING..." : (whatProgress == VIEWING_REVENUE) ? "OPENING..." : "INVALID PROGRESS TYPE");
                    }
                    loggingOutProgress.progressBar.setValue(i);
                }

                if (whatProgress == LOGGING_OUT) System.exit(0);
                else if (whatProgress == EDITING_PRODUCTS || whatProgress == VIEWING_REVENUE) {
                    loggingOutProgress.setVisible(false);
                    loggingOutProgress.dispose();
                    if (whatProgress == EDITING_PRODUCTS) {
                        assert manageProducts != null;
                        manageProducts.run();
                    }
                    if (whatProgress == VIEWING_REVENUE) {
                        assert viewRevenue != null;
                        viewRevenue.run();
                    }
                }
            } catch(InterruptedException interruptedException) {
                JOptionPane.showMessageDialog(null, interruptedException.getMessage());
            }
        });
        PROGRESS_BAR_THREAD.setPriority(Thread.MIN_PRIORITY);
        PROGRESS_BAR_THREAD.start();
    } // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel header;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel message;
    private javax.swing.JLabel percentage;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}

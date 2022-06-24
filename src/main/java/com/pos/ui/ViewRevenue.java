package com.pos.ui;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import static com.pos.ui.Main.OS_NAME;
import com.pos.service.SalesService;
import com.pos.entity.Sales;
import java.net.URL;
import java.util.ArrayList;
import com.pos.Config;
import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author peter
 */
public class ViewRevenue extends javax.swing.JFrame {
    
    private final List<Sales> SALES = new ArrayList<>();
    
    private final AbstractApplicationContext CONTEXT=  new AnnotationConfigApplicationContext(Config.class);
    
    private final SalesService SALES_SERVICE = CONTEXT.getBean(SalesService.class);
    
    // <editor-fold defaultstate="collapsed" desc="Creates new form ViewSales">//
    public ViewRevenue() {
        initComponents();
        setIcon();
        final DefaultTableCellRenderer RENDERER = new DefaultTableCellRenderer();
        RENDERER.setHorizontalAlignment(JLabel.CENTER);
        salesTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        salesTable.getColumnModel().getColumn(1).setCellRenderer(RENDERER);
        salesTable.getColumnModel().getColumn(2).setCellRenderer(RENDERER);
        loadSales();
        getTotalRevenue();
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that gets all the sales Object then adding it to the List.">//
    private void loadSales() {
        SALES.addAll(SALES_SERVICE.getAllSales().get());
        refreshSalesTable();
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that refreshes the salesTable.">//
    private void refreshSalesTable() {
            DefaultTableModel defaultTableModel = (DefaultTableModel) salesTable.getModel();
            defaultTableModel.setRowCount(0);
            Object[] data = new Object[defaultTableModel.getColumnCount()];

            for (Sales sale : SALES) {
                data[0] = sale.getSalesNumber();
                data[1] = sale.getDateProcessed();
                data[2] = sale.getProfit();

                defaultTableModel.addRow(data);
            }
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that computes the total revenue from profits.">//
    private void getTotalRevenue() {
        double totalProfit = SALES_SERVICE.getAllProfit()
                .get()
                .stream()
                .reduce(0.0, Double::sum);
        totalRevenue.setText(String.valueOf(Main.NUMBER_FORMAT.format(totalProfit)));
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that sets the icon for this frame.">//
    private void setIcon() {
        try {
            Image img = ImageIO.read(new URL("https://github.com/pitzzahh/point-of-sale/blob/220ccaa9681f18faa17a76b38ed6d91764303c5b/src/main/resources/view.png?raw=true"));
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
        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tablePanel = new javax.swing.JPanel();
        salesTableScrollPane = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        totalRevenueLabel = new javax.swing.JLabel();
        totalRevenue = new javax.swing.JTextField();
        totalRevenuePesoSignLabel = new javax.swing.JLabel();
        close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(204, 204, 0));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(51, 51, 0));
        headerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("POINT OF SALE");
        headerPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, 30));

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 530, 50));

        tablePanel.setBackground(new java.awt.Color(0, 153, 153));
        tablePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tablePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "SALES_NUMBER", "DATE_PROCESSED", "PROFIT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        salesTable.getTableHeader().setReorderingAllowed(false);
        salesTableScrollPane.setViewportView(salesTable);
        if (salesTable.getColumnModel().getColumnCount() > 0) {
            salesTable.getColumnModel().getColumn(0).setResizable(false);
            salesTable.getColumnModel().getColumn(1).setResizable(false);
            salesTable.getColumnModel().getColumn(2).setResizable(false);
        }

        tablePanel.add(salesTableScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 750, 370));

        jPanel1.setBackground(new java.awt.Color(51, 51, 0));
        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalRevenueLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalRevenueLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalRevenueLabel.setText("TOTAL REVENUE :");
        jPanel1.add(totalRevenueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        totalRevenue.setEditable(false);
        totalRevenue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalRevenue.setText("0.00");
        totalRevenue.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(totalRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 220, 30));

        totalRevenuePesoSignLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalRevenuePesoSignLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalRevenuePesoSignLabel.setText("₱");
        jPanel1.add(totalRevenuePesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));

        close.setText("CLOSE");
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });
        jPanel1.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, -1, 30));

        tablePanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 750, 50));

        mainPanel.add(tablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 770, 450));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeActionPerformed

    /**
     * Runs this frame.
     */
    public void run() {

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
        java.awt.EventQueue.invokeLater(() -> this.setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton close;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTable salesTable;
    private javax.swing.JScrollPane salesTableScrollPane;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTextField totalRevenue;
    private javax.swing.JLabel totalRevenueLabel;
    private javax.swing.JLabel totalRevenuePesoSignLabel;
    // End of variables declaration//GEN-END:variables
}

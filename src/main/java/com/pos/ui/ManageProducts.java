package com.pos.ui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.stream.Collectors;
import static com.pos.Main.OS_NAME;
import com.pos.Config;
import com.pos.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import com.pos.Main;
import com.pos.service.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * @author peter
 */
public class ManageProducts extends javax.swing.JFrame {
    
    public static List<Product> AVAILABLE_PRODUCTS = new ArrayList<>();

    public static List<Product> EXPIRED_PRODUCTS = new ArrayList<>();

    private final AbstractApplicationContext CONTEXT=  new AnnotationConfigApplicationContext(Config.class);
    public final ProductService PRODUCT_SERVICE = CONTEXT.getBean(ProductService.class);

    private static final int IS_AVAILABLE_PRODUCTS_TABLE = 1;
    private static final int IS_EXPIRED_PRODUCTS_TABLE = 2;

    /**
     * Creates new form ExpiredProducts
     */
    public ManageProducts() {
        initComponents();
        var progress = new Progress();
        progress.startProgressBar(System.out::println, Progress.LOADING);
        final DefaultTableCellRenderer RENDERER = new DefaultTableCellRenderer();
        RENDERER.setHorizontalAlignment(JLabel.CENTER);
        availableProductsTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(2).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(3).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(4).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(5).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(6).setCellRenderer(RENDERER);
        expiredProductsTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        loadAvailableProducts();
        loadExpiredProducts();
    }

    /**
     * Method that loads the available products which are not expired from the database to the {@code List<Product>} AVAILABLE_PRODUCTS.
     */
    private void loadAvailableProducts() {

        AVAILABLE_PRODUCTS = PRODUCT_SERVICE
                .getAllProducts()
                .get()
                .stream()
                .filter(product -> !product.getExpired())
                .collect(Collectors.toList());
        refreshTable(IS_AVAILABLE_PRODUCTS_TABLE);
    }

    /**
     * Method that loads the expired products from the database to the {@code List<Product>} EXPIRED_PRODUCTS.
     */
    private void loadExpiredProducts() {
        EXPIRED_PRODUCTS = PRODUCT_SERVICE
                .getAllProducts()
                .get()
                .stream()
                .filter(Product::getExpired)
                .collect(Collectors.toList());
        refreshTable(IS_EXPIRED_PRODUCTS_TABLE);
    }

    /**
     * Method that refresh the availableProductsTable.
     */
    private void refreshTable(int whatTable) {
        if (whatTable == 1) {
            DefaultTableModel defaultTableModel = (DefaultTableModel) availableProductsTable.getModel();
            defaultTableModel.setRowCount(0);
            Object[] data = new Object[defaultTableModel.getColumnCount()];

            for (Product product : AVAILABLE_PRODUCTS) {
                data[0] = product.getId();
                data[1] = product.getName();
                data[2] = product.getPrice();
                data[3] = product.getCategory();
                data[4] = product.getExpirationDate();
                data[5] = product.getStocks();
                data[6] = (product.getDiscount() == null) ? "" : String.valueOf(product.getDiscount());

                defaultTableModel.addRow(data);
            }
        }
        if (whatTable == 2) {
            DefaultTableModel defaultTableModel = (DefaultTableModel) expiredProductsTable.getModel();
            defaultTableModel.setRowCount(0);
            Object[] data = new Object[defaultTableModel.getColumnCount()];

            for (Product product : EXPIRED_PRODUCTS) {
                data[0] = product.getId();
                data[1] = product.getName();

                defaultTableModel.addRow(data);
            }
        }
    }

    /**
     * Method that makes an Order object based from the selection from the order table.
     * @return the product id of a selected product. returns 0 if no product was selected.
     */
    private int getProductFromTableSelection() {
        try {
            return Integer.parseInt(String.valueOf(expiredProductsTable.getModel().getValueAt(expiredProductsTable.getSelectedRow(), 0)));
        } catch (RuntimeException runtimeException) {
            return 0;
        }
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
        mainPanelTab = new javax.swing.JTabbedPane();
        restockProductsPanel = new javax.swing.JPanel();
        avaialbleProductsTableScrollPane = new javax.swing.JScrollPane();
        availableProductsTable = new javax.swing.JTable();
        availableProductsHeader = new javax.swing.JLabel();
        availableProductsSubHeader = new javax.swing.JLabel();
        editStocks = new javax.swing.JButton();
        editPrice = new javax.swing.JButton();
        editDiscount = new javax.swing.JButton();
        expiredProductsPanel = new javax.swing.JPanel();
        expiredProductsHeader = new javax.swing.JLabel();
        expiredProductsScrollPane = new javax.swing.JScrollPane();
        expiredProductsTable = new javax.swing.JTable();
        removeProduct = new javax.swing.JButton();
        removeAllProducts = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanelTab.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        mainPanelTab.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                mainPanelTabComponentShown(evt);
            }
        });

        restockProductsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        availableProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "product_id", "product_name", "price", "category", "expiration_date", "stocks", "discount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        avaialbleProductsTableScrollPane.setViewportView(availableProductsTable);
        if (availableProductsTable.getColumnModel().getColumnCount() > 0) {
            availableProductsTable.getColumnModel().getColumn(0).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(1).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(2).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(3).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(4).setResizable(false);
        }

        restockProductsPanel.add(avaialbleProductsTableScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 890, 400));

        availableProductsHeader.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        availableProductsHeader.setText("AVAILABLE PRODUCTS ");
        restockProductsPanel.add(availableProductsHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        availableProductsSubHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        availableProductsSubHeader.setText("EXPIRED PRODUCTS ARE EXCLUDED");
        restockProductsPanel.add(availableProductsSubHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, -1, -1));

        editStocks.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editStocks.setText("EDIT STOCKS");
        restockProductsPanel.add(editStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 120, 30));

        editPrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editPrice.setText("EDIT PRICE");
        restockProductsPanel.add(editPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 490, 110, 30));

        editDiscount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editDiscount.setText("EDIT DISCOUNT");
        restockProductsPanel.add(editDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 490, 140, 30));

        mainPanelTab.addTab("EDIT PRODUCTS", restockProductsPanel);

        expiredProductsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        expiredProductsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expiredProductsHeader.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        expiredProductsHeader.setText("EXPIRED PRODUCTS");
        expiredProductsPanel.add(expiredProductsHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        expiredProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "product_id", "product_name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        expiredProductsScrollPane.setViewportView(expiredProductsTable);

        expiredProductsPanel.add(expiredProductsScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 910, 420));

        removeProduct.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        removeProduct.setText("REMOVE PRODUCT");
        removeProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProductActionPerformed(evt);
            }
        });
        expiredProductsPanel.add(removeProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 270, 40));

        removeAllProducts.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        removeAllProducts.setText("REMOVE ALL EXPIRED PRODUCTS");
        removeAllProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllProductsActionPerformed(evt);
            }
        });
        expiredProductsPanel.add(removeAllProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 490, 440, 40));

        mainPanelTab.addTab("VIEW EXPIRED PRODUCTS", expiredProductsPanel);

        mainPanel.add(mainPanelTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 580));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 927, 581));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Method that removes a product.
     * @param evt not used.
     */
    private void removeProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProductActionPerformed
        try {
            int selectedProduct = getProductFromTableSelection();
            if (EXPIRED_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO EXPIRED PRODUCTS TO BE REMOVED\nLIST IS EMPTY");
            if (selectedProduct == 0) throw new IllegalStateException("TO REMOVE A PRODUCT\nPLEASE SELECT A ROW FROM THE TABLE AND CLICK REMOVE PRODUCT");
            PRODUCT_SERVICE.deleteProductById().accept(selectedProduct);
            EXPIRED_PRODUCTS.removeIf(product -> product.getId().equals(selectedProduct));
            Main.PROMPT.show.accept("PRODUCT REMOVED SUCCESSFULLY", false);
            refreshTable(ManageProducts.IS_EXPIRED_PRODUCTS_TABLE);
        } catch (RuntimeException runtimeException) {
            Main.PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_removeProductActionPerformed

    /**
     * Method that removes all the expired products from the table in the database.
     * @param evt not used.
     */
    private void removeAllProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllProductsActionPerformed
        try {
            if (EXPIRED_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO EXPIRED PRODUCTS TO BE REMOVED\nLIST IS EMPTY");
            PRODUCT_SERVICE.deleteAllExpiredProducts();
            EXPIRED_PRODUCTS.clear();
            Main.PROMPT.show.accept("EXPIRED PRODUCTS REMOVED SUCCESSFULLY", false);
            refreshTable(ManageProducts.IS_EXPIRED_PRODUCTS_TABLE);
        } catch (RuntimeException runtimeException) {
            Main.PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_removeAllProductsActionPerformed

    private void mainPanelTabComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mainPanelTabComponentShown
        Main.PROMPT.show.accept(String.valueOf(evt.getComponent().getName().equals("RESTOCK PRODUCTS PANEL")), false);
    }//GEN-LAST:event_mainPanelTabComponentShown

    /**
     * main method.
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
            new ManageProducts().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane avaialbleProductsTableScrollPane;
    private javax.swing.JLabel availableProductsHeader;
    private javax.swing.JLabel availableProductsSubHeader;
    private javax.swing.JTable availableProductsTable;
    private javax.swing.JButton editDiscount;
    private javax.swing.JButton editPrice;
    private javax.swing.JButton editStocks;
    private javax.swing.JLabel expiredProductsHeader;
    private javax.swing.JPanel expiredProductsPanel;
    private javax.swing.JScrollPane expiredProductsScrollPane;
    private javax.swing.JTable expiredProductsTable;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTabbedPane mainPanelTab;
    private javax.swing.JButton removeAllProducts;
    private javax.swing.JButton removeProduct;
    private javax.swing.JPanel restockProductsPanel;
    // End of variables declaration//GEN-END:variables
}

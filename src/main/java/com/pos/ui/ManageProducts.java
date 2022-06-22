package com.pos.ui;

import com.pos.Checker;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.pos.service.ProductService;
import static com.pos.Main.OS_NAME;
import com.pos.entity.Product;
import java.util.ArrayList;
import com.pos.Config;
import java.util.List;
import javax.swing.*;
import com.pos.Main;
import static com.pos.Main.getExpiredProductsCount;
import static com.pos.Main.numberOfExpiredProducts;
import static com.pos.Main.setProductsPrices;

/**
 *
 * @author peter
 */
public class ManageProducts extends javax.swing.JFrame {
    
    public static List<Product> AVAILABLE_PRODUCTS = new ArrayList<>();

    public static List<Product> EXPIRED_PRODUCTS = new ArrayList<>();

    private final AbstractApplicationContext CONTEXT=  new AnnotationConfigApplicationContext(Config.class);
    private final ProductService PRODUCT_SERVICE = CONTEXT.getBean(ProductService.class);
    
    private static final int AVAILABLE_PRODUCTS_TABLE = 1;
    private static final int EXPIRED_PRODUCTS_TABLE = 2;
    
    private static final int EDIT_STOCKS = 3;
    private static final int EDIT_PRICE = 4;
    private static final int EDIT_DISCOUNT = 5;

    /**
     * Creates new form ExpiredProducts
     */
    public ManageProducts() {
        initComponents();
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
        refreshTable(AVAILABLE_PRODUCTS_TABLE);
        refreshTable(EXPIRED_PRODUCTS_TABLE);
    }

    /**
     * Method that loads the available products which are not expired from the database to the {@code List<Product>} AVAILABLE_PRODUCTS.
     */
    private void loadAvailableProducts() {
        PRODUCT_SERVICE
                .getAllProducts()
                .get()
                .stream()
                .filter(product -> !product.getExpired())
                .forEach(AVAILABLE_PRODUCTS::add);

    }
    /**
     * Method that loads the expired products from the database to the {@code List<Product>} EXPIRED_PRODUCTS.
     */
    private void loadExpiredProducts() {
        PRODUCT_SERVICE
                .getAllProducts()
                .get()
                .stream()
                .filter(Product::getExpired)
                .forEach(EXPIRED_PRODUCTS::add);
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
                data[6] = (product.getDiscount() == null || product.getDiscount() == 0.0) ? "" : String.valueOf(product.getDiscount());

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
        setProductsPrices();
        numberOfExpiredProducts.setForeground((getExpiredProductsCount() > 0) ? new java.awt.Color(255, 0, 0) : new java.awt.Color(0, 0, 255));
    }

    /**
     * Method that makes an Order object based from the selection from the order table.
     * @return the product id of a selected product. returns 0 if no product was selected.
     */
    private int getProductFromTable(int whatTable) {
        if(whatTable == 1) {
            try {
                return Integer.parseInt(String.valueOf(availableProductsTable.getModel().getValueAt(availableProductsTable.getSelectedRow(), 0)));
            } catch (RuntimeException runtimeException) {
                return 0;
            }
        } 
        if(whatTable == 2) {
            try {
                return Integer.parseInt(String.valueOf(expiredProductsTable.getModel().getValueAt(expiredProductsTable.getSelectedRow(), 0)));
            } catch (RuntimeException runtimeException) {
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * Method that update the product information.
     * @param whatToUpdate used for checking of what to update on the product info.
     */
    private void updateProduct(int whatToUpdate) {
        try {
            int selectedProductId = getProductFromTable(AVAILABLE_PRODUCTS_TABLE);
            if (AVAILABLE_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO AVAILABLE PRODUCTS");
            String options = (whatToUpdate == 3) ? "STOCKS" : (whatToUpdate == 4) ? "PRICE" : (whatToUpdate == 5) ? "DISCOUNT" : "NULL";
            if (selectedProductId == 0) throw new IllegalStateException("TO ADD " + options + "  TO A PRODUCT\nPLEASE SELECT A ROW FROM THE TABLE AND CLICK " + options);
            String temporaryString = String.valueOf(JOptionPane.showInputDialog("ENTER NEW " + options));

            int newStocks;
            double newPrice;
            double newDiscount;
            
            if (checkInput(temporaryString, whatToUpdate) || ((whatToUpdate == EDIT_DISCOUNT)) && temporaryString.equals("0")) {

                if(whatToUpdate == EDIT_STOCKS) {
                    newStocks = Integer.parseInt(temporaryString);
                    if(PRODUCT_SERVICE.getProductStocksById().apply(selectedProductId) > newStocks) throw new IllegalStateException("NEW STOCKS SHOULD NOT BE LESS THAN CURRENT STOCKS");
                    PRODUCT_SERVICE.updateProductStocksById().accept(newStocks, selectedProductId);
                }

                if(whatToUpdate == EDIT_PRICE) {
                    newPrice = Double.parseDouble(temporaryString);
                    if (newPrice <= 0) throw new IllegalStateException("INVALID PRICE\nPRICE SHOULD NOT BE LESS THAN OR EQUAL TO 0");
                    PRODUCT_SERVICE.updateProductPriceById().accept(newPrice, selectedProductId);
                }

                if(whatToUpdate == EDIT_DISCOUNT) {
                    if(temporaryString.equals("0")) newDiscount = 0;
                    else newDiscount = Double.parseDouble(temporaryString);
                    if (newDiscount <= 0) PRODUCT_SERVICE.updateProductDiscountById().accept(0.0, selectedProductId);
                    else PRODUCT_SERVICE.updateProductDiscountById().accept(newDiscount, selectedProductId);
                }
                AVAILABLE_PRODUCTS.clear();
                loadAvailableProducts();
                refreshTable(AVAILABLE_PRODUCTS_TABLE);
                Main.PROMPT.show.accept(options + " UPDATED SUCCESSFULLY", false);
            }
            else throw new IllegalStateException("INVALID " + options);
        } catch (RuntimeException runtimeException) {
            Main.PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }
    
    /**
     * Method that checks if the inputs of new product details is valid.
     * @param stringToCheck the {@code String} that contains a possible {@code Integer} or {@code Double} value.
     * @param whatToUpdate used for checking of what to update on the product info.
     * @return {@code true} if {@code String} stringToCheck is a valid {@code Integer} or {@code Double} value.  
     */
    private boolean checkInput(String stringToCheck, int whatToUpdate) {
        try {
            if(whatToUpdate == EDIT_STOCKS) Integer.parseInt(stringToCheck);
            else {
                switch (whatToUpdate) {
                    case EDIT_DISCOUNT -> {
                        if(stringToCheck.equals("0"));
                        return true;
                    }
                    case EDIT_PRICE -> Double.parseDouble(stringToCheck);
                    default -> {
                            return false;
                    }
                }
            }
            return true;
        } catch(RuntimeException ignored) {}
        return false;
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
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
        editStocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editStocksActionPerformed(evt);
            }
        });
        restockProductsPanel.add(editStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 120, 30));

        editPrice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editPrice.setText("EDIT PRICE");
        editPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPriceActionPerformed(evt);
            }
        });
        restockProductsPanel.add(editPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 490, 110, 30));

        editDiscount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        editDiscount.setText("EDIT DISCOUNT");
        editDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDiscountActionPerformed(evt);
            }
        });
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
            int selectedProduct = getProductFromTable(EXPIRED_PRODUCTS_TABLE);
            if (EXPIRED_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO EXPIRED PRODUCTS TO BE REMOVED\nLIST IS EMPTY");
            if (selectedProduct == 0) throw new IllegalStateException("TO REMOVE A PRODUCT\nPLEASE SELECT A ROW FROM THE TABLE AND CLICK REMOVE PRODUCT");
            PRODUCT_SERVICE.deleteProductById().accept(selectedProduct);
            EXPIRED_PRODUCTS.removeIf(product -> product.getId().equals(selectedProduct));
            Main.PROMPT.show.accept("PRODUCT REMOVED SUCCESSFULLY", false);
            refreshTable(EXPIRED_PRODUCTS_TABLE);
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
            loadExpiredProducts();
            refreshTable(EXPIRED_PRODUCTS_TABLE);
            Main.PROMPT.show.accept("EXPIRED PRODUCTS REMOVED SUCCESSFULLY", false);
        } catch (RuntimeException runtimeException) {
            Main.PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_removeAllProductsActionPerformed

    private void mainPanelTabComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mainPanelTabComponentShown
        Main.PROMPT.show.accept(String.valueOf(evt.getComponent().getName().equals("RESTOCK PRODUCTS PANEL")), false);
    }//GEN-LAST:event_mainPanelTabComponentShown

    private void editStocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editStocksActionPerformed
        updateProduct(EDIT_STOCKS);
    }//GEN-LAST:event_editStocksActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        AVAILABLE_PRODUCTS.clear();
        EXPIRED_PRODUCTS.clear();
    }//GEN-LAST:event_formWindowClosed

    private void editPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPriceActionPerformed
        updateProduct(EDIT_PRICE);
    }//GEN-LAST:event_editPriceActionPerformed

    private void editDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDiscountActionPerformed
        updateProduct(EDIT_DISCOUNT);
    }//GEN-LAST:event_editDiscountActionPerformed

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

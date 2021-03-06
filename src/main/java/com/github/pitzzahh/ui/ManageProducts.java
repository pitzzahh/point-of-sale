package com.github.pitzzahh.ui;

import javax.swing.table.DefaultTableCellRenderer;
import static com.github.pitzzahh.ui.Main.*;
import javax.swing.table.DefaultTableModel;
import com.github.pitzzahh.entity.Product;
import com.github.pitzzahh.enums.Status;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import javax.swing.*;
import java.net.URL;
import java.awt.*;

/**
 * UI used to acces and modify the products details.
 * @author peter
 */
public class ManageProducts extends javax.swing.JFrame {
    
    public static List<Product> AVAILABLE_PRODUCTS = new ArrayList<>();

    public static List<Product> EXPIRED_PRODUCTS = new ArrayList<>();

    private static final int AVAILABLE_PRODUCTS_TABLE = 1;
    private static final int EXPIRED_PRODUCTS_TABLE = 2;
    
    private static final int EDIT_STOCKS = 3;
    private static final int EDIT_PRICE = 4;
    private static final int EDIT_DISCOUNT = 5;

    // <editor-fold defaultstate="collapsed" desc="Creates new form ExpiredProducts">//
    public ManageProducts() {
        FlatDarkLaf.setup();
        initComponents();
        setIcon();
        final DefaultTableCellRenderer RENDERER = new DefaultTableCellRenderer();
        RENDERER.setHorizontalAlignment(JLabel.CENTER);
        availableProductsTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(2).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(3).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(4).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(5).setCellRenderer(RENDERER);
        availableProductsTable.getColumnModel().getColumn(6).setCellRenderer(RENDERER);
        expiredProductsTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        AVAILABLE_PRODUCTS.clear();
        EXPIRED_PRODUCTS.clear();
        loadAvailableProducts();
        loadExpiredProducts();
        refreshTable(AVAILABLE_PRODUCTS_TABLE);
        refreshTable(EXPIRED_PRODUCTS_TABLE);
    } // </editor-fold>//

    /**
     * Method that loads the available products which are not expired from the database to the List<Product> AVAILABLE_PRODUCTS.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that loads the available products which are not expired from the database to the List<Product> AVAILABLE_PRODUCTS.">//
    private void loadAvailableProducts() {
        ALL_PRODUCTS
                .stream()
                .map(Optional::get)
                .filter(p -> p.getExpirationDate().isAfter(LocalDate.now()))
                .forEach(AVAILABLE_PRODUCTS::add);

    } // </editor-fold>//

    /**
     * Method that loads the expired products from the database to the List<Product> EXPIRED_PRODUCTS.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that loads the expired products from the database to the List<Product> EXPIRED_PRODUCTS.">//
    private void loadExpiredProducts() {
        Main.ALL_PRODUCTS
                .stream()
                .map(Optional::get)
                .filter(p -> p.getExpirationDate().isBefore(LocalDate.now()))
                .forEach(EXPIRED_PRODUCTS::add);
    } // </editor-fold>//

    /**
     * Method that refresh the availableProductsTable.
     * @param whatTable refers to the table if available products or expired products.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that refresh the availableProductsTable.">//
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
        ALL_PRODUCTS.clear();
        Main.getAllProductsToList();
        setProductsInfo();
        numberOfExpiredProducts.setText(String.valueOf(getExpiredProductsCount()));
        numberOfExpiredProducts.setForeground((getExpiredProductsCount() > 0) ? new java.awt.Color(255, 0, 0) : new java.awt.Color(0, 0, 255));
    } // </editor-fold>//

    /**
     * Method that returns the index of a product / sales when a table row is selected.
     * @param whatTable refers to the table if available products or expired products.
     * @return the index of the product from the table, otherwise 0.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that returns the index of a product / sales when a table row is selected.">//
    private int getProductIndexFromTheTable(int whatTable) {
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
    } // </editor-fold>//

    /**
     * Method that update the product information.
     * @param whatToUpdate used for checking of what to update on the product info.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that update the product information. @param whatToUpdate used for checking of what to update on the product info.">//
    private void updateProduct(int whatToUpdate) {
        String temporaryString = "";
        Status status = Status.ERROR;
        try {
            int selectedProductId = getProductIndexFromTheTable(AVAILABLE_PRODUCTS_TABLE);
            if (AVAILABLE_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO AVAILABLE PRODUCTS");
            String options = (whatToUpdate == EDIT_STOCKS) ? "STOCKS" : (whatToUpdate == EDIT_PRICE) ? "PRICE" : (whatToUpdate == EDIT_DISCOUNT) ? "DISCOUNT" : "NULL";
            if (selectedProductId == 0) throw new IllegalStateException("TO ADD " + options + "  TO A PRODUCT\nPLEASE SELECT A ROW FROM THE TABLE AND CLICK " + options);
            temporaryString = String.valueOf(JOptionPane.showInputDialog("ENTER NEW " + options));

            int newStocks;
            double newPrice;
            double newDiscount;
            if (checkInput(temporaryString, whatToUpdate) || ((whatToUpdate == EDIT_DISCOUNT)) && temporaryString.equals("0")) {
                Optional<Product> product = Optional.of(AVAILABLE_PRODUCTS.stream().filter(p -> p.getId() == selectedProductId).findAny().get());

                if(whatToUpdate == EDIT_STOCKS) {
                    newStocks = Integer.parseInt(temporaryString);
                    if (newStocks <= 0) throw new IllegalStateException("NEW STOCK SHOULD NOT BE LESS THAN OR EQUAL TO 0");
                    if(product.get().getStocks() > newStocks) throw new IllegalStateException("NEW STOCKS SHOULD NOT BE LESS THAN CURRENT STOCKS");
                    status = Main.PRODUCT_SERVICE.updateProductStocksByName().apply(newStocks, product.get().getName());
                }

                if(whatToUpdate == EDIT_PRICE) {
                    newPrice = Double.parseDouble(temporaryString);
                    if (newPrice <= 0) throw new IllegalStateException("INVALID PRICE\nPRICE SHOULD NOT BE LESS THAN OR EQUAL TO 0");
                    status = Main.PRODUCT_SERVICE.updateProductPriceById().apply(newPrice, selectedProductId);
                }

                if(whatToUpdate == EDIT_DISCOUNT) {
                    if(temporaryString.equals("0")) newDiscount = 0;
                    else newDiscount = Double.parseDouble(temporaryString);
                    double product_price = product.get().getPrice();
                    if (newDiscount >= product_price) throw new IllegalStateException("DISCOUNT SHOULD NOT BE GREATER THAN OR EQUAL TO THE PRICE OF THE PRODUCT");
                    else if (newDiscount == 0) Main.PRODUCT_SERVICE.updateProductDiscountById().apply(0.0, selectedProductId);
                    else if (newDiscount < 0 ) throw new IllegalStateException("DISCOUNT SHOULD NOT BE LESS THAN 0");
                    status = Main.PRODUCT_SERVICE.updateProductDiscountById().apply(newDiscount, selectedProductId);
                }
                AVAILABLE_PRODUCTS.clear();
                loadAvailableProducts();
                refreshTable(AVAILABLE_PRODUCTS_TABLE);
                if(!temporaryString.trim().isEmpty()) Main.PROMPT.show.accept(status.toString(), false);
            }
            else throw new IllegalStateException("INVALID " + options);
        } catch (RuntimeException runtimeException) {
            if(temporaryString != null) Main.PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    } // </editor-fold>//

    /**
     * Method that checks if the inputs of new product details is valid.
     * @param stringToCheck contains an integer value or double value to be parsed.
     * @param whatToUpdate used for checking of what to update on the product info.
     * @return {@code true} if the string is a valid Integer or a Double value, otherwise false.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that checks if the inputs of new product details is valid. returns true if the string is a valid Integer or a Double value.">//
    private boolean checkInput(String stringToCheck, int whatToUpdate) {
        try {
            if(whatToUpdate == EDIT_STOCKS) Integer.parseInt(stringToCheck);
            else {
                switch (whatToUpdate) {
                    case EDIT_DISCOUNT -> {
                        if(stringToCheck.equals("0")) return true;
                        Double.parseDouble(stringToCheck);
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
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that sets the icon for this frame.">//
    private void setIcon() {
        try {
            Image img = ImageIO.read(new URL("https://github.com/pitzzahh/point-of-sale/blob/220ccaa9681f18faa17a76b38ed6d91764303c5b/src/main/resources/edit.png?raw=true"));
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
        mainPanelTab = new javax.swing.JTabbedPane();
        restockProductsPanel = new javax.swing.JPanel();
        availableProductsTableScrollPane = new javax.swing.JScrollPane();
        availableProductsTable = new javax.swing.JTable();
        availableProductsHeader = new javax.swing.JLabel();
        availableProductsSubHeader = new javax.swing.JLabel();
        editStocks = new javax.swing.JButton();
        editPrice = new javax.swing.JButton();
        editDiscount = new javax.swing.JButton();
        close = new javax.swing.JButton();
        expiredProductsPanel = new javax.swing.JPanel();
        expiredProductsHeader = new javax.swing.JLabel();
        expiredProductsScrollPane = new javax.swing.JScrollPane();
        expiredProductsTable = new javax.swing.JTable();
        removeProduct = new javax.swing.JButton();
        removeAllProducts = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBackground(new java.awt.Color(204, 204, 0));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanelTab.setForeground(new java.awt.Color(255, 255, 255));
        mainPanelTab.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 18)); // NOI18N
        mainPanelTab.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                mainPanelTabComponentShown(evt);
            }
        });

        restockProductsPanel.setBackground(new java.awt.Color(51, 0, 51));
        restockProductsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        availableProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "product_id", "product_name", "price", "category", "expiration_date", "stocks", "discount"
            }
        ) {
            final Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            final boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        availableProductsTableScrollPane.setViewportView(availableProductsTable);
        if (availableProductsTable.getColumnModel().getColumnCount() > 0) {
            availableProductsTable.getColumnModel().getColumn(0).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(1).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(2).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(3).setResizable(false);
            availableProductsTable.getColumnModel().getColumn(4).setResizable(false);
        }

        restockProductsPanel.add(availableProductsTableScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 870, 420));

        availableProductsHeader.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        availableProductsHeader.setForeground(new java.awt.Color(255, 255, 255));
        availableProductsHeader.setText("AVAILABLE PRODUCTS ");
        restockProductsPanel.add(availableProductsHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        availableProductsSubHeader.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        availableProductsSubHeader.setForeground(new java.awt.Color(255, 255, 255));
        availableProductsSubHeader.setText("EXPIRED PRODUCTS ARE EXCLUDED");
        restockProductsPanel.add(availableProductsSubHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, -1, -1));

        editStocks.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14)); // NOI18N
        editStocks.setText("EDIT STOCKS");
        editStocks.addActionListener(this::editStocksActionPerformed);
        restockProductsPanel.add(editStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 120, 30));

        editPrice.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14)); // NOI18N
        editPrice.setText("EDIT PRICE");
        editPrice.addActionListener(this::editPriceActionPerformed);
        restockProductsPanel.add(editPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, 110, 30));

        editDiscount.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14)); // NOI18N
        editDiscount.setText("EDIT DISCOUNT");
        editDiscount.addActionListener(this::editDiscountActionPerformed);
        restockProductsPanel.add(editDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 500, 140, 30));

        close.setText("CLOSE");
        close.addActionListener(this::closeActionPerformed);
        restockProductsPanel.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 500, 80, 30));

        mainPanelTab.addTab("EDIT PRODUCTS", restockProductsPanel);

        expiredProductsPanel.setBackground(new java.awt.Color(51, 0, 51));
        expiredProductsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        expiredProductsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expiredProductsHeader.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 36)); // NOI18N
        expiredProductsHeader.setForeground(new java.awt.Color(255, 255, 255));
        expiredProductsHeader.setText("EXPIRED PRODUCTS");
        expiredProductsPanel.add(expiredProductsHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, -1, -1));

        expiredProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "product_id", "product_name"
            }
        ) {
            final Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };
            final boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expiredProductsTable.getTableHeader().setReorderingAllowed(false);
        expiredProductsScrollPane.setViewportView(expiredProductsTable);
        if (expiredProductsTable.getColumnModel().getColumnCount() > 0) {
            expiredProductsTable.getColumnModel().getColumn(0).setResizable(false);
            expiredProductsTable.getColumnModel().getColumn(1).setResizable(false);
        }

        expiredProductsPanel.add(expiredProductsScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 870, 420));

        removeProduct.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        removeProduct.setText("REMOVE PRODUCT");
        removeProduct.addActionListener(this::removeProductActionPerformed);
        expiredProductsPanel.add(removeProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 270, 40));

        removeAllProducts.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        removeAllProducts.setText("REMOVE ALL EXPIRED PRODUCTS");
        removeAllProducts.addActionListener(this::removeAllProductsActionPerformed);
        expiredProductsPanel.add(removeAllProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 490, 440, 40));

        mainPanelTab.addTab("VIEW EXPIRED PRODUCTS", expiredProductsPanel);

        mainPanel.add(mainPanelTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 580));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 581));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Method that removes a product.
     * @param evt not used.
     */
    private void removeProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProductActionPerformed
        try {
            int selectedProduct = getProductIndexFromTheTable(EXPIRED_PRODUCTS_TABLE);
            if (EXPIRED_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO EXPIRED PRODUCTS TO BE REMOVED\nLIST IS EMPTY");
            if (selectedProduct == 0) throw new IllegalStateException("TO REMOVE A PRODUCT\nPLEASE SELECT A ROW FROM THE TABLE AND CLICK REMOVE PRODUCT");
            Main.PRODUCT_SERVICE.deleteProductById().apply(selectedProduct);
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
        Status status;
        try {
            if (EXPIRED_PRODUCTS.isEmpty()) throw new IllegalStateException("THERE ARE NO EXPIRED PRODUCTS TO BE REMOVED\nLIST IS EMPTY");
            status = Main.PRODUCT_SERVICE.deleteAllExpiredProducts().get();
            EXPIRED_PRODUCTS.clear();
            loadExpiredProducts();
            refreshTable(EXPIRED_PRODUCTS_TABLE);
            Main.PROMPT.show.accept(status.toString(), false);
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

    private void editPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPriceActionPerformed
        updateProduct(EDIT_PRICE);
    }//GEN-LAST:event_editPriceActionPerformed

    private void editDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDiscountActionPerformed
        updateProduct(EDIT_DISCOUNT);
    }//GEN-LAST:event_editDiscountActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeActionPerformed

    // <editor-fold defaultstate="collapsed" desc="Runs this frame.">//
    public void run() {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* displays the form */
        java.awt.EventQueue.invokeLater(() -> this.setVisible(true));
    } // </editor-fold>//

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane availableProductsTableScrollPane;
    private javax.swing.JLabel availableProductsHeader;
    private javax.swing.JLabel availableProductsSubHeader;
    private javax.swing.JTable availableProductsTable;
    private javax.swing.JButton close;
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

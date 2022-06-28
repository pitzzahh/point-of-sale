package com.pos.ui;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static com.pos.validation.ProductValidator.ValidationResult.EXPIRED;
import org.springframework.context.support.AbstractApplicationContext;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.pos.validation.ProductValidator;
import com.pos.service.ProductService;
import com.pos.service.SalesService;
import com.formdev.flatlaf.FlatDarkLaf;
import com.pos.validation.Checker;
import java.util.stream.Collectors;
import com.pos.entity.Category;
import java.text.NumberFormat;
import com.pos.entity.Product;
import javax.imageio.ImageIO;
import com.pos.entity.Sales;
import com.pos.entity.Order;
import java.time.LocalDate;
import java.util.List;
import com.pos.Config;
import javax.swing.*;
import java.net.URL;
import java.util.*;
import java.awt.*;

/**
 * Main user interface of point of sale system.
 * JDK version used: 17 LTS
 * @author peter
 */
public class Main extends JFrame {
    
    private static final AbstractApplicationContext CONTEXT=  new AnnotationConfigApplicationContext(Config.class);

    private static final ProductService PRODUCT_SERVICE = CONTEXT.getBean(ProductService.class);

    private final SalesService SALES_SERVICE = CONTEXT.getBean(SalesService.class);

    public static final String OS_NAME = System.getProperty("os.name", "").toUpperCase();

    private final List<Order> ORDERS_LIST = new ArrayList<>();

    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.ENGLISH);

    public static final Prompt PROMPT = new Prompt();


    // <editor-fold defaultstate="collapsed" desc="Creates a main form.">//
    public Main() {
        PRODUCT_SERVICE.insertAllProductsToDatabase();
        FlatDarkLaf.setup();
        initComponents();
        setIcon();
        initializeDate();
        day.setText(LocalDate.now().getDayOfWeek().name());
        setProductsInfo();
        final DefaultTableCellRenderer RENDERER = new DefaultTableCellRenderer();
        RENDERER.setHorizontalAlignment(JLabel.CENTER);
        ordersTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(1).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(2).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(3).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(4).setCellRenderer(RENDERER);
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Creates a Hashtable of expired products.">//
    private static Hashtable<Integer, Boolean> getExpiredProducts() {
        return PRODUCT_SERVICE.getAllProducts()
                .get()
                .stream()
                .filter(Product::getExpired)
                .collect(Collectors.toMap(
                        Product::getId, Product::getExpired,
                        (key, value) -> {
                            throw new IllegalStateException(
                                    String.format("Cannot have 2 values (%s, %s) for the same key", key, value)
                            );
                        }, Hashtable::new
                ));
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Method that refresh the order's table for every button click of a product.">//
    private void refreshOrdersTable() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) ordersTable.getModel();
        defaultTableModel.setRowCount(0);
        Object[] data = new Object[defaultTableModel.getColumnCount()];

        for (Order order : ORDERS_LIST) {
            data[0] = order.getName();
            data[1] = order.getPrice();
            data[2] = order.getCategory();
            data[3] = order.getQuantity();
            data[4] = (order.getDiscount() == 0.0) ? "" : String.valueOf(order.getDiscount());

            defaultTableModel.addRow(data);
        }
        updateTransaction();
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that makes a new order. Takes a product to be searched for order modification">//
    private void makeOrder(Product product) {
        ORDERS_LIST.add(new Order(
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                1,
                (product.getDiscount() != null) ? product.getDiscount() : 0.0
        ));
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that modifies the order. Takes a product to be searched for order modification">//
    private void modifyOrder(Product product, boolean isRemovingProduct) {
        Order order = ORDERS_LIST.stream()
                .filter(o -> o.getName().equals(product.getName()))
                .findAny()
                .get(); // no checking if product is not present because products are sure available.

        int productQuantity = ORDERS_LIST.stream()
                .filter(o -> o.getName().equals(order.getName()))
                .map(Order::getQuantity)
                .findAny()
                .get();

        if (productQuantity == 1 && isRemovingProduct) ORDERS_LIST.removeIf(o -> o.getName().equals(order.getName()));
        else {
            ORDERS_LIST.stream()
                    .filter(o -> o.getName().equals(order.getName()))
                    .findAny()
                    .get()
                    .setQuantity(isRemovingProduct ? (order.getQuantity() - 1) : order.getQuantity() + 1);
        }
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that sets the product information on the labels.">//
    public static void setProductsInfo() {
        final char PESO_SIGN = '₱';

        Hashtable<Integer, Double> priceList = PRODUCT_SERVICE.getAllProducts()
                .get()
                .stream()
                .collect(Collectors.toMap(
                        Product::getId, Product::getPrice,
                        (key, value) -> {
                            throw new IllegalStateException(
                                    String.format("Cannot have 2 values (%s, %s) for the same key", key, value)
                            );
                        }, Hashtable::new
                ));

        Hashtable<Integer, Boolean> expiredProducts = getExpiredProducts();

        Hashtable<Integer, Boolean> outOfStockProducts = PRODUCT_SERVICE.getAllProducts()
                .get()
                .stream()
                .filter(p -> p.getStocks() == 0)
                .collect(Collectors.toMap(
                        Product::getId, p -> p.getStocks() == 0,
                        (key, value) -> {
                            throw new IllegalStateException(
                                    String.format("Cannot have 2 values (%s, %s) for the same key", key, value)
                            );
                        }, Hashtable::new
                ));

        /*
            Setting prices label for cleaning products
        */
        cleanFirstPrice.setText((expiredProducts.containsKey(1)) ? "EXPIRED" : (outOfStockProducts.containsKey(1)) ? "OUT OF STOCK" : (priceList.containsKey(1)) ? PESO_SIGN + " " + priceList.get(1) : "NOT AVAILABLE");
        spinMopPrice.setText((expiredProducts.containsKey(2) ? "EXPIRED" : (outOfStockProducts.containsKey(2)) ? "OUT OF STOCK" : (priceList.containsKey(2) ? PESO_SIGN + " " + priceList.get(2) : "NOT AVAILABLE")));
        windexPrice.setText((expiredProducts.containsKey(3) ? "EXPIRED" : (outOfStockProducts.containsKey(3)) ? "OUT OF STOCK" : (priceList.containsKey(3) ? PESO_SIGN + " " + priceList.get(3) : "NOT AVAILABLE")));
        cloroxPrice.setText((expiredProducts.containsKey(4) ? "EXPIRED" : (outOfStockProducts.containsKey(4)) ? "OUT OF STOCK" : (priceList.containsKey(4) ? PESO_SIGN + " " + priceList.get(4) : "NOT AVAILABLE")));
        dysonPrice.setText((expiredProducts.containsKey(5) ? "EXPIRED" : (outOfStockProducts.containsKey(5)) ? "OUT OF STOCK" : (priceList.containsKey(5) ? PESO_SIGN + " " + priceList.get(5) : "NOT AVAILABLE")));
        roombaPrice.setText((expiredProducts.containsKey(6) ? "EXPIRED" : (outOfStockProducts.containsKey(6)) ? "OUT OF STOCK" : (priceList.containsKey(6) ? PESO_SIGN + " " + priceList.get(6) : "NOT AVAILABLE")));
        cleanCutPrice.setText((expiredProducts.containsKey(7) ? "EXPIRED" : (outOfStockProducts.containsKey(7)) ? "OUT OF STOCK" : (priceList.containsKey(7) ? PESO_SIGN + " " + priceList.get(7) : "NOT AVAILABLE")));
        sureCleanPrice.setText((expiredProducts.containsKey(8) ? "EXPIRED" : (outOfStockProducts.containsKey(8)) ? "OUT OF STOCK" : (priceList.containsKey(8) ? PESO_SIGN + " " + priceList.get(8) : "NOT AVAILABLE")));
        arielPrice.setText((expiredProducts.containsKey(9) ? "EXPIRED" : (outOfStockProducts.containsKey(9)) ? "OUT OF STOCK" : (priceList.containsKey(9) ? PESO_SIGN + " " + priceList.get(9) : "NOT AVAILABLE")));
        joyPrice.setText(((expiredProducts.containsKey(10) ? "EXPIRED" : (outOfStockProducts.containsKey(10)) ? "OUT OF STOCK" : priceList.containsKey(10) ? PESO_SIGN + " " + priceList.get(10) : "NOT AVAILABLE")));
        smartPrice.setText((expiredProducts.containsKey(11) ? "EXPIRED" : (outOfStockProducts.containsKey(11)) ? "OUT OF STOCK" : (priceList.containsKey(11) ? PESO_SIGN + " " + priceList.get(11) : "NOT AVAILABLE")));
        domexPrice.setText((expiredProducts.containsKey(12) ? "EXPIRED" : (outOfStockProducts.containsKey(12)) ? "OUT OF STOCK" : (priceList.containsKey(12) ? PESO_SIGN + " " + priceList.get(12) : "NOT AVAILABLE")));
        mrMusclePrice.setText((expiredProducts.containsKey(13) ? "EXPIRED" : (outOfStockProducts.containsKey(13)) ? "OUT OF STOCK" : (priceList.containsKey(13) ? PESO_SIGN + " " + priceList.get(13) : "NOT AVAILABLE")));
        lysolPrice.setText((expiredProducts.containsKey(14) ? "EXPIRED" : (outOfStockProducts.containsKey(14)) ? "OUT OF STOCK" : (priceList.containsKey(14) ? PESO_SIGN + " " + priceList.get(14) : "NOT AVAILABLE")));
        surfPrice.setText((expiredProducts.containsKey(15) ? "EXPIRED" : (outOfStockProducts.containsKey(15)) ? "OUT OF STOCK" : (priceList.containsKey(15) ? PESO_SIGN + " " + priceList.get(15) : "NOT AVAILABLE")));

        /*
            Setting prices label for chocolates
        */
        hersheysPrice.setText((expiredProducts.containsKey(16) ? "EXPIRED" : (outOfStockProducts.containsKey(16)) ? "OUT OF STOCK" : (priceList.containsKey(16) ? PESO_SIGN + " " + priceList.get(16) : "NOT AVAILABLE")));
        snickersPrice.setText((expiredProducts.containsKey(17) ? "EXPIRED" : (outOfStockProducts.containsKey(17)) ? "OUT OF STOCK" : (priceList.containsKey(17) ? PESO_SIGN + " " + priceList.get(17) : "NOT AVAILABLE")));
        ferreroRocherPrice.setText((expiredProducts.containsKey(18) ? "EXPIRED" : (outOfStockProducts.containsKey(18)) ? "OUT OF STOCK" : (priceList.containsKey(18) ? PESO_SIGN + " " + priceList.get(18) : "NOT AVAILABLE")));
        esthechocPrice.setText((expiredProducts.containsKey(19) ? "EXPIRED" : (outOfStockProducts.containsKey(19)) ? "OUT OF STOCK" : (priceList.containsKey(19) ? PESO_SIGN + " " + priceList.get(19) : "NOT AVAILABLE")));
        flyingNoirPrice.setText((expiredProducts.containsKey(20) ? "EXPIRED" : (outOfStockProducts.containsKey(20)) ? "OUT OF STOCK" : (priceList.containsKey(20) ? PESO_SIGN + " " + priceList.get(20) : "NOT AVAILABLE")));
        drostePrice.setText((expiredProducts.containsKey(21) ? "EXPIRED" : (outOfStockProducts.containsKey(21)) ? "OUT OF STOCK" : (priceList.containsKey(21) ? PESO_SIGN + " " + priceList.get(21) : "NOT AVAILABLE")));
        whittakersPrice.setText((expiredProducts.containsKey(22) ? "EXPIRED" : (outOfStockProducts.containsKey(22)) ? "OUT OF STOCK" : (priceList.containsKey(22) ? PESO_SIGN + " " + priceList.get(22) : "NOT AVAILABLE")));
        amedeiPrice.setText((expiredProducts.containsKey(23) ? "EXPIRED" : (outOfStockProducts.containsKey(23)) ? "OUT OF STOCK" : (priceList.containsKey(23) ? PESO_SIGN + " " + priceList.get(23) : "NOT AVAILABLE")));
        jacquesGeninPrice.setText((expiredProducts.containsKey(24) ? "EXPIRED" : (outOfStockProducts.containsKey(24)) ? "OUT OF STOCK" : (priceList.containsKey(24) ? PESO_SIGN + " " + priceList.get(24) : "NOT AVAILABLE")));
        richartPrice.setText((expiredProducts.containsKey(25) ? "EXPIRED" : (outOfStockProducts.containsKey(25)) ? "OUT OF STOCK" : (priceList.containsKey(25) ? PESO_SIGN + " " + priceList.get(25) : "NOT AVAILABLE")));
        patchiPrice.setText((expiredProducts.containsKey(26) ? "EXPIRED" : (outOfStockProducts.containsKey(26)) ? "OUT OF STOCK" : (priceList.containsKey(26) ? PESO_SIGN + " " + priceList.get(26) : "NOT AVAILABLE")));
        teuscherPrice.setText((expiredProducts.containsKey(27) ? "EXPIRED" : (outOfStockProducts.containsKey(27)) ? "OUT OF STOCK" : (priceList.containsKey(27) ? PESO_SIGN + " " + priceList.get(27) : "NOT AVAILABLE")));
        valrhonaPrice.setText((expiredProducts.containsKey(28) ? "EXPIRED" : (outOfStockProducts.containsKey(28)) ? "OUT OF STOCK" : (priceList.containsKey(28) ? PESO_SIGN + " " + priceList.get(28) : "NOT AVAILABLE")));
        dovePrice.setText((expiredProducts.containsKey(29) ? "EXPIRED" : (outOfStockProducts.containsKey(29)) ? "OUT OF STOCK" : (priceList.containsKey(29) ? PESO_SIGN + " " + priceList.get(29) : "NOT AVAILABLE")));
        russelStoverPrice.setText((expiredProducts.containsKey(30) ? "EXPIRED" : (outOfStockProducts.containsKey(30)) ? "OUT OF STOCK" : (priceList.containsKey(30) ? PESO_SIGN + " " + priceList.get(30) : "NOT AVAILABLE")));
        ritterSportPrice.setText((expiredProducts.containsKey(31) ? "EXPIRED" : (outOfStockProducts.containsKey(31)) ? "OUT OF STOCK" : (priceList.containsKey(31) ? PESO_SIGN + " " + priceList.get(31) : "NOT AVAILABLE")));
        guyLianPrice.setText((expiredProducts.containsKey(32) ? "EXPIRED" : (outOfStockProducts.containsKey(32)) ? "OUT OF STOCK" : (priceList.containsKey(32) ? PESO_SIGN + " " + priceList.get(32) : "NOT AVAILABLE")));
        kinderPrice.setText((expiredProducts.containsKey(33) ? "EXPIRED" : (outOfStockProducts.containsKey(33)) ? "OUT OF STOCK" : (priceList.containsKey(33) ? PESO_SIGN + " " + priceList.get(33) : "NOT AVAILABLE")));
        marsPrice.setText((expiredProducts.containsKey(34) ? "EXPIRED" : (outOfStockProducts.containsKey(34)) ? "OUT OF STOCK" : (priceList.containsKey(34) ? PESO_SIGN + " " + priceList.get(34) : "NOT AVAILABLE")));
        tobleronePrice.setText((expiredProducts.containsKey(35) ? "EXPIRED" : (outOfStockProducts.containsKey(35)) ? "OUT OF STOCK" : (priceList.containsKey(35) ? PESO_SIGN + " " + priceList.get(35) : "NOT AVAILABLE")));
        nestlePrice.setText((expiredProducts.containsKey(36) ? "EXPIRED" : (outOfStockProducts.containsKey(36)) ? "OUT OF STOCK" : (priceList.containsKey(36) ? PESO_SIGN + " " + priceList.get(36) : "NOT AVAILABLE")));
        milkaPrice.setText((expiredProducts.containsKey(37) ? "EXPIRED" : (outOfStockProducts.containsKey(37)) ? "OUT OF STOCK" : (priceList.containsKey(37) ? PESO_SIGN + " " + priceList.get(37) : "NOT AVAILABLE")));
        ghirardelliPrice.setText((expiredProducts.containsKey(38) ? "EXPIRED" : (outOfStockProducts.containsKey(38)) ? "OUT OF STOCK" : (priceList.containsKey(38) ? PESO_SIGN + " " + priceList.get(38) : "NOT AVAILABLE")));
        cadburyPrice.setText((expiredProducts.containsKey(39) ? "EXPIRED" : (outOfStockProducts.containsKey(39)) ? "OUT OF STOCK" : (priceList.containsKey(39) ? PESO_SIGN + " " + priceList.get(39) : "NOT AVAILABLE")));
        godivaPrice.setText((expiredProducts.containsKey(40) ? "EXPIRED" : (outOfStockProducts.containsKey(40)) ? "OUT OF STOCK" : (priceList.containsKey(40) ? PESO_SIGN + " " + priceList.get(40) : "NOT AVAILABLE")));

        /*
            Setting price labels for beverages
        */
        cocaColaPrice.setText((expiredProducts.containsKey(41) ? "EXPIRED" : (outOfStockProducts.containsKey(41)) ? "OUT OF STOCK" : (priceList.containsKey(41) ? PESO_SIGN + " " + priceList.get(41) : "NOT AVAILABLE")));
        pepsiPrice.setText((expiredProducts.containsKey(42) ? "EXPIRED" : (outOfStockProducts.containsKey(42)) ? "OUT OF STOCK" : (priceList.containsKey(42) ? PESO_SIGN + " " + priceList.get(42) : "NOT AVAILABLE")));
        redBullPrice.setText((expiredProducts.containsKey(43) ? "EXPIRED" : (outOfStockProducts.containsKey(42)) ? "OUT OF STOCK" : (priceList.containsKey(43) ? PESO_SIGN + " " + priceList.get(43) : "NOT AVAILABLE")));
        budWeiserPrice.setText((expiredProducts.containsKey(44) ? "EXPIRED" : (outOfStockProducts.containsKey(44)) ? "OUT OF STOCK" : (priceList.containsKey(44) ? PESO_SIGN + " " + priceList.get(44) : "NOT AVAILABLE")));
        heinekenPrice.setText((expiredProducts.containsKey(45) ? "EXPIRED" : (outOfStockProducts.containsKey(45)) ? "OUT OF STOCK" : (priceList.containsKey(45) ? PESO_SIGN + " " + priceList.get(45) : "NOT AVAILABLE")));
        gatoradePrice.setText((expiredProducts.containsKey(46) ? "EXPIRED" : (outOfStockProducts.containsKey(46)) ? "OUT OF STOCK" : (priceList.containsKey(46) ? PESO_SIGN + " " + priceList.get(46) : "NOT AVAILABLE")));
        spritePrice.setText((expiredProducts.containsKey(47) ? "EXPIRED" : (outOfStockProducts.containsKey(47)) ? "OUT OF STOCK" : (priceList.containsKey(47) ? PESO_SIGN + " " + priceList.get(47) : "NOT AVAILABLE")));
        minuteMaidPrice.setText((expiredProducts.containsKey(48) ? "EXPIRED" : (outOfStockProducts.containsKey(48)) ? "OUT OF STOCK" : (priceList.containsKey(48) ? PESO_SIGN + " " + priceList.get(48) : "NOT AVAILABLE")));
        tropicanaPrice.setText((expiredProducts.containsKey(49) ? "EXPIRED" : (outOfStockProducts.containsKey(49)) ? "OUT OF STOCK" : (priceList.containsKey(49) ? PESO_SIGN + " " + priceList.get(49) : "NOT AVAILABLE")));
        dolePrice.setText((expiredProducts.containsKey(50) ? "EXPIRED" : (outOfStockProducts.containsKey(50)) ? "OUT OF STOCK" : (priceList.containsKey(50) ? PESO_SIGN + " " + priceList.get(50) : "NOT AVAILABLE")));
        koolAidPrice.setText((expiredProducts.containsKey(51) ? "EXPIRED" : (outOfStockProducts.containsKey(51)) ? "OUT OF STOCK" : (priceList.containsKey(51) ? PESO_SIGN + " " + priceList.get(51) : "NOT AVAILABLE")));
        sevenUpPrice.setText((expiredProducts.containsKey(52) ? "EXPIRED" : (outOfStockProducts.containsKey(52)) ? "OUT OF STOCK" : (priceList.containsKey(52) ? PESO_SIGN + " " + priceList.get(52) : "NOT AVAILABLE")));
        mountainDewPrice.setText((expiredProducts.containsKey(53) ? "EXPIRED" : (outOfStockProducts.containsKey(53)) ? "OUT OF STOCK" : (priceList.containsKey(53) ? PESO_SIGN + " " + priceList.get(53) : "NOT AVAILABLE")));
        liptonPrice.setText((expiredProducts.containsKey(54) ? "EXPIRED" : (outOfStockProducts.containsKey(54)) ? "OUT OF STOCK" : (priceList.containsKey(54) ? PESO_SIGN + " " + priceList.get(54) : "NOT AVAILABLE")));
        sunkistPrice.setText((expiredProducts.containsKey(55) ? "EXPIRED" : (outOfStockProducts.containsKey(55)) ? "OUT OF STOCK" : (priceList.containsKey(55) ? PESO_SIGN + " " + priceList.get(55) : "NOT AVAILABLE")));
        appleJuicePrice.setText((expiredProducts.containsKey(56) ? "EXPIRED" : (outOfStockProducts.containsKey(56)) ? "OUT OF STOCK" : (priceList.containsKey(56) ? PESO_SIGN + " " + priceList.get(56) : "NOT AVAILABLE")));
        pineAppleJuicePrice.setText((expiredProducts.containsKey(57) ? "EXPIRED" : (outOfStockProducts.containsKey(57)) ? "OUT OF STOCK" : (priceList.containsKey(57) ? PESO_SIGN + " " + priceList.get(57) : "NOT AVAILABLE")));
        blackCherryPrice.setText((expiredProducts.containsKey(58) ? "EXPIRED" : (outOfStockProducts.containsKey(58)) ? "OUT OF STOCK" : (priceList.containsKey(58) ? PESO_SIGN + " " + priceList.get(58) : "NOT AVAILABLE")));
        // liquors
        tequilaPrice.setText((expiredProducts.containsKey(59) ? "EXPIRED" : (outOfStockProducts.containsKey(59)) ? "OUT OF STOCK" : (priceList.containsKey(59) ? PESO_SIGN + " " + priceList.get(59) : "NOT AVAILABLE")));
        beerPrice.setText((expiredProducts.containsKey(60) ? "EXPIRED" : (outOfStockProducts.containsKey(60)) ? "OUT OF STOCK" : (priceList.containsKey(60) ? PESO_SIGN + " " + priceList.get(60) : "NOT AVAILABLE")));
        winePrice.setText((expiredProducts.containsKey(61) ? "EXPIRED" : (outOfStockProducts.containsKey(61)) ? "OUT OF STOCK" : (priceList.containsKey(61) ? PESO_SIGN + " " + priceList.get(61) : "NOT AVAILABLE")));
        hardCiderPrice.setText((expiredProducts.containsKey(62) ? "EXPIRED" : (outOfStockProducts.containsKey(62)) ? "OUT OF STOCK" : (priceList.containsKey(62) ? PESO_SIGN + " " + priceList.get(62) : "NOT AVAILABLE")));
        meadPrice.setText((expiredProducts.containsKey(63) ? "EXPIRED" : (outOfStockProducts.containsKey(63)) ? "OUT OF STOCK" : (priceList.containsKey(63) ? PESO_SIGN + " " + priceList.get(63) : "NOT AVAILABLE")));
        ginPrice.setText((expiredProducts.containsKey(64) ? "EXPIRED" : (outOfStockProducts.containsKey(64)) ? "OUT OF STOCK" : (priceList.containsKey(64) ? PESO_SIGN + " " + priceList.get(64) : "NOT AVAILABLE")));
        brandyPrice.setText((expiredProducts.containsKey(65) ? "EXPIRED" : (outOfStockProducts.containsKey(65)) ? "OUT OF STOCK" : (priceList.containsKey(65) ? PESO_SIGN + " " + priceList.get(65) : "NOT AVAILABLE")));
        whiskyPrice.setText((expiredProducts.containsKey(66) ? "EXPIRED" : (outOfStockProducts.containsKey(66)) ? "OUT OF STOCK" : (priceList.containsKey(66) ? PESO_SIGN + " " + priceList.get(66) : "NOT AVAILABLE")));
        rumPrice.setText((expiredProducts.containsKey(67) ? "EXPIRED" : (outOfStockProducts.containsKey(67)) ? "OUT OF STOCK" : (priceList.containsKey(67) ? PESO_SIGN + " " + priceList.get(67) : "NOT AVAILABLE")));
        vodkaPrice.setText((expiredProducts.containsKey(68) ? "EXPIRED" : (outOfStockProducts.containsKey(68)) ? "OUT OF STOCK" : (priceList.containsKey(68) ? PESO_SIGN + " " + priceList.get(68) : "NOT AVAILABLE")));
    }// </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that gets the number expired products and returns it.">//
    public static int getExpiredProductsCount() {
        return PRODUCT_SERVICE.getExpiredProductsCount().get();
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that sets the current date to the date text field.">//
    private void initializeDate() {
        final LocalDate LOCALDATE = LocalDate.now();
        date.setText(LOCALDATE.getMonth().name() + " " + LOCALDATE.getDayOfMonth() + ", " + LOCALDATE.getYear());
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that handles orders.">//
    private void handleOrder(int productId) {
        Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(productId);

        boolean alreadyAdded = ORDERS_LIST.stream()
                .anyMatch(order -> order.getName().equals(product.get().getName()));
        if (alreadyAdded) modifyOrder(product.get(), false);
        else makeOrder(product.get());
        refreshOrdersTable();
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that updates the total, subtotal and total discount when order is being modified.">//
    private void updateTransaction() {
        final double TOTAL_PRICE = ORDERS_LIST.stream()
                .map(product -> (product.getPrice() * product.getQuantity()))
                .reduce(0.0, Double::sum);

        final double SUB_TOTAL = ORDERS_LIST.stream()
                .map(product ->
                        (product.getDiscount() != 0.0) ?
                                (product.getPrice() * product.getQuantity() - (product.getDiscount() * product.getQuantity())) :
                                (product.getPrice() * product.getQuantity()))
                .reduce(0.0, Double::sum);

        final double TOTAL_DISCOUNT = ORDERS_LIST.stream()
                .filter(product -> (product.getDiscount() != 0.0))
                .map(order -> (order.getDiscount() * order.getQuantity()))
                .reduce(0.0, Double::sum);

        total.setText(String.valueOf(NUMBER_FORMAT.format(TOTAL_PRICE)));
        subTotal.setText(String.valueOf(NUMBER_FORMAT.format(SUB_TOTAL)));
        totalDiscount.setText(String.valueOf(NUMBER_FORMAT.format(TOTAL_DISCOUNT)));
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that makes an Order object based from the selection from the order table and returns it.">//
    private Order getOrderFromTableSelection() {
        try{
            return new Order(
                    String.valueOf(ordersTable.getModel().getValueAt(ordersTable.getSelectedRow(), 0)),
                    Double.parseDouble(String.valueOf(ordersTable.getModel().getValueAt(ordersTable.getSelectedRow(), 1))),
                    Category.valueOf(String.valueOf(ordersTable.getModel().getValueAt(ordersTable.getSelectedRow(), 2))),
                    Integer.parseInt(String.valueOf(ordersTable.getModel().getValueAt(ordersTable.getSelectedRow(), 3))),
                    Double.parseDouble(
                            ((String.valueOf(
                                    ordersTable.getModel().getValueAt(ordersTable.getSelectedRow(), 4))
                                    ).trim().isEmpty() ? "0" : (String.valueOf(
                                    ordersTable.getModel().getValueAt(ordersTable.getSelectedRow(), 4)))
                    )
            ));
        } catch (RuntimeException ignored) {
            return null;
        }
    } // </editor-fold>//

    /**
     * Method that returns a value from the invoked method from the {@code ProductValidator} that checks if the product is expired.
     * @param product the product to test for.
     * @return {@code true} if the product is expired.
     */
    // <editor-fold defaultstate="collapsed" desc="Method that returns a value from the invoked method from the {@code ProductValidator} that checks if the product is expired.">//
    private boolean isProductExpired(Product product) {
        return ProductValidator.isProductExpired().apply(PRODUCT_SERVICE.getProductById().apply(product.getId()).get()) == EXPIRED;
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that sets the icon for this frame.">//
    private void setIcon() {
        try {
            Image img = ImageIO.read(new URL("https://github.com/pitzzahh/point-of-sale/blob/220ccaa9681f18faa17a76b38ed6d91764303c5b/src/main/resources/ico.png?raw=true"));
            setIconImage(new ImageIcon(img).getImage());
        }
        catch(Exception ignored) {}
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that checks if the product is out of stock.">//
    private boolean isOutOfStock(Product product) {
        return PRODUCT_SERVICE.getProductById().apply(product.getId())
                .get()
                .getStocks().equals(0);
    } // </editor-fold>//

    // <editor-fold defaultstate="collapsed" desc="Method that resets the order.">//
    private void reset() {
        ORDERS_LIST.clear();
        cash.setText("");
        change.setText("0.00");
        refreshOrdersTable();
    } // </editor-fold>//

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        informationPanel = new javax.swing.JPanel();
        expiredProductsInformationPanel = new javax.swing.JPanel();
        expiredProductsLabel = new javax.swing.JLabel();
        manageProducts = new javax.swing.JButton();
        numberOfExpiredProducts = new javax.swing.JLabel();
        datePanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        dayTodayPanel = new javax.swing.JPanel();
        dayTodayLabel = new javax.swing.JLabel();
        day = new javax.swing.JLabel();
        exit = new javax.swing.JButton();
        viewSales = new javax.swing.JButton();
        headerPanel = new javax.swing.JPanel();
        header = new javax.swing.JLabel();
        productsPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        menuTabs = new javax.swing.JTabbedPane();
        cleaningProductsScroller = new javax.swing.JScrollPane(
            mainPanel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cleaningProductsPanel = new javax.swing.JPanel();
        cleanFirst = new javax.swing.JButton();
        spinMop = new javax.swing.JButton();
        windex = new javax.swing.JButton();
        clorox = new javax.swing.JButton();
        dyson = new javax.swing.JButton();
        dysonPrice = new javax.swing.JLabel();
        cloroxPrice = new javax.swing.JLabel();
        windexPrice = new javax.swing.JLabel();
        spinMopPrice = new javax.swing.JLabel();
        cleanFirstPrice = new javax.swing.JLabel();
        roomba = new javax.swing.JButton();
        cleanCut = new javax.swing.JButton();
        sureClean = new javax.swing.JButton();
        mrClean = new javax.swing.JButton();
        joy = new javax.swing.JButton();
        joyPrice = new javax.swing.JLabel();
        arielPrice = new javax.swing.JLabel();
        sureCleanPrice = new javax.swing.JLabel();
        cleanCutPrice = new javax.swing.JLabel();
        roombaPrice = new javax.swing.JLabel();
        smart = new javax.swing.JButton();
        smartPrice = new javax.swing.JLabel();
        domex = new javax.swing.JButton();
        domexPrice = new javax.swing.JLabel();
        mrMuscle = new javax.swing.JButton();
        mrMusclePrice = new javax.swing.JLabel();
        lysol = new javax.swing.JButton();
        lysolPrice = new javax.swing.JLabel();
        surf = new javax.swing.JButton();
        surfPrice = new javax.swing.JLabel();
        chocolatesScroller = new javax.swing.JScrollPane(
            mainPanel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chocolatesPanel = new javax.swing.JPanel();
        hersheys = new javax.swing.JButton();
        hersheysPrice = new javax.swing.JLabel();
        snickers = new javax.swing.JButton();
        snickersPrice = new javax.swing.JLabel();
        flyingNoir = new javax.swing.JButton();
        ferreroRocher = new javax.swing.JButton();
        esthechoc = new javax.swing.JButton();
        flyingNoirPrice = new javax.swing.JLabel();
        ferreroRocherPrice = new javax.swing.JLabel();
        esthechocPrice = new javax.swing.JLabel();
        droste = new javax.swing.JButton();
        whittakers = new javax.swing.JButton();
        amedei = new javax.swing.JButton();
        jacquesGenin = new javax.swing.JButton();
        richart = new javax.swing.JButton();
        richartPrice = new javax.swing.JLabel();
        jacquesGeninPrice = new javax.swing.JLabel();
        amedeiPrice = new javax.swing.JLabel();
        whittakersPrice = new javax.swing.JLabel();
        drostePrice = new javax.swing.JLabel();
        patchi = new javax.swing.JButton();
        teuscher = new javax.swing.JButton();
        valrhona = new javax.swing.JButton();
        dove = new javax.swing.JButton();
        russelStover = new javax.swing.JButton();
        russelStoverPrice = new javax.swing.JLabel();
        dovePrice = new javax.swing.JLabel();
        valrhonaPrice = new javax.swing.JLabel();
        teuscherPrice = new javax.swing.JLabel();
        patchiPrice = new javax.swing.JLabel();
        ritterSport = new javax.swing.JButton();
        guyLian = new javax.swing.JButton();
        kinder = new javax.swing.JButton();
        mars = new javax.swing.JButton();
        toblerone = new javax.swing.JButton();
        godivaPrice = new javax.swing.JLabel();
        marsPrice = new javax.swing.JLabel();
        kinderPrice = new javax.swing.JLabel();
        guyLianPrice = new javax.swing.JLabel();
        ritterSportPrice = new javax.swing.JLabel();
        godiva = new javax.swing.JButton();
        nestle = new javax.swing.JButton();
        milka = new javax.swing.JButton();
        ghirardelli = new javax.swing.JButton();
        cadbury = new javax.swing.JButton();
        cadburyPrice = new javax.swing.JLabel();
        ghirardelliPrice = new javax.swing.JLabel();
        milkaPrice = new javax.swing.JLabel();
        nestlePrice = new javax.swing.JLabel();
        tobleronePrice = new javax.swing.JLabel();
        beveragesScroller = new javax.swing.JScrollPane(
            mainPanel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        beveragesPanel = new javax.swing.JPanel();
        dole = new javax.swing.JButton();
        dolePrice = new javax.swing.JLabel();
        tropicanaPrice = new javax.swing.JLabel();
        tropicana = new javax.swing.JButton();
        minuteMaid = new javax.swing.JButton();
        minuteMaidPrice = new javax.swing.JLabel();
        spritePrice = new javax.swing.JLabel();
        sprite = new javax.swing.JButton();
        gatoradePrice = new javax.swing.JLabel();
        gatorade = new javax.swing.JButton();
        cocaColaPrice = new javax.swing.JLabel();
        pepsiPrice = new javax.swing.JLabel();
        redBullPrice = new javax.swing.JLabel();
        budWeiserPrice = new javax.swing.JLabel();
        heinekenPrice = new javax.swing.JLabel();
        heineken = new javax.swing.JButton();
        budWeiser = new javax.swing.JButton();
        redBull = new javax.swing.JButton();
        pepsi = new javax.swing.JButton();
        cocaCola = new javax.swing.JButton();
        sunkist = new javax.swing.JButton();
        koolAid = new javax.swing.JButton();
        sevenUp = new javax.swing.JButton();
        mountainDew = new javax.swing.JButton();
        lipton = new javax.swing.JButton();
        sunkistPrice = new javax.swing.JLabel();
        liptonPrice = new javax.swing.JLabel();
        mountainDewPrice = new javax.swing.JLabel();
        sevenUpPrice = new javax.swing.JLabel();
        koolAidPrice = new javax.swing.JLabel();
        appleJuice = new javax.swing.JButton();
        pineAppleJuice = new javax.swing.JButton();
        blackCherry = new javax.swing.JButton();
        blackCherryPrice = new javax.swing.JLabel();
        pineAppleJuicePrice = new javax.swing.JLabel();
        appleJuicePrice = new javax.swing.JLabel();
        mead = new javax.swing.JButton();
        hardCider = new javax.swing.JButton();
        wine = new javax.swing.JButton();
        beer = new javax.swing.JButton();
        tequila = new javax.swing.JButton();
        tequilaPrice = new javax.swing.JLabel();
        beerPrice = new javax.swing.JLabel();
        winePrice = new javax.swing.JLabel();
        hardCiderPrice = new javax.swing.JLabel();
        meadPrice = new javax.swing.JLabel();
        vodka = new javax.swing.JButton();
        rum = new javax.swing.JButton();
        whisky = new javax.swing.JButton();
        brandy = new javax.swing.JButton();
        gin = new javax.swing.JButton();
        ginPrice = new javax.swing.JLabel();
        brandyPrice = new javax.swing.JLabel();
        whiskyPrice = new javax.swing.JLabel();
        rumPrice = new javax.swing.JLabel();
        vodkaPrice = new javax.swing.JLabel();
        ordersPanel = new javax.swing.JPanel();
        ordersTableScroller = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        transactionPanel = new javax.swing.JPanel();
        totalPanel = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        totalPesoSignLabel = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        subTotalPanel = new javax.swing.JPanel();
        subTotalLabel = new javax.swing.JLabel();
        subTotalPesoSignLabel = new javax.swing.JLabel();
        subTotal = new javax.swing.JTextField();
        totalDiscountPanel = new javax.swing.JPanel();
        totalDiscountPesoSignLabel = new javax.swing.JLabel();
        totalDiscount = new javax.swing.JTextField();
        totalDiscountLabel = new javax.swing.JLabel();
        chashPanel = new javax.swing.JPanel();
        cashLabel = new javax.swing.JLabel();
        cashPesoSignLabel = new javax.swing.JLabel();
        cash = new javax.swing.JTextField();
        changePanel = new javax.swing.JPanel();
        changeLabel = new javax.swing.JLabel();
        changePesoSignLabel = new javax.swing.JLabel();
        change = new javax.swing.JTextField();
        pay = new javax.swing.JButton();
        removeItem = new javax.swing.JButton();
        reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 718));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1366, 718));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBackground(new java.awt.Color(0, 102, 102));
        mainPanel.setMinimumSize(new java.awt.Dimension(1360, 718));
        mainPanel.setPreferredSize(new java.awt.Dimension(1360, 718));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        informationPanel.setBackground(new java.awt.Color(51, 51, 0));
        informationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        informationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expiredProductsInformationPanel.setBackground(new java.awt.Color(0, 102, 102));
        expiredProductsInformationPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        expiredProductsInformationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expiredProductsLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        expiredProductsLabel.setForeground(new java.awt.Color(102, 255, 255));
        expiredProductsLabel.setText("EXPIRED PRODUCTS");
        expiredProductsInformationPanel.add(expiredProductsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        manageProducts.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14)); // NOI18N
        manageProducts.setText("MANAGE PRODUCTS");
        manageProducts.addActionListener(this::manageProductsActionPerformed);
        expiredProductsInformationPanel.add(manageProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 180, -1));

        numberOfExpiredProducts.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 34)); // NOI18N
        numberOfExpiredProducts.setForeground((getExpiredProductsCount() > 0) ? new java.awt.Color(255, 0, 0) : new java.awt.Color(0, 0, 255));
        numberOfExpiredProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numberOfExpiredProducts.setText(String.valueOf(getExpiredProductsCount()));
        expiredProductsInformationPanel.add(numberOfExpiredProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, -1));

        informationPanel.add(expiredProductsInformationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 250, 130));

        datePanel.setBackground(new java.awt.Color(0, 102, 102));
        datePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        datePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dateLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        dateLabel.setForeground(new java.awt.Color(102, 255, 255));
        dateLabel.setText("DATE");
        datePanel.add(dateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        date.setBackground(new java.awt.Color(255, 255, 255));
        date.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datePanel.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 50));

        informationPanel.add(datePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 240, 140));

        dayTodayPanel.setBackground(new java.awt.Color(0, 102, 102));
        dayTodayPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dayTodayPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dayTodayLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        dayTodayLabel.setForeground(new java.awt.Color(102, 255, 255));
        dayTodayLabel.setText("DAY TODAY");
        dayTodayPanel.add(dayTodayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        day.setBackground(new java.awt.Color(255, 255, 255));
        day.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        day.setForeground(new java.awt.Color(255, 255, 255));
        day.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayTodayPanel.add(day, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 50));

        informationPanel.add(dayTodayPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 240, 140));

        exit.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        exit.setForeground(new java.awt.Color(255, 0, 0));
        exit.setText("EXIT");
        exit.addActionListener(this::exitActionPerformed);
        informationPanel.add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 640, -1, 30));

        viewSales.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        viewSales.setText("VIEW REVENUE");
        viewSales.addActionListener(this::viewSalesActionPerformed);
        informationPanel.add(viewSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 170, 30));

        mainPanel.add(informationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 260, 690));

        headerPanel.setBackground(new java.awt.Color(51, 51, 0));
        headerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setFont(new java.awt.Font("Yu Gothic", Font.BOLD, 36)); // NOI18N
        header.setForeground(new java.awt.Color(255, 255, 255));
        header.setText("POINT OF SALE");
        headerPanel.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, -1, -1));

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 1080, 70));

        productsPanel.setBackground(new java.awt.Color(51, 51, 0));
        productsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        productsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        menuPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuTabs.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 18)); // NOI18N

        cleaningProductsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cleanFirst.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/clean_first.jpg")))); // NOI18N
        cleanFirst.setMaximumSize(new java.awt.Dimension(118, 118));
        cleanFirst.setMinimumSize(new java.awt.Dimension(118, 118));
        cleanFirst.addActionListener(this::cleanFirstActionPerformed);
        cleaningProductsPanel.add(cleanFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 90));

        spinMop.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/spin_mop.jpg")))); // NOI18N
        spinMop.setMaximumSize(new java.awt.Dimension(118, 118));
        spinMop.setMinimumSize(new java.awt.Dimension(118, 118));
        spinMop.addActionListener(this::spinMopActionPerformed);
        cleaningProductsPanel.add(spinMop, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 90));

        windex.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/windex.jpg")))); // NOI18N
        windex.setMaximumSize(new java.awt.Dimension(118, 118));
        windex.setMinimumSize(new java.awt.Dimension(118, 118));
        windex.addActionListener(this::windexActionPerformed);
        cleaningProductsPanel.add(windex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 100, 90));

        clorox.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/clorox.jpg")))); // NOI18N
        clorox.setMaximumSize(new java.awt.Dimension(118, 118));
        clorox.setMinimumSize(new java.awt.Dimension(118, 118));
        clorox.addActionListener(this::cloroxActionPerformed);
        cleaningProductsPanel.add(clorox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 90));

        dyson.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/dyson.jpg")))); // NOI18N
        dyson.setMaximumSize(new java.awt.Dimension(118, 118));
        dyson.setMinimumSize(new java.awt.Dimension(118, 118));
        dyson.addActionListener(this::dysonActionPerformed);
        cleaningProductsPanel.add(dyson, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 90));

        dysonPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dysonPrice.setText("₱ 777");
        dysonPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dysonPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(dysonPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 100, 20));

        cloroxPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cloroxPrice.setText("₱ 777");
        cloroxPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cloroxPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(cloroxPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 100, 20));

        windexPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        windexPrice.setText("₱ 777");
        windexPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        windexPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(windexPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 100, 20));

        spinMopPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        spinMopPrice.setText("₱ 777");
        spinMopPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        spinMopPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(spinMopPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 100, 20));

        cleanFirstPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cleanFirstPrice.setText("₱ 777");
        cleanFirstPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cleanFirstPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(cleanFirstPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 20));

        roomba.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/roomba.jpg")))); // NOI18N
        roomba.setMaximumSize(new java.awt.Dimension(118, 118));
        roomba.setMinimumSize(new java.awt.Dimension(118, 118));
        roomba.addActionListener(this::roombaActionPerformed);
        cleaningProductsPanel.add(roomba, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 90));

        cleanCut.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/clean_cut.jpg")))); // NOI18N
        cleanCut.setMaximumSize(new java.awt.Dimension(118, 118));
        cleanCut.setMinimumSize(new java.awt.Dimension(118, 118));
        cleanCut.addActionListener(this::cleanCutActionPerformed);
        cleaningProductsPanel.add(cleanCut, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 100, 90));

        sureClean.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/sure_clean.jpg")))); // NOI18N
        sureClean.setMaximumSize(new java.awt.Dimension(118, 118));
        sureClean.setMinimumSize(new java.awt.Dimension(118, 118));
        sureClean.addActionListener(this::sureCleanActionPerformed);
        cleaningProductsPanel.add(sureClean, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 100, 90));

        mrClean.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/ariel.jpg")))); // NOI18N
        mrClean.setMaximumSize(new java.awt.Dimension(118, 118));
        mrClean.setMinimumSize(new java.awt.Dimension(118, 118));
        mrClean.addActionListener(this::mrCleanActionPerformed);
        cleaningProductsPanel.add(mrClean, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 90));

        joy.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/joy.jpg")))); // NOI18N
        joy.setMaximumSize(new java.awt.Dimension(118, 118));
        joy.setMinimumSize(new java.awt.Dimension(118, 118));
        joy.addActionListener(this::joyActionPerformed);
        cleaningProductsPanel.add(joy, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 100, 90));

        joyPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        joyPrice.setText("₱ 777");
        joyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        joyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(joyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 100, 20));

        arielPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        arielPrice.setText("₱ 777");
        arielPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        arielPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(arielPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 210, 100, 20));

        sureCleanPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sureCleanPrice.setText("₱ 777");
        sureCleanPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sureCleanPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(sureCleanPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 100, 20));

        cleanCutPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cleanCutPrice.setText("₱ 777");
        cleanCutPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cleanCutPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(cleanCutPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 100, 20));

        roombaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roombaPrice.setText("₱ 777");
        roombaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        roombaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(roombaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 100, 20));

        smart.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/smart.jpg")))); // NOI18N
        smart.setMaximumSize(new java.awt.Dimension(118, 118));
        smart.setMinimumSize(new java.awt.Dimension(118, 118));
        smart.addActionListener(this::smartActionPerformed);
        cleaningProductsPanel.add(smart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 90));

        smartPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        smartPrice.setText("₱ 777");
        smartPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        smartPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(smartPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 100, 20));

        domex.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/domex.jpg")))); // NOI18N
        domex.setMaximumSize(new java.awt.Dimension(118, 118));
        domex.setMinimumSize(new java.awt.Dimension(118, 118));
        domex.addActionListener(this::domexActionPerformed);
        cleaningProductsPanel.add(domex, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 100, 90));

        domexPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        domexPrice.setText("₱ 777");
        domexPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        domexPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(domexPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 100, 20));

        mrMuscle.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/mr_muscle.jpg")))); // NOI18N
        mrMuscle.setMaximumSize(new java.awt.Dimension(118, 118));
        mrMuscle.setMinimumSize(new java.awt.Dimension(118, 118));
        mrMuscle.addActionListener(this::mrMuscleActionPerformed);
        cleaningProductsPanel.add(mrMuscle, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 100, 90));

        mrMusclePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mrMusclePrice.setText("₱ 777");
        mrMusclePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        mrMusclePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(mrMusclePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 100, 20));

        lysol.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/lysol.jpg")))); // NOI18N
        lysol.setMaximumSize(new java.awt.Dimension(118, 118));
        lysol.setMinimumSize(new java.awt.Dimension(118, 118));
        lysol.addActionListener(this::lysolActionPerformed);
        cleaningProductsPanel.add(lysol, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 100, 90));

        lysolPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lysolPrice.setText("₱ 777");
        lysolPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lysolPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(lysolPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 100, 20));

        surf.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/cleaning products/surf.jpg")))); // NOI18N
        surf.setMaximumSize(new java.awt.Dimension(118, 118));
        surf.setMinimumSize(new java.awt.Dimension(118, 118));
        surf.addActionListener(this::surfActionPerformed);
        cleaningProductsPanel.add(surf, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, 90));

        surfPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        surfPrice.setText("₱ 777");
        surfPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        surfPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(surfPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, 100, 20));

        cleaningProductsScroller.setViewportView(cleaningProductsPanel);

        menuTabs.addTab("CLEANING PRODUCTS", cleaningProductsScroller);

        chocolatesScroller.setAutoscrolls(true);

        chocolatesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        hersheys.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/hersheys.jpg")))); // NOI18N
        hersheys.setMaximumSize(new java.awt.Dimension(118, 118));
        hersheys.setMinimumSize(new java.awt.Dimension(118, 118));
        hersheys.addActionListener(this::hersheysActionPerformed);
        chocolatesPanel.add(hersheys, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 90));

        hersheysPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hersheysPrice.setText("₱ 777");
        hersheysPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        hersheysPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(hersheysPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 20));

        snickers.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/snickers.jpg")))); // NOI18N
        snickers.setMaximumSize(new java.awt.Dimension(118, 118));
        snickers.setMinimumSize(new java.awt.Dimension(118, 118));
        snickers.addActionListener(this::snickersActionPerformed);
        chocolatesPanel.add(snickers, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 90));

        snickersPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        snickersPrice.setText("₱ 777");
        snickersPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        snickersPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(snickersPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 100, 20));

        flyingNoir.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/flying_noir.jpg")))); // NOI18N
        flyingNoir.setMaximumSize(new java.awt.Dimension(118, 118));
        flyingNoir.setMinimumSize(new java.awt.Dimension(118, 118));
        flyingNoir.addActionListener(this::flyingNoirActionPerformed);
        chocolatesPanel.add(flyingNoir, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 90));

        ferreroRocher.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/ferrero_rocher.jpg")))); // NOI18N
        ferreroRocher.setMaximumSize(new java.awt.Dimension(118, 118));
        ferreroRocher.setMinimumSize(new java.awt.Dimension(118, 118));
        ferreroRocher.addActionListener(this::ferreroRocherActionPerformed);
        chocolatesPanel.add(ferreroRocher, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 100, 90));

        esthechoc.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/esthechoc.jpg")))); // NOI18N
        esthechoc.setMaximumSize(new java.awt.Dimension(118, 118));
        esthechoc.setMinimumSize(new java.awt.Dimension(118, 118));
        esthechoc.addActionListener(this::esthechocActionPerformed);
        chocolatesPanel.add(esthechoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 90));

        flyingNoirPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        flyingNoirPrice.setText("₱ 777");
        flyingNoirPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        flyingNoirPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(flyingNoirPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 100, 20));

        ferreroRocherPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ferreroRocherPrice.setText("₱ 777");
        ferreroRocherPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ferreroRocherPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(ferreroRocherPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 100, 20));

        esthechocPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        esthechocPrice.setText("₱ 777");
        esthechocPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        esthechocPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(esthechocPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 100, 20));

        droste.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/droste.png")))); // NOI18N
        droste.setMaximumSize(new java.awt.Dimension(118, 118));
        droste.setMinimumSize(new java.awt.Dimension(118, 118));
        droste.addActionListener(this::drosteActionPerformed);
        chocolatesPanel.add(droste, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 90));

        whittakers.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/whittakers.jpg")))); // NOI18N
        whittakers.setMaximumSize(new java.awt.Dimension(118, 118));
        whittakers.setMinimumSize(new java.awt.Dimension(118, 118));
        whittakers.addActionListener(this::whittakersActionPerformed);
        chocolatesPanel.add(whittakers, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 100, 90));

        amedei.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/amedei.jpg")))); // NOI18N
        amedei.setMaximumSize(new java.awt.Dimension(118, 118));
        amedei.setMinimumSize(new java.awt.Dimension(118, 118));
        amedei.addActionListener(this::amedeiActionPerformed);
        chocolatesPanel.add(amedei, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 100, 90));

        jacquesGenin.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/jacques_genin.jpg")))); // NOI18N
        jacquesGenin.setMaximumSize(new java.awt.Dimension(118, 118));
        jacquesGenin.setMinimumSize(new java.awt.Dimension(118, 118));
        jacquesGenin.addActionListener(this::jacquesGeninActionPerformed);
        chocolatesPanel.add(jacquesGenin, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 90));

        richart.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/richart.jpg")))); // NOI18N
        richart.setMaximumSize(new java.awt.Dimension(118, 118));
        richart.setMinimumSize(new java.awt.Dimension(118, 118));
        richart.addActionListener(this::richartActionPerformed);
        chocolatesPanel.add(richart, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 100, 90));

        richartPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        richartPrice.setText("₱ 777");
        richartPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        richartPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(richartPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 100, 20));

        jacquesGeninPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jacquesGeninPrice.setText("₱ 777");
        jacquesGeninPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jacquesGeninPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(jacquesGeninPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 210, 100, 20));

        amedeiPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        amedeiPrice.setText("₱ 777");
        amedeiPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        amedeiPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(amedeiPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 100, 20));

        whittakersPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        whittakersPrice.setText("₱ 777");
        whittakersPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        whittakersPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(whittakersPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 100, 20));

        drostePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        drostePrice.setText("₱ 777");
        drostePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        drostePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(drostePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 100, 20));

        patchi.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/patchi.jpg")))); // NOI18N
        patchi.setMaximumSize(new java.awt.Dimension(118, 118));
        patchi.setMinimumSize(new java.awt.Dimension(118, 118));
        patchi.addActionListener(this::patchiActionPerformed);
        chocolatesPanel.add(patchi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 90));

        teuscher.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/teuscher.jpg")))); // NOI18N
        teuscher.setMaximumSize(new java.awt.Dimension(118, 118));
        teuscher.setMinimumSize(new java.awt.Dimension(118, 118));
        teuscher.addActionListener(this::teuscherActionPerformed);
        chocolatesPanel.add(teuscher, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 100, 90));

        valrhona.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/valrhona.jpg")))); // NOI18N
        valrhona.setMaximumSize(new java.awt.Dimension(118, 118));
        valrhona.setMinimumSize(new java.awt.Dimension(118, 118));
        valrhona.addActionListener(this::valrhonaActionPerformed);
        chocolatesPanel.add(valrhona, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 100, 90));

        dove.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/dove.jpg")))); // NOI18N
        dove.setMaximumSize(new java.awt.Dimension(118, 118));
        dove.setMinimumSize(new java.awt.Dimension(118, 118));
        dove.addActionListener(this::doveActionPerformed);
        chocolatesPanel.add(dove, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 100, 90));

        russelStover.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/russell_stover.jpg")))); // NOI18N
        russelStover.setMaximumSize(new java.awt.Dimension(118, 118));
        russelStover.setMinimumSize(new java.awt.Dimension(118, 118));
        russelStover.addActionListener(this::russelStoverActionPerformed);
        chocolatesPanel.add(russelStover, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, 90));

        russelStoverPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        russelStoverPrice.setText("₱ 777");
        russelStoverPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        russelStoverPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(russelStoverPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, 100, 20));

        dovePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dovePrice.setText("₱ 777");
        dovePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dovePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(dovePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 100, 20));

        valrhonaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valrhonaPrice.setText("₱ 777");
        valrhonaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        valrhonaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(valrhonaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 100, 20));

        teuscherPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        teuscherPrice.setText("₱ 777");
        teuscherPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        teuscherPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(teuscherPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 100, 20));

        patchiPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        patchiPrice.setText("₱ 777");
        patchiPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        patchiPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(patchiPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 100, 20));

        ritterSport.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/ritter_sport.jpg")))); // NOI18N
        ritterSport.setMaximumSize(new java.awt.Dimension(118, 118));
        ritterSport.setMinimumSize(new java.awt.Dimension(118, 118));
        ritterSport.addActionListener(this::ritterSportActionPerformed);
        chocolatesPanel.add(ritterSport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 100, 90));

        guyLian.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/guylian.jpg")))); // NOI18N
        guyLian.setMaximumSize(new java.awt.Dimension(118, 118));
        guyLian.setMinimumSize(new java.awt.Dimension(118, 118));
        guyLian.addActionListener(this::guyLianActionPerformed);
        chocolatesPanel.add(guyLian, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 340, 100, 90));

        kinder.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/kinder.jpg")))); // NOI18N
        kinder.setMaximumSize(new java.awt.Dimension(118, 118));
        kinder.setMinimumSize(new java.awt.Dimension(118, 118));
        kinder.addActionListener(this::kinderActionPerformed);
        chocolatesPanel.add(kinder, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 100, 90));

        mars.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/mars.jpg")))); // NOI18N
        mars.setMaximumSize(new java.awt.Dimension(118, 118));
        mars.setMinimumSize(new java.awt.Dimension(118, 118));
        mars.addActionListener(this::marsActionPerformed);
        chocolatesPanel.add(mars, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, 100, 90));

        toblerone.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/toblerone.jpg")))); // NOI18N
        toblerone.setMaximumSize(new java.awt.Dimension(118, 118));
        toblerone.setMinimumSize(new java.awt.Dimension(118, 118));
        toblerone.addActionListener(this::tobleroneActionPerformed);
        chocolatesPanel.add(toblerone, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 340, 100, 90));

        godivaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        godivaPrice.setText("₱ 777");
        godivaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        godivaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(godivaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 540, 100, 20));

        marsPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        marsPrice.setText("₱ 777");
        marsPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        marsPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(marsPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 430, 100, 20));

        kinderPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        kinderPrice.setText("₱ 777");
        kinderPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        kinderPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(kinderPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 100, 20));

        guyLianPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        guyLianPrice.setText("₱ 777");
        guyLianPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        guyLianPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(guyLianPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 430, 100, 20));

        ritterSportPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ritterSportPrice.setText("₱ 777");
        ritterSportPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ritterSportPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(ritterSportPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 100, 20));

        godiva.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/godiva.jpg")))); // NOI18N
        godiva.setMaximumSize(new java.awt.Dimension(118, 118));
        godiva.setMinimumSize(new java.awt.Dimension(118, 118));
        godiva.addActionListener(this::godivaActionPerformed);
        chocolatesPanel.add(godiva, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 100, 90));

        nestle.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/nestle.jpg")))); // NOI18N
        nestle.setMaximumSize(new java.awt.Dimension(118, 118));
        nestle.setMinimumSize(new java.awt.Dimension(118, 118));
        nestle.addActionListener(this::nestleActionPerformed);
        chocolatesPanel.add(nestle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 100, 90));

        milka.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/milka.jpg")))); // NOI18N
        milka.setMaximumSize(new java.awt.Dimension(118, 118));
        milka.setMinimumSize(new java.awt.Dimension(118, 118));
        milka.addActionListener(this::milkaActionPerformed);
        chocolatesPanel.add(milka, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, 100, 90));

        ghirardelli.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/ghirardelli.jpg")))); // NOI18N
        ghirardelli.setMaximumSize(new java.awt.Dimension(118, 118));
        ghirardelli.setMinimumSize(new java.awt.Dimension(118, 118));
        ghirardelli.addActionListener(this::ghirardelliActionPerformed);
        chocolatesPanel.add(ghirardelli, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 100, 90));

        cadbury.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/chocolates/cadbury.jpg")))); // NOI18N
        cadbury.setMaximumSize(new java.awt.Dimension(118, 118));
        cadbury.setMinimumSize(new java.awt.Dimension(118, 118));
        cadbury.addActionListener(this::cadburyActionPerformed);
        chocolatesPanel.add(cadbury, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 450, 100, 90));

        cadburyPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cadburyPrice.setText("₱ 777");
        cadburyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cadburyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(cadburyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 540, 100, 20));

        ghirardelliPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ghirardelliPrice.setText("₱ 777");
        ghirardelliPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ghirardelliPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(ghirardelliPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 540, 100, 20));

        milkaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        milkaPrice.setText("₱ 777");
        milkaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        milkaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(milkaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 540, 100, 20));

        nestlePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nestlePrice.setText("₱ 777");
        nestlePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        nestlePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(nestlePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 100, 20));

        tobleronePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tobleronePrice.setText("₱ 777");
        tobleronePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tobleronePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(tobleronePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 430, 100, 20));

        chocolatesScroller.setViewportView(chocolatesPanel);

        menuTabs.addTab("CHOCOLATES", chocolatesScroller);

        beveragesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dole.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/dole.jpg")))); // NOI18N
        dole.setMaximumSize(new java.awt.Dimension(118, 118));
        dole.setMinimumSize(new java.awt.Dimension(118, 118));
        dole.addActionListener(this::doleActionPerformed);
        beveragesPanel.add(dole, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 100, 90));

        dolePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dolePrice.setText("₱ 777");
        dolePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dolePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(dolePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 100, 20));

        tropicanaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tropicanaPrice.setText("₱ 777");
        tropicanaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tropicanaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(tropicanaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 210, 100, 20));

        tropicana.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/tropicana.jpg")))); // NOI18N
        tropicana.setMaximumSize(new java.awt.Dimension(118, 118));
        tropicana.setMinimumSize(new java.awt.Dimension(118, 118));
        tropicana.addActionListener(this::tropicanaActionPerformed);
        beveragesPanel.add(tropicana, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 90));

        minuteMaid.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/minute_maid.jpg")))); // NOI18N
        minuteMaid.setMaximumSize(new java.awt.Dimension(118, 118));
        minuteMaid.setMinimumSize(new java.awt.Dimension(118, 118));
        minuteMaid.addActionListener(this::minuteMaidActionPerformed);
        beveragesPanel.add(minuteMaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 100, 90));

        minuteMaidPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minuteMaidPrice.setText("₱ 777");
        minuteMaidPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        minuteMaidPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(minuteMaidPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 100, 20));

        spritePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        spritePrice.setText("₱ 777");
        spritePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        spritePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(spritePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 100, 20));

        sprite.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/sprite.jpg")))); // NOI18N
        sprite.setMaximumSize(new java.awt.Dimension(118, 118));
        sprite.setMinimumSize(new java.awt.Dimension(118, 118));
        sprite.addActionListener(this::spriteActionPerformed);
        beveragesPanel.add(sprite, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 100, 90));

        gatoradePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gatoradePrice.setText("₱ 777");
        gatoradePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gatoradePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(gatoradePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 100, 20));

        gatorade.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/gatorade.jpg")))); // NOI18N
        gatorade.setMaximumSize(new java.awt.Dimension(118, 118));
        gatorade.setMinimumSize(new java.awt.Dimension(118, 118));
        gatorade.addActionListener(this::gatoradeActionPerformed);
        beveragesPanel.add(gatorade, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 90));

        cocaColaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cocaColaPrice.setText("₱ 777");
        cocaColaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cocaColaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(cocaColaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 20));

        pepsiPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pepsiPrice.setText("₱ 777");
        pepsiPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pepsiPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(pepsiPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 100, 20));

        redBullPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        redBullPrice.setText("₱ 777");
        redBullPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        redBullPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(redBullPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 100, 20));

        budWeiserPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        budWeiserPrice.setText("₱ 777");
        budWeiserPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        budWeiserPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(budWeiserPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 100, 20));

        heinekenPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heinekenPrice.setText("₱ 777");
        heinekenPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        heinekenPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(heinekenPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 100, 20));

        heineken.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/heineken.jpg")))); // NOI18N
        heineken.setToolTipText("");
        heineken.setMaximumSize(new java.awt.Dimension(118, 118));
        heineken.setMinimumSize(new java.awt.Dimension(118, 118));
        heineken.addActionListener(this::heinekenActionPerformed);
        beveragesPanel.add(heineken, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 90));

        budWeiser.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/budweiser.jpg")))); // NOI18N
        budWeiser.setMaximumSize(new java.awt.Dimension(118, 118));
        budWeiser.setMinimumSize(new java.awt.Dimension(118, 118));
        budWeiser.addActionListener(this::budWeiserActionPerformed);
        beveragesPanel.add(budWeiser, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 90));

        redBull.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/red_bull.jpg")))); // NOI18N
        redBull.setMaximumSize(new java.awt.Dimension(118, 118));
        redBull.setMinimumSize(new java.awt.Dimension(118, 118));
        redBull.addActionListener(this::redBullActionPerformed);
        beveragesPanel.add(redBull, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 100, 90));

        pepsi.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/pepsi.jpg")))); // NOI18N
        pepsi.setMaximumSize(new java.awt.Dimension(118, 118));
        pepsi.setMinimumSize(new java.awt.Dimension(118, 118));
        pepsi.addActionListener(this::pepsiActionPerformed);
        beveragesPanel.add(pepsi, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 90));

        cocaCola.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/coke.jpg")))); // NOI18N
        cocaCola.setMaximumSize(new java.awt.Dimension(118, 118));
        cocaCola.setMinimumSize(new java.awt.Dimension(118, 118));
        cocaCola.addActionListener(this::cocaColaActionPerformed);
        beveragesPanel.add(cocaCola, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 90));

        sunkist.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/sunkist.jpg")))); // NOI18N
        sunkist.setMaximumSize(new java.awt.Dimension(118, 118));
        sunkist.setMinimumSize(new java.awt.Dimension(118, 118));
        sunkist.addActionListener(this::sunkistActionPerformed);
        beveragesPanel.add(sunkist, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, 90));

        koolAid.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/kool_aid.jpg")))); // NOI18N
        koolAid.setMaximumSize(new java.awt.Dimension(118, 118));
        koolAid.setMinimumSize(new java.awt.Dimension(118, 118));
        koolAid.addActionListener(this::koolAidActionPerformed);
        beveragesPanel.add(koolAid, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 90));

        sevenUp.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/seven_up.jpg")))); // NOI18N
        sevenUp.setMaximumSize(new java.awt.Dimension(118, 118));
        sevenUp.setMinimumSize(new java.awt.Dimension(118, 118));
        sevenUp.addActionListener(this::sevenUpActionPerformed);
        beveragesPanel.add(sevenUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 100, 90));

        mountainDew.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/mountain_dew.jpg")))); // NOI18N
        mountainDew.setMaximumSize(new java.awt.Dimension(118, 118));
        mountainDew.setMinimumSize(new java.awt.Dimension(118, 118));
        mountainDew.addActionListener(this::mountainDewActionPerformed);
        beveragesPanel.add(mountainDew, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 100, 90));

        lipton.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/lipton.jpg")))); // NOI18N
        lipton.setMaximumSize(new java.awt.Dimension(118, 118));
        lipton.setMinimumSize(new java.awt.Dimension(118, 118));
        lipton.addActionListener(this::liptonActionPerformed);
        beveragesPanel.add(lipton, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 100, 90));

        sunkistPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sunkistPrice.setText("₱ 777");
        sunkistPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sunkistPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(sunkistPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, 100, 20));

        liptonPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        liptonPrice.setText("₱ 777");
        liptonPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        liptonPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(liptonPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 100, 20));

        mountainDewPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mountainDewPrice.setText("₱ 777");
        mountainDewPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        mountainDewPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(mountainDewPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 100, 20));

        sevenUpPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sevenUpPrice.setText("₱ 777");
        sevenUpPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sevenUpPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(sevenUpPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, 100, 20));

        koolAidPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        koolAidPrice.setText("₱ 777");
        koolAidPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        koolAidPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(koolAidPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 100, 20));

        appleJuice.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/apple_juice.jpg")))); // NOI18N
        appleJuice.setMaximumSize(new java.awt.Dimension(118, 118));
        appleJuice.setMinimumSize(new java.awt.Dimension(118, 118));
        appleJuice.addActionListener(this::appleJuiceActionPerformed);
        beveragesPanel.add(appleJuice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 100, 90));

        pineAppleJuice.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/pine_apple_juice.jpg")))); // NOI18N
        pineAppleJuice.setMaximumSize(new java.awt.Dimension(118, 118));
        pineAppleJuice.setMinimumSize(new java.awt.Dimension(118, 118));
        pineAppleJuice.addActionListener(this::pineAppleJuiceActionPerformed);
        beveragesPanel.add(pineAppleJuice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 340, 100, 90));

        blackCherry.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/black_cherry.jpg")))); // NOI18N
        blackCherry.setMaximumSize(new java.awt.Dimension(118, 118));
        blackCherry.setMinimumSize(new java.awt.Dimension(118, 118));
        blackCherry.addActionListener(this::blackCherryActionPerformed);
        beveragesPanel.add(blackCherry, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 100, 90));

        blackCherryPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        blackCherryPrice.setText("₱ 777");
        blackCherryPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        blackCherryPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(blackCherryPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 100, 20));

        pineAppleJuicePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pineAppleJuicePrice.setText("₱ 777");
        pineAppleJuicePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pineAppleJuicePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(pineAppleJuicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 430, 100, 20));

        appleJuicePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        appleJuicePrice.setText("₱ 777");
        appleJuicePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        appleJuicePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(appleJuicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 100, 20));

        mead.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/mead.jpg")))); // NOI18N
        mead.setMaximumSize(new java.awt.Dimension(118, 118));
        mead.setMinimumSize(new java.awt.Dimension(118, 118));
        mead.addActionListener(this::meadActionPerformed);
        beveragesPanel.add(mead, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 340, 100, 90));

        hardCider.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/hard_cider.jpg")))); // NOI18N
        hardCider.setMaximumSize(new java.awt.Dimension(118, 118));
        hardCider.setMinimumSize(new java.awt.Dimension(118, 118));
        hardCider.addActionListener(this::hardCiderActionPerformed);
        beveragesPanel.add(hardCider, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, 100, 90));

        wine.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/wine.jpg")))); // NOI18N
        wine.setMaximumSize(new java.awt.Dimension(118, 118));
        wine.setMinimumSize(new java.awt.Dimension(118, 118));
        wine.addActionListener(this::wineActionPerformed);
        beveragesPanel.add(wine, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 100, 90));

        beer.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/beer.jpg")))); // NOI18N
        beer.setMaximumSize(new java.awt.Dimension(118, 118));
        beer.setMinimumSize(new java.awt.Dimension(118, 118));
        beer.addActionListener(this::beerActionPerformed);
        beveragesPanel.add(beer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, 100, 90));

        tequila.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/tequila.jpg")))); // NOI18N
        tequila.setMaximumSize(new java.awt.Dimension(118, 118));
        tequila.setMinimumSize(new java.awt.Dimension(118, 118));
        tequila.addActionListener(this::tequilaActionPerformed);
        beveragesPanel.add(tequila, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 100, 90));

        tequilaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tequilaPrice.setText("₱ 777");
        tequilaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tequilaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(tequilaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 100, 20));

        beerPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        beerPrice.setText("₱ 777");
        beerPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        beerPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(beerPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 540, 100, 20));

        winePrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        winePrice.setText("₱ 777");
        winePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        winePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(winePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 540, 100, 20));

        hardCiderPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hardCiderPrice.setText("₱ 777");
        hardCiderPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        hardCiderPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(hardCiderPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 430, 100, 20));

        meadPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        meadPrice.setText("₱ 777");
        meadPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        meadPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(meadPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 430, 100, 20));

        vodka.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/vodka.jpg")))); // NOI18N
        vodka.setMaximumSize(new java.awt.Dimension(118, 118));
        vodka.setMinimumSize(new java.awt.Dimension(118, 118));
        vodka.addActionListener(this::vodkaActionPerformed);
        beveragesPanel.add(vodka, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 100, 90));

        rum.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/rum.jpg")))); // NOI18N
        rum.setMaximumSize(new java.awt.Dimension(118, 118));
        rum.setMinimumSize(new java.awt.Dimension(118, 118));
        rum.addActionListener(this::rumActionPerformed);
        beveragesPanel.add(rum, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 450, 100, 90));

        whisky.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/whisky.png")))); // NOI18N
        whisky.setMaximumSize(new java.awt.Dimension(118, 118));
        whisky.setMinimumSize(new java.awt.Dimension(118, 118));
        whisky.addActionListener(this::whiskyActionPerformed);
        beveragesPanel.add(whisky, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 560, 100, 90));

        brandy.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/brandy.jpg")))); // NOI18N
        brandy.setMaximumSize(new java.awt.Dimension(118, 118));
        brandy.setMinimumSize(new java.awt.Dimension(118, 118));
        brandy.addActionListener(this::brandyActionPerformed);
        beveragesPanel.add(brandy, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 560, 100, 90));

        gin.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/static/beverages/gin.jpg")))); // NOI18N
        gin.setMaximumSize(new java.awt.Dimension(118, 118));
        gin.setMinimumSize(new java.awt.Dimension(118, 118));
        gin.addActionListener(this::ginActionPerformed);
        beveragesPanel.add(gin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 100, 90));

        ginPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ginPrice.setText("₱ 777");
        ginPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ginPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(ginPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 100, 20));

        brandyPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        brandyPrice.setText("₱ 777");
        brandyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        brandyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(brandyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 650, 100, 20));

        whiskyPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        whiskyPrice.setText("₱ 777");
        whiskyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        whiskyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(whiskyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 650, 100, 20));

        rumPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rumPrice.setText("₱ 777");
        rumPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        rumPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(rumPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 540, 100, 20));

        vodkaPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        vodkaPrice.setText("₱ 777");
        vodkaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        vodkaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(vodkaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 540, 100, 20));

        beveragesScroller.setViewportView(beveragesPanel);

        menuTabs.addTab("BEVERAGES", beveragesScroller);

        menuPanel.add(menuTabs, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 570, 400));

        productsPanel.add(menuPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 580, 420));

        ordersPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ordersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ordersTable.setBorder(new javax.swing.border.MatteBorder(null));
        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAME", "PRICE", "CATEGORY", "QUANTITY", "DISCOUNT"
            }
        ) {
            final Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class
            };
            final boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ordersTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersTableScroller.setViewportView(ordersTable);
        if (ordersTable.getColumnModel().getColumnCount() > 0) {
            ordersTable.getColumnModel().getColumn(1).setResizable(false);
            ordersTable.getColumnModel().getColumn(2).setResizable(false);
            ordersTable.getColumnModel().getColumn(3).setResizable(false);
            ordersTable.getColumnModel().getColumn(4).setResizable(false);
        }

        ordersPanel.add(ordersTableScroller, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, -1, 400));

        productsPanel.add(ordersPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 470, 420));

        mainPanel.add(productsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 1080, 450));

        transactionPanel.setBackground(new java.awt.Color(51, 51, 0));
        transactionPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        transactionPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        totalLabel.setText("TOTAL:");
        totalPanel.add(totalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        totalPesoSignLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        totalPesoSignLabel.setText("₱");
        totalPanel.add(totalPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        total.setEditable(false);
        total.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        total.setText("0.00");
        total.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalPanel.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 170, 30));

        transactionPanel.add(totalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 370, 30));

        subTotalPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        subTotalPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subTotalLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        subTotalLabel.setText("SUB TOTAL:");
        subTotalPanel.add(subTotalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        subTotalPesoSignLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        subTotalPesoSignLabel.setText("₱");
        subTotalPanel.add(subTotalPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        subTotal.setEditable(false);
        subTotal.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        subTotal.setText("0.00");
        subTotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        subTotalPanel.add(subTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 170, 30));

        transactionPanel.add(subTotalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 370, 30));

        totalDiscountPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalDiscountPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalDiscountPesoSignLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        totalDiscountPesoSignLabel.setText("₱");
        totalDiscountPanel.add(totalDiscountPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        totalDiscount.setEditable(false);
        totalDiscount.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        totalDiscount.setText("0.00");
        totalDiscount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalDiscountPanel.add(totalDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 170, 30));

        totalDiscountLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        totalDiscountLabel.setText("TOTAL DISCOUNT:");
        totalDiscountPanel.add(totalDiscountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        transactionPanel.add(totalDiscountPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 370, 30));

        chashPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chashPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cashLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        cashLabel.setText("CASH:");
        chashPanel.add(cashLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        cashPesoSignLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        cashPesoSignLabel.setText("₱");
        chashPanel.add(cashPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, -1));

        cash.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        cash.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chashPanel.add(cash, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 160, 30));

        transactionPanel.add(chashPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 310, 30));

        changePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        changePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        changeLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        changeLabel.setText("CHANGE:");
        changePanel.add(changeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        changePesoSignLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        changePesoSignLabel.setText("₱");
        changePanel.add(changePesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, -1));

        change.setEditable(false);
        change.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18)); // NOI18N
        change.setText("0.00");
        change.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        changePanel.add(change, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 160, 30));

        transactionPanel.add(changePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 310, 30));

        pay.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        pay.setText("PAY");
        pay.addActionListener(this::payActionPerformed);
        transactionPanel.add(pay, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 20, 110, 40));

        removeItem.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        removeItem.setText("REMOVE ITEM");
        removeItem.addActionListener(this::removeItemActionPerformed);
        transactionPanel.add(removeItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 90, 210, 40));

        reset.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24)); // NOI18N
        reset.setText("RESET");
        reset.addActionListener(this::resetActionPerformed);
        transactionPanel.add(reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 90, 110, 40));

        mainPanel.add(transactionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 550, 1080, 150));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1368, 720));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Method that calculates the payment.">//
    private void payActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (ORDERS_LIST.isEmpty()) throw new IllegalStateException("NO PRODUCTS TO PAY");
            if(cash.getText().trim().isEmpty()) throw new IllegalStateException("PLEASE INSERT YOUR CASH AMOUNT");
            if(!Checker.isNumber(cash.getText().trim())) throw new IllegalStateException("THAT IS NOT A NUMBER");
            final double SUB_TOTAL = Double.parseDouble(subTotal.getText().trim().replaceAll(",", ""));
            final double CASH = Double.parseDouble(cash.getText().trim().replaceAll(",", ""));
            if (CASH < SUB_TOTAL) throw new IllegalStateException("CASH AMOUNT NOT ENOUGH");
            change.setText(NUMBER_FORMAT.format(CASH - SUB_TOTAL));

            Sales sales = new Sales(LocalDate.now(), SUB_TOTAL);
            ORDERS_LIST.forEach(
                    (order) -> {
                        int currentProductStock = PRODUCT_SERVICE.getProductByName().apply(order.getName()).getStocks();
                        PRODUCT_SERVICE.updateProductStocksByName().accept((currentProductStock - order.getQuantity()), order.getName());
                    }
            );
            SALES_SERVICE.saveSales().accept(sales);
            PROMPT.show.accept("SUCCESS", false);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    } // </editor-fold>//

    private void hersheysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hersheysActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(16);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(16);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_hersheysActionPerformed

    private void manageProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageProductsActionPerformed
        final ProgressBar PROGRESS = new ProgressBar();
        PROGRESS.run(Main.this, PROGRESS.EDITING_PRODUCTS);
    }//GEN-LAST:event_manageProductsActionPerformed

    private void cleanFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanFirstActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(1);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(1);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_cleanFirstActionPerformed

    private void roombaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roombaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(6);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(6);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_roombaActionPerformed

    private void gatoradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gatoradeActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(46);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(46);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_gatoradeActionPerformed

    private void cocaColaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cocaColaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(41);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(41);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_cocaColaActionPerformed




    private void guyLianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guyLianActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(32);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(32);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_guyLianActionPerformed

    private void milkaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_milkaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(37);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(37);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_milkaActionPerformed

    private void koolAidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_koolAidActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(51);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(51);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_koolAidActionPerformed

    private void appleJuiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appleJuiceActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(56);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(56);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_appleJuiceActionPerformed

    private void sunkistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunkistActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(55);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(55);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_sunkistActionPerformed

    private void tequilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tequilaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(59);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(59);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_tequilaActionPerformed

    private void ginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ginActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(64);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(64);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_ginActionPerformed

    private void smartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smartActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(11);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(11);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_smartActionPerformed

    private void spinMopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spinMopActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(2);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(2);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_spinMopActionPerformed

    private void windexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_windexActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(3);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(3);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_windexActionPerformed

    private void cloroxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cloroxActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(4);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(4);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_cloroxActionPerformed

    private void dysonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dysonActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(5);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(5);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_dysonActionPerformed

    private void cleanCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanCutActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(7);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(7);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_cleanCutActionPerformed

    private void sureCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sureCleanActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(8);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(8);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_sureCleanActionPerformed

    private void mrCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mrCleanActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(9);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(9);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_mrCleanActionPerformed

    private void joyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joyActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(10);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(10);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_joyActionPerformed

    private void domexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_domexActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(12);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(12);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_domexActionPerformed

    private void mrMuscleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mrMuscleActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(13);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(13);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_mrMuscleActionPerformed

    private void lysolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lysolActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(14);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(14);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_lysolActionPerformed

    private void surfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surfActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(15);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(15);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_surfActionPerformed

    private void snickersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snickersActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(17);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(17);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_snickersActionPerformed

    private void ferreroRocherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ferreroRocherActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(18);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(18);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_ferreroRocherActionPerformed

    private void esthechocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_esthechocActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(19);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(19);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_esthechocActionPerformed

    private void flyingNoirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flyingNoirActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(20);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(20);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }    }//GEN-LAST:event_flyingNoirActionPerformed

    private void drosteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drosteActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(21);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(21);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_drosteActionPerformed

    private void whittakersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whittakersActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(22);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(22);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }    }//GEN-LAST:event_whittakersActionPerformed

    private void amedeiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amedeiActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(23);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(23);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_amedeiActionPerformed

    private void jacquesGeninActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jacquesGeninActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(24);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(24);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_jacquesGeninActionPerformed

    private void richartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_richartActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(25);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(25);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }    }//GEN-LAST:event_richartActionPerformed

    private void patchiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patchiActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(26);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(26);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_patchiActionPerformed

    private void teuscherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teuscherActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(27);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(27);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_teuscherActionPerformed

    private void valrhonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valrhonaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(28);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(28);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_valrhonaActionPerformed

    private void doveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doveActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(29);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(29);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_doveActionPerformed

    private void russelStoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_russelStoverActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(30);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(30);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_russelStoverActionPerformed

    private void ritterSportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ritterSportActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(31);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(31);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_ritterSportActionPerformed

    private void kinderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kinderActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(33);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(33);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_kinderActionPerformed

    private void marsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marsActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(34);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(34);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_marsActionPerformed

    private void tobleroneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tobleroneActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(35);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(35);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_tobleroneActionPerformed

    private void nestleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nestleActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(36);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(36);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_nestleActionPerformed

    private void ghirardelliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ghirardelliActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(37);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(37);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_ghirardelliActionPerformed

    private void cadburyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadburyActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(39);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(39);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_cadburyActionPerformed

    private void godivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_godivaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(40);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(40);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_godivaActionPerformed

    private void pepsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pepsiActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(42);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(42);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_pepsiActionPerformed

    private void redBullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redBullActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(43);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(43);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_redBullActionPerformed

    private void budWeiserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_budWeiserActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(44);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(44);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_budWeiserActionPerformed

    private void heinekenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heinekenActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(45);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(45);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_heinekenActionPerformed

    private void spriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spriteActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(47);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(47);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_spriteActionPerformed

    private void minuteMaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minuteMaidActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(48);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(48);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }    }//GEN-LAST:event_minuteMaidActionPerformed

    private void tropicanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tropicanaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(49);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(49);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_tropicanaActionPerformed

    private void doleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doleActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(50);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(50);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_doleActionPerformed

    private void sevenUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sevenUpActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(52);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(52);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_sevenUpActionPerformed

    private void mountainDewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mountainDewActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(53);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(53);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_mountainDewActionPerformed

    private void liptonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liptonActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(54);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(54);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_liptonActionPerformed

    private void pineAppleJuiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pineAppleJuiceActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(57);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(57);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_pineAppleJuiceActionPerformed

    private void blackCherryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blackCherryActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(58);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(58);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_blackCherryActionPerformed

    private void beerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beerActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(60);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(60);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_beerActionPerformed

    private void wineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wineActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(61);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(61);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }    }//GEN-LAST:event_wineActionPerformed

    private void hardCiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hardCiderActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(62);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(62);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_hardCiderActionPerformed

    private void meadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meadActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(63);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(63);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_meadActionPerformed

    private void brandyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brandyActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(65);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(65);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_brandyActionPerformed

    private void whiskyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whiskyActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(66);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(66);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_whiskyActionPerformed

    private void rumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rumActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(67);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(67);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_rumActionPerformed

    private void vodkaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vodkaActionPerformed
        try {
            Optional<Product> product = PRODUCT_SERVICE.getProductById().apply(68);
            if(product.isEmpty()) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS NOT AVAILABLE");
            else if (isOutOfStock(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS OUT OF STOCK");
            else if(isProductExpired(product.get())) throw new IllegalStateException("CANNOT ADD PRODUCT\nPRODUCT IS EXPIRED");
            handleOrder(68);
        } catch(RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_vodkaActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        reset();
    }//GEN-LAST:event_resetActionPerformed

    private void removeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeItemActionPerformed
        try {
            Order order = getOrderFromTableSelection();
            if (order == null) throw new IllegalStateException("NO ITEM TO BE REMOVED!\n PLEASE SELECT A ROW FROM THE TABLE IF DATA IS AVAILABLE ");
            else {
                Product product = PRODUCT_SERVICE.getProductByName().apply(order.getName());
                modifyOrder(product, true);
                refreshOrdersTable();
            }
        } catch (RuntimeException runtimeException) {
            PROMPT.show.accept(runtimeException.getMessage(), true);
        }
    }//GEN-LAST:event_removeItemActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        ORDERS_LIST.clear();
        final ProgressBar PROGRESS = new ProgressBar();
        PROGRESS.run(Main.this, PROGRESS.LOGGING_OUT);
    }//GEN-LAST:event_exitActionPerformed

    private void viewSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSalesActionPerformed
        final ProgressBar PROGRESS = new ProgressBar();
        PROGRESS.run(Main.this, PROGRESS.VIEWING_REVENUE);
    }//GEN-LAST:event_viewSalesActionPerformed

    /**
     * Runs this frame.
     */
    public void run() {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> this.setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton amedei;
    private static javax.swing.JLabel amedeiPrice;
    private javax.swing.JButton appleJuice;
    private static javax.swing.JLabel appleJuicePrice;
    private static javax.swing.JLabel arielPrice;
    private javax.swing.JButton beer;
    private static javax.swing.JLabel beerPrice;
    private javax.swing.JPanel beveragesPanel;
    private javax.swing.JScrollPane beveragesScroller;
    private javax.swing.JButton blackCherry;
    private static javax.swing.JLabel blackCherryPrice;
    private javax.swing.JButton brandy;
    private static javax.swing.JLabel brandyPrice;
    private javax.swing.JButton budWeiser;
    private static javax.swing.JLabel budWeiserPrice;
    private javax.swing.JButton cadbury;
    private static javax.swing.JLabel cadburyPrice;
    private javax.swing.JTextField cash;
    private javax.swing.JLabel cashLabel;
    private javax.swing.JLabel cashPesoSignLabel;
    private javax.swing.JTextField change;
    private javax.swing.JLabel changeLabel;
    private javax.swing.JPanel changePanel;
    private javax.swing.JLabel changePesoSignLabel;
    private javax.swing.JPanel chashPanel;
    private javax.swing.JPanel chocolatesPanel;
    private javax.swing.JScrollPane chocolatesScroller;
    private javax.swing.JButton cleanCut;
    private static javax.swing.JLabel cleanCutPrice;
    private javax.swing.JButton cleanFirst;
    private static javax.swing.JLabel cleanFirstPrice;
    private javax.swing.JPanel cleaningProductsPanel;
    private javax.swing.JScrollPane cleaningProductsScroller;
    private javax.swing.JButton clorox;
    private static javax.swing.JLabel cloroxPrice;
    private javax.swing.JButton cocaCola;
    private static javax.swing.JLabel cocaColaPrice;
    private javax.swing.JLabel date;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JPanel datePanel;
    private javax.swing.JLabel day;
    private javax.swing.JLabel dayTodayLabel;
    private javax.swing.JPanel dayTodayPanel;
    private javax.swing.JButton dole;
    private static javax.swing.JLabel dolePrice;
    private javax.swing.JButton domex;
    private static javax.swing.JLabel domexPrice;
    private javax.swing.JButton dove;
    private static javax.swing.JLabel dovePrice;
    private javax.swing.JButton droste;
    private static javax.swing.JLabel drostePrice;
    private javax.swing.JButton dyson;
    private static javax.swing.JLabel dysonPrice;
    private javax.swing.JButton esthechoc;
    private static javax.swing.JLabel esthechocPrice;
    private javax.swing.JButton exit;
    private javax.swing.JPanel expiredProductsInformationPanel;
    private javax.swing.JLabel expiredProductsLabel;
    private javax.swing.JButton ferreroRocher;
    private static javax.swing.JLabel ferreroRocherPrice;
    private javax.swing.JButton flyingNoir;
    private static javax.swing.JLabel flyingNoirPrice;
    private javax.swing.JButton gatorade;
    private static javax.swing.JLabel gatoradePrice;
    private javax.swing.JButton ghirardelli;
    private static javax.swing.JLabel ghirardelliPrice;
    private javax.swing.JButton gin;
    private static javax.swing.JLabel ginPrice;
    private javax.swing.JButton godiva;
    private static javax.swing.JLabel godivaPrice;
    private javax.swing.JButton guyLian;
    private static javax.swing.JLabel guyLianPrice;
    private javax.swing.JButton hardCider;
    private static javax.swing.JLabel hardCiderPrice;
    private javax.swing.JLabel header;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton heineken;
    private static javax.swing.JLabel heinekenPrice;
    private javax.swing.JButton hersheys;
    private static javax.swing.JLabel hersheysPrice;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JButton jacquesGenin;
    private static javax.swing.JLabel jacquesGeninPrice;
    private javax.swing.JButton joy;
    private static javax.swing.JLabel joyPrice;
    private javax.swing.JButton kinder;
    private static javax.swing.JLabel kinderPrice;
    private javax.swing.JButton koolAid;
    private static javax.swing.JLabel koolAidPrice;
    private javax.swing.JButton lipton;
    private static javax.swing.JLabel liptonPrice;
    private javax.swing.JButton lysol;
    private static javax.swing.JLabel lysolPrice;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton manageProducts;
    private javax.swing.JButton mars;
    private static javax.swing.JLabel marsPrice;
    private javax.swing.JButton mead;
    private static javax.swing.JLabel meadPrice;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTabbedPane menuTabs;
    private javax.swing.JButton milka;
    private static javax.swing.JLabel milkaPrice;
    private javax.swing.JButton minuteMaid;
    private static javax.swing.JLabel minuteMaidPrice;
    private javax.swing.JButton mountainDew;
    private static javax.swing.JLabel mountainDewPrice;
    private javax.swing.JButton mrClean;
    private javax.swing.JButton mrMuscle;
    private static javax.swing.JLabel mrMusclePrice;
    private javax.swing.JButton nestle;
    private static javax.swing.JLabel nestlePrice;
    public static javax.swing.JLabel numberOfExpiredProducts;
    private javax.swing.JPanel ordersPanel;
    private javax.swing.JTable ordersTable;
    private javax.swing.JScrollPane ordersTableScroller;
    private javax.swing.JButton patchi;
    private static javax.swing.JLabel patchiPrice;
    private javax.swing.JButton pay;
    private javax.swing.JButton pepsi;
    private static javax.swing.JLabel pepsiPrice;
    private javax.swing.JButton pineAppleJuice;
    private static javax.swing.JLabel pineAppleJuicePrice;
    private javax.swing.JPanel productsPanel;
    private javax.swing.JButton redBull;
    private static javax.swing.JLabel redBullPrice;
    private javax.swing.JButton removeItem;
    private javax.swing.JButton reset;
    private javax.swing.JButton richart;
    private static javax.swing.JLabel richartPrice;
    private javax.swing.JButton ritterSport;
    private static javax.swing.JLabel ritterSportPrice;
    private javax.swing.JButton roomba;
    private static javax.swing.JLabel roombaPrice;
    private javax.swing.JButton rum;
    private static javax.swing.JLabel rumPrice;
    private javax.swing.JButton russelStover;
    private static javax.swing.JLabel russelStoverPrice;
    private javax.swing.JButton sevenUp;
    private static javax.swing.JLabel sevenUpPrice;
    private javax.swing.JButton smart;
    private static javax.swing.JLabel smartPrice;
    private javax.swing.JButton snickers;
    private static javax.swing.JLabel snickersPrice;
    private javax.swing.JButton spinMop;
    private static javax.swing.JLabel spinMopPrice;
    private javax.swing.JButton sprite;
    private static javax.swing.JLabel spritePrice;
    private javax.swing.JTextField subTotal;
    private javax.swing.JLabel subTotalLabel;
    private javax.swing.JPanel subTotalPanel;
    private javax.swing.JLabel subTotalPesoSignLabel;
    private javax.swing.JButton sunkist;
    private static javax.swing.JLabel sunkistPrice;
    private javax.swing.JButton sureClean;
    private static javax.swing.JLabel sureCleanPrice;
    private javax.swing.JButton surf;
    private static javax.swing.JLabel surfPrice;
    private javax.swing.JButton tequila;
    private static javax.swing.JLabel tequilaPrice;
    private javax.swing.JButton teuscher;
    private static javax.swing.JLabel teuscherPrice;
    private javax.swing.JButton toblerone;
    private static javax.swing.JLabel tobleronePrice;
    private javax.swing.JTextField total;
    private javax.swing.JTextField totalDiscount;
    private javax.swing.JLabel totalDiscountLabel;
    private javax.swing.JPanel totalDiscountPanel;
    private javax.swing.JLabel totalDiscountPesoSignLabel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JLabel totalPesoSignLabel;
    private javax.swing.JPanel transactionPanel;
    private javax.swing.JButton tropicana;
    private static javax.swing.JLabel tropicanaPrice;
    private javax.swing.JButton valrhona;
    private static javax.swing.JLabel valrhonaPrice;
    private javax.swing.JButton viewSales;
    private javax.swing.JButton vodka;
    private static javax.swing.JLabel vodkaPrice;
    private javax.swing.JButton whisky;
    private static javax.swing.JLabel whiskyPrice;
    private javax.swing.JButton whittakers;
    private static javax.swing.JLabel whittakersPrice;
    private javax.swing.JButton windex;
    private static javax.swing.JLabel windexPrice;
    private javax.swing.JButton wine;
    private static javax.swing.JLabel winePrice;
    // End of variables declaration//GEN-END:variables
}
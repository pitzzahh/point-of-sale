package com.pos;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import javax.swing.table.DefaultTableCellRenderer;
import com.pos.service.ProductService;
import com.pos.service.SalesService;
import java.text.SimpleDateFormat;
import com.pos.entity.Product;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author peter
 */
public class Main extends JFrame {

    private final AbstractApplicationContext CONTEXT=  new AnnotationConfigApplicationContext(JPAConfiguration.class);
    private final ProductService PRODUCT_SERVICE = CONTEXT.getBean(ProductService.class);
    private final SalesService SALES_SERVICE = CONTEXT.getBean(SalesService.class);

    /**
     * Creates new form Main
     */
    public Main() {
        PRODUCT_SERVICE.insertAllProductsToDatabase();
        initComponents();
        initializeDate();
        initializeTime();
        day.setText(LocalDate.now().getDayOfWeek().name());
        setProductsPrices();
        final DefaultTableCellRenderer RENDERER = new DefaultTableCellRenderer();
        RENDERER.setHorizontalAlignment(JLabel.CENTER);
        ordersTable.getColumnModel().getColumn(0).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(1).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(2).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(3).setCellRenderer(RENDERER);
        ordersTable.getColumnModel().getColumn(4).setCellRenderer(RENDERER);
    }
    
    /**
     * Method that sets the prices label for all the products.
     * Products discount is already computed and discounted price will be shown to the label.
     */
    private void setProductsPrices() {
        final char PESO_SIGN = '₱';

        PRODUCT_SERVICE.getAllProducts().get()
                        .stream()
                        .map(p -> (p.getDiscount() != null) ? (p.getPrice() - p.getDiscount()) : p.getPrice())
                        .forEach(System.out::println);


//        /*
//            Setting prices label for cleaning products
//         */
//        cleanFirstPrice.setText(PESO_SIGN + " " + priceList.get(0));
//        hydroSafePrice.setText(PESO_SIGN + " " + priceList.get(1));
//        rightFlexPrice.setText(PESO_SIGN + " " + priceList.get(2));
//        cloroxPrice.setText(PESO_SIGN + " " + priceList.get(3));
//        dirtBustersPrice.setText(PESO_SIGN + " " + priceList.get(4));
//        myCleanPrice.setText(PESO_SIGN + " " + priceList.get(5));
//        cleanCutPrice.setText(PESO_SIGN + " " + priceList.get(6));
//        sureCleanPrice.setText(PESO_SIGN + " " + priceList.get(7));
//        arielPrice.setText(PESO_SIGN + " " + priceList.get(8));
//        joyPrice.setText(PESO_SIGN + " " + priceList.get(9));
//        smartPrice.setText(PESO_SIGN + " " + priceList.get(10));
//        domexPrice.setText(PESO_SIGN + " " + priceList.get(11));
//        mrMusclePrice.setText(PESO_SIGN + " " + priceList.get(12));
//        lysolPrice.setText(PESO_SIGN + " " + priceList.get(13));
//        surfPrice.setText(PESO_SIGN + " " + priceList.get(14));
//
//        /*
//            Setting prices label for chocolates
//         */
//        hersheysPrice.setText(PESO_SIGN + " " + priceList.get(15));
//        snickersPrice.setText(PESO_SIGN + " " + priceList.get(16));
//        ferreroRocherPrice.setText(PESO_SIGN + " " + priceList.get(17));
//        esthechocPrice.setText(PESO_SIGN + " " + priceList.get(18));
//        flyingNoirPrice.setText(PESO_SIGN + " " + priceList.get(19));
//        drostePrice.setText(PESO_SIGN + " " + priceList.get(20));
//        wittakersPrice.setText(PESO_SIGN + " " + priceList.get(21));
//        amedeiPrice.setText(PESO_SIGN + " " + priceList.get(22));
//        jacquesGeninPrice.setText(PESO_SIGN + " " + priceList.get(23));
//        richartPrice.setText(PESO_SIGN + " " + priceList.get(24));
//        patchiPrice.setText(PESO_SIGN + " " + priceList.get(25));
//        teuscherPrice.setText(PESO_SIGN + " " + priceList.get(26));
//        valrhonaPrice.setText(PESO_SIGN + " " + priceList.get(27));
//        dovePrice.setText(PESO_SIGN + " " + priceList.get(28));
//        russelStoverPrice.setText(PESO_SIGN + " " + priceList.get(29));
//        ritterSportPrice.setText(PESO_SIGN + " " + priceList.get(30));
//        guyLianPrice.setText(PESO_SIGN + " " + priceList.get(31));
//        kinderPrice.setText(PESO_SIGN + " " + priceList.get(32));
//        marsPrice.setText(PESO_SIGN + " " + priceList.get(33));
//        tobleronePrice.setText(PESO_SIGN + " " + priceList.get(34));
//        nestlePrice.setText(PESO_SIGN + " " + priceList.get(35));
//        milkaPrice.setText(PESO_SIGN + " " + priceList.get(36));
//        ghirardellPrice.setText(PESO_SIGN + " " + priceList.get(37));
//        cadburyPrice.setText(PESO_SIGN + " " + priceList.get(38));
//        godivaPrice.setText(PESO_SIGN + " " + priceList.get(39));
//
//        /*
//            Setting prices label for beverages
//         */
//        cocaColaPrice.setText(PESO_SIGN + " " + priceList.get(40));
//        pepsiPrice.setText(PESO_SIGN + " " + priceList.get(41));
//        redBullPrice.setText(PESO_SIGN + " " + priceList.get(42));
//        budWeiserPrice.setText(PESO_SIGN + " " + priceList.get(43));
//        heinekenPrice.setText(PESO_SIGN + " " + priceList.get(44));
//        gatoradePrice.setText(PESO_SIGN + " " + priceList.get(45));
//        spritePrice.setText(PESO_SIGN + " " + priceList.get(46));
//        minuteMaidPrice.setText(PESO_SIGN + " " + priceList.get(47));
//        tropicanaPrice.setText(PESO_SIGN + " " + priceList.get(48));
//        dolePrice.setText(PESO_SIGN + " " + priceList.get(49));
//        koolAidPrice.setText(PESO_SIGN + " " + priceList.get(50));
//        sevenUpPrice.setText(PESO_SIGN + " " + priceList.get(51));
//        mountainDewPrice.setText(PESO_SIGN + " " + priceList.get(52));
//        liptonPrice.setText(PESO_SIGN + " " + priceList.get(53));
//        sunkistPrice.setText(PESO_SIGN + " " + priceList.get(54));
//        appleJuicePrice.setText(PESO_SIGN + " " + priceList.get(55));
//        pineAppleJuicePrice.setText(PESO_SIGN + " " + priceList.get(56));
//        blackCherPrice.setText(PESO_SIGN + " " + priceList.get(57));
//        // liquors
//        tequilaPrice.setText(PESO_SIGN + " " + priceList.get(58));
//        beerPrice.setText(PESO_SIGN + " " + priceList.get(59));
//        winePrice.setText(PESO_SIGN + " " + priceList.get(60));
//        hardCiderPrice.setText(PESO_SIGN + " " + priceList.get(61));
//        meadPrice.setText(PESO_SIGN + " " + priceList.get(62));
//        ginPrice.setText(PESO_SIGN + " " + priceList.get(63));
//        brandyPrice.setText(PESO_SIGN + " " + priceList.get(64));
//        whiskyPrice.setText(PESO_SIGN + " " + priceList.get(65));
//        rumPrice.setText(PESO_SIGN + " " + priceList.get(66));
//        vodkaPrice.setText(PESO_SIGN + " " + priceList.get(67));

    }

    private Product getProductById(int id) throws IllegalAccessException {
        return PRODUCT_SERVICE.getAllProducts()
                .get()
                .stream()
                .filter(product -> product.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new IllegalAccessException("Product with id: " + id + " does not exist"));

    }

    /**
     *  Method that gets the expired products
     * @return the number of expired products.
     */
    private int getExpiredProductsCount() {
        return PRODUCT_SERVICE.getExpiredProductsCount().get();
    }

    /**
     * Method that sets the current date to the date text field.
     */
    private void initializeDate() {
        final LocalDate LOCALDATE = LocalDate.now();
        date.setText(LOCALDATE.getMonth().name() + " " + LOCALDATE.getDayOfWeek().getValue() + ", " + LOCALDATE.getYear());
    }

    /**
     * Method that displays a live current time to the graphical user interface.
     * Starts a new {@code Thread} only for displaying a live time.
     */
    private void initializeTime() {
        final DateFormat TIME_FORMAT = new SimpleDateFormat("hh : mm : ss aa");
        final Thread TIME_THREAD = new Thread(() -> {
            while(true) {
                final String LIVE_TIME = TIME_FORMAT.format(new Date().getTime());
                time.setText(LIVE_TIME);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        TIME_THREAD.setPriority(Thread.MIN_PRIORITY);
        TIME_THREAD.start();
    }

    private static void updateTransaction() {

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
        informationPanel = new javax.swing.JPanel();
        expiredProductsInformationPanel = new javax.swing.JPanel();
        expiredProductsLabel = new javax.swing.JLabel();
        viewExpiredProducts = new javax.swing.JButton();
        numberOfExpiredProducts = new javax.swing.JLabel();
        datePanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        dayTodayPanel = new javax.swing.JPanel();
        dayTodayLabel = new javax.swing.JLabel();
        day = new javax.swing.JLabel();
        timePanel = new javax.swing.JPanel();
        timeLabel = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        logout = new javax.swing.JButton();
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
        hydroSafe = new javax.swing.JButton();
        rightFlex = new javax.swing.JButton();
        clorox = new javax.swing.JButton();
        dirtBuster = new javax.swing.JButton();
        dirtBustersPrice = new javax.swing.JLabel();
        cloroxPrice = new javax.swing.JLabel();
        rightFlexPrice = new javax.swing.JLabel();
        hydroSafePrice = new javax.swing.JLabel();
        cleanFirstPrice = new javax.swing.JLabel();
        myClean = new javax.swing.JButton();
        cleanCut = new javax.swing.JButton();
        sureClean = new javax.swing.JButton();
        mrClean = new javax.swing.JButton();
        joy = new javax.swing.JButton();
        joyPrice = new javax.swing.JLabel();
        arielPrice = new javax.swing.JLabel();
        sureCleanPrice = new javax.swing.JLabel();
        cleanCutPrice = new javax.swing.JLabel();
        myCleanPrice = new javax.swing.JLabel();
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
        wittakers = new javax.swing.JButton();
        amedei = new javax.swing.JButton();
        jacqueslGenin = new javax.swing.JButton();
        richart = new javax.swing.JButton();
        richartPrice = new javax.swing.JLabel();
        jacquesGeninPrice = new javax.swing.JLabel();
        amedeiPrice = new javax.swing.JLabel();
        wittakersPrice = new javax.swing.JLabel();
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
        ghirardell = new javax.swing.JButton();
        cadbury = new javax.swing.JButton();
        cadburyPrice = new javax.swing.JLabel();
        ghirardellPrice = new javax.swing.JLabel();
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
        blackCher = new javax.swing.JButton();
        blackCherPrice = new javax.swing.JLabel();
        pineAppleJuicePrice = new javax.swing.JLabel();
        appleJuicePrice = new javax.swing.JLabel();
        liquorsLabel = new javax.swing.JLabel();
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
        printReceipt = new javax.swing.JButton();
        pay = new javax.swing.JButton();
        removeItem = new javax.swing.JButton();
        reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 718));
        setPreferredSize(new java.awt.Dimension(1366, 718));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setBackground(new java.awt.Color(0, 102, 102));
        mainPanel.setMinimumSize(new java.awt.Dimension(1360, 718));
        mainPanel.setPreferredSize(new java.awt.Dimension(1360, 718));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        informationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        informationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expiredProductsInformationPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        expiredProductsInformationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        expiredProductsLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        expiredProductsLabel.setText("EXPIRED PRODUCTS");
        expiredProductsInformationPanel.add(expiredProductsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        viewExpiredProducts.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        viewExpiredProducts.setText("VIEW EXPIRED PRODUCTS");
        viewExpiredProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewExpiredProductsActionPerformed(evt);
            }
        });
        expiredProductsInformationPanel.add(viewExpiredProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 230, -1));

        numberOfExpiredProducts.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        numberOfExpiredProducts.setForeground((getExpiredProductsCount() > 0) ? new java.awt.Color(255, 0, 0) : new java.awt.Color(0, 0, 255));
        numberOfExpiredProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numberOfExpiredProducts.setText(String.valueOf(getExpiredProductsCount()));
        expiredProductsInformationPanel.add(numberOfExpiredProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, -1));

        informationPanel.add(expiredProductsInformationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 250, 130));

        datePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        datePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        dateLabel.setText("DATE");
        datePanel.add(dateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        date.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        date.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        datePanel.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 50));

        informationPanel.add(datePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 240, 140));

        dayTodayPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dayTodayPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dayTodayLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        dayTodayLabel.setText("DAY TODAY");
        dayTodayPanel.add(dayTodayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

        day.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        day.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        dayTodayPanel.add(day, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 50));

        informationPanel.add(dayTodayPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 240, 140));

        timePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        timePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        timeLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        timeLabel.setText("TIME");
        timePanel.add(timeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        time.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        time.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        timePanel.add(time, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 50));

        informationPanel.add(timePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 240, 140));

        logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 0, 0));
        logout.setText("LOGOUT");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        informationPanel.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 640, -1, 30));

        viewSales.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        viewSales.setText("VIEW SALES");
        informationPanel.add(viewSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 170, 30));

        mainPanel.add(informationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 260, 680));

        headerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setFont(new java.awt.Font("Yu Gothic", 1, 36)); // NOI18N
        header.setText("PITZZAHH STORE");
        headerPanel.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 1080, 70));

        productsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        productsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        menuPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuTabs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        cleaningProductsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cleanFirst.setText("CLEAN FIRST");
        cleanFirst.setMaximumSize(new java.awt.Dimension(118, 118));
        cleanFirst.setMinimumSize(new java.awt.Dimension(118, 118));
        cleanFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanFirstActionPerformed(evt);
            }
        });
        cleaningProductsPanel.add(cleanFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 90));

        hydroSafe.setText("HYDROSAFE");
        hydroSafe.setMaximumSize(new java.awt.Dimension(118, 118));
        hydroSafe.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(hydroSafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 90));

        rightFlex.setText("RIGHTFLEX");
        rightFlex.setMaximumSize(new java.awt.Dimension(118, 118));
        rightFlex.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(rightFlex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 100, 90));

        clorox.setText("CLOROX");
        clorox.setMaximumSize(new java.awt.Dimension(118, 118));
        clorox.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(clorox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 90));

        dirtBuster.setText("DIRTBUSTERS");
        dirtBuster.setMaximumSize(new java.awt.Dimension(118, 118));
        dirtBuster.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(dirtBuster, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 90));

        dirtBustersPrice.setText("₱ 1000");
        dirtBustersPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dirtBustersPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(dirtBustersPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 50, 20));

        cloroxPrice.setText("₱ 1000");
        cloroxPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cloroxPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(cloroxPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 50, 20));

        rightFlexPrice.setText("₱ 1000");
        rightFlexPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        rightFlexPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(rightFlexPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 50, 20));

        hydroSafePrice.setText("₱ 1000");
        hydroSafePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        hydroSafePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(hydroSafePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 50, 20));

        cleanFirstPrice.setText("₱ 1000");
        cleanFirstPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cleanFirstPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(cleanFirstPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 50, 20));

        myClean.setText("MY CLEAN");
        myClean.setMaximumSize(new java.awt.Dimension(118, 118));
        myClean.setMinimumSize(new java.awt.Dimension(118, 118));
        myClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myCleanActionPerformed(evt);
            }
        });
        cleaningProductsPanel.add(myClean, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 90));

        cleanCut.setText("CLEAN CUT");
        cleanCut.setMaximumSize(new java.awt.Dimension(118, 118));
        cleanCut.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(cleanCut, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 100, 90));

        sureClean.setText("SURE CLEAN");
        sureClean.setMaximumSize(new java.awt.Dimension(118, 118));
        sureClean.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(sureClean, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 100, 90));

        mrClean.setText("ARIEL");
        mrClean.setMaximumSize(new java.awt.Dimension(118, 118));
        mrClean.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(mrClean, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 90));

        joy.setText("JOY");
        joy.setMaximumSize(new java.awt.Dimension(118, 118));
        joy.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(joy, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 100, 90));

        joyPrice.setText("₱ 1000");
        joyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        joyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(joyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 50, 20));

        arielPrice.setText("₱ 1000");
        arielPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        arielPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(arielPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 50, 20));

        sureCleanPrice.setText("₱ 1000");
        sureCleanPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sureCleanPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(sureCleanPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 50, 20));

        cleanCutPrice.setText("₱ 1000");
        cleanCutPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cleanCutPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(cleanCutPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 50, 20));

        myCleanPrice.setText("₱ 1000");
        myCleanPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        myCleanPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(myCleanPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 50, 20));

        smart.setText("SMART");
        smart.setMaximumSize(new java.awt.Dimension(118, 118));
        smart.setMinimumSize(new java.awt.Dimension(118, 118));
        smart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smartActionPerformed(evt);
            }
        });
        cleaningProductsPanel.add(smart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 90));

        smartPrice.setText("₱ 1000");
        smartPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        smartPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(smartPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 50, 20));

        domex.setText("DOMEX");
        domex.setMaximumSize(new java.awt.Dimension(118, 118));
        domex.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(domex, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 100, 90));

        domexPrice.setText("₱ 1000");
        domexPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        domexPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(domexPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 50, 20));

        mrMuscle.setText("MR MUSCLE");
        mrMuscle.setMaximumSize(new java.awt.Dimension(118, 118));
        mrMuscle.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(mrMuscle, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 100, 90));

        mrMusclePrice.setText("₱ 1000");
        mrMusclePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        mrMusclePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(mrMusclePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 50, 20));

        lysol.setText("LYSOL");
        lysol.setMaximumSize(new java.awt.Dimension(118, 118));
        lysol.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(lysol, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 100, 90));

        lysolPrice.setText("₱ 1000");
        lysolPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lysolPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(lysolPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 50, 20));

        surf.setText("SURF");
        surf.setMaximumSize(new java.awt.Dimension(118, 118));
        surf.setMinimumSize(new java.awt.Dimension(118, 118));
        cleaningProductsPanel.add(surf, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, 90));

        surfPrice.setText("₱ 1000");
        surfPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        surfPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cleaningProductsPanel.add(surfPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 320, 50, 20));

        cleaningProductsScroller.setViewportView(cleaningProductsPanel);

        menuTabs.addTab("CLEANING PRODUCTS", cleaningProductsScroller);

        chocolatesScroller.setAutoscrolls(true);

        chocolatesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        hersheys.setText("HERSHEY'S");
        hersheys.setMaximumSize(new java.awt.Dimension(118, 118));
        hersheys.setMinimumSize(new java.awt.Dimension(118, 118));
        hersheys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hersheysActionPerformed(evt);
            }
        });
        chocolatesPanel.add(hersheys, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 90));

        hersheysPrice.setText("₱ 1000");
        hersheysPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        hersheysPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(hersheysPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 50, 20));

        snickers.setText("SNICKERS");
        snickers.setMaximumSize(new java.awt.Dimension(118, 118));
        snickers.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(snickers, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 90));

        snickersPrice.setText("₱ 1000");
        snickersPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        snickersPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(snickersPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 50, 20));

        flyingNoir.setText("FLYING NOIR");
        flyingNoir.setMaximumSize(new java.awt.Dimension(118, 118));
        flyingNoir.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(flyingNoir, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 90));

        ferreroRocher.setText("FERRERO ROCHER");
        ferreroRocher.setMaximumSize(new java.awt.Dimension(118, 118));
        ferreroRocher.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(ferreroRocher, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 100, 90));

        esthechoc.setText("ESTHECHOC");
        esthechoc.setMaximumSize(new java.awt.Dimension(118, 118));
        esthechoc.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(esthechoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 90));

        flyingNoirPrice.setText("₱ 1000");
        flyingNoirPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        flyingNoirPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(flyingNoirPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 50, 20));

        ferreroRocherPrice.setText("₱ 1000");
        ferreroRocherPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ferreroRocherPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(ferreroRocherPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 50, 20));

        esthechocPrice.setText("₱ 1000");
        esthechocPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        esthechocPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(esthechocPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 50, 20));

        droste.setText("DROSTE");
        droste.setMaximumSize(new java.awt.Dimension(118, 118));
        droste.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(droste, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 90));

        wittakers.setText("WITTAKER'S");
        wittakers.setMaximumSize(new java.awt.Dimension(118, 118));
        wittakers.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(wittakers, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 100, 90));

        amedei.setText("AMEDEI");
        amedei.setMaximumSize(new java.awt.Dimension(118, 118));
        amedei.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(amedei, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 100, 90));

        jacqueslGenin.setText("JACQUES GENIN");
        jacqueslGenin.setMaximumSize(new java.awt.Dimension(118, 118));
        jacqueslGenin.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(jacqueslGenin, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 90));

        richart.setText("RICHART");
        richart.setMaximumSize(new java.awt.Dimension(118, 118));
        richart.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(richart, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 100, 90));

        richartPrice.setText("₱ 1000");
        richartPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        richartPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(richartPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 50, 20));

        jacquesGeninPrice.setText("₱ 1000");
        jacquesGeninPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jacquesGeninPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(jacquesGeninPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 50, 20));

        amedeiPrice.setText("₱ 1000");
        amedeiPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        amedeiPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(amedeiPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 50, 20));

        wittakersPrice.setText("₱ 1000");
        wittakersPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        wittakersPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(wittakersPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 50, 20));

        drostePrice.setText("₱ 1000");
        drostePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        drostePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(drostePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 50, 20));

        patchi.setText("PATCHI");
        patchi.setMaximumSize(new java.awt.Dimension(118, 118));
        patchi.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(patchi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 90));

        teuscher.setText("TEUSCHER");
        teuscher.setMaximumSize(new java.awt.Dimension(118, 118));
        teuscher.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(teuscher, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 100, 90));

        valrhona.setText("VALRHONA");
        valrhona.setMaximumSize(new java.awt.Dimension(118, 118));
        valrhona.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(valrhona, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 100, 90));

        dove.setText("DOVE");
        dove.setMaximumSize(new java.awt.Dimension(118, 118));
        dove.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(dove, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 100, 90));

        russelStover.setText("RUSSEL STOVER");
        russelStover.setMaximumSize(new java.awt.Dimension(118, 118));
        russelStover.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(russelStover, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, 90));

        russelStoverPrice.setText("₱ 1000");
        russelStoverPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        russelStoverPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(russelStoverPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 320, 50, 20));

        dovePrice.setText("₱ 1000");
        dovePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dovePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(dovePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 50, 20));

        valrhonaPrice.setText("₱ 1000");
        valrhonaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        valrhonaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(valrhonaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 50, 20));

        teuscherPrice.setText("₱ 1000");
        teuscherPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        teuscherPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(teuscherPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 50, 20));

        patchiPrice.setText("₱ 1000");
        patchiPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        patchiPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(patchiPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 50, 20));

        ritterSport.setText("RITTER SPORT");
        ritterSport.setMaximumSize(new java.awt.Dimension(118, 118));
        ritterSport.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(ritterSport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 100, 90));

        guyLian.setText("GUYLIAN");
        guyLian.setMaximumSize(new java.awt.Dimension(118, 118));
        guyLian.setMinimumSize(new java.awt.Dimension(118, 118));
        guyLian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guyLianActionPerformed(evt);
            }
        });
        chocolatesPanel.add(guyLian, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 340, 100, 90));

        kinder.setText("KINDER");
        kinder.setMaximumSize(new java.awt.Dimension(118, 118));
        kinder.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(kinder, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 100, 90));

        mars.setText("MARS");
        mars.setMaximumSize(new java.awt.Dimension(118, 118));
        mars.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(mars, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, 100, 90));

        toblerone.setText("TOBLERONE");
        toblerone.setMaximumSize(new java.awt.Dimension(118, 118));
        toblerone.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(toblerone, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 340, 100, 90));

        godivaPrice.setText("₱ 1000");
        godivaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        godivaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(godivaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 540, 50, 20));

        marsPrice.setText("₱ 1000");
        marsPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        marsPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(marsPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 50, 20));

        kinderPrice.setText("₱ 1000");
        kinderPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        kinderPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(kinderPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 430, 50, 20));

        guyLianPrice.setText("₱ 1000");
        guyLianPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        guyLianPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(guyLianPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 50, 20));

        ritterSportPrice.setText("₱ 1000");
        ritterSportPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ritterSportPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(ritterSportPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 50, 20));

        godiva.setText("GODIVA");
        godiva.setMaximumSize(new java.awt.Dimension(118, 118));
        godiva.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(godiva, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 100, 90));

        nestle.setText("NESTLE");
        nestle.setMaximumSize(new java.awt.Dimension(118, 118));
        nestle.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(nestle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 100, 90));

        milka.setText("MILKA");
        milka.setMaximumSize(new java.awt.Dimension(118, 118));
        milka.setMinimumSize(new java.awt.Dimension(118, 118));
        milka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                milkaActionPerformed(evt);
            }
        });
        chocolatesPanel.add(milka, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, 100, 90));

        ghirardell.setText("GHIRARDELLI");
        ghirardell.setMaximumSize(new java.awt.Dimension(118, 118));
        ghirardell.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(ghirardell, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 100, 90));

        cadbury.setText("CADBURY");
        cadbury.setMaximumSize(new java.awt.Dimension(118, 118));
        cadbury.setMinimumSize(new java.awt.Dimension(118, 118));
        chocolatesPanel.add(cadbury, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 450, 100, 90));

        cadburyPrice.setText("₱ 1000");
        cadburyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cadburyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(cadburyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 50, 20));

        ghirardellPrice.setText("₱ 1000");
        ghirardellPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ghirardellPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(ghirardellPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 540, 50, 20));

        milkaPrice.setText("₱ 1000");
        milkaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        milkaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(milkaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 540, 50, 20));

        nestlePrice.setText("₱ 1000");
        nestlePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        nestlePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(nestlePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, 50, 20));

        tobleronePrice.setText("₱ 1000");
        tobleronePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tobleronePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        chocolatesPanel.add(tobleronePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 430, 50, 20));

        chocolatesScroller.setViewportView(chocolatesPanel);

        menuTabs.addTab("CHOCOLATES", chocolatesScroller);

        beveragesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dole.setText("DOLE");
        dole.setMaximumSize(new java.awt.Dimension(118, 118));
        dole.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(dole, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 100, 90));

        dolePrice.setText("₱ 1000");
        dolePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dolePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(dolePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 50, 20));

        tropicanaPrice.setText("₱ 1000");
        tropicanaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tropicanaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(tropicanaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 50, 20));

        tropicana.setText("TROPICANA");
        tropicana.setMaximumSize(new java.awt.Dimension(118, 118));
        tropicana.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(tropicana, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 90));

        minuteMaid.setText("MINUTE MAID");
        minuteMaid.setMaximumSize(new java.awt.Dimension(118, 118));
        minuteMaid.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(minuteMaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 100, 90));

        minuteMaidPrice.setText("₱ 1000");
        minuteMaidPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        minuteMaidPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(minuteMaidPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 50, 20));

        spritePrice.setText("₱ 1000");
        spritePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        spritePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(spritePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 50, 20));

        sprite.setText("SPRITE");
        sprite.setMaximumSize(new java.awt.Dimension(118, 118));
        sprite.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(sprite, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 100, 90));

        gatoradePrice.setText("₱ 1000");
        gatoradePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gatoradePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(gatoradePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 50, 20));

        gatorade.setText("GATORADE");
        gatorade.setMaximumSize(new java.awt.Dimension(118, 118));
        gatorade.setMinimumSize(new java.awt.Dimension(118, 118));
        gatorade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gatoradeActionPerformed(evt);
            }
        });
        beveragesPanel.add(gatorade, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 100, 90));

        cocaColaPrice.setText("₱ 1000");
        cocaColaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        cocaColaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(cocaColaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 50, 20));

        pepsiPrice.setText("₱ 1000");
        pepsiPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pepsiPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(pepsiPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 50, 20));

        redBullPrice.setText("₱ 1000");
        redBullPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        redBullPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(redBullPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 50, 20));

        budWeiserPrice.setText("₱ 1000");
        budWeiserPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        budWeiserPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(budWeiserPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 50, 20));

        heinekenPrice.setText("₱ 1000");
        heinekenPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        heinekenPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(heinekenPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 100, 50, 20));

        heineken.setText("HEINEKEN");
        heineken.setMaximumSize(new java.awt.Dimension(118, 118));
        heineken.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(heineken, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 100, 90));

        budWeiser.setText("BUD WEISER");
        budWeiser.setMaximumSize(new java.awt.Dimension(118, 118));
        budWeiser.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(budWeiser, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 100, 90));

        redBull.setText("RED BULL");
        redBull.setMaximumSize(new java.awt.Dimension(118, 118));
        redBull.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(redBull, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 100, 90));

        pepsi.setText("PEPSI");
        pepsi.setMaximumSize(new java.awt.Dimension(118, 118));
        pepsi.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(pepsi, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 100, 90));

        cocaCola.setText("COCA-COLA");
        cocaCola.setMaximumSize(new java.awt.Dimension(118, 118));
        cocaCola.setMinimumSize(new java.awt.Dimension(118, 118));
        cocaCola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cocaColaActionPerformed(evt);
            }
        });
        beveragesPanel.add(cocaCola, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 90));

        sunkist.setText("SUNKIST");
        sunkist.setMaximumSize(new java.awt.Dimension(118, 118));
        sunkist.setMinimumSize(new java.awt.Dimension(118, 118));
        sunkist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunkistActionPerformed(evt);
            }
        });
        beveragesPanel.add(sunkist, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, 90));

        koolAid.setText("KOOL AID");
        koolAid.setMaximumSize(new java.awt.Dimension(118, 118));
        koolAid.setMinimumSize(new java.awt.Dimension(118, 118));
        koolAid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                koolAidActionPerformed(evt);
            }
        });
        beveragesPanel.add(koolAid, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, 90));

        sevenUp.setText("7 UP");
        sevenUp.setMaximumSize(new java.awt.Dimension(118, 118));
        sevenUp.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(sevenUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 100, 90));

        mountainDew.setText("MOUNTAIN DEW");
        mountainDew.setMaximumSize(new java.awt.Dimension(118, 118));
        mountainDew.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(mountainDew, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 100, 90));

        lipton.setText("LIPTON");
        lipton.setMaximumSize(new java.awt.Dimension(118, 118));
        lipton.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(lipton, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 100, 90));

        sunkistPrice.setText("₱ 1000");
        sunkistPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sunkistPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(sunkistPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 320, 50, 20));

        liptonPrice.setText("₱ 1000");
        liptonPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        liptonPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(liptonPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 320, 50, 20));

        mountainDewPrice.setText("₱ 1000");
        mountainDewPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        mountainDewPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(mountainDewPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 50, 20));

        sevenUpPrice.setText("₱ 1000");
        sevenUpPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sevenUpPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(sevenUpPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 50, 20));

        koolAidPrice.setText("₱ 1000");
        koolAidPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        koolAidPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(koolAidPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 50, 20));

        appleJuice.setText("APPLE JUICE");
        appleJuice.setMaximumSize(new java.awt.Dimension(118, 118));
        appleJuice.setMinimumSize(new java.awt.Dimension(118, 118));
        appleJuice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appleJuiceActionPerformed(evt);
            }
        });
        beveragesPanel.add(appleJuice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 100, 90));

        pineAppleJuice.setText("PINEAPPLE JUICE");
        pineAppleJuice.setMaximumSize(new java.awt.Dimension(118, 118));
        pineAppleJuice.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(pineAppleJuice, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 340, 100, 90));

        blackCher.setText("BLACK CHERRY");
        blackCher.setMaximumSize(new java.awt.Dimension(118, 118));
        blackCher.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(blackCher, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 100, 90));

        blackCherPrice.setText("₱ 1000");
        blackCherPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        blackCherPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(blackCherPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 430, 50, 20));

        pineAppleJuicePrice.setText("₱ 1000");
        pineAppleJuicePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        pineAppleJuicePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(pineAppleJuicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 50, 20));

        appleJuicePrice.setText("₱ 1000");
        appleJuicePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        appleJuicePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(appleJuicePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 50, 20));

        liquorsLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        liquorsLabel.setText("LIQUORS");
        beveragesPanel.add(liquorsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, -1, -1));

        mead.setText("MEAD");
        mead.setMaximumSize(new java.awt.Dimension(118, 118));
        mead.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(mead, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 500, 100, 90));

        hardCider.setText("HARD CIDER");
        hardCider.setMaximumSize(new java.awt.Dimension(118, 118));
        hardCider.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(hardCider, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 500, 100, 90));

        wine.setText("WINE");
        wine.setMaximumSize(new java.awt.Dimension(118, 118));
        wine.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(wine, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 500, 100, 90));

        beer.setText("BEER");
        beer.setMaximumSize(new java.awt.Dimension(118, 118));
        beer.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(beer, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 500, 100, 90));

        tequila.setText("TEQUILA");
        tequila.setMaximumSize(new java.awt.Dimension(118, 118));
        tequila.setMinimumSize(new java.awt.Dimension(118, 118));
        tequila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tequilaActionPerformed(evt);
            }
        });
        beveragesPanel.add(tequila, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 100, 90));

        tequilaPrice.setText("₱ 1000");
        tequilaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tequilaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(tequilaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 590, 50, 20));

        beerPrice.setText("₱ 1000");
        beerPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        beerPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(beerPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 590, 50, 20));

        winePrice.setText("₱ 1000");
        winePrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        winePrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(winePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 590, 50, 20));

        hardCiderPrice.setText("₱ 1000");
        hardCiderPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        hardCiderPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(hardCiderPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 590, 50, 20));

        meadPrice.setText("₱ 1000");
        meadPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        meadPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(meadPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 590, 50, 20));

        vodka.setText("VODKA");
        vodka.setMaximumSize(new java.awt.Dimension(118, 118));
        vodka.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(vodka, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 610, 100, 90));

        rum.setText("RUM");
        rum.setMaximumSize(new java.awt.Dimension(118, 118));
        rum.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(rum, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 610, 100, 90));

        whisky.setText("WHISKY");
        whisky.setMaximumSize(new java.awt.Dimension(118, 118));
        whisky.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(whisky, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 610, 100, 90));

        brandy.setText("BRANDY");
        brandy.setMaximumSize(new java.awt.Dimension(118, 118));
        brandy.setMinimumSize(new java.awt.Dimension(118, 118));
        beveragesPanel.add(brandy, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 610, 100, 90));

        gin.setText("GIN");
        gin.setMaximumSize(new java.awt.Dimension(118, 118));
        gin.setMinimumSize(new java.awt.Dimension(118, 118));
        gin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ginActionPerformed(evt);
            }
        });
        beveragesPanel.add(gin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 610, 100, 90));

        ginPrice.setText("₱ 1000");
        ginPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ginPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(ginPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 700, 50, 20));

        brandyPrice.setText("₱ 1000");
        brandyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        brandyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(brandyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 700, 50, 20));

        whiskyPrice.setText("₱ 1000");
        whiskyPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        whiskyPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(whiskyPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 700, 50, 20));

        rumPrice.setText("₱ 1000");
        rumPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        rumPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(rumPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 700, 50, 20));

        vodkaPrice.setText("₱ 1000");
        vodkaPrice.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        vodkaPrice.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        beveragesPanel.add(vodkaPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 700, 50, 20));

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
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

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

        mainPanel.add(productsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 90, 1080, 440));

        transactionPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        transactionPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalLabel.setText("TOTAL:");
        totalPanel.add(totalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        totalPesoSignLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalPesoSignLabel.setText("₱");
        totalPanel.add(totalPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        total.setEditable(false);
        total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        total.setText("0.00");
        total.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalPanel.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 170, 30));

        transactionPanel.add(totalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 370, 30));

        subTotalPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        subTotalPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subTotalLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotalLabel.setText("SUB TOTAL:");
        subTotalPanel.add(subTotalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        subTotalPesoSignLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotalPesoSignLabel.setText("₱");
        subTotalPanel.add(subTotalPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        subTotal.setEditable(false);
        subTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        subTotal.setText("0.00");
        subTotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        subTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subTotalActionPerformed(evt);
            }
        });
        subTotalPanel.add(subTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 170, 30));

        transactionPanel.add(subTotalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 370, 30));

        totalDiscountPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalDiscountPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalDiscountPesoSignLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalDiscountPesoSignLabel.setText("₱");
        totalDiscountPanel.add(totalDiscountPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        totalDiscount.setEditable(false);
        totalDiscount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalDiscount.setText("0.00");
        totalDiscount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        totalDiscountPanel.add(totalDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 170, 30));

        totalDiscountLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalDiscountLabel.setText("TOTAL DISCOUNT:");
        totalDiscountPanel.add(totalDiscountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        transactionPanel.add(totalDiscountPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 370, 30));

        chashPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        chashPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cashLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cashLabel.setText("CASH:");
        chashPanel.add(cashLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        cashPesoSignLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cashPesoSignLabel.setText("₱");
        chashPanel.add(cashPesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, -1));

        cash.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cash.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashActionPerformed(evt);
            }
        });
        chashPanel.add(cash, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 160, 30));

        transactionPanel.add(chashPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 310, 30));

        changePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        changePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        changeLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        changeLabel.setText("CHANGE:");
        changePanel.add(changeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        changePesoSignLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        changePesoSignLabel.setText("₱");
        changePanel.add(changePesoSignLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, -1));

        change.setEditable(false);
        change.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        change.setText("0.00");
        change.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        changePanel.add(change, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 160, 30));

        transactionPanel.add(changePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 310, 30));

        printReceipt.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        printReceipt.setText("PRINT RECEIPT");
        printReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printReceiptActionPerformed(evt);
            }
        });
        transactionPanel.add(printReceipt, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 210, 40));

        pay.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        pay.setText("PAY");
        pay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payActionPerformed(evt);
            }
        });
        transactionPanel.add(pay, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 20, 110, 40));

        removeItem.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        removeItem.setText("REMOVE ITEM");
        transactionPanel.add(removeItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 90, 210, 40));

        reset.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        reset.setText("RESET");
        transactionPanel.add(reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 90, 110, 40));

        mainPanel.add(transactionPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 540, 1080, 150));

        getContentPane().add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1368, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void payActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_payActionPerformed

    private void hersheysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hersheysActionPerformed

    }//GEN-LAST:event_hersheysActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        System.exit(0);
    }//GEN-LAST:event_logoutActionPerformed

    private void viewExpiredProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewExpiredProductsActionPerformed

    }//GEN-LAST:event_viewExpiredProductsActionPerformed

    private void cleanFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanFirstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cleanFirstActionPerformed

    private void myCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myCleanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myCleanActionPerformed

    private void gatoradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gatoradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gatoradeActionPerformed

    private void cocaColaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cocaColaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cocaColaActionPerformed

    private void cashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashActionPerformed

    private void subTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subTotalActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_subTotalActionPerformed

    private void printReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printReceiptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printReceiptActionPerformed

    private void guyLianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guyLianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_guyLianActionPerformed

    private void milkaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_milkaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_milkaActionPerformed

    private void koolAidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_koolAidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_koolAidActionPerformed

    private void appleJuiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appleJuiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_appleJuiceActionPerformed

    private void sunkistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunkistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sunkistActionPerformed

    private void tequilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tequilaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tequilaActionPerformed

    private void ginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ginActionPerformed

    private void smartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_smartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>Application.runSpringServer(args);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton amedei;
    private javax.swing.JLabel amedeiPrice;
    private javax.swing.JButton appleJuice;
    private javax.swing.JLabel appleJuicePrice;
    private javax.swing.JLabel arielPrice;
    private javax.swing.JButton beer;
    private javax.swing.JLabel beerPrice;
    private javax.swing.JPanel beveragesPanel;
    private javax.swing.JScrollPane beveragesScroller;
    private javax.swing.JButton blackCher;
    private javax.swing.JLabel blackCherPrice;
    private javax.swing.JButton brandy;
    private javax.swing.JLabel brandyPrice;
    private javax.swing.JButton budWeiser;
    private javax.swing.JLabel budWeiserPrice;
    private javax.swing.JButton cadbury;
    private javax.swing.JLabel cadburyPrice;
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
    private javax.swing.JLabel cleanCutPrice;
    private javax.swing.JButton cleanFirst;
    private javax.swing.JLabel cleanFirstPrice;
    private javax.swing.JPanel cleaningProductsPanel;
    private javax.swing.JScrollPane cleaningProductsScroller;
    private javax.swing.JButton clorox;
    private javax.swing.JLabel cloroxPrice;
    private javax.swing.JButton cocaCola;
    private javax.swing.JLabel cocaColaPrice;
    private javax.swing.JLabel date;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JPanel datePanel;
    private javax.swing.JLabel day;
    private javax.swing.JLabel dayTodayLabel;
    private javax.swing.JPanel dayTodayPanel;
    private javax.swing.JButton dirtBuster;
    private javax.swing.JLabel dirtBustersPrice;
    private javax.swing.JButton dole;
    private javax.swing.JLabel dolePrice;
    private javax.swing.JButton domex;
    private javax.swing.JLabel domexPrice;
    private javax.swing.JButton dove;
    private javax.swing.JLabel dovePrice;
    private javax.swing.JButton droste;
    private javax.swing.JLabel drostePrice;
    private javax.swing.JButton esthechoc;
    private javax.swing.JLabel esthechocPrice;
    private javax.swing.JPanel expiredProductsInformationPanel;
    private javax.swing.JLabel expiredProductsLabel;
    private javax.swing.JButton ferreroRocher;
    private javax.swing.JLabel ferreroRocherPrice;
    private javax.swing.JButton flyingNoir;
    private javax.swing.JLabel flyingNoirPrice;
    private javax.swing.JButton gatorade;
    private javax.swing.JLabel gatoradePrice;
    private javax.swing.JButton ghirardell;
    private javax.swing.JLabel ghirardellPrice;
    private javax.swing.JButton gin;
    private javax.swing.JLabel ginPrice;
    private javax.swing.JButton godiva;
    private javax.swing.JLabel godivaPrice;
    private javax.swing.JButton guyLian;
    private javax.swing.JLabel guyLianPrice;
    private javax.swing.JButton hardCider;
    private javax.swing.JLabel hardCiderPrice;
    private javax.swing.JLabel header;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton heineken;
    private javax.swing.JLabel heinekenPrice;
    private javax.swing.JButton hersheys;
    private javax.swing.JLabel hersheysPrice;
    private javax.swing.JButton hydroSafe;
    private javax.swing.JLabel hydroSafePrice;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JLabel jacquesGeninPrice;
    private javax.swing.JButton jacqueslGenin;
    private javax.swing.JButton joy;
    private javax.swing.JLabel joyPrice;
    private javax.swing.JButton kinder;
    private javax.swing.JLabel kinderPrice;
    private javax.swing.JButton koolAid;
    private javax.swing.JLabel koolAidPrice;
    private javax.swing.JButton lipton;
    private javax.swing.JLabel liptonPrice;
    private javax.swing.JLabel liquorsLabel;
    private javax.swing.JButton logout;
    private javax.swing.JButton lysol;
    private javax.swing.JLabel lysolPrice;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton mars;
    private javax.swing.JLabel marsPrice;
    private javax.swing.JButton mead;
    private javax.swing.JLabel meadPrice;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTabbedPane menuTabs;
    private javax.swing.JButton milka;
    private javax.swing.JLabel milkaPrice;
    private javax.swing.JButton minuteMaid;
    private javax.swing.JLabel minuteMaidPrice;
    private javax.swing.JButton mountainDew;
    private javax.swing.JLabel mountainDewPrice;
    private javax.swing.JButton mrClean;
    private javax.swing.JButton mrMuscle;
    private javax.swing.JLabel mrMusclePrice;
    private javax.swing.JButton myClean;
    private javax.swing.JLabel myCleanPrice;
    private javax.swing.JButton nestle;
    private javax.swing.JLabel nestlePrice;
    private javax.swing.JLabel numberOfExpiredProducts;
    private javax.swing.JPanel ordersPanel;
    private javax.swing.JTable ordersTable;
    private javax.swing.JScrollPane ordersTableScroller;
    private javax.swing.JButton patchi;
    private javax.swing.JLabel patchiPrice;
    private javax.swing.JButton pay;
    private javax.swing.JButton pepsi;
    private javax.swing.JLabel pepsiPrice;
    private javax.swing.JButton pineAppleJuice;
    private javax.swing.JLabel pineAppleJuicePrice;
    private javax.swing.JButton printReceipt;
    private javax.swing.JPanel productsPanel;
    private javax.swing.JButton redBull;
    private javax.swing.JLabel redBullPrice;
    private javax.swing.JButton removeItem;
    private javax.swing.JButton reset;
    private javax.swing.JButton richart;
    private javax.swing.JLabel richartPrice;
    private javax.swing.JButton rightFlex;
    private javax.swing.JLabel rightFlexPrice;
    private javax.swing.JButton ritterSport;
    private javax.swing.JLabel ritterSportPrice;
    private javax.swing.JButton rum;
    private javax.swing.JLabel rumPrice;
    private javax.swing.JButton russelStover;
    private javax.swing.JLabel russelStoverPrice;
    private javax.swing.JButton sevenUp;
    private javax.swing.JLabel sevenUpPrice;
    private javax.swing.JButton smart;
    private javax.swing.JLabel smartPrice;
    private javax.swing.JButton snickers;
    private javax.swing.JLabel snickersPrice;
    private javax.swing.JButton sprite;
    private javax.swing.JLabel spritePrice;
    private javax.swing.JTextField subTotal;
    private javax.swing.JLabel subTotalLabel;
    private javax.swing.JPanel subTotalPanel;
    private javax.swing.JLabel subTotalPesoSignLabel;
    private javax.swing.JButton sunkist;
    private javax.swing.JLabel sunkistPrice;
    private javax.swing.JButton sureClean;
    private javax.swing.JLabel sureCleanPrice;
    private javax.swing.JButton surf;
    private javax.swing.JLabel surfPrice;
    private javax.swing.JButton tequila;
    private javax.swing.JLabel tequilaPrice;
    private javax.swing.JButton teuscher;
    private javax.swing.JLabel teuscherPrice;
    private javax.swing.JLabel time;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JPanel timePanel;
    private javax.swing.JButton toblerone;
    private javax.swing.JLabel tobleronePrice;
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
    private javax.swing.JLabel tropicanaPrice;
    private javax.swing.JButton valrhona;
    private javax.swing.JLabel valrhonaPrice;
    private javax.swing.JButton viewExpiredProducts;
    private javax.swing.JButton viewSales;
    private javax.swing.JButton vodka;
    private javax.swing.JLabel vodkaPrice;
    private javax.swing.JButton whisky;
    private javax.swing.JLabel whiskyPrice;
    private javax.swing.JButton wine;
    private javax.swing.JLabel winePrice;
    private javax.swing.JButton wittakers;
    private javax.swing.JLabel wittakersPrice;
    // End of variables declaration//GEN-END:variables
}

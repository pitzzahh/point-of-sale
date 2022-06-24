package com.pos.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pos.repository.ProductRepository;
import java.util.stream.Collectors;
import com.pos.entity.Category;
import com.pos.entity.Product;
import java.util.function.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import com.pos.validation.Checker;
import java.util.List;
import java.io.File;

@Transactional
@Service("productService")
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Supplier<Integer> getExpiredProductsCount() {
        updateAllProductsExpirationDate();
        return () -> (int) productRepository.getAllProducts()
                .stream()
                .filter(Product::getExpired)
                .count();
    }

    @Override
    public Consumer<Product> saveProduct() {
        return productRepository::save;
    }

    @Override
    public Consumer<List<Product>> saveAllProducts() {
        return productRepository::saveAll;
    }

    @Override
    public Function<Integer, Optional<Product>> getProductById() {
        return id -> Optional.ofNullable(productRepository.getProductById(id));
    }

    @Override
    public Function<String, Product> getProductByName() {
        return productRepository::getProductByName;
    }

    @Override
    public Function<Integer, Integer> getProductStocksById() {
        return productRepository::getProductStocksById;
    }

    @Override
    public Supplier<List<Product>> getAllProducts() {
        return productRepository::getAllProducts;
    }

    @Override
    public BiConsumer<Double, Integer> updateProductPriceById() {
        return (newPrice, id) -> productRepository.updateProductPriceById(newPrice, id);
    }

    @Override
    public BiConsumer<Integer, String> updateProductStocksByName() {
        return (newStock, name) -> productRepository.updateProductStocksByName(newStock, name);
    }

    @Override
    public BiConsumer<Double, Integer> updateProductDiscountById() {
        return (newDiscount, id) -> productRepository.updateProductDiscountById(newDiscount, id);
    }

    @Override
    public Consumer<Integer> deleteProductById() {
        return productRepository::deleteProductById;
    }

    @Override
    public void deleteAllExpiredProducts() {
        productRepository.deleteAllExpiredProducts();
    }

    /**
     * checks all the product's expiration date and updates the expired column if a product is expired.
     */
    @Override
    public void updateAllProductsExpirationDate() {
        productRepository
                .getAllProducts()
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getExpirationDate))
                .forEach(
                        (id, expirationDate) ->
                                productRepository.updateProductExpiredStatusById(expirationDate.isBefore(LocalDate.now()), id));

    }

    /**
     * Method that inserts all the products to the table in the database.
     * If the products have been added to the table in the database, it will skip the insertion.
     */
    @Override
    public void insertAllProductsToDatabase() {
//        if (!new File("src/main/resources/check.txt").exists()) productRepository.saveAll(initializeAllProducts());
//        Checker.createFile();
        try {
            productRepository.saveAll(initializeAllProducts());
        } catch (Exception ignored) {}
    }

    /**
     * Method that makes all the products to be added to the table in the database.
     * @return a {@code List<Product>} containing the products.
     */
    private List<Product> initializeAllProducts() {

        /*
            id (auto increment)
            name
            price
            category
            expirationDate
            stocks
            discount
            expired
         */
        List<Product> products = new ArrayList<>();
        products.add(
                new Product(
                        1,
                        "CLEAN FIRST",
                        45.69,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        2,
                        "HYDRO SAFE",
                        40.41,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        3,
                        "RIGHT FLEX",
                        34.12,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        4,
                        "CLOROX",
                        35.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        5,
                        "DIRTBUSTERS",
                        45.69,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        6,
                        "MY CLEAN",
                        89.60,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        7,
                        "CLEAN CUT",
                        120.12,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        8,
                        "SURE CLEAN",
                        150.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        9,
                        "ARIEL",
                        50.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        10,
                        "JOY",
                        10.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        11,
                        "SMART DISHWASHING PASTE",
                        35.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        12,
                        "DOMEX",
                        36.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        13,
                        "MR MUSCLE",
                        69.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        14,
                        "LYSOL",
                        45.69,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        15,
                        "SURF POWDER",
                        7.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        0.0,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        16,
                        "HERSHEYS",
                        32.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 3),
                        100,
                        0.0,
                        LocalDate.of(2022, 7, 3).isBefore(LocalDate.now())
                )
        );
         /*
            Chocolates
         */
        products.add(
                new Product(
                        17,
                        "SNICKERS",
                        24.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 14),
                        100,
                        0.0,
                        LocalDate.of(2022, 7, 14).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        18,
                        "FERRERO ROCHER",
                        120.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 8, 12),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        19,
                        "ESTHECHOC",
                        240.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 3),
                        100,
                        0.0,
                        LocalDate.of(2022, 7, 3).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        20,
                        "FLYING NOIR",
                        300.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 6, 22),
                        100,
                        0.0,
                        LocalDate.of(2022, 6, 22).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        21,
                        "DROSTE",
                        120.54,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 6, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 6, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        22,
                        "WITTAKER'S",
                        126.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 25),
                        100,
                        0.0,
                        LocalDate.of(2022, 7, 25).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        23,
                        "AMEDEI",
                        260.69,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 12, 12),
                        100,
                        0.0,
                        LocalDate.of(2022, 12, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        24,
                        "JACQUES GENIN",
                        240.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 3),
                        100,
                        0.0,
                        LocalDate.of(2022, 7, 3).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        25,
                        "RICHART",
                        50.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 5, 12),
                        100,
                        0.0,
                        LocalDate.of(2022, 5, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        26,
                        "PATCHI",
                        320.69,
                        Category.CHOCOLATE,
                        LocalDate.of(2023, 5, 12),
                        100,
                        0.0,
                        LocalDate.of(2023, 5, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        27,
                        "TEUSCHER",
                        120.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 3, 12),
                        100,
                        0.0,
                        LocalDate.of(2022, 3, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        28,
                        "VALRHONA",
                        150.78,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 3, 24),
                        100,
                        0.0,
                        LocalDate.of(2022, 3, 24).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        29,
                        "DOVE",
                        150.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 4, 30),
                        100,
                        0.0,
                        LocalDate.of(2022, 4, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        30,
                        "RUSSEL STOVER",
                        130.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 8, 12),
                        100,
                        0.0,
                        LocalDate.of(2022, 3, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        31,
                        "RITTER SPORT",
                        170.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 9, 14),
                        100,
                        0.0,
                        LocalDate.of(2022, 9, 14).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        32,
                        "GUYLIAN",
                        150.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 4, 30),
                        100,
                        0.0,
                        LocalDate.of(2022, 4, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        33,
                        "KINDER",
                        121.61,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 12, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 12, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        34,
                        "MARS",
                        32.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 2, 12),
                        100,
                        0.0,
                        LocalDate.of(2022, 2, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        35,
                        "TOBLERONE",
                        150.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 9, 30),
                        100,
                        0.0,
                        LocalDate.of(2022, 9, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        36,
                        "NESTLE",
                        143.53,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 4, 30),
                        100,
                        31.00,
                        LocalDate.of(2022, 4, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        37,
                        "MILKA",
                        240.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 8, 19),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 19).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        38,
                        "GHIRARDELLI",
                        310.78,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 9, 4),
                        100,
                        15.12,
                        LocalDate.of(2022, 9, 4).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        39,
                        "CADBURY",
                        80.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 1, 23),
                        100,
                        14.00,
                        LocalDate.of(2022, 1, 23).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        40,
                        "GODIVA",
                        100.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 2, 15),
                        100,
                        12.00,
                        LocalDate.of(2022, 2, 15).isBefore(LocalDate.now())
                )
        );
        /*
            Beverages
         */
        products.add(
                new Product(
                        41,
                        "COCA-COLA",
                        75.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        5.00,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())

                )
        );
        products.add(
                new Product(
                        42,
                        "PEPSI",
                        50.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        2.00,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        43,
                        "RED BULL",
                        67.12,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 7, 22),
                        100,
                        2.00,
                        LocalDate.of(2022, 7, 22).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        44,
                        "BUD WISER",
                        120.43,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 29),
                        100,
                        2.00,
                        LocalDate.of(2022, 8, 29).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        45,
                        "HEINEKEN",
                        45.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 22),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 22).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        46,
                        "GATORADE",
                        35.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 25),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 25).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        47,
                        "SPRITE",
                        65.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        48,
                        "MINUTE MAID",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        49,
                        "TROPICANA",
                        80.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        50,
                        "DOLE",
                        32.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 6, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 6, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        51,
                        "KOOL AID",
                        20.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 6, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 6, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        52,
                        "7 UP",
                        65.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        53,
                        "MOUNTAIN DEW",
                        15.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        54,
                        "LIPTON",
                        43.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        55,
                        "SUNKIST",
                        76.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        56,
                        "APPLE JUICE",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        57,
                        "PINEAPPLE JUICE",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        58,
                        "BLACK CHERRY",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        // liquors
        products.add(
                new Product(
                        59,
                        "TEQUILA",
                        2200.69,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        60,
                        "BEER",
                        150.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        61,
                        "WINE",
                        2000.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        62,
                        "HARD CIDER",
                        500.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        63,
                        "MEAD",
                        400.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        64,
                        "GIN",
                        90.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        65,
                        "BRANDY",
                        760.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        66,
                        "WHISKY",
                        300.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 29),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 29).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        67,
                        "RUM",
                        320.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 7, 21),
                        100,
                        0.0,
                        LocalDate.of(2022, 7, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        68,
                        "VODKA",
                        240.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 15),
                        100,
                        0.0,
                        LocalDate.of(2022, 10, 15).isBefore(LocalDate.now())
                )
        );
        return products;
    }
}


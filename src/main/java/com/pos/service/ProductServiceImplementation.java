package com.pos.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pos.repository.ProductRepository;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.pos.entity.Category;
import com.pos.entity.Product;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("productService")
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Supplier<Integer> getExpiredProductsCount() {
        return () -> (int) productRepository.getAllProductsExpirationDate()
                .stream().filter(localDate -> localDate.isBefore(LocalDate.now()))
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
    public Function<Integer, Product> getProductById() {
        return productRepository::getProductById;
    }

    @Override
    public Function<String, Product> getProductByName() {
        return productRepository::getProductByName;
    }

    @Override
    public Supplier<List<Product>> getAllProducts() {
        return productRepository::getAllProducts;
    }

    @Override
    public BiConsumer<LocalDate, Integer> updateProductExpiredStatusById() {
        return (newExpirationDate, id) -> productRepository.updateProductExpiredStatusById(false, id);
    }

    @Override
    public Consumer<Integer> deleteProductById() {
        return productRepository::deleteProductById;
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

    @Override
    public void insertAllProductsToDatabase() {
        int size = productRepository.getAllProducts().size();
        if (initializeAllProducts().size() != size) productRepository.saveAll(initializeAllProducts());
    }
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
                        "CLEAN FIRST",
                        45.69,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "HYDRO SAFE",
                        40.41,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "RIGHT FLEX",
                        34.12,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "CLOROX",
                        35.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "DIRTBUSTERS",
                        45.69,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "MY CLEAN",
                        89.60,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "CLEAN CUT",
                        120.12,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "SURE CLEAN",
                        150.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "ARIEL",
                        50.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "JOY",
                        10.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "SMART DISHWASHING PASTE",
                        35.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "DOMEX",
                        36.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "MR MUSCLE",
                        69.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "LYSOL",
                        45.69,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "SURF POWDER",
                        7.00,
                        Category.CLEANING_PRODUCT,
                        LocalDate.of(2023, 1, 1),
                        100,
                        null,
                        LocalDate.of(2023, 1, 1).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "HERSHEYS",
                        32.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 3),
                        100,
                        null,
                        LocalDate.of(2022, 7, 3).isBefore(LocalDate.now())
                )
        );
         /*
            Chocolates
         */
        products.add(
                new Product(
                        "SNICKERS",
                        24.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 14),
                        100,
                        null,
                        LocalDate.of(2022, 7, 14).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "FERRERO ROCHER",
                        120.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 8, 12),
                        100,
                        null,
                        LocalDate.of(2022, 8, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "ESTHECHOC",
                        240.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 3),
                        100,
                        null,
                        LocalDate.of(2022, 7, 3).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "FLYING NOIR",
                        300.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 6, 22),
                        100,
                        null,
                        LocalDate.of(2022, 6, 22).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "DROSTE",
                        120.54,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 6, 21),
                        100,
                        null,
                        LocalDate.of(2022, 6, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "WITTAKER'S",
                        126.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 25),
                        100,
                        null,
                        LocalDate.of(2022, 7, 25).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "AMEDEI",
                        260.69,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 12, 12),
                        100,
                        null,
                        LocalDate.of(2022, 12, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "JACQUES GENIN",
                        240.00,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 7, 3),
                        100,
                        null,
                        LocalDate.of(2022, 7, 3).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "RICHART",
                        50.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 5, 12),
                        100,
                        null,
                        LocalDate.of(2022, 5, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "PATCHI",
                        320.69,
                        Category.CHOCOLATE,
                        LocalDate.of(2023, 5, 12),
                        100,
                        null,
                        LocalDate.of(2023, 5, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "TEUSCHER",
                        120.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 3, 12),
                        100,
                        null,
                        LocalDate.of(2022, 3, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "VALRHONA",
                        150.78,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 3, 24),
                        100,
                        null,
                        LocalDate.of(2022, 3, 24).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "DOVE",
                        150.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 4, 30),
                        100,
                        null,
                        LocalDate.of(2022, 4, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "RUSSEL STOVER",
                        130.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 8, 12),
                        100,
                        null,
                        LocalDate.of(2022, 3, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "RITTER SPORT",
                        170.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 9, 14),
                        100,
                        null,
                        LocalDate.of(2022, 9, 14).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "GUYLIAN",
                        150.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 4, 30),
                        100,
                        null,
                        LocalDate.of(2022, 4, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "KINDER",
                        121.61,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 12, 21),
                        100,
                        null,
                        LocalDate.of(2022, 12, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "MARS",
                        32.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 2, 12),
                        100,
                        null,
                        LocalDate.of(2022, 2, 12).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "TOBLERONE",
                        150.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 9, 30),
                        100,
                        null,
                        LocalDate.of(2022, 9, 30).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
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
                        "MILKA",
                        240.12,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 8, 19),
                        100,
                        null,
                        LocalDate.of(2022, 8, 19).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "GHIRARDELLI",
                        310.78,
                        Category.CHOCOLATE,
                        LocalDate.of(2022, 9, 4),
                        100,
                        null,
                        LocalDate.of(2022, 9, 4).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
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
                        "HEINEKEN",
                        45.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 22),
                        100,
                        null,
                        LocalDate.of(2022, 8, 22).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "GATORADE",
                        35.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 25),
                        100,
                        null,
                        LocalDate.of(2022, 8, 25).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "SPRITE",
                        65.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "MINUTE MAID",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "TROPICANA",
                        80.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "DOLE",
                        32.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 6, 21),
                        100,
                        null,
                        LocalDate.of(2022, 6, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "KOOL AID",
                        20.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 6, 21),
                        100,
                        null,
                        LocalDate.of(2022, 6, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "7 UP",
                        65.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "MOUNTAIN DEW",
                        15.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "LIPTON",
                        43.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "SUNKIST",
                        76.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "APPLE JUICE",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "PINEAPPLE JUICE",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "BLACK CHERRY",
                        25.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 8, 21),
                        100,
                        null,
                        LocalDate.of(2022, 8, 21).isBefore(LocalDate.now())
                )
        );
        // liquors
        products.add(
                new Product(
                        "TEQUILA",
                        2200.69,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "BEER",
                        150.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "WINE",
                        2000.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "HARD CIDER",
                        500.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "MEAD",
                        400.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "GIN",
                        90.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "BRANDY",
                        760.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 21),
                        100,
                        null,
                        LocalDate.of(2022, 10, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "WHISKY",
                        300.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 29),
                        100,
                        null,
                        LocalDate.of(2022, 10, 29).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "RUM",
                        320.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 7, 21),
                        100,
                        null,
                        LocalDate.of(2022, 7, 21).isBefore(LocalDate.now())
                )
        );
        products.add(
                new Product(
                        "VODKA",
                        240.00,
                        Category.BEVERAGE,
                        LocalDate.of(2022, 10, 15),
                        100,
                        null,
                        LocalDate.of(2022, 10, 15).isBefore(LocalDate.now())
                )
        );
        return products;
    }
}


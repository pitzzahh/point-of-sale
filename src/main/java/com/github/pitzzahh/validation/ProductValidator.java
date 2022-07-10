package com.github.pitzzahh.validation;

import java.util.function.Function;
import com.github.pitzzahh.entity.Product;

/**
 *
 * @author peter
 */
// TODO Create a validation for products
public interface ProductValidator extends Function<Product, Boolean> {

}

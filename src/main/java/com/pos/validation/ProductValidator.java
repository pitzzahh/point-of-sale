package com.pos.validation;

import java.util.function.Function;
import com.pos.entity.Product;

/**
 *
 * @author peter
 */
// TODO Create a validation for products
public interface ProductValidator extends Function<Product, Boolean> {

}

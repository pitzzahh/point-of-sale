package com.pos.validation;

import static com.pos.validation.ProductValidator.ValidationResult;
import static com.pos.validation.ProductValidator.ValidationResult.*;
import java.util.function.Function;
import com.pos.entity.Product;

/**
 *
 * @author peter
 */
public interface ProductValidator extends Function<Product, ValidationResult> {

    static ProductValidator isProductExpired() {
        return p -> p.getExpired() ? EXPIRED : VALID;
    }
    static ProductValidator isProductNotAvailable() {
        return p -> p.getId() == null ? NOT_AVAILABLE : VALID;
    }

    /**
     * Method that chains Function together.
     * @param otherValidation the other validation.
     * @return a {@code ValidationResult} whether it is a {@code VALID} or whatever the result of the chained Function.
     */
    default ProductValidator and(ProductValidator otherValidation) {
        return student -> this.apply(student).equals(VALID) ? otherValidation.apply(student) : this.apply(student);
    }
    enum ValidationResult {
        VALID,
        EXPIRED,
        NOT_AVAILABLE
    }
}

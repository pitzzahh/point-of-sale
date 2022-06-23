package com.pos.validation;

import static com.pos.validation.ProductValidator.ValidationResult.*;
import static com.pos.validation.ProductValidator.ValidationResult;
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

    enum ValidationResult {
        VALID,
        EXPIRED,
        NOT_AVAILABLE
    }
}

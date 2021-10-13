package com.ardctraining.core.product.service;

import com.ardctraining.core.model.CostumProductLabelModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

public interface CustomProductLabelService {

    /**
     * Finds a custom label by customer and product
     * @param customer
     * @param product
     * @return
     */

    List<CostumProductLabelModel> findByCustomerAndProduct(CustomerModel customer, ProductModel product);

    /**
     * fins all expired labels
     * @return
     */
    List<CostumProductLabelModel> findExpired();

    /**
     * Find all labels of cusstomer and product or null customer and product
     * @param customer
     * @param product
     * @return
     */
    List<CostumProductLabelModel> findByCustomerAndProductAndNullCustomer(CustomerModel customer, ProductModel product);

    /**
     * finds a product
     * @param product
     * @return
     */
    List<CostumProductLabelModel> findByProduct(ProductModel product);

}

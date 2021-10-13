package com.ardctraining.core.product.dao;

import com.ardctraining.core.model.CostumProductLabelModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;



public interface CustomProductLabelDao {
    /**
     * Find by customer and product
     * @param customer
     * @param product
     * @return
     */

    List<CostumProductLabelModel> findByCustomerAndProduct(CustomerModel customer, ProductModel product);

    /**
     * fins all expired labels
     * @param now
     * @return
     */
    List<CostumProductLabelModel> findExpired(Date now);

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

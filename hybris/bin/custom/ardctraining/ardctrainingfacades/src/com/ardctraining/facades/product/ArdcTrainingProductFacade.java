package com.ardctraining.facades.product;

import com.ardctraining.facades.product.data.CustomProductLabelData;
import de.hybris.platform.commercefacades.product.ProductFacade;

import java.util.List;

public interface ArdcTrainingProductFacade extends ProductFacade {
    /**
     * get custom labels by product
     * @param product
     * @return
     */
    List<CustomProductLabelData> getCustomLabels(final String product);

    /**
     * Get custom labels by customer amd product
     * @param customerId
     * @param productCode
     * @return
     */
    List<CustomProductLabelData> getCustomLabelsByCustomerAndProduct(final String customerId, final String productCode);
}

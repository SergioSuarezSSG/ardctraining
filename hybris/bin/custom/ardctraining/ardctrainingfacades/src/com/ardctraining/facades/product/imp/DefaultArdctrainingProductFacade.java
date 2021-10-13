package com.ardctraining.facades.product.imp;

import com.ardctraining.core.model.CostumProductLabelModel;
import com.ardctraining.core.product.service.CustomProductLabelService;
import com.ardctraining.facades.product.ArdcTrainingProductFacade;
import com.ardctraining.facades.product.data.CustomProductLabelData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;

public class DefaultArdctrainingProductFacade extends DefaultProductFacade implements ArdcTrainingProductFacade {

    private CustomProductLabelService customProductLabelService;
    private Converter<CostumProductLabelModel, CustomProductLabelData> customProductLabelDataConverter;

    private static final Logger LOG = Logger.getLogger(DefaultArdctrainingProductFacade.class);

    @Override
    public List<CustomProductLabelData> getCustomLabels(final String productCode) {
        try {
            final ProductModel product = getProductService().getProductForCode(productCode);
            final UserModel user = getUserService().getCurrentUser();

            if (Objects.nonNull(user) && user instanceof CustomerModel){
                final CustomerModel customer = (CustomerModel) user;
                final List<CostumProductLabelModel> labels = getCustomProductLabelService().findByCustomerAndProduct(customer,product);

                return getCustomProductLabelDataConverter().convertAll(labels);
            } else {
                LOG.error(String.format("unable to get custom labels with current user %s", user.getUid()));
            }

        } catch (final UnknownIdentifierException | AmbiguousIdentifierException ex){
            LOG.error(String.format("unable to find product with code %s",productCode),ex);
        }

        return Collections.emptyList();
    }

    @Override
    public List<CustomProductLabelData> getCustomLabelsByCustomerAndProduct(final String customerId,final String productCode) {
        final CustomerModel user = getUserService().getUserForUID(customerId, CustomerModel.class);
        final ProductModel product = getProductService().getProductForCode(productCode);

        return getCustomProductLabelDataConverter().convertAll(getCustomProductLabelService().findByCustomerAndProduct(user, product));
    }

    public CustomProductLabelService getCustomProductLabelService() {
        return customProductLabelService;
    }

    public void setCustomProductLabelService(CustomProductLabelService customProductLabelService) {
        this.customProductLabelService = customProductLabelService;
    }

    public Converter<CostumProductLabelModel, CustomProductLabelData> getCustomProductLabelDataConverter() {
        return customProductLabelDataConverter;
    }

    public void setCustomProductLabelDataConverter(Converter<CostumProductLabelModel, CustomProductLabelData> customProductLabelDataConverter) {
        this.customProductLabelDataConverter = customProductLabelDataConverter;
    }
}

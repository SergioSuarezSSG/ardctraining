package com.ardctraining.facades.populators;

import com.ardctraining.core.model.CostumProductLabelModel;
import com.ardctraining.facades.product.data.CustomProductLabelData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class CustomProductLabelPopulator implements Populator<CostumProductLabelModel, CustomProductLabelData> {

    @Override
    public void populate(final CostumProductLabelModel source,final CustomProductLabelData target) throws ConversionException {
        target.setType(source.getLabelType().getCode().toLowerCase());
        target.setLabel(source.getLabel());
    }
}

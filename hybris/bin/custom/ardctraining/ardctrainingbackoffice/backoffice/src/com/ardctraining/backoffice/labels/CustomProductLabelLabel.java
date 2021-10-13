package com.ardctraining.backoffice.labels;

import com.ardctraining.core.model.CostumProductLabelModel;
import com.hybris.cockpitng.labels.LabelProvider;

import java.util.Objects;

public class CustomProductLabelLabel implements LabelProvider<CostumProductLabelModel>{

    private static final String ALL_WILDCARD ="*";

    @Override
    public String getLabel(CostumProductLabelModel model) {
        final String customer = Objects.nonNull(model.getCustomer()) ?
                model.getCustomer().getUid() : ALL_WILDCARD;


        return new StringBuilder()
                .append(model.getProduct().getCode())
                .append(" - ")
                .append(customer)
                .append(" - ")
                .append(model.getLabel())
                .toString();
    }

    @Override
    public String getDescription(CostumProductLabelModel model) {
        return getLabel(model);
    }

    @Override
    public String getIconPath(CostumProductLabelModel model) {
        return null;
    }
}

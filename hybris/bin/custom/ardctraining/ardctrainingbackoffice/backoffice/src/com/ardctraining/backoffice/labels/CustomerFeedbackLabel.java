package com.ardctraining.backoffice.labels;

import com.ardctraining.core.model.CustomerFeedbackModel;
import com.hybris.cockpitng.labels.LabelProvider;

import java.util.Objects;

public class CustomerFeedbackLabel implements LabelProvider<CustomerFeedbackModel> {
    private static final String ALL_WILDCARD ="*";

    @Override
    public String getLabel(CustomerFeedbackModel model) {
        final String customer = Objects.nonNull(model.getCustomer()) ?
                model.getCustomer().getUid() : ALL_WILDCARD;


        return new StringBuilder()
                .append(model.getSubject())
                .append(" - ")
                .append(customer)
                .append(" - ")
                .append(model.getStatus())
                .toString();
    }

    @Override
    public String getDescription(CustomerFeedbackModel model) {
        return getLabel(model);
    }

    @Override
    public String getIconPath(CustomerFeedbackModel model) {
        return null;
    }
}

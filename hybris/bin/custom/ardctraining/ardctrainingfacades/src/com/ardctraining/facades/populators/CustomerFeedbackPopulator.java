package com.ardctraining.facades.populators;

import com.ardctraining.core.model.CustomerFeedbackModel;
import com.ardctraining.facades.customer.data.CustomerFeedbackData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class CustomerFeedbackPopulator implements Populator <CustomerFeedbackModel, CustomerFeedbackData>{
    @Override
    public void populate(final CustomerFeedbackModel source,final CustomerFeedbackData target) throws ConversionException {
        target.setSubject(source.getSubject());
        target.setMessage(source.getMessage());
        target.setSubmittedDate(String.valueOf(source.getSubmittedDate()));
    }
}

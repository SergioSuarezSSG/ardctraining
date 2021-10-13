package com.ardctraining.facades.customerfeedback.imp;

import com.ardctraining.core.feedback.service.CustomerFeedbackService;
import com.ardctraining.core.model.CustomerFeedbackModel;
import com.ardctraining.facades.customer.data.CustomerFeedbackData;
import com.ardctraining.facades.customerfeedback.ArdcTrainingCustomerFeedbackFacade;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultArdcTrainingCustomerFeedbackFacade extends DefaultCustomerFacade implements ArdcTrainingCustomerFeedbackFacade {
    private CustomerFeedbackService customerFeedbackService;
    private Converter<CustomerFeedbackModel, CustomerFeedbackData> customerFeedbackConverter;

    private static final Logger LOG = Logger.getLogger(DefaultArdcTrainingCustomerFeedbackFacade.class);
    @Override
    public List<CustomerFeedbackData> findFeedbacks() {
        try {
            final UserModel user = getUserService().getCurrentUser();

            if (Objects.nonNull(user) && user instanceof CustomerModel){
                final CustomerModel customer = (CustomerModel) user;
                final List<CustomerFeedbackModel> feedbacks = getCustomerFeedbackService().findByCustomerAndStatusNRAndPastdue(customer);

                return getCustomerFeedbackConverter().convertAll(feedbacks);
            } else {
                LOG.error(String.format("unable to get feedbacks with current user %s", user.getUid()));
            }

        } catch (final UnknownIdentifierException | AmbiguousIdentifierException ex){
            LOG.error(String.format("unable to find feedbacks"),ex);
        }

        return Collections.emptyList();
    }

    @Override
    public void saveFeedback(final String subject,final String message) {
        try {
            final UserModel user = getUserService().getCurrentUser();

            if (Objects.nonNull(user) && user instanceof CustomerModel){
                final CustomerModel customer = (CustomerModel) user;
                getCustomerFeedbackService().saveFeedback(customer,subject,message);
            } else {
                LOG.error(String.format("unable to set feedbacks with current user %s", user.getUid()));
            }

        } catch (final UnknownIdentifierException | AmbiguousIdentifierException ex){
            LOG.error(String.format("unable to set feedbacks"),ex);
        }

    }

    public CustomerFeedbackService getCustomerFeedbackService() {
        return customerFeedbackService;
    }

    public void setCustomerFeedbackService(CustomerFeedbackService customerFeedbackService) {
        this.customerFeedbackService = customerFeedbackService;
    }

    public Converter<CustomerFeedbackModel, CustomerFeedbackData> getCustomerFeedbackConverter() {
        return customerFeedbackConverter;
    }

    public void setCustomerFeedbackConverter(Converter<CustomerFeedbackModel, CustomerFeedbackData> customerFeedbackConverter) {
        this.customerFeedbackConverter = customerFeedbackConverter;
    }
}

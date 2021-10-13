package com.ardctraining.facades.customerfeedback;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import com.ardctraining.facades.customer.data.CustomerFeedbackData;

import java.util.List;

public interface ArdcTrainingCustomerFeedbackFacade extends CustomerFacade {

    List<CustomerFeedbackData> findFeedbacks();

    void saveFeedback(String subject, String message);
}

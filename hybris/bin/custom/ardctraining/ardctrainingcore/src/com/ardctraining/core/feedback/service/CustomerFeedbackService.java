package com.ardctraining.core.feedback.service;


import com.ardctraining.core.model.CustomerFeedbackModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

public interface CustomerFeedbackService {
    /**
     * find all feedbacks by customer and status non read and pastdue
     * @param customer
     * @return
     */
    List<CustomerFeedbackModel> findByCustomerAndStatusNRAndPastdue(CustomerModel customer);

    /**
     * Save a feedback from a customer
     * @param customer
     * @param subject
     * @param message
     */
    void saveFeedback(CustomerModel customer, String subject, String message);
}

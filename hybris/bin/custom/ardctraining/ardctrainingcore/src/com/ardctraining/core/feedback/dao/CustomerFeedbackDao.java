package com.ardctraining.core.feedback.dao;

import com.ardctraining.core.model.CustomerFeedbackModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

public interface CustomerFeedbackDao {

    /**
     * find all feedbacks by customer and status non read and pastdue
     * @param customer
     * @return
     */
    List<CustomerFeedbackModel> findByCustomerAndStatusNRAndPastdue(CustomerModel customer);
}

package com.ardctraining.facades.process.email.context;

import com.ardctraining.core.model.CustomerFeedbackEmailProcessModel;
import com.ardctraining.facades.constants.ArdctrainingFacadesConstants;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomerFeedbackEmailContext extends AbstractEmailContext<CustomerFeedbackEmailProcessModel> {

    private Integer size;
    private Set<Map<String,String>> feedbacks;

    private static final String SPLIT =  "\\|";
    private static final String WILDCART ="*";

    private ConfigurationService configurationService;

    private static final Logger LOG = Logger.getLogger(CustomerFeedbackEmailContext.class);

    @Override
    public void init(CustomerFeedbackEmailProcessModel businessProcessModel, EmailPageModel emailPageModel) {
        super.init(businessProcessModel, emailPageModel);

        LOG.info("Entering init method from customerFeedbackEmailContext");

        setSize(businessProcessModel.getCustomerFeedbacks().size());
        setFeedbacks(businessProcessModel.getCustomerFeedbacks());
    }

    public void setFeedbacks(final Set<String> feedbacks) {
        final Set<Map<String,String>> customerFeedbacks = new HashSet<>();
        feedbacks.forEach((String s) -> {
            final Map<String, String> feedback = new HashMap<>();
            final String[] parts = s.split(SPLIT);

            if (parts.length > 0){
                feedback.put("customer", StringUtils.isNotEmpty(parts[0]) ? parts[0] : WILDCART);
                feedback.put("read",parts[1]);
                feedback.put("subject",parts[2]);
                feedback.put("message",parts[3]);
                feedback.put("submittedDate",parts[4]);

                customerFeedbacks.add(feedback);
            }
        });

        this.feedbacks = customerFeedbacks;
    }

    @Override
    public String getToEmail() {
        return getConfigurationService().getConfiguration().getString(ArdctrainingFacadesConstants.PRODUCT_LABELS_CRONJOB_RECIPIENT_EMAIL);
    }

    @Override
    public String getToDisplayName() {
        return getConfigurationService().getConfiguration().getString(ArdctrainingFacadesConstants.PRODUCT_LABELS_CRONJOB_RECIPIENT_DISPLAYNAME);
    }

    @Override
    protected BaseSiteModel getSite(CustomerFeedbackEmailProcessModel businessProcessModel) {
        return businessProcessModel.getSite();
    }

    @Override
    protected CustomerModel getCustomer(CustomerFeedbackEmailProcessModel businessProcessModel) {
        return (CustomerModel) businessProcessModel.getUser();
    }

    @Override
    protected LanguageModel getEmailLanguage(CustomerFeedbackEmailProcessModel businessProcessModel) {
        return businessProcessModel.getLanguage();
    }

    @Override
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    @Override
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Set<Map<String, String>> getFeedbacks() {
        return feedbacks;
    }


}

package com.ardctraining.core.feedback.service.imp;

import com.ardctraining.core.feedback.dao.CustomerFeedbackDao;
import com.ardctraining.core.feedback.dao.imp.DefaultCustomerFeedbackDao;
import com.ardctraining.core.feedback.service.CustomerFeedbackService;
import com.ardctraining.core.model.CustomerFeedbackEmailProcessModel;
import com.ardctraining.core.model.CustomerFeedbackModel;
import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultCustomerFeedbackService implements CustomerFeedbackService {
    private DefaultCustomerFeedbackDao defaultCustomerFeedbackDao;
    private ModelService modelService;
    private TimeService timeService;
    private DefaultEmailService defaultEmailService;
    private BusinessProcessService businessProcessService;
    private BaseSiteService baseSiteService;
    private CommerceCommonI18NService commerceCommonI18NService;

    private static final String FIELD_SEPARATOR = "|";
    private static final String EMAIL_PROCESS = "customerFeedbackEmailProcess";
    private static final String DASH = "-";
    private static final String SITE = "electronics";

    @Override
    public List<CustomerFeedbackModel> findByCustomerAndStatusNRAndPastdue(final CustomerModel customer) {
        ServicesUtil.validateParameterNotNull(customer, "customer cannot be null");

        return getDefaultCustomerFeedbackDao().findByCustomerAndStatusNRAndPastdue(customer);
    }

    @Override
    public void saveFeedback(final CustomerModel customer,final String subject,final String message) {
        CustomerFeedbackModel customerToSave = new CustomerFeedbackModel();
        customerToSave.setCustomer(customer);
        customerToSave.setSubject(subject);
        customerToSave.setMessage(message);
        final Date now = getTimeService().getCurrentTime();
        customerToSave.setSubmittedDate(now);

        getModelService().save(customerToSave);
        /*
        String body = new StringBuilder()
                .append("hi ")
                .append(customerToSave.getCustomer().getName())
                .append(" your feedback is ")
                .append(customerToSave.getSubject())
                .append("|")
                .append(customerToSave.getMessage())
                .toString();

        getDefaultEmailService().send(
        getDefaultEmailService().createEmailMessage(Collections.singletonList(getDefaultEmailService().getOrCreateEmailAddressForEmail("koke.ssg@gmail.com","sergio")),
                null,null,
                getDefaultEmailService().getOrCreateEmailAddressForEmail("test.mail@mail.com","test"),
                null,"Feedback",body,null));*/
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss.S");

        final Set<String> feedback = getCustomerFeedbacks(Collections.singletonList(customerToSave));

        final CustomerFeedbackEmailProcessModel process = getBusinessProcessService().createProcess(
                new StringBuilder().append(EMAIL_PROCESS).append(DASH).append(dateFormat.format(now)).toString(),
                "customerFeedbackEmailProcess");
        process.setLanguage(getCommerceCommonI18NService().getCurrentLanguage());
        process.setCustomerFeedbacks(feedback);
        process.setSite(getBaseSiteService().getBaseSiteForUID(SITE));

        getModelService().save(process);

        getBusinessProcessService().startProcess(process);
    }

    public Set<String> getCustomerFeedbacks(final List<CustomerFeedbackModel> feedbacks){
        return feedbacks
                .stream()
                .map((CustomerFeedbackModel feedback) ->
                        new StringBuilder()
                                .append(feedback.getCustomer().getUid())
                                .append(FIELD_SEPARATOR)
                                .append(feedback.getRead())
                                .append(FIELD_SEPARATOR)
                                .append(feedback.getSubject())
                                .append(FIELD_SEPARATOR)
                                .append(feedback.getMessage())
                                .append(FIELD_SEPARATOR)
                                .append(feedback.getSubmittedDate())
                                .toString()
                )
                .collect(Collectors.toSet());
    }


    public DefaultCustomerFeedbackDao getDefaultCustomerFeedbackDao() {
        return defaultCustomerFeedbackDao;
    }

    public void setDefaultCustomerFeedbackDao(DefaultCustomerFeedbackDao defaultCustomerFeedbackDao) {
        this.defaultCustomerFeedbackDao = defaultCustomerFeedbackDao;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public TimeService getTimeService() {
        return timeService;
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    public DefaultEmailService getDefaultEmailService() {
        return defaultEmailService;
    }

    public void setDefaultEmailService(DefaultEmailService defaultEmailService) {
        this.defaultEmailService = defaultEmailService;
    }

    public BusinessProcessService getBusinessProcessService() {
        return businessProcessService;
    }

    public void setBusinessProcessService(BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    public CommerceCommonI18NService getCommerceCommonI18NService() {
        return commerceCommonI18NService;
    }

    public void setCommerceCommonI18NService(CommerceCommonI18NService commerceCommonI18NService) {
        this.commerceCommonI18NService = commerceCommonI18NService;
    }
}

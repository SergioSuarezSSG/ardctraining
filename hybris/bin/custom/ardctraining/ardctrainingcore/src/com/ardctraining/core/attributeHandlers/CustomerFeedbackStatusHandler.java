package com.ardctraining.core.attributeHandlers;

import com.ardctraining.core.enums.FeedbackStatusEnum;
import com.ardctraining.core.model.CustomerFeedbackModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomerFeedbackStatusHandler implements DynamicAttributeHandler<FeedbackStatusEnum, CustomerFeedbackModel> {
    private TimeService timeService;
    private ConfigurationService configurationService;



    @Override
    public FeedbackStatusEnum get(final CustomerFeedbackModel model) {
        // set the value based on difference of days
        final Date now = getTimeService().getCurrentTime();
        final Date submittedDate = model.getSubmittedDate();
        final long diff = now.getTime() - submittedDate.getTime();

        final TimeUnit time = TimeUnit.DAYS;
        final long difference = time.convert(diff,TimeUnit.MILLISECONDS);
        final long max_days = getConfigurationService().getConfiguration().getLong("feedback.status.duedate.calculation.days.threshold");

        if (model.getRead()){
            if(difference > max_days){
                return FeedbackStatusEnum.READ_PASTDUE;
            }else {
                return  FeedbackStatusEnum.READ;
            }
        }else {
            if(difference > max_days){
                return FeedbackStatusEnum.PASTDUE;
            }else {
                return  FeedbackStatusEnum.NOT_READ;
            }
        }
    }

    @Override
    public void set(final CustomerFeedbackModel model,final FeedbackStatusEnum feedbackStatusEnum) {
        model.setStatus(feedbackStatusEnum);
    }

    public TimeService getTimeService() {
        return timeService;
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}

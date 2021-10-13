package com.ardctraining.storefront.feedbackform.validator;

import com.ardctraining.storefront.feedbackform.ArdctrainingFeedbackForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("ardctrainingFeedbackValidator")
public class ArdctrainingFeedbackFormValidator implements Validator{
    @Override
    public boolean supports(Class<?> aClass) {
        return ArdctrainingFeedbackForm.class.equals(aClass);
    }

    public void validate(final Object object, final Errors errors) {

        final ArdctrainingFeedbackForm form = (ArdctrainingFeedbackForm) object;

        validateSubject(form.getSubject(), errors);
        validateMessage(form.getMessage(), errors);
    }

    private void validateSubject(final String subject, final Errors errors) {
        if (StringUtils.isEmpty(subject)) {
            errors.rejectValue("subject", "feedback.subject.invalid");
        }
    }

    private void validateMessage(final String message, final Errors errors) {
        if (StringUtils.isEmpty(message) || message.length() > 500) {
            errors.rejectValue("message", "feedback.message.invalid");
        }
    }
}

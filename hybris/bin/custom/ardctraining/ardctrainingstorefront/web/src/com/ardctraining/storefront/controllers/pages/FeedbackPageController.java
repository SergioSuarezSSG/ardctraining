package com.ardctraining.storefront.controllers.pages;


import com.ardctraining.facades.customerfeedback.ArdcTrainingCustomerFeedbackFacade;
import com.ardctraining.storefront.controllers.ControllerConstants;
import com.ardctraining.storefront.feedbackform.ArdctrainingFeedbackForm;
import com.ardctraining.storefront.feedbackform.validator.ArdctrainingFeedbackFormValidator;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import org.apache.commons.lang.BooleanUtils;
import org.drools.core.util.StringUtils;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/feedback")
public class FeedbackPageController extends AbstractPageController {
    @Resource(name = "customerFeedbackFacade")
    private ArdcTrainingCustomerFeedbackFacade customerFeedbackFacade;

    @Resource
    private ArdctrainingFeedbackFormValidator ardctrainingFeedbackFormValidator;

    public static final String FEEDBACK_PAGE_LABEL = "feedback";
    public static final String FEEDBACK_FORM_ATTR = "ardctrainingFeedbackForm";
    public static final String FEEDBACK_CUSTOMER_NR_FEEDBACKS = "feedbacks";



    @GetMapping
    public String feedbackView(final Model model) throws CMSItemNotFoundException{
       createData(model);

        return ControllerConstants.Views.Pages.Feedback.FeedbackPage;

    }

    @PostMapping
    public String submitFeedback(final Model model,
                                 final ArdctrainingFeedbackForm form,
                                 final BindingResult bindingResult) throws CMSItemNotFoundException {
        getArdctrainingFeedbackFormValidator().validate(form,bindingResult);
        if(BooleanUtils.isFalse(bindingResult.hasErrors())){
            final String subject;
            final String message;
            subject = form.getSubject();
            message = form.getMessage();
            getCustomerFeedbackFacade().saveFeedback(subject,message);
            model.addAttribute("newFeedback",true);
            createData(model);
            return ControllerConstants.Views.Pages.Feedback.FeedbackPage;
        }
        createData(model,form);
        model.addAttribute("faildFeedback",true);
        return ControllerConstants.Views.Pages.Feedback.FeedbackPage;
    }

    private void createData(final Model model) throws CMSItemNotFoundException {
        final ContentPageModel contentPageModel = getContentPageForLabelOrId(FEEDBACK_PAGE_LABEL);
        storeCmsPageInModel(model, contentPageModel);
        setUpMetaDataForContentPage(model, contentPageModel);

        model.addAttribute(FEEDBACK_FORM_ATTR, createEmptyForm());
        model.addAttribute(FEEDBACK_CUSTOMER_NR_FEEDBACKS, getCustomerFeedbackFacade().findFeedbacks());
    }

    private void createData(final Model model, final ArdctrainingFeedbackForm form) throws CMSItemNotFoundException {
        final ContentPageModel contentPageModel = getContentPageForLabelOrId(FEEDBACK_PAGE_LABEL);
        storeCmsPageInModel(model, contentPageModel);
        setUpMetaDataForContentPage(model, contentPageModel);

        model.addAttribute(FEEDBACK_FORM_ATTR, form);
        model.addAttribute(FEEDBACK_CUSTOMER_NR_FEEDBACKS, getCustomerFeedbackFacade().findFeedbacks());
    }

    private ArdctrainingFeedbackForm createEmptyForm(){
        final ArdctrainingFeedbackForm form = new ArdctrainingFeedbackForm();
        form.setMessage(StringUtils.EMPTY);
        form.setSubject(StringUtils.EMPTY);

        return form;
    }

    public ArdcTrainingCustomerFeedbackFacade getCustomerFeedbackFacade() {
        return customerFeedbackFacade;
    }

    public void setCustomerFeedbackFacade(ArdcTrainingCustomerFeedbackFacade customerFeedbackFacade) {
        this.customerFeedbackFacade = customerFeedbackFacade;
    }

    public ArdctrainingFeedbackFormValidator getArdctrainingFeedbackFormValidator() {
        return ardctrainingFeedbackFormValidator;
    }

    public void setArdctrainingFeedbackFormValidator(ArdctrainingFeedbackFormValidator ardctrainingFeedbackFormValidator) {
        this.ardctrainingFeedbackFormValidator = ardctrainingFeedbackFormValidator;
    }
}

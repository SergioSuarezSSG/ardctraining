package com.ardctraining.core.interceptor;

import com.ardctraining.core.enums.CustomLabelTypeEnum;
import com.ardctraining.core.model.CostumProductLabelModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.UserConstants;

import java.util.Objects;

public class CustomProductLabelPrepareInterceptor implements PrepareInterceptor<CostumProductLabelModel> {
    @Override
    public void onPrepare(final CostumProductLabelModel model,final InterceptorContext interceptorContext) throws InterceptorException {
        if (Objects.isNull(model.getLabelType())){
            model.setLabelType(CustomLabelTypeEnum.PRIMARY);
        }

        if (Objects.nonNull(model.getCustomer()) && UserConstants.ANONYMOUS_CUSTOMER_UID.equals(model.getCustomer().getUid())){
            throw new IllegalArgumentException("Unable to sabe custom label for anonymous customer, change it and try again");
        }
    }
}


package com.ardctraining.facades.populators;

import com.ardctraining.facades.product.data.CustomProductLabelData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArdcTrainingSearchResultVarianPopulator extends SearchResultVariantProductPopulator {

    private UserService userService;

    private static final String CUSTOM_LABEL_PROPERTY = "customLabel";
    private static final String PROPERTYVALUE_SEPARATOR = "\\|";
    private static final String ALL_WILDCARD = "*";

    private static final Logger LOG = Logger.getLogger(ArdcTrainingSearchResultVarianPopulator.class);

    @Override
    public void populate(final SearchResultValueData source,final ProductData target) {
        super.populate(source, target);

        populateCustomLabels(source, target);
    }

    private void populateCustomLabels(final SearchResultValueData source,final ProductData target){
        final List<String> labels = getValue(source, CUSTOM_LABEL_PROPERTY);

        if(CollectionUtils.isNotEmpty(labels)){
            final UserModel user = getUserService().getCurrentUser();

            target.setLabels(convertLabels(labels, user));
        }else{
            target.setLabels(Collections.emptyList());
        }
    }

    private List<CustomProductLabelData> convertLabels(final List<String> labels, final UserModel user){
        final List<CustomProductLabelData> labelsList = new ArrayList<>();

        labels.forEach((String label) -> {
            final String[] parts = label.split(PROPERTYVALUE_SEPARATOR);

            if(parts.length == 3){
                if(parts[0].trim().equals(user.getUid()) || parts[0].trim().equals(ALL_WILDCARD)){
                    final CustomProductLabelData thelabel = new CustomProductLabelData();
                    thelabel.setLabel(parts[1]);
                    thelabel.setType(parts[2]);

                    labelsList.add(thelabel);
                } else {
                    LOG.error(String.format("unable to convert custom label indexed property [%s]", label));
                }
            }
        });

        return labelsList;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

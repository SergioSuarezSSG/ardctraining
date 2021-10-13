package com.ardctraining.core.feedback.dao.imp;

import com.ardctraining.core.feedback.dao.CustomerFeedbackDao;
import com.ardctraining.core.model.CustomerFeedbackModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultCustomerFeedbackDao implements CustomerFeedbackDao {
    private FlexibleSearchService flexibleSearchService;

    private static final Logger LOG = Logger.getLogger(DefaultCustomerFeedbackDao.class);
    private static final String SELECT =
            "SELECT {" + ItemModel.PK + "} "+
                    "FROM   {" + CustomerFeedbackModel._TYPECODE + "} ";
    private static final String FIND_LABELS_BY_COSTUMER_AND_STATUS_NR_AND_PASTDUE_QUERY =
            SELECT+
                    "WHERE  {" + CustomerFeedbackModel.CUSTOMER + "} = ?customer AND "+
                            "{"+ CustomerFeedbackModel.READ + "} = false";

    @Override
    public List<CustomerFeedbackModel> findByCustomerAndStatusNRAndPastdue(final CustomerModel customer) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_LABELS_BY_COSTUMER_AND_STATUS_NR_AND_PASTDUE_QUERY);
        query.addQueryParameter("customer", customer);

        return findResult(query);
    }

    public List<CustomerFeedbackModel> findResult(final FlexibleSearchQuery query){
        final SearchResult<CustomerFeedbackModel> result = getFlexibleSearchService().search(query);

        if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult())){
            return result.getResult();
        }
        LOG.warn("unable to find results for query");

        return Collections.emptyList();
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}

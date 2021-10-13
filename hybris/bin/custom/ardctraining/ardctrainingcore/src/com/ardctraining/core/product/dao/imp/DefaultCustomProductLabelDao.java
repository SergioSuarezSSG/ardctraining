package com.ardctraining.core.product.dao.imp;

import com.ardctraining.core.model.CostumProductLabelModel;
import com.ardctraining.core.product.dao.CustomProductLabelDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;

public class DefaultCustomProductLabelDao implements CustomProductLabelDao {
    private FlexibleSearchService flexibleSearchService;

    private static final Logger LOG = Logger.getLogger(DefaultCustomProductLabelDao.class);
    private static final String SELECT =
            "SELECT {" + ItemModel.PK + "} "+
            "FROM   {" + CostumProductLabelModel._TYPECODE + "} ";
    private static final String FIND_LABELS_BY_COSTUMER_AND_PRODUCT_QUERY =
            SELECT+
            "WHERE  {" + CostumProductLabelModel.CUSTOMER + "} = ?customer AND " +
            "       {" + CostumProductLabelModel.PRODUCT + "} = ?product";
    private static final String FIND_EXPIRED_LABELS_QUERY =
            SELECT+
            "WHERE  {" + CostumProductLabelModel.VALIDITYDATE + "} < ?now";
    private static final String FIND_LABELS_BY_COSTUMER_AND_PRODUCT_AND_NULLCUSTOMER_QUERY =
            SELECT+
            "WHERE  ({" + CostumProductLabelModel.CUSTOMER + "} = ?customer OR {" + CostumProductLabelModel.CUSTOMER + "} is null) AND " +
            "       {" + CostumProductLabelModel.PRODUCT + "} = ?product";
    private static final String FIND_LABELS_BY_PRODUCT_QUERY =
            SELECT+
            "WHERE  {" + CostumProductLabelModel.PRODUCT + "} = ?product";

    @Override
    public List<CostumProductLabelModel> findByCustomerAndProduct(CustomerModel customer, ProductModel product) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_LABELS_BY_COSTUMER_AND_PRODUCT_QUERY);
        query.addQueryParameter("customer", customer);
        query.addQueryParameter("product", product);

        return findResult(query);
    }

    @Override
    public List<CostumProductLabelModel> findExpired(final Date now) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_EXPIRED_LABELS_QUERY);
        query.addQueryParameter("now", now);

        return findResult(query);
    }

    @Override
    public List<CostumProductLabelModel> findByCustomerAndProductAndNullCustomer(final CustomerModel customer,final ProductModel product) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_LABELS_BY_COSTUMER_AND_PRODUCT_AND_NULLCUSTOMER_QUERY);
        query.addQueryParameter("customer", customer);
        query.addQueryParameter("product", product);

        return findResult(query);
    }

    @Override
    public List<CostumProductLabelModel> findByProduct(final ProductModel product) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_LABELS_BY_PRODUCT_QUERY);
        query.addQueryParameter("product", product);

        return findResult(query);
    }

    public List<CostumProductLabelModel> findResult(final FlexibleSearchQuery query){
        final SearchResult<CostumProductLabelModel> result = getFlexibleSearchService().search(query);

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

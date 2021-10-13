package com.ardctraining.core.job;

import com.ardctraining.core.model.CostumProductLabelModel;
import com.ardctraining.core.model.CustomProductLabelCleanupCronjobModel;
import com.ardctraining.core.model.CustomProductLabelCleanupEmailProcessModel;
import com.ardctraining.core.product.service.CustomProductLabelService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomProductLabelCleanupJobPerformable extends AbstractJobPerformable<CustomProductLabelCleanupCronjobModel>{

    private CustomProductLabelService customProductLabelService;
    private BusinessProcessService businessProcessService;
    private TimeService timeService;
    private BaseSiteService baseSiteService;

    private static final String EMAIL_PROCESS = "customProductLabelCleanupEmailProcess";
    private static final String FIELD_SEPARATOR = "|";
    private static final String DASH = "-";
    private static final String SITE = "electronics";
    private static final Logger LOG = Logger.getLogger(CustomProductLabelCleanupJobPerformable.class);

    @Override
    public PerformResult perform(final CustomProductLabelCleanupCronjobModel customProductLabelCleanupCronjobModel) {
        LOG.debug("entering CustomProductLabelCleanupJobPerformable::perform");

        final List<CostumProductLabelModel> labelsToDelete = getCustomProductLabelService().findExpired();

        LOG.info(String.format("Labels to delete %s", labelsToDelete.size()));

        try {
            final Set<String> labels = getCustomLabels(labelsToDelete);


            modelService.removeAll(labelsToDelete);

            final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss.S");
            final Date now = getTimeService().getCurrentTime();
            final CustomProductLabelCleanupEmailProcessModel process = getBusinessProcessService().createProcess(
                    new StringBuilder().append(EMAIL_PROCESS).append(DASH).append(dateFormat.format(now)).toString(),
                    "customLabelCleanupEmailProcess");
            process.setLanguage(customProductLabelCleanupCronjobModel.getSessionLanguage());
            process.setCustomLabels(labels);
            process.setSite(getBaseSiteService().getBaseSiteForUID(SITE));

            modelService.save(process);

            getBusinessProcessService().startProcess(process);
        } catch (final ModelRemovalException ex){
            LOG.error("unable to delete custom labels", ex);

            return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
        }

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private Set<String> getCustomLabels(final List<CostumProductLabelModel> labels){
        return labels
                .stream()
                .map((CostumProductLabelModel label) ->
                    new StringBuilder()
                            .append(Objects.isNull(label.getCustomer()) ? StringUtils.EMPTY : label.getCustomer().getUid())
                            .append(FIELD_SEPARATOR)
                            .append(label.getProduct().getCode())
                            .append(FIELD_SEPARATOR)
                            .append(label.getLabel())
                            .append(FIELD_SEPARATOR)
                            .append(label.getLabelType().getCode())
                            .toString()
                )
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAbortable() {
        return Boolean.TRUE;
    }

    public CustomProductLabelService getCustomProductLabelService() {
        return customProductLabelService;
    }

    public void setCustomProductLabelService(CustomProductLabelService customProductLabelService) {
        this.customProductLabelService = customProductLabelService;
    }

    public BusinessProcessService getBusinessProcessService() {
        return businessProcessService;
    }

    public void setBusinessProcessService(BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }

    public TimeService getTimeService() {
        return timeService;
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }
}

package com.clarityvisionsolutions.osgi.multiples;

import com.clarityvisionsolutions.insurance.benefits.tracker.service.BenefitUsageLocalService;
import com.clarityvisionsolutions.insurance.benefits.tracker.service.BenefitUsageService;

import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates how OSGi Declarative Services manages a component with
 * multiple @Reference dependencies.
 *
 * The component will NOT activate until all four references are satisfied.
 * If any one of them goes away after activation, the component is
 * deactivated automatically. Watch the log to see how SCR responds to
 * service availability changes.
 */
@Component(
    immediate = true,
    service = MultipleReferenceInjectionDemo.class
)
public class MultipleReferenceInjectionDemo {

    private static final Logger _log =
        LoggerFactory.getLogger(MultipleReferenceInjectionDemo.class);

    @Activate
    protected void activate() {
        _log.info("=== MultipleReferenceInjectionDemo ACTIVATED ===");
        _log.info("All four service references were bound successfully:");
        _log.info("  - UserLocalService:          {}", _userLocalService);
        _log.info("  - BenefitUsageLocalService:  {}", _benefitUsageLocalService);
        _log.info("  - BenefitUsageService:       {}", _benefitUsageService);
        _log.info("  - ConfigurationProvider:     {}", _configurationProvider);
        _log.info("Component is now ACTIVE and ready to use.");
    }

    @Deactivate
    protected void deactivate() {
        _log.warn("=== MultipleReferenceInjectionDemo DEACTIVATING ===");
        _log.warn(
            "One or more required services became unavailable, " +
            "or the bundle is being stopped. SCR is tearing down " +
            "this component. Check 'scr:info' to see which reference " +
            "is now UNSATISFIED.");
    }

    @Reference
    private UserLocalService _userLocalService;

    @Reference
    private BenefitUsageLocalService _benefitUsageLocalService;

    @Reference(unbind = "unbindBenefitUsageService")
    private BenefitUsageService _benefitUsageService;
    
    protected void unbindBenefitUsageService(BenefitUsageService service) {
        _log.warn("UNBINDING BenefitUsageService — component will deactivate");
    }

    @Reference
    private ConfigurationProvider _configurationProvider;

}
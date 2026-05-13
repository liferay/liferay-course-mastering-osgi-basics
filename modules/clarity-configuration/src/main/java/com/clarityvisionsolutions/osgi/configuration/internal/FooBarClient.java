package com.clarityvisionsolutions.osgi.configuration.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.clarityvisionsolutions.osgi.configuration.FooBarConfiguration;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumes FooBarConfiguration two different ways:
 *
 *   1. activate(properties) — the configuration is
 *      pushed to us as properties and coerced into a configuration object 
 *      by ConfigurableUtil when the component activates or is reconfigured.
 *      This is the "I am the configuration's owner" pattern.
 *
 *   2. ConfigurationProvider — we pull the configuration on demand at
 *      runtime. This is the "I want to look up someone else's config"
 *      pattern, and it's essential for COMPANY-scoped configs where we
 *      need to ask "what's the value for *this* company right now?"
 *
 * Both paths should report the same values when the system-default
 * config is in play; the difference becomes visible when you start
 * setting per-company overrides via Instance Settings.
 */
@Component(
    configurationPid = "com.clarityvisionsolutions.osgi.configuration.FooBarConfiguration",
    immediate = true,
    service = FooBarClient.class
)
public class FooBarClient {

    @Activate
    @Modified
	protected void activate(Map<String, Object> properties) {
		_configuration = ConfigurableUtil.createConfigurable(
			FooBarConfiguration.class, properties);

        _log.info("=== FooBarClient ACTIVATED / RECONFIGURED ===");
        _logConfiguration(_configuration);

        if (_configuration.enabled()) {
            try {
                URL url = new URL(_configuration.serviceUrl());

                _log.info(
                    "FooBar integration ready against host '{}'",
                    url.getHost());
            }
            catch (MalformedURLException malformedURLException) {
                _log.error(
                    "Configured serviceUrl is not a valid URL: '{}'",
                    _configuration.serviceUrl(), malformedURLException);
            }
        }
        else {
            _log.warn(
                "FooBar integration is DISABLED. " +
                "Toggle 'enabled' in System Settings to activate.");
        }

        // Also demonstrate the pull-based lookup via ConfigurationProvider.
        _lookupConfiguration();
    }

    @Deactivate
    protected void deactivate() {
        _log.info("=== FooBarClient DEACTIVATED ===");
        _configuration = null;
    }

    private void _logConfiguration(FooBarConfiguration configuration) {
        _log.info("  enabled:       {}", configuration.enabled());
        _log.info("  serviceUrl:    {}", configuration.serviceUrl());
        _log.info("  activationKey: {}", _maskKey(configuration.activationKey()));
    }

    private void _lookupConfiguration() {
        _log.info("--- Configuration pulled via ConfigurationProvider ---");

        try {
            FooBarConfiguration configuration =
                _configurationProvider.getSystemConfiguration(
                    FooBarConfiguration.class);

            if (configuration == null) {
                _log.warn(
                    "ConfigurationProvider returned null. " +
                    "No system configuration exists yet for {}.",
                    FooBarConfiguration.class.getName());

                return;
            }

            _logConfiguration(configuration);
        }
        catch (ConfigurationException configurationException) {
            _log.error(
                "Failed to look up FooBarConfiguration via " +
                "ConfigurationProvider",
                configurationException);
        }
    }

    private String _maskKey(String key) {
        if ((key == null) || key.isEmpty()) {
            return "(not set)";
        }

        if (key.length() <= 4) {
            return "****";
        }

        return "****" + key.substring(key.length() - 4);
    }

    private static final Logger _log = LoggerFactory.getLogger(FooBarClient.class);

    @Reference
    private ConfigurationProvider _configurationProvider;

    private volatile FooBarConfiguration _configuration;

    @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
    protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
        _log.info("=== ModuleServiceLifecycle SET ===");
    }
}


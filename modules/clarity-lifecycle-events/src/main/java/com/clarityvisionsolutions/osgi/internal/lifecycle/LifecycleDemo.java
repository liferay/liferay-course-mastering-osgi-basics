package com.clarityvisionsolutions.osgi.internal.lifecycle;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.module.ModuleServiceLifecycle;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

// @Component(
//     immediate = true,
//     service = LifecycleDemo.class,
//     property = {
//         "demo.message=Hello from OSGi",
//         "demo.iteration=1"
//     }
// )
public class LifecycleDemo {

    private static final Logger _log = LoggerFactory.getLogger(LifecycleDemo.class);

    @Activate
    protected void activate(ComponentContext context, Map<String, Object> properties) {
        _log.info("=== LifecycleDemo ACTIVATED ===");
        _log.info("Bundle: {}", context.getBundleContext().getBundle().getSymbolicName());
        _log.info("Properties: {}", properties);
    }

    @Modified
    protected void modified(Map<String, Object> properties) {
        _log.info("=== LifecycleDemo MODIFIED ===");
        _log.info("New properties: {}", properties);
    }

    @Deactivate
    protected void deactivate() {
        _log.info("=== LifecycleDemo DEACTIVATED ===");
    }

    @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
    protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
        _log.info("=== ModuleServiceLifecycle SET ===");
    }
}
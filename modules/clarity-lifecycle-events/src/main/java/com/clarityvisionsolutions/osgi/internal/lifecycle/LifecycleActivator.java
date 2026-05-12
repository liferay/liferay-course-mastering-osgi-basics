package com.clarityvisionsolutions.osgi.internal.lifecycle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifecycleActivator implements BundleActivator {

    private static final Logger _log = LoggerFactory.getLogger(LifecycleActivator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        _log.info(">>> BUNDLE START: {} (id={}, state={})",
            context.getBundle().getSymbolicName(),
            context.getBundle().getBundleId(),
            context.getBundle().getState());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        _log.info("<<< BUNDLE STOP: {}", context.getBundle().getSymbolicName());
    }
}
package com.clarityvisionsolutions.osgi.service.tracker;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates ServiceTrackerMap — Liferay's higher-level wrapper over
 * the standard OSGi ServiceTracker. Instead of a flat list of services,
 * it gives you a live Map keyed by a service property of your choice.
 *
 * This component tracks every registered MVCActionCommand in the
 * runtime and indexes them by their "mvc.command.name" property.
 * The map updates automatically as bundles come and go.
 */
@Component(
    immediate = true,
    service = ServiceTrackerMapDemo.class
)
public class ServiceTrackerMapDemo {

    private static final Logger _log =
        LoggerFactory.getLogger(ServiceTrackerMapDemo.class);

    @Activate
    protected void activate(BundleContext bundleContext) {
        _log.info("=== ServiceTrackerMapDemo ACTIVATING ===");

        // Open a ServiceTrackerMap that:
        //   - tracks every service of type MVCActionCommand
        //   - groups them by the "mvc.command.name" service property
        //   - returns a List<MVCActionCommand> per key, because multiple
        //     components may register under the same command name
        //     (e.g. the original handler + an override)
        _serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
            bundleContext, MVCActionCommand.class, "mvc.command.name");

        _logTrackedCommands();

        _log.info("=== ServiceTrackerMapDemo ACTIVATED ===");
    }

    @Deactivate
    protected void deactivate() {
        _log.info("=== ServiceTrackerMapDemo DEACTIVATING ===");

        if (_serviceTrackerMap != null) {
            _serviceTrackerMap.close();
            _serviceTrackerMap = null;
            _log.info("ServiceTrackerMap closed; no longer tracking services.");
        }

        _log.info("=== ServiceTrackerMapDemo DEACTIVATED ===");
    }

    private void _logTrackedCommands() {
        Map<String, List<MVCActionCommand>> snapshot = new TreeMap<>();

        for (String commandName : _serviceTrackerMap.keySet()) {
            snapshot.put(commandName, _serviceTrackerMap.getService(commandName));
        }

        _log.info(
            "Tracking {} distinct mvc.command.name keys:", snapshot.size());

        for (Map.Entry<String, List<MVCActionCommand>> entry :
                snapshot.entrySet()) {

            List<MVCActionCommand> commands = entry.getValue();

            _log.info(
                "  [{}] -> {} implementation(s)",
                entry.getKey(), commands.size());

            for (MVCActionCommand command : commands) {
                _log.info("       {}", command.getClass().getName());
            }
        }
    }

    private ServiceTrackerMap<String, List<MVCActionCommand>> _serviceTrackerMap;

}

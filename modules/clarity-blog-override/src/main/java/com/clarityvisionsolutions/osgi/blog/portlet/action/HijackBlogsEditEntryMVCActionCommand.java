package com.clarityvisionsolutions.osgi.blog.portlet.action;

import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import jakarta.portlet.ActionRequest;
import jakarta.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise: Override Liferay's blogs edit-entry action handler using
 * service ranking. When deployed, clicking "Publish" on a blog entry
 * will log a message instead of saving the entry.
 *
 * To restore normal behavior, undeploy this bundle (or lower the ranking
 * below 0) and Liferay's original handler will take over again.
 */
@Component(
    property = {
        "jakarta.portlet.name=" + BlogsPortletKeys.BLOGS,
        "jakarta.portlet.name=" + BlogsPortletKeys.BLOGS_ADMIN,
        "jakarta.portlet.name=" + BlogsPortletKeys.BLOGS_AGGREGATOR,
        "mvc.command.name=/blogs/edit_entry",
        "service.ranking:Integer=100"
    },
    service = MVCActionCommand.class
)
public class HijackBlogsEditEntryMVCActionCommand extends BaseMVCActionCommand {

    private static final Logger _log =
        LoggerFactory.getLogger(HijackBlogsEditEntryMVCActionCommand.class);

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        String cmd = ParamUtil.getString(actionRequest, "cmd");
        String title = ParamUtil.getString(actionRequest, "title");

        _log.info("=== HIJACKED /blogs/edit_entry ===");
        _log.info("cmd parameter: '{}'", cmd);
        _log.info("title parameter: '{}'", title);
        _log.info("Original handler was NOT invoked. Entry not saved.");
    }
}
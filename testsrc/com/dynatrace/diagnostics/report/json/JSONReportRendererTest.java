/***************************************************
 * dynaTrace Diagnostics (c) dynaTrace software GmbH
 *
 * @file: JSONReportRendererTest.java
 * @date: 22.11.2010
 * @author: dominik.stadler
 */
package com.dynatrace.diagnostics.report.json;

import com.dynatrace.diagnostics.sdk.dashboard.*;
import com.dynatrace.diagnostics.sdk.dashboard.events.DashboardEventPostInterface;
import com.dynatrace.diagnostics.sdk.dashboard.events.DashboardRequestEvent;
import com.dynatrace.diagnostics.sdk.dashboard.events.PortletRequestEvent;
import com.dynatrace.diagnostics.sdk.dashboard.layout.PortletContainerInterface;
import com.dynatrace.diagnostics.sdk.dashboard.layout.PortletFolder;
import com.dynatrace.diagnostics.sdk.dashboard.reporting.ReportType;
import com.dynatrace.diagnostics.sdk.dashboard.requests.PortletRequestInterface;
import com.dynatrace.diagnostics.sdk.events.RefreshEvent;
import com.dynatrace.diagnostics.sdk.memento.MementoInterface;
import com.dynatrace.diagnostics.sdk.resources.images.ImageReference;
import com.dynatrace.diagnostics.sdk.security.Permission;
import com.dynatrace.diagnostics.sdk.sessions.SessionReference;
import com.dynatrace.diagnostics.sdk.ui.components.JobDescriptor;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertTrue;


/**
 *
 * @author dominik.stadler
 */
public class JSONReportRendererTest {

    @Test
    public void testReportDashboard() throws Exception {
        JSONFromXMLReportRenderer renderer = new JSONFromXMLReportRenderer();

        DashboardConfig config = new DashboardConfig();
        File file = File.createTempFile("test", ".json");
        renderer.reportDashboard(new LocalDashboard(), config, "user", file,
                ReportType.registerType("JSON"), Collections.<String>emptySet());

        assertTrue(file.exists());
        assertTrue(file.delete());
    }

    @Test
    public void testReportDashboardCompatible() throws Exception {
        JSONFromXMLReportRenderer renderer = new JSONFromXMLReportRenderer();

        DashboardConfig config = new DashboardConfig();
        File file = File.createTempFile("test", ".json");
        renderer.reportDashboard(new LocalDashboard(), config, "user1", file,
                ReportType.registerType("JSON"));
        renderer.reportDashboard(new LocalDashboard(), config, "user2", file,
                ReportType.registerType("JSON"));

        assertTrue(file.exists());
        assertTrue(file.delete());
    }

    @Test
    public void testJSONRendererReportDashboardCompatible() throws Exception {
        JSONReportRenderer renderer = new JSONReportRenderer();

        renderer.setParameters(Collections.singletonMap("mode", "badgerfish"));

        DashboardConfig config = new DashboardConfig();
        File file = File.createTempFile("test", ".json");
        renderer.reportDashboard(new LocalDashboard(), config, "user1", file,
                ReportType.registerType("JSON"));
        renderer.reportDashboard(new LocalDashboard(), config, "user2", file,
                ReportType.registerType("JSON"));

        assertTrue(file.exists());
        assertTrue(file.delete());
    }

    @Test
    public void testCreateJSON() {
        XMLSerializer serializer = new XMLSerializer();
        JSON json = serializer.read("<xml1><tag1 attribute1=\"value1\"></tag1><tag1 attribute1=\"value1\"></tag1><tag1 attribute1=\"value1\"></tag1><tag1 attribute1=\"value1\"></tag1></xml1>");
        System.out.println( json );
    }

    private class LocalDashboard implements DashboardInterface {

        @Override
        public void postEvent(PortletRequestEvent dashboardEvent) {


        }

        @Override
        public void postEvents(List<PortletRequestEvent> dashboardEvents) {


        }

        @Override
        public boolean hasPermission(AbstractSourceReference sourceReference, Permission[] permission) {

            return false;
        }

        @Override
        public void cancelAllRequests(PortletConfig<?> portletConfig) {


        }

        @Override
        public void cancelRequest(PortletConfig<?> portletConfig, PortletRequestInterface portletRequest) {


        }

        @Override
        public Object getAdapter(@SuppressWarnings("rawtypes") Class arg0) {

            return null;
        }

        @Override
        public UUID getId() {

            return null;
        }

        @Override
        public String getName() {
            return "Title";
        }

        @Override
        public String getDescription() {
            return "description";
        }

        @Override
        public ImageReference getIcon() {

            return null;
        }

        @Override
        public boolean hasAnySystemPermission(AbstractSourceReference sourceReference, SessionReference sessionReference) {

            return false;
        }

        @Override
        public JobDescriptor[] getJobDescriptorForPortlet(PortletConfig<?> portletConfig) {

            return null;
        }

        @Override
        public boolean hasPermission(Permission... permission) {

            return false;
        }

        @Override
        public boolean hasSystemPermission(AbstractSourceReference sourceReference, SessionReference sessionReference,
                Permission[] permission) {

            return false;
        }

        @Override
        public JobDescriptor removeJobDescriptorForPortlet(PortletConfig<?> portletConfig, PortletRequestInterface portletRequest) {

            return null;
        }

        @Override
        public JobDescriptor[] removeAllJobDescriptorsForPortlet(PortletConfig<?> portletConfig) {

            return null;
        }

        @Override
        public void setName(String name) {


        }

        @Override
        public void setDescription(String newDescription) {


        }

        @Override
        public void setIcon(ImageReference imageRef) {
        }

        @Override
        public DashboardConfig getConfig() {
            return null;
        }

        @Override
        public PortletInterface[] getPortlets() {
            return new PortletInterface[] {};
        }

        @Override
        public PortletInterface[] getPortlets(PortletTypeInterface portletType) {
            return null;
        }

        @Override
        public PortletInterface getPortletById(String id) {
            return null;
        }

        @Override
        public void setDashboardEventDelegator(DashboardEventPostInterface<DashboardRequestEvent> eventDelegator) {
        }

        @Override
        public void refreshData(RefreshEvent event) {


        }

        @Override
        public void refreshData(RefreshEvent event, PortletInterface... portlets) {


        }

        @Override
        public void refreshData(PortletInterface... portlets) {


        }

        @Override
        public PortletInterface addPortlet(PortletTypeInterface type, PortletFolder folder,
                AbstractSourceReference sourceReference, SessionReference sessionReference) throws PortletInitializationException {

            return null;
        }

        @Override
        public PortletInterface addPortlet(PortletConfig<?> config, PortletFolder folder, int position)
                throws PortletInitializationException {

            return null;
        }

        @Override
        public PortletInterface addPortlet(PortletTypeInterface type, PortletFolder folder,
                AbstractSourceReference sourceReference, SessionReference sessionReference, Map<String, ?> parametersMap)
                throws PortletInitializationException {

            return null;
        }

        @Override
        public PortletInterface addPortlet(PortletConfig<?> config, PortletContainerInterface container,
                Map<String, ?> parametersMap) throws PortletInitializationException {

            return null;
        }

        @Override
        public PortletInterface removePortlet(PortletInterface portlet) {

            return null;
        }

        @Override
        public DashboardLocation getLocation() {

            return null;
        }

        @Override
        public void setLocation(DashboardLocation location) {


        }

        @Override
        public boolean matchesNameAndLocation(String name, DashboardLocation location) {

            return false;
        }

        @Override
        public boolean requiresRefreshDataOnActivation() {

            return false;
        }

        @Override
        public void markRefreshedDataOnActivation() {


        }

        @Override
        public void updateLatestStoredState() {


        }

        @Override
        public MementoInterface getLatestStoredState() {

            return null;
        }

        @Override
        public String getDisplayName() {
            return "title";
        }

        @Override
        public boolean hasDisplayName() {
            return true;
        }

        @Override
        public void setDisplayName(String displayName) {
        }
    }
}

/***************************************************
 * dynaTrace Diagnostics (c) dynaTrace software GmbH
 *
 * @file: JSONReportRenderer.java
 * @date: 22.11.2010
 * @author: dominik.stadler
 */
package com.dynatrace.diagnostics.report.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.io.FileUtils;

import com.dynatrace.diagnostics.sdk.dashboard.DashboardConfig;
import com.dynatrace.diagnostics.sdk.dashboard.DashboardInterface;
import com.dynatrace.diagnostics.sdk.dashboard.reporting.ReportType;
import com.dynatrace.diagnostics.sdk.dashboard.reporting.XMLReportResult;
import com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer;
import com.dynatrace.diagnostics.server.shared.reporting.exception.ReportException;
import com.dynatrace.diagnostics.server.shared.reporting.exception.ReportRenderException;


/**
 * A sample JSON Report Renderer.
 *
 * This implementation first executes the XML Reporting to produce the XML content and
 * then transforms this XML file into JSON by using code from http://json-lib.sourceforge.net/
 *
 * Note: this keeps the complete XML content in memory and thus will cause problems
 * 		with big reports.
 *
 * Look at JSONReportRenderer for an implementation that avoids the
 * memory overhead.
 *
 * @author dominik.stadler
 */
public class JSONFromXMLReportRenderer implements ReportRenderer {
	protected Map<String, String> params;

	/**
	 * For backwards-compatibility to < 6.5
	 */
	public void reportDashboard(DashboardInterface dashboard, DashboardConfig dashboardConfig, String userID, File outputFile,
								ReportType reportType) throws ReportException {
		reportDashboard(dashboard, dashboardConfig, userID, outputFile, reportType, Collections.<String>emptySet());
	}

	@Override
	public void reportDashboard(DashboardInterface dashboard, DashboardConfig dashboardConfig, String userID, File outputFile,
								ReportType reportType, Collection<String> collection) throws ReportException {
		try {
			// first use the standard XML Reporter to create an XML file
			String xml = reportToXMLFile(dashboard, dashboardConfig, userID, collection);

			// use json-lib from http://json-lib.sourceforge.net/ to conver the XML
			XMLSerializer serializer = new XMLSerializer();
			JSON json = serializer.read(xml);

			if(params != null && "true".equalsIgnoreCase(params.get("prettyprint"))) { //$NON-NLS-1$ //$NON-NLS-2$
				FileUtils.writeStringToFile(outputFile, json.toString(4));
			} else {
				FileWriter writer = new FileWriter(outputFile);
				try {
					json.write(writer);
				} finally {
					writer.close();
				}
			}
		} catch (IOException e) {
			throw new ReportRenderException(e);
		}
	}

	/**
	 *
	 * @param dashboard
	 * @param dashboardConfig
	 * @param userID
	 * @param collection
	 * @throws ReportRenderException
	 * @author dominik.stadler
	 * @return
	 */
	private String reportToXMLFile(DashboardInterface dashboard, DashboardConfig dashboardConfig, String userID, Collection<String> collection)
			throws ReportRenderException {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("user", userID); //$NON-NLS-1$
		XMLReportResult result = new XMLReportResult(dashboard, headers, dashboardConfig);
		try {
			ByteArrayOutputStream strmOut = new ByteArrayOutputStream();
			try {
				result.createXMLReport(strmOut, null, collection);
			} finally {
				strmOut.close();
			}

			return new String(strmOut.toByteArray());
		} catch (IOException | XMLStreamException e) {
			throw new ReportRenderException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer#setDefaultImages(byte[], byte[])
	 */
	@Override
	public void setDefaultImages(byte[] headerImage, byte[] footerImage) throws ReportException {
		// nothing to do here...
	}

	/* (non-Javadoc)
	 * @see com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer#setTemplate(java.lang.String)
	 */
	@Override
	public void setTemplate(String template) {
		throw new IllegalArgumentException("Should not receive a template in the JSON Reporter"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer#setParameters(java.util.Map)
	 */
	@Override
	public void setParameters(Map<String, String> params) {
		this.params = params;
	}

	/* (non-Javadoc)
	 * @see com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer#cleanup()
	 */
	@Override
	public void cleanup() {
		// nothing to do here for now...
	}
}

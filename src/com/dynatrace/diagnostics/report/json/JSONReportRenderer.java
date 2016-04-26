/***************************************************
 * dynaTrace Diagnostics (c) dynaTrace software GmbH
 *
 * @file: JSONXMLReportRenderer.java
 * @date: 22.11.2010
 * @author: dominik.stadler
 */
package com.dynatrace.diagnostics.report.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.dynatrace.diagnostics.sdk.dashboard.DashboardConfig;
import com.dynatrace.diagnostics.sdk.dashboard.DashboardInterface;
import com.dynatrace.diagnostics.sdk.dashboard.reporting.DashboardXMLReportPartFactory;
import com.dynatrace.diagnostics.sdk.dashboard.reporting.ReportType;
import com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer;
import com.dynatrace.diagnostics.server.shared.reporting.exception.ReportException;
import com.dynatrace.diagnostics.server.shared.reporting.exception.ReportRenderException;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.json.impl.writer.JsonXmlStreamWriter;

/**
 * A sample JSON Report Renderer.
 *
 * This implementation encapsulates the XMLStreamWriter with a special JsonXmlStreamWriter from the
 * Sun Jersey project to redirect the XML-streaming calls directly into JSON format without
 * any intermediate XML format being created.
 *
 * @author dominik.stadler
 */
public class JSONReportRenderer implements ReportRenderer {
	private static final String PARAM_MODE = "mode";

	protected Map<String, String> params;

	/* (non-Javadoc)
	 * @see com.dynatrace.diagnostics.server.shared.reporting.ReportRenderer#reportDashboard(com.dynatrace.diagnostics.sdk.dashboard.DashboardInterface, com.dynatrace.diagnostics.sdk.dashboard.DashboardConfig, java.lang.String, java.io.File, com.dynatrace.diagnostics.sdk.dashboard.reporting.ReportType)
	 */
	@Override
	public void reportDashboard(DashboardInterface dashboard, DashboardConfig dashboardConfig, String userID, File outputFile,
			ReportType reportType) throws ReportException {
		try {
			XMLStreamWriter writer;
			if (params != null && "badgerfish".equalsIgnoreCase(params.get(PARAM_MODE))) { //$NON-NLS-1$ //$NON-NLS-2$
				writer = JsonXmlStreamWriter.createWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"),
						JSONConfiguration.badgerFish().build());
			} else if (params != null && "natural".equalsIgnoreCase(params.get(PARAM_MODE))) { //$NON-NLS-1$ //$NON-NLS-2$ {
				writer = JsonXmlStreamWriter.createWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"),
						JSONConfiguration.natural().build());
			} else if (params != null && "mapped".equalsIgnoreCase(params.get(PARAM_MODE))) { //$NON-NLS-1$ //$NON-NLS-2$ {
				writer = JsonXmlStreamWriter.createWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"),
						JSONConfiguration.mapped().build());
			} else if (params != null && "mappedJettison".equalsIgnoreCase(params.get(PARAM_MODE))) { //$NON-NLS-1$ //$NON-NLS-2$ {
				writer = JsonXmlStreamWriter.createWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"),
						JSONConfiguration.mappedJettison().build());
			} else {
				throw new IllegalArgumentException("Unknown mode-flag: " + params.get(PARAM_MODE) +
						", known modes are badgerfish, natural, mapped and mappedJettison");
			}

			try {
				// some methods in JsonXmlStreamWriter throw an "UnsupportedOperationException"
				// which we want to ignore here, e.g. writeComments()!
				writer = (XMLStreamWriter) PreventUnsupportedOperationExceptionProxy.newInstance(writer);

				Map<String, Object> headers = new HashMap<String, Object>();
				headers.put("user", userID); //$NON-NLS-1$

				// XMLStreamWriter writer = XMLSpreadSheetHelper.checkForIndentingWriterAvailable(xmlStreamWriter);

				// unsupported: writer.writeStartDocument("utf-8", "1.0");
				writer.writeStartDocument();

				Map<String, Object> parameters = new HashMap<String, Object>(1);

			/*if(parameters == null) {
				parameters = new HashMap<String, Object>(1);
			}
			parameters.put(PARAMETER_INCLUDE_LAYOUT, includeLayout);
			if(selectedPurePath != null && selectedPurePath.length() > 0) {
				parameters.put(PARAMETER_SELECT_PUREPATH, selectedPurePath);
			}*/

				DashboardXMLReportPartFactory.createDashboardReport(writer, dashboard, dashboardConfig, headers, parameters);

				writer.writeEndDocument();
			} finally {
				writer.close();
			}

		} catch (IOException e) {
			throw new ReportRenderException(e);
		} catch (XMLStreamException e) {
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

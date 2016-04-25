/***************************************************
 * dynaTrace Diagnostics (c) dynaTrace software GmbH
 *
 * @file: JsonXmlStreamWriterTest.java
 * @date: 22.12.2010
 * @author: dominik.stadler
 */
package com.dynatrace.diagnostics.report.json;

import java.io.StringWriter;

import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.json.impl.writer.JsonXmlStreamWriter;


/**
 *
 * @author dominik.stadler
 */
public class JsonXmlStreamWriterTest {

	@Test
	public void testJsonXmlStreamWriter() throws Exception {
		StringWriter stringWriter = new StringWriter();
		XMLStreamWriter writer = JsonXmlStreamWriter.createWriter(stringWriter, JSONConfiguration.natural().build());

		writer = (XMLStreamWriter)PreventUnsupportedOperationExceptionProxy.newInstance(writer);


		try {
			// XMLStreamWriter writer = XMLSpreadSheetHelper.checkForIndentingWriterAvailable(xmlStreamWriter);

			// unsupported: writer.writeStartDocument("utf-8", "1.0");
			writer.writeStartDocument();
			{
				writer.writeCData("someCData");
				writer.writeDTD("mydtddefinition");
				writer.writeStartElement("myelem");
				{
					writer.writeAttribute("key", "value");
					writer.writeComment("somecomment");
				}
				writer.writeEndElement();
			}
			writer.writeEndDocument();
		} finally {
			writer.close();
		}

		System.out.println("JSON: " + stringWriter.toString());
	}
}

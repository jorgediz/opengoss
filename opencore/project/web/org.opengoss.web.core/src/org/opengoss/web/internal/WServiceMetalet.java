/*
 * Copyright 2005-2006 the original authors and www.opengoss.org community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.opengoss.web.internal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opengoss.web.service.meta.IWMethodMeta;
import org.opengoss.web.service.meta.IWServiceMeta;

@SuppressWarnings("serial")
public class WServiceMetalet extends HttpServlet {

	static final String REPRESENTATION = "representation";

	static final String HTML = "html";

	static final String XML = "xml";

	private final IWServiceMeta meta;

	public WServiceMetalet(IWServiceMeta meta) {
		this.meta = meta;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws IOException {
		try {
			String representation = request.getParameter(REPRESENTATION);
			if (representation == null) {
				representation = HTML;
			}
			if (!(HTML.equals(representation) || XML.equals(representation))) {
				representation = HTML;
			}
			if (XML.equals(representation)) {
				response.getWriter().write(toXml(meta));
			} else {
				response.getWriter().write(toHtml(meta));
			}
		} catch (IOException e) {
			response.sendError(500, e.getMessage());
		}
		response.flushBuffer();
	}

	private String toHtml(IWServiceMeta meta) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("<title>Web Service Metadata</title>");
		sb.append("</head>\n");
		sb.append("<body>\n");

		sb.append("<h4>");
		sb.append("Service Name: " + meta.getServiceName());
		sb.append("</h4>");

		sb.append("<h4>");
		sb.append("Service URI: " + meta.getServiceUri());
		sb.append("</h4>");

		sb.append("<table border=\"1\">");
		sb.append("<tr><th>Resource</td><th>Method</th><th>Representation</th><th>Status Code</th></tr>");
		IWMethodMeta[] wmMetas = meta.getWMMetas();
		for (IWMethodMeta wmMeta : wmMetas) {
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(wmMeta.getUriPattern().toString());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(wmMeta.getMethodType());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(getRepresentation(wmMeta));
			sb.append("</td>");
			sb.append("<td>");
			sb.append("N/A");
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table");

		sb.append("</body>\n");
		sb.append("</html>\n");

		return sb.toString();
	}

	private String getRepresentation(IWMethodMeta wmMeta) {
		String rtClassName = wmMeta.getMethod().getReturnType().getName();
		return rtClassName.substring(rtClassName.lastIndexOf(".") + 1);
	}

	private String toXml(IWServiceMeta meta) {
		// TODO Auto-generated method stub
		return null;
	}

}

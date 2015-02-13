<%@ page import="org.apache.commons.codec.binary.Base64" %>
<%@ page contentType="image/gif" language="java" trimDirectiveWhitespaces="true" session="false" %>
<%!
	private static final byte[] imageData;
	static {
		/*
			Credit goes here:
			http://matthew.mceachen.us/blog/how-to-serve-a-transparent-1x1-pixel-gif-from-a-servlet-711.html
		 */
		String PIXEL_B64  = "R0lGODlhAQABAPAAAAAAAAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
		imageData = Base64.decodeBase64(PIXEL_B64);
	}
%>
<%
	response.getOutputStream().write(imageData);
	response.setContentLength(imageData.length);
%>
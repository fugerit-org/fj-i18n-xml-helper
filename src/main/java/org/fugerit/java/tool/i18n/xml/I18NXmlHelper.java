package org.fugerit.java.tool.i18n.xml;

import java.util.Properties;

import org.fugerit.java.core.cli.ArgUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class I18NXmlHelper {

	public static final String ARG_INPUT_XML = "input-xml";
	
	public static final String ARG_OUTPUT_XML = "output-xml";
	
	public static final String ARG_OUTPUT_PROPERTIES = "output-properties";
	
	public static final String ARG_CONVERT_CONFIG = "convert-config";
	
	public static int handle( Properties params ) {
		String inputXml = params.getProperty( ARG_INPUT_XML );
		String outputXml = params.getProperty( ARG_OUTPUT_XML );
		String outputProperties = params.getProperty( ARG_OUTPUT_PROPERTIES );
		String convertConfig = params.getProperty( ARG_CONVERT_CONFIG );
		I18NXmlContext context = new I18NXmlContext(inputXml, outputXml, outputProperties, convertConfig);
		log.info( "context {}", context );
		I18NXmlHandler handler = new I18NXmlHandler();
		int res = handler.handle(context);
		log.info( "res -> {}", res );
		return res;
	}
	
	public static void main( String[] args ) {
		log.info( "start" );
		handle( ArgUtils.getArgs( args  ) );
		log.info( "end" );
	}
	
}

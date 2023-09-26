package org.fugerit.java.tool.i18n.xml;

import java.util.Properties;

import org.fugerit.java.core.cli.ArgUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class I18NXmlHelper {
	
	private I18NXmlHelper() {}

	/**
	 * [REQUIRED] The input path to XML, if a directory all files will be handled recursively.
	 */
	public static final String ARG_INPUT_XML = "input-xml";
	
	/**
	 * [REQUIRED] The output path where to produce the XML, if input is a directory, output MUST be too.
	 */
	public static final String ARG_OUTPUT_XML = "output-xml";
	
	/**
	 * [REQUIRED] The output XML properties containing the processed labels.
	 */
	public static final String ARG_OUTPUT_PROPERTIES = "output-properties";
	
	/**
	 * [REQUIRED] Convert rules configuration XML path.
	 */
	public static final String ARG_CONVERT_CONFIG = "convert-config";
	
	/**
	 * [OPTIONAL] A extension filter, if inputXml is directory, only files matching the extension will be processed. 
	 */
	public static final String ARG_FILTER_EXT = "filter-ext";
	
	/**
	 * [OPTIONAL] if set, the rule catalog in convert-config will be used instead of the default one.    
	 */
	public static final String ARG_CATALOG_RULE_ID= "catalog-rule-id";
	
	public static int handle( Properties params ) {
		String inputXml = params.getProperty( ARG_INPUT_XML );
		String outputXml = params.getProperty( ARG_OUTPUT_XML );
		String outputProperties = params.getProperty( ARG_OUTPUT_PROPERTIES );
		String convertConfig = params.getProperty( ARG_CONVERT_CONFIG );
		I18NXmlContext context = new I18NXmlContext(inputXml, outputXml, outputProperties, convertConfig, params);
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

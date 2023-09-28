package org.fugerit.java.tool.i18n.xml;

import java.util.Properties;

import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.SafeIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.ObjectUtils;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.tool.util.ArgHelper;

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
	public static final String ARG_CATALOG_RULE_ID = "catalog-rule-id";
	
	/**
	 * [OPTIONAL] print help.    
	 */
	public static final String ARG_HELP = "help";
	
	
	private static void printHelp() {
		log.info( "help : \n\n{}", SafeIO.readStringStream( () -> ClassHelper.loadFromDefaultClassLoader( "tool/help.txt" ) ) );
	}
	
	public static int handle( Properties params ) {
		return ObjectUtils.objectWithDefault( SafeFunction.get( () -> {
			String help = params.getProperty( ARG_HELP );
			if ( help != null ) {
				printHelp();
				return Result.RESULT_CODE_KO;
			} else {
				ArgHelper.checkAllRequiredThrowRuntimeEx(params, ARG_INPUT_XML, ARG_INPUT_XML, ARG_OUTPUT_PROPERTIES, ARG_CONVERT_CONFIG);
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
		}, e -> {
			log.info( "Error : "+e, e );
			printHelp();
		} ), Integer.valueOf( Result.RESULT_CODE_KO ) );
	}
	
	public static void main( String[] args ) {
		log.info( "start" );
		handle( ArgUtils.getArgs( args  ) );
		log.info( "end" );
	}
	
}

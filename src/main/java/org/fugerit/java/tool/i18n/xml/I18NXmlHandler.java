package org.fugerit.java.tool.i18n.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.core.cfg.xml.FactoryType;
import org.fugerit.java.core.cfg.xml.GenericListCatalogConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.tool.i18n.xml.config.ConvertRule;
import org.fugerit.java.tool.i18n.xml.config.RuleContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class I18NXmlHandler {

	private void writeCleanXml( PrintWriter writer, String content ) throws IOException {
		try ( BufferedReader cleaner = new BufferedReader( new StringReader( content ) ) ) {
			// maybe not the most elegant way but it does the job.
			cleaner.lines().forEach( l -> {
					// all lines containing only whit spaces or tabs are not written to the output.
					if ( !l.replace( "\t" , "").trim().isEmpty() ) {
						writer.println( l );
					}
				});
		}
	}
	
	public int handle( I18NXmlContext context ) {
		return SafeFunction.get( () -> {
			int res = Result.RESULT_CODE_OK;
			FactoryCatalog rulesCatalog = new FactoryCatalog();
			try ( FileInputStream fis = new FileInputStream( new File( context.getConvertConfig() ) ) ) {
				GenericListCatalogConfig.load( fis , rulesCatalog );	
			}
			try ( FileInputStream fis = new FileInputStream( new File( context.getInputXml() ) );
					PrintWriter writer = new PrintWriter( new OutputStreamWriter( new FileOutputStream( new File( context.getOutputXml() ) ) ) );
					StringWriter outXmlBuffer = new StringWriter();
					FileOutputStream fosProps = new FileOutputStream( new File( context.getOutputProperties() ) ) ) {
				Document doc = DOMIO.loadDOMDoc( fis );
				Element root = doc.getDocumentElement();
				Collection<FactoryType> rules = rulesCatalog.getDataList( "default-catalog" );
				RuleContext ruleContext = new RuleContext();
				ruleContext.setDocument(doc);
				for ( FactoryType ruleConfig : rules ) {
					log.info( "ruleConfig : {} - {}", ruleConfig.getId(), ruleConfig.getType() );
					ConvertRule currentRule = (ConvertRule) ClassHelper.newInstance( ruleConfig.getType() );
					if ( ruleConfig.getElement() != null ) {
						NodeList kids = ruleConfig.getElement().getElementsByTagName( "config" );
						if ( kids.getLength() > 0 ) {
							currentRule.config( (Element) kids.item( 0 ) );	
						}
					}
					currentRule.apply(root, ruleContext);
				}
				
				// write output xml
				DOMIO.writeDOMIndent( doc , outXmlBuffer );
				this.writeCleanXml(writer, outXmlBuffer.toString());
				
				// write output properties
				ruleContext.saveEntries(fosProps);
			}
			return res;
		} );
	}
	
}

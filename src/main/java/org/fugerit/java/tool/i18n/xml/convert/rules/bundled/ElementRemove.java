package org.fugerit.java.tool.i18n.xml.convert.rules.bundled;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.tool.i18n.xml.convert.rules.ConvertRule;
import org.fugerit.java.tool.i18n.xml.convert.rules.RuleContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElementRemove implements ConvertRule {

	public static final String ATT_ELEMENT_TO_REMOVE_PATH = "elementToRemovePath";
	
	private String elementToRemovePath;
	
	private XPath xPath = XPathFactory.newInstance().newXPath();
	
	@Override
	public void config(Element config) throws XMLException {
		this.elementToRemovePath = config.getAttribute( ATT_ELEMENT_TO_REMOVE_PATH );
		log.info( "elementToRemovePath {}", elementToRemovePath );
	}
	
	@Override
	public void apply(Element node, RuleContext ruleContext) throws XMLException {
		// convert any Exception in XMLException and throw it
		XMLException.apply( () -> {
			NodeList elementsToRemove = (NodeList) xPath.compile( this.elementToRemovePath ).evaluate( node, XPathConstants.NODESET ) ;
			for ( int k=0; k<elementsToRemove.getLength(); k++ ) {
				Node current = elementsToRemove.item(k);
				current.getParentNode().removeChild( current );
			}
		} );
	}

}

package org.fugerit.java.tool.i18n.xml.config;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.fugerit.java.core.lang.helpers.BooleanUtils;
import org.fugerit.java.core.lang.helpers.StringUtils;
import org.fugerit.java.core.xml.XMLException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LabelConvertRule implements ConvertRule {

	public static final String ATT_ELEMENT_FROM_PATH = "elementFromPath";
	public static final String ATT_ELEMENT_FROM = "elementFrom";
	public static final String ATT_ELEMENT_TO = "elementTo";
	
	public static final String ATT_PREFIX_TEXT = "prefixText";
	public static final String ATT_PREFIX_NODE = "prefixNode";
	
	public static final String ATT_REMOVE_FROM_TAG = "removeFromTag";
	
	private String elementFromPath;
	
	private String elementFrom;
	
	private String elementTo;
	
	private String prefixText;
	
	private String prefixNode;
	
	private String removeFromTag;
	
	private XPath xPath = XPathFactory.newInstance().newXPath();
	
	@Override
	public void config(Element config) throws XMLException {
		this.elementFromPath = config.getAttribute( ATT_ELEMENT_FROM_PATH );
		this.elementFrom = config.getAttribute( ATT_ELEMENT_FROM );
		this.elementTo = config.getAttribute( ATT_ELEMENT_TO );
		this.prefixText = config.getAttribute( ATT_PREFIX_TEXT );
		this.prefixNode = config.getAttribute( ATT_PREFIX_NODE );
		this.removeFromTag = config.getAttribute( ATT_REMOVE_FROM_TAG );
		log.info( "elementFromPath {}", elementFromPath );
		log.info( "elementFrom {}", elementFrom );
		log.info( "elementTo {}", elementTo );
		log.info( "prefixText {}", prefixText );
		log.info( "prefixNode {}", prefixNode );
		log.info( "removeFromTag {}", removeFromTag );
	}

	private void handleCurrent( Element current, RuleContext ruleContext ) throws XPathExpressionException {
		NodeList from = current.getElementsByTagName( this.elementFrom );
		if ( from.getLength() > 0 ) {
			Element fromTag = (Element) from.item( 0 );
			String text = fromTag.getTextContent();
			log.info( "content -> {}", text );
			Element toTag = ruleContext.getDocument().createElement( this.elementTo );
			String key = text.replace( " " , "" );
			// add node?
			if ( StringUtils.isNotEmpty( this.prefixNode ) ) {
				NodeList atts = (NodeList) xPath.compile( this.prefixNode ).evaluate( current, XPathConstants.NODESET) ;
				if ( atts.getLength() > 0 ) {
					key = atts.item(0).getNodeValue()+"."+key;
				}
			}
			// fixed prefix?
			if ( StringUtils.isNotEmpty( this.prefixText ) ) {
				key = this.prefixText+key;
			}
			if ( BooleanUtils.isTrue( this.removeFromTag ) ) {
				current.removeChild( fromTag );
			}
			toTag.setTextContent( key );
			ruleContext.addEntry( key, text );
			current.appendChild( toTag );
		}
	}
	
	@Override
	public void apply(Element node, RuleContext ruleContext) throws XMLException {
		// convert any Excetion in XMLException and throw it
		XMLException.apply( () -> {
			NodeList listFrom = (NodeList) xPath.compile( this.elementFromPath ).evaluate( node, XPathConstants.NODESET);
			for ( int k=0; k<listFrom.getLength(); k++ ) {
				Element current = (Element)listFrom.item( k );
				this.handleCurrent(current, ruleContext);
			}
		} );
	}

}

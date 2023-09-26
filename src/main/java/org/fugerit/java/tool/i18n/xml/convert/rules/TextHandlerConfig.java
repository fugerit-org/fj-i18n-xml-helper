package org.fugerit.java.tool.i18n.xml.convert.rules;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.fugerit.java.core.cfg.xml.XmlBeanHelper;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class TextHandlerConfig {

	@Getter @Setter private String id;
	@Getter @Setter private String position;
	@Getter @Setter private String mode;
	@Getter @Setter private String value;
	
	public static final String POSITION_PREFIX = "prefix";
	public static final String POSITION_SUFFIX = "suffix";
	
	public static final String MODE_FIXED = "fixed";
	public static final String MODE_NODE = "node";
	public static final String MODE_NORMALIZE = "normalize";

	public static final String MODE_NORMALIZE_REMOVE_WHITESPACES = "removeWhitespaces";
	public static final String MODE_NORMALIZE_ALPHANUMERIC = "alphanumeric";

	public static List<TextHandlerConfig> parse( Element tag, String childTagName ) {
		List<TextHandlerConfig> res = new ArrayList<>();
		NodeList list = tag.getElementsByTagName(childTagName);
		for ( int k=0; k<list.getLength(); k++ ) {
			Element currentTag = (Element)list.item(k);
			TextHandlerConfig config = new TextHandlerConfig();
			XmlBeanHelper.setFromElementSafe( config , currentTag, XmlBeanHelper.MODE_XML_ATTRIBUTES );
			res.add( config );
		}
		return res;
	}
	
	private static String normalize( String inputText, String option ) {
		String output = null;
		if ( MODE_NORMALIZE_ALPHANUMERIC.equalsIgnoreCase( option ) ) {
			output = inputText.replaceAll( "[^a-zA-Z0-9]" , "" );
		} else {
			output = inputText.replace( " " , "" );
		}
		return output;
	}
	
	private String apply( String input, final Element node ) {
		return SafeFunction.get( () -> {
			String output = input;
			log.info( "applying : {}", this );
			String text = "";
			// mode
			if ( MODE_NODE.equalsIgnoreCase( this.mode ) ) {
				XPath xPath = XPathFactory.newInstance().newXPath();
				NodeList atts = (NodeList) xPath.compile( this.value ).evaluate( node, XPathConstants.NODESET) ;
				if ( atts.getLength() > 0 ) {
					text = atts.item(0).getNodeValue();
				}
			} else if ( MODE_NORMALIZE.equalsIgnoreCase( this.mode ) ) {
				// this mode rewrite output
				output = normalize( output, this.value );
			} else {
				text = value;
			}
			// append
			if ( POSITION_SUFFIX.equalsIgnoreCase( this.position ) ) {
				output+=text;
			} else {
				output = text+output;
			}
			return output;	
		} );
	}
	
	public static String handle( List<TextHandlerConfig> handlers, String input, final Element node ) {
		SimpleValue<String> output = new SimpleValue<>( input );
		handlers.stream().forEach( h -> output.setValue( h.apply( output.getValue(), node ) ) );
		return output.getValue();
	}
	
}

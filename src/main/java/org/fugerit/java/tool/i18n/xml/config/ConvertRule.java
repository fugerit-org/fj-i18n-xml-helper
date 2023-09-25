package org.fugerit.java.tool.i18n.xml.config;

import org.fugerit.java.core.xml.XMLException;
import org.w3c.dom.Element;

public interface ConvertRule {

	void config( Element config ) throws XMLException;
	
	void apply( Element node, RuleContext ruleContext ) throws XMLException;
	
}

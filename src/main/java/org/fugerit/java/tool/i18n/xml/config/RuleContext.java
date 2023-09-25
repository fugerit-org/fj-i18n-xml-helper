package org.fugerit.java.tool.i18n.xml.config;

import java.util.ArrayList;
import java.util.List;

import org.fugerit.java.core.lang.helpers.AttributeHolderDefault;
import org.fugerit.java.core.util.MapEntry;
import org.w3c.dom.Document;

import lombok.Getter;
import lombok.Setter;

public class RuleContext extends AttributeHolderDefault {

	private static final long serialVersionUID = 6577340920721820407L;

	@Getter private transient List<MapEntry<String, String>> entries;
	
	@Getter @Setter private Document document;
	
	public RuleContext() {
		this.entries = new ArrayList<>();
	}
	
}

package org.fugerit.java.tool.i18n.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class I18NXmlContext {

	@Getter @NonNull private String inputXml;
	@Getter @NonNull private String outputXml;
	@Getter @NonNull private String outputProperties;
	@Getter @NonNull private String convertConfig;
	
}

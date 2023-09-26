package org.fugerit.java.tool.i18n.xml;

import java.io.FileFilter;
import java.util.Collection;
import java.util.Properties;

import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.core.cfg.xml.FactoryType;
import org.fugerit.java.core.lang.helpers.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@ToString
@Slf4j
public class I18NXmlContext {

	public static final String ATT_USE_CATALOG = "use-catalog";		// general property for default catalog
	
	@Getter @NonNull private String inputXml;
	@Getter @NonNull private String outputXml;
	@Getter @NonNull private String outputProperties;
	@Getter @NonNull private String convertConfig;
	@Getter @NonNull private Properties params;
	
	public FileFilter getFileFilter() {
		String ext = params.getProperty( I18NXmlHelper.ARG_FILTER_EXT );
		if ( StringUtils.isEmpty( ext ) ) {
			return f -> Boolean.TRUE.booleanValue();
		} else {
			return f -> f.isDirectory() || f.getName().endsWith( ext );
		}
	}
	
	public Collection<FactoryType> getRuleCatalog( FactoryCatalog catalog ) {
		String catalogId = params.getProperty( I18NXmlHelper.ARG_CATALOG_RULE_ID , catalog.getGeneralProps().getProperty( ATT_USE_CATALOG ) );
		log.info( "catalogId : {}", catalogId );
		return catalog.getDataList( catalogId );
	}
	
}

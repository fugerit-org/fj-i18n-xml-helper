package test.org.fugerit.java.tool.i18n.xml;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.tool.i18n.xml.I18NXmlHelper;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestI18NXmlHelper {

	@Test
	public void testConvertHelp() {
		File outputFile = new File( "target/na1.xml" );
		String[] args = {
				ArgUtils.getArgString( I18NXmlHelper.ARG_HELP ), "na" ,
		};
		I18NXmlHelper.main(args);
		Assert.assertFalse( outputFile.exists() );
	}
	
	@Test
	public void testConvertRequired() {
		File outputFile = new File( "target/na2.xml" );
		I18NXmlHelper.main( new String[0] );
		Assert.assertFalse( outputFile.exists() );
	}
	
	@Test
	public void testConvertMain() {
		File outputFile = new File( "target/test2.xml" );
		String[] args = {
				ArgUtils.getArgString( I18NXmlHelper.ARG_CONVERT_CONFIG ), "cl://config/test/convert-config.xml" ,
				ArgUtils.getArgString( I18NXmlHelper.ARG_INPUT_XML ), "src/test/resources/config/test/xml/test2.xml" ,
				ArgUtils.getArgString( I18NXmlHelper.ARG_OUTPUT_XML ), outputFile.getAbsolutePath() ,
				ArgUtils.getArgString( I18NXmlHelper.ARG_OUTPUT_PROPERTIES ), "target/test2_properties.xml" ,
		};
		I18NXmlHelper.main(args);
		Assert.assertTrue( outputFile.exists() );
	}
	
	@Test
	public void testConvertFolder() {
		Properties params = new Properties();
		params.setProperty( I18NXmlHelper.ARG_CONVERT_CONFIG , "src/test/resources/config/test/convert-config.xml" );
		params.setProperty( I18NXmlHelper.ARG_INPUT_XML , "src/test/resources/config/test/xml" );
		params.setProperty( I18NXmlHelper.ARG_OUTPUT_XML , "target/test_xml" );
		params.setProperty( I18NXmlHelper.ARG_OUTPUT_PROPERTIES , "target/test_xml/test1_properties.xml" );
		params.setProperty( I18NXmlHelper.ARG_FILTER_EXT , "xml" );		// will filter files not ending with 'xml'
		try {
			int res = I18NXmlHelper.handle( params );
			Assert.assertEquals( Result.RESULT_CODE_OK , res );	
		} catch (ConfigRuntimeException e) {
			log.error( "Error "+e, e );
			fail( "Error : "+e );
		}
	}
	
	@Test
	public void testConvertSingleXml() {
		Properties params = new Properties();
		params.setProperty( I18NXmlHelper.ARG_CONVERT_CONFIG , "src/test/resources/config/test/convert-config.xml" );
		params.setProperty( I18NXmlHelper.ARG_INPUT_XML , "src/test/resources/config/test/xml/test1.xml" );
		params.setProperty( I18NXmlHelper.ARG_OUTPUT_XML , "target/test1.xml" );
		params.setProperty( I18NXmlHelper.ARG_OUTPUT_PROPERTIES , "target/test1_properties.xml" );
		params.setProperty( I18NXmlHelper.ARG_CATALOG_RULE_ID , "simple-catalog" );
		try {
			int res = I18NXmlHelper.handle( params );
			Assert.assertEquals( Result.RESULT_CODE_OK , res );	
		} catch (ConfigRuntimeException e) {
			log.error( "Error "+e, e );
			fail( "Error : "+e );
		}
	}
	
}


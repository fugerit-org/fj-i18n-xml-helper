# fj-i18n-xml-helper

Project to extract xml attributes or elements label and converting to property keys

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](https://github.com/fugerit-org/fj-i18n-xml-helper/blob/master/CHANGELOG.md) 
[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/fj-i18n-xml-helper.svg)](https://mvnrepository.com/artifact/org.fugerit.java/fj-i18n-xml-helper)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-i18n-xml-helper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-i18n-xml-helper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_fj-i18n-xml-helper&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_fj-i18n-xml-helper)

![Java runtime version](https://img.shields.io/badge/run%20on-java%208+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Java build version](https://img.shields.io/badge/build%20on-java%2011+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

## 1. Standalone tool quickstart

build : 

`mvn clean install -P singlepackage`

run : 

```
java -jar target/dist-fj-i18n-xml-helper-0.1.0-SNAPSHOT.jar\
	--input-xml src/test/resources/config/test/xml\
	--output-xml target/test-output\
	--output-properties target/test-properties.xml\
	--convert-config src/test/resources/config/test/convert-config.xml
```

See sections on parameters and convert-config for more info.

## 2. API usage quickstart

```
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
		}
```

It is possible to see the [TestI18NXmlHelper unit test](src/test/java/test/org/fugerit/java/tool/i18n/xml/TestI18NXmlHelper.java) for an example.

See sections on parameters and convert-config for more info.

## 3. Parameters cheat sheet

|parameter           |required|description                                                                                            |
|--------------------|--------|-------------------------------------------------------------------------------------------------------|
|input-xml           |true    |The input path to XML, if a directory all files will be handled recursively.                           |
|output-xml          |true    |The output path where to produce the XML, if input is a directory, output MUST be too.                 |
|output-properties   |true    |The output xml properties containing the processed labels.                                             |
|convert-config      |true    |Convert rules configuration XML path.                                                                  |
|filter-ext          |false   |A extension filter, if inputXml is directory, only files matching the extension will be processed.     |
|catalog-rule-id     |false   |if set, the rule catalog in convert-config will be used instead of the default one.                    |


## 4. convert-config cheat sheet

It is possible to see a [sample configuration](src/test/resources/config/test/convert-config.xml).

```
+--convert-config  - the main configuration, the default catalog can be set via 'use-catalog' attribute
   +--factory(*)   - a rule catalog, is a container for rules, 'id' attributes must be set
      +--data(*)   - implementation of a rule, attributes 'id', 'info' (for) and type ()
         +--config - custom configuration of a rule
```

### The 'data' element

Attributes : 
- *id* - id of the rule
- *info* - user defined information about the rule
- *type* - fully qualified name of a class implementing org.fugerit.java.tool.i18n.xml.convert.ConvertRule

And a child element 'config' with a custom configuration which will be passed by to ConvertRule.config() method.

## 5. Bundled convert rules

In API Mode it is possible to define custom rules, but there are some bundled ones in pacakge org.fugerit.java.tool.i18n.xml.convert.rules.bundled : 

### 5.1 LabelExtract

Main rule, process elements and salve keys to properties.

**config attributes**
- *elementFromPath* : xpath to the parent element to process
- *elementFrom* : child element of elementFromPath to be processed for lebel
- *elementTo* : the new lement to be created with the property keys

The config element can have one or more key processing handler, which will transform the key to be set in property file : 

*keyTextHandler* - See section 6. Text handlers


### 5.2 ElementRemove

Utility rule, remove an element with given path, no actions on the label properties.

**config attributes**
- *elementToRemovePath* : xpath to the element to remove

## 6. Text handlers

In some rules it is possible to set a chain of handlers, any handlers can have the following attributes : 

**test handler attributes**
- *id* : id of the handler (can be used as description)
- *position* : 'prefix' or 'suffix' to set where the current handler result will be added.
- *mode* : 
    - 'fixed' : the *value* attribute will be simply added
    - 'node' : will do a xpath search on current element for *value* path, and the value will be added (1), the found value will be normalized to aphanumeric.
    - 'normalize' : if *value* is 'removeWhitespaces' will simply remove whitespaces, if 'alphanumeric' will remove all non alphanumeric characters. 
    - 'cute' : *value* should be the maximum size of the key
- *value* : see above for *mode* attribute. 
- *altValue* : may sometimes be used by some *mode*
- *info* : may sometimes be used by some *mode*

(1) - in 'node' mode if *value* is not found as a path, *altValue* will be tried in the same way. If again no result *info* attribute will be take as fixed value. And at last it will produce empty string.

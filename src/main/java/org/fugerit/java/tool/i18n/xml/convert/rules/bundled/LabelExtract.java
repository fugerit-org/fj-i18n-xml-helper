package org.fugerit.java.tool.i18n.xml.convert.rules.bundled;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.tool.i18n.xml.convert.rules.ConvertRule;
import org.fugerit.java.tool.i18n.xml.convert.rules.RuleContext;
import org.fugerit.java.tool.i18n.xml.convert.rules.TextHandlerConfig;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LabelExtract implements ConvertRule {

    public static final String ATT_ELEMENT_FROM_PATH = "elementFromPath";
    public static final String ATT_ELEMENT_FROM = "elementFrom";
    public static final String ATT_ELEMENT_TO = "elementTo";

    private String elementFromPath;

    private String elementFrom;

    private String elementTo;

    private List<TextHandlerConfig> listKeyHandlers;

    private XPath xPath = XPathFactory.newInstance().newXPath();

    @Override
    public void config(Element config) throws XMLException {
        this.elementFromPath = config.getAttribute(ATT_ELEMENT_FROM_PATH);
        this.elementFrom = config.getAttribute(ATT_ELEMENT_FROM);
        this.elementTo = config.getAttribute(ATT_ELEMENT_TO);
        log.info("elementFromPath {}", elementFromPath);
        log.info("elementFrom {}", elementFrom);
        log.info("elementTo {}", elementTo);
        this.listKeyHandlers = TextHandlerConfig.parse(config, "keyTextHandler");
        log.info("key handlers size : {}", this.listKeyHandlers.size());
    }

    private void handleCurrent(Element current, RuleContext ruleContext) throws XPathExpressionException {
        NodeList from = current.getElementsByTagName(this.elementFrom);
        if (from.getLength() > 0) {
            Element fromTag = (Element) from.item(0);
            String text = fromTag.getTextContent();
            log.info("content -> {}", text);
            Element toTag = ruleContext.getDocument().createElement(this.elementTo);
            String key = TextHandlerConfig.handle(this.listKeyHandlers, text, current);
            toTag.setTextContent(key);
            ruleContext.addEntry(key, text);
            current.appendChild(toTag);
        }
    }

    @Override
    public void apply(Element node, RuleContext ruleContext) throws XMLException {
        // convert any Exception in XMLException and throw it
        XMLException.apply(() -> {
            NodeList listFrom = (NodeList) xPath.compile(this.elementFromPath).evaluate(node, XPathConstants.NODESET);
            for (int k = 0; k < listFrom.getLength(); k++) {
                Element current = (Element) listFrom.item(k);
                this.handleCurrent(current, ruleContext);
            }
        });
    }

}

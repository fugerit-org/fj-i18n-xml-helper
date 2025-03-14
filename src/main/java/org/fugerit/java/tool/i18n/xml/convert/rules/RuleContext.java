package org.fugerit.java.tool.i18n.xml.convert.rules;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.fugerit.java.core.lang.helpers.AttributeHolderDefault;
import org.w3c.dom.Document;

import lombok.Getter;
import lombok.Setter;

public class RuleContext extends AttributeHolderDefault {

    private static final long serialVersionUID = 6577340920721820407L;

    @Getter
    private transient SortedXMLProperties entries;

    @Getter
    @Setter
    private transient Document document;

    public RuleContext() {
        this.entries = new SortedXMLProperties();
    }

    public void addEntry(String key, String value) {
        this.entries.setProperty(key, value);
    }

    public void saveEntries(OutputStream out) throws IOException {
        entries.storeToXML(out, "Entries saved on " + new Timestamp(System.currentTimeMillis()), StandardCharsets.UTF_8.name());
    }

}

/*
 * The purpose of this class is to take advantage of Properties.storeToXml() method to store the properties in the same order as
 * they are added.
 */
class SortedXMLProperties extends Properties {

    private static final long serialVersionUID = 4862022744670019101L;

    // setProperty() and entrySet() are the only two method we are actually using for our API.

    private transient LinkedHashMap<Object, Object> orderedMap;

    public SortedXMLProperties() {
        this.orderedMap = new LinkedHashMap<>();
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        Object res = null;
        synchronized (this) {
            this.orderedMap.put(key, value);
            res = super.setProperty(key, value);
        }
        return res;
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return this.orderedMap.entrySet();
    }

    @Override
    public synchronized int hashCode() {
        return super.hashCode();
    }

    @Override
    public synchronized boolean equals(Object o) {
        return super.equals(o);
    }

}
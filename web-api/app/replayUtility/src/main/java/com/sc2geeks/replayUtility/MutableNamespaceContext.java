package com.sc2geeks.replayUtility;

import javax.xml.namespace.NamespaceContext;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: robert
 * Date: 8/22/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MutableNamespaceContext implements NamespaceContext {
  private Map<String, String> map;

  public MutableNamespaceContext() {
    map = new HashMap();
  }

  public void setNamespace(String prefix, String namespaceURI) {
    map.put(prefix, namespaceURI);
  }

  public String getNamespaceURI(String prefix) {
    return map.get(prefix);
  }

  public String getPrefix(String namespaceURI) {
    for (String prefix : map.keySet()) {
      if (map.get(prefix).equals(namespaceURI)) {
        return prefix;
      }
    }
    return null;
  }

  public Iterator getPrefixes(String namespaceURI) {
    List prefixes = new ArrayList();
    for (String prefix : map.keySet()) {
      if (map.get(prefix).equals(namespaceURI)) {
        prefixes.add(prefix);
      }
    }
    return prefixes.iterator();
  }
}

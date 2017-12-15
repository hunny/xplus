package com.xplus.commons.util.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

public class LDAPCRUDUser {
  DirContext dc = null;
  String root = "DC=2003,DC=com"; // LDAP的根节点的DC

  public static void main(String[] args) {
    LDAPCRUDUser ldap = new LDAPCRUDUser();
    // ldap.delete("CN=用户名,OU=所在部门,DC=2003,DC=com");
    // ldap.renameEntry("CN=foo.bar,OU=test,DC=2003,DC=com",
    // "CN=foo.bar,OU=DEV,DC=2003,DC=com");
    SearchResult sr = ldap.searchByUserName(ldap.root, "foo.bar");
    System.out.println(sr.getName());
    // ldap.modifyInformation(sr.getName(), "test");
    ldap.searchInformation(ldap.root);
    ldap.close();
  }

  /**
   * 
   */
  public LDAPCRUDUser() {
    super();
    init();
  }

  public void init() {
    Properties env = new Properties();
    String adminName = "administrator@2003.com";// username@domain
    String adminPassword = "admin";// password
    String ldapURL = "LDAP://172.17.0.212:389";// ip:port
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.SECURITY_AUTHENTICATION, "simple");// "none","simple","strong"
    env.put(Context.SECURITY_PRINCIPAL, adminName);
    env.put(Context.SECURITY_CREDENTIALS, adminPassword);
    env.put(Context.PROVIDER_URL, ldapURL);
    try {
      dc = new InitialLdapContext(env, null);
      System.out.println("认证成功");
    } catch (Exception e) {
      System.out.println("认证失败");
      e.printStackTrace();
    }
  }

  public void close() {
    if (dc != null) {
      try {
        dc.close();
      } catch (NamingException e) {
        System.out.println("NamingException in close():" + e);
      }
    }
  }

  public void add(String newUserName) {
    try {
      BasicAttributes attrs = new BasicAttributes();
      BasicAttribute objclassSet = new BasicAttribute("objectClass");
      objclassSet.add("sAMAccountName");
      objclassSet.add("employeeID");
      attrs.put(objclassSet);
      attrs.put("ou", newUserName);
      dc.createSubcontext("ou=" + newUserName + "," + root, attrs);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception in add():" + e);
    }
  }

  public void delete(String dn) {
    try {
      dc.destroySubcontext(dn);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception in delete():" + e);
    }
  }

  public boolean renameEntry(String oldDN, String newDN) {
    try {
      dc.rename(oldDN, newDN);
      return true;
    } catch (NamingException ne) {
      System.err.println("Error: " + ne.getMessage());
      return false;
    }
  }

  public boolean modifyInformation(String dn, String employeeID) {
    try {
      System.out.println("updating...\n");
      ModificationItem[] mods = new ModificationItem[1];
      // 修改属性
      Attribute attr0 = new BasicAttribute("OU", employeeID);
      mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr0);
      /* 修改属性 */
      dc.modifyAttributes(dn + ",DC=2003,DC=com", mods);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error: " + e.getMessage());
      return false;
    }
  }

  public void searchInformation(String searchBase) {
    try {
      SearchControls searchCtls = new SearchControls();
      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
      String returnedAtts[] = {
          "memberOf" };
      searchCtls.setReturningAttributes(returnedAtts);
      NamingEnumeration<SearchResult> answer = dc.search(searchBase, searchFilter, searchCtls);
      while (answer.hasMoreElements()) {
        SearchResult sr = answer.next();
        System.out.println("<<<::[" + sr.getName() + "]::>>>>");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public SearchResult searchByUserName(String searchBase, String userName) {
    SearchControls searchCtls = new SearchControls();
    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String searchFilter = "sAMAccountName=" + userName;
    String returnedAtts[] = {
        "memberOf" }; // 定制返回属性
    searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集
    try {
      NamingEnumeration<SearchResult> answer = dc.search(searchBase, searchFilter, searchCtls);
      return answer.next();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Throw Exception : " + e);
    }
    return null;
  }
}

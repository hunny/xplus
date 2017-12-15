package com.xplus.commons.util.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * 通过管理员账号，拉取AD域账户
 */
public class FetchLDAPList {

  public static void main(String[] args) {

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
      LdapContext ctx = new InitialLdapContext(env, null);
      SearchControls searchCtls = new SearchControls();
      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
      String searchBase = "DC=2003,DC=com";
      String returnedAtts[] = {
          "memberOf" };
      searchCtls.setReturningAttributes(returnedAtts);
      NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
      while (answer.hasMoreElements()) {
        SearchResult sr = answer.next();
        System.out.println("<<<::[" + sr.getName() + "]::>>>>");
      }
      ctx.close();
    } catch (NamingException e) {
      e.printStackTrace();
      System.err.println("Problem searching directory: " + e);
    }

  }
}

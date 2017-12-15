package com.xplus.commons.util.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class WindowsLdapAllDemo {
  private String host, url, adminName, adminPassword;
  private LdapContext ctx = null;

  /**
   * 初始化ldap
   */
  @SuppressWarnings({
      "unchecked", "rawtypes" })
  public void initLdap() {
    // ad服务器
    this.host = "1.1.1.1:389"; // AD服务器
    this.url = new String("ldap://" + host);// 默认端口为80的可以不用填写，其他端口需要填写，如ldap://xxx.com:8080
    this.adminName = "administrator@ld.com";// 注意用户名的写法：User@domain.com
    this.adminPassword = "12345";
    Hashtable HashEnv = new Hashtable();
    HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
    HashEnv.put(Context.SECURITY_PRINCIPAL, adminName); // AD User
    HashEnv.put(Context.SECURITY_CREDENTIALS, adminPassword); // AD Password
    HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
    HashEnv.put(Context.PROVIDER_URL, url);
    try {
      ctx = new InitialLdapContext(HashEnv, null);
      System.out.println("初始化ldap成功！");
    } catch (NamingException e) {
      e.printStackTrace();
      System.err.println("Throw Exception : " + e);
    }
  }

  /**
   * 关闭ldap
   */
  public void closeLdap() {
    try {
      this.ctx.close();
    } catch (NamingException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   * @param type
   *          organizationalUnit:组织架构 group：用户组 user|person：用户
   * @param name
   * @return
   */
  public String getADInfo(String type, String filter, String name) {

    String userName = name; // 用户名称
    if (userName == null) {
      userName = "";
    }
    String result = "";
    try {
      // 域节点OU=zhongguo,DC=ld,DC=com
      String searchBase = "OU=zhongguo,DC=ld,DC=com";
      // LDAP搜索过滤器类
      // cn=*name*模糊查询 cn=name 精确查询
      // String searchFilter = "(objectClass="+type+")";
      String searchFilter = "objectClass=" + type + "";
      // 创建搜索控制器
      SearchControls searchCtls = new SearchControls();
      // 设置搜索范围
      searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      // String returnedAtts[] = { "memberOf" }; // 定制返回属性
      // searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集 不设置则返回所有属性
      // 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
      NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);// Search
                                                                                  // for
                                                                                  // objects
                                                                                  // using
                                                                                  // the
                                                                                  // filter
      while (answer.hasMore()) {
        SearchResult result1 = (SearchResult) answer.next();
        System.err.println(result1.getName());
        NamingEnumeration<? extends Attribute> attrs = result1.getAttributes().getAll();
        while (attrs.hasMore()) {
          Attribute attr = attrs.next();
          System.out.println(attr.getID() + "  =  " + attr.get());
        }
        System.out.println("============");
      }
      System.out.println("************************************************");
    } catch (NamingException e) {
      e.printStackTrace();
      System.err.println("Throw Exception : " + e);
    }
    return result;
  }

  public static void main(String args[]) {
    // 实例化
    WindowsLdapAllDemo ad = new WindowsLdapAllDemo();
    ad.initLdap();
    ad.getADInfo("User", "uid", "");// 查找用户
    // ad.getADInfo("organizationalUnit", "ou", "工程");// 查找组织架构
    // ad.getADInfo("group", "cn", "xxx");// 查找用户组

    ad.closeLdap();
  }
}

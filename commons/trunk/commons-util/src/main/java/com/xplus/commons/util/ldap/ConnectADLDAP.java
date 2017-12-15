package com.xplus.commons.util.ldap;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * 使用java连接AD域LDAP,验证账号密码是否正确
 */
public class ConnectADLDAP {

  /**
   * 使用java连接AD域
   * 
   * @return void
   * @param host
   *          连接AD域服务器的ip
   * @param post
   *          AD域服务器的端口
   * @param username
   *          用户名
   * @param password
   *          密码
   */
  public static void connect(String host, String post, String username, String password) {
    DirContext ctx = null;
    Hashtable<String, String> HashEnv = new Hashtable<String, String>();
    HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong)
    HashEnv.put(Context.SECURITY_PRINCIPAL, username); // AD的用户名
    HashEnv.put(Context.SECURITY_CREDENTIALS, password); // AD的密码
    HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
    HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");// 连接超时设置为3秒
    HashEnv.put(Context.PROVIDER_URL, "ldap://" + host + ":" + post);// 默认端口389
    try {
      ctx = new InitialDirContext(HashEnv);// 初始化上下文
      System.out.println("身份验证成功!");
      SearchResult searchByUserName = searchByUserName(ctx, "", "huzexiong");
      System.out.println(searchByUserName);
    } catch (AuthenticationException e) {
      System.out.println("身份验证失败!");
      e.printStackTrace();
    } catch (javax.naming.CommunicationException e) {
      System.out.println("AD域连接失败!");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("身份验证未知异常!");
      e.printStackTrace();
    } finally {
      if (null != ctx) {
        try {
          ctx.close();
          ctx = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  public static SearchResult searchByUserName(DirContext ctx, String searchBase, String userName) {
    SearchControls searchCtls = new SearchControls();
    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String searchFilter = "sAMAccountName=" + userName;
    String returnedAtts[] = {
        "memberOf" }; // 定制返回属性
    searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集
    try {
      NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
      return answer.next();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Throw Exception : " + e);
    }
    return null;
  }

  public static void main(String[] args) {
    ConnectADLDAP.connect("yourdomain", "389", "yourname@yourdomain.com", "yourpassword");
  }
}

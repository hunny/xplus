package com.xplus.commons.thread.junior.join;

/**
 * 希望这个程序的输出结果能够为5,但是通常情况下很难遇到这种情况.原因如下:当主线程main()执行到System.out.println(n)这条语句时,线程t可能正在分配资源准备运行,但是还没有真正运行.正因为线程在启动时需要分配资源,当main()方法执行完t.start()后,紧接着执行System.out.println(n),这个时候的结果可能是还没有改变的值.
 * 那么我如何得到期望的值5呢？既然线程在真正运行前需要一段时间,那么我可以在t.start()方法之后加上注释一的代码,让主线程休眠一段时间,等待线程t的启动.
 * 那么现在看一下注释二,在t.start()之后加上t.join(),也能得到结果5.那么join()方法到底起到了什么作用呢？
 * 简单点来说:t.join()使线程t执行完毕.结合上例的解释就是说,当线程t在某个线程上调用join()方法时,该线程被挂起,直到线程t执行完毕再继续执行.
 * join()方法在使用时可以带上一个超时参数,这样如果线程t在这段时间到期时还没有结束的话,join()方法也能返回,使当前线程继续运行.
 *
 */
public class JoinDemo implements Runnable {
  private static int n = 0;

  public void run() {
    for (int i = 0; i < 5; i++) {
      n += 1;
    }
  }

  public static void main(String[] args) throws Exception {
    Thread t = new Thread(new JoinDemo());
    t.start();
    // TimeUnit.MICROSECONDS.sleep(10); // 注释一
    
    // 当线程t在某个线程上调用join()方法时,该线程被挂起,直到线程t执行完毕再继续执行.
    // t.join();// 注释二
    System.out.println(n);
  }

}

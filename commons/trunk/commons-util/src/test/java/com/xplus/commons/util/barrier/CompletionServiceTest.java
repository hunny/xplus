package com.xplus.commons.util.barrier;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 将（生产新的异步任务）与（使用已完成任务的结果）分离开来的服务。生产者 submit 执行的任务。使用者 take
 * 已完成的任务，并按照完成这些任务的顺序处理它们的结果。例如，CompletionService 可以用来管理异步 IO
 * ，执行读操作的任务作为程序或系统的一部分提交，然后，当完成读操作时，会在程序的不同部分执行其他操作，执行操作的顺序可能与所请求的顺序不同。
 * 通常，CompletionService 依赖于一个单独的 Executor 来实际执行任务，在这种情况下，CompletionService
 * 只管理一个内部完成队列。ExecutorCompletionService 类提供了此方法的一个实现。 内存一致性效果：线程中向
 * CompletionService 提交任务之前的操作 happen-before 该任务执行的操作，后者依次 happen-before 紧跟在从对应
 * take() 成功返回的操作。
 *
 */
public class CompletionServiceTest {
  // 这个东西的使用上很类似于CallableFutureTest，不同的是，它会首先取完成任务的线程。
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService exec = Executors.newFixedThreadPool(10);
    // 创建CompletionService
    CompletionService<String> completionService = new ExecutorCompletionService<String>(exec);
    for (int index = 0; index < 5; index++) {
      final int NO = index;
      // Callable 接口类似于 Runnable
      Callable<String> downImg = new Callable<String>() {
        public String call() throws Exception {
          Thread.sleep((long) (Math.random() * 1000));
          return "Downloaded Image " + NO;
        }
      };
      // 提交要执行的值返回任务，并返回表示挂起的任务结果的 Future。在完成时，可能会提取或轮询此任务。
      completionService.submit(downImg);
    }
    System.out.println("Show web content");
    for (int index = 0; index < 5; index++) {
      // 获取并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
      Future<String> task = completionService.take();
      // 如有必要，等待计算完成，然后获取其结果。
      String img = task.get();
      System.out.println(img);
    }
    System.out.println("End");
    // 关闭线程池
    exec.shutdown();
  }

}

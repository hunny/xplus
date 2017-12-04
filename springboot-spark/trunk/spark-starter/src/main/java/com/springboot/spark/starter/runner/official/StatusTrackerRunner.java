package com.springboot.spark.starter.runner.official;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkJobInfo;
import org.apache.spark.SparkStageInfo;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.springboot.spark.starter.runner.official.profile.StatusTrackerProfile;

@Component
@StatusTrackerProfile
public class StatusTrackerRunner implements CommandLineRunner, Serializable {

  private static final long serialVersionUID = -1240166366466641399L;

  public static final class IdentityWithDelay<T> implements Function<T, T> {

    private static final long serialVersionUID = -6926905652789315926L;

    @Override
    public T call(T x) throws Exception {
      Thread.sleep(2 * 1000); // 2 seconds
      return x;
    }
  }

  @Autowired
  private JavaSparkContext jsc;

  @Override
  public void run(String... args) throws Exception {

    // Example of implementing a progress reporter for a simple job.
    JavaRDD<Integer> rdd = jsc.parallelize(Arrays.asList(1, 2, 3, 4, 5), 5)
        .map(new IdentityWithDelay<>());
    JavaFutureAction<List<Integer>> jobFuture = rdd.collectAsync();
    while (!jobFuture.isDone()) {
      Thread.sleep(1000); // 1 second
      List<Integer> jobIds = jobFuture.jobIds();
      if (jobIds.isEmpty()) {
        continue;
      }
      int currentJobId = jobIds.get(jobIds.size() - 1);
      System.out.println("currentJobId:" + currentJobId);
      SparkJobInfo jobInfo = jsc.statusTracker().getJobInfo(currentJobId);
      SparkStageInfo stageInfo = jsc.statusTracker().getStageInfo(jobInfo.stageIds()[0]);
      System.out.println(stageInfo.numTasks() + " tasks total: " + stageInfo.numActiveTasks()
          + " active, " + stageInfo.numCompletedTasks() + " complete");
    }

    System.out.println("Job results are: " + jobFuture.get());

    jsc.stop();
    System.exit(0);
  }

}

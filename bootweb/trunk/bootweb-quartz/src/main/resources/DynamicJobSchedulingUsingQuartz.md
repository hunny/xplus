# Dynamic Job Scheduling Using Quartz(2.2.1) Scheduler

## Step 1- How to Configure Quartz Scheduler?

The below code helps developers to configure the Quartz Scheduler:

```xml
<bean id="schedulerFactoryBean"
class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
  <property name="applicationContextSchedulerContextKey">
    <value>applicationContext</value>
  </property>
  <property name="configLocation" value="classpath:quartz.properties" />
  <property name="overwriteExistingJobs" value="true" />
</bean>
```

`applicationContextSchedulerContextKey` – This property is used to pass a message to the Quartz Scheduler i.e. how and where to find Spring’s applicationContext. Furthermore, while executing a job, the Quartz Scheduler can access Spring beans using scheduler context. Here’s the code:

```java
ApplicationContext appContext= jobExecutionContext.get(“applicationContextSchedulerContextKey”);
```

`quartz.properties` – This function is used to define all Quartz Scheduler properties; here are few examples of these properties (jobStore, datasource, threadPool, plugin, etc.).

## Step 2 – How to Implement a Quartz Scheduler Job?

In order to implement a Quartz Scheduler Job, developers should make use of the Job class to implement the execute(JobExecutionContext) method of the Quartz Job interface. The Job class throws JobExecutionException, if an error occurs during job execution, JobExecutionContext should be populated with all the required information to run the job, while adding/editing a job. Below is the sample job code, which would help us understanding the concept better:

```java
public class MyJob implements Job{
 @Override
 public void execute(final JobExecutionContext context)throws JobExecutionException {
  JobDetail jobDetail = context.getJobDetail();
  Long reportIdToRun = jobDetail.getKey();
  // do the actual job logic here. May be running a report, sending an
  // email, purging file system directory, etc.
 }
}
```

## Step 3 – How to Add a New Job to Quartz Scheduler?

To add a new job to the Quartz Job Scheduler for dynamic job scheduling, developers have to make use of JobDetail and Trigger components. The JobDetail interface is created using a unique key and a Job class, whereas the Trigger component is created using a unique trigger key. Lastly, schedules are created using TriggerBuilder with start and end dates. Here’s a sample job code that helps us gain a better understanding on the process of adding a new job schedule:

```java
  scheduler = schedulerFactoryBean.getScheduler();
  JobKey jobKey = reportIdToRun; //
  JobDetail jobDetail= newJob(MyJob.class).withIdentity(jobKey).build();
  Trigger trigger= newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(cronExpression)).startAt(startDate).endAt(endDate).build()
  scheduler.scheduleJob(jobDetail, trigger);
```

We have learnt the process of adding a job to Quartz Scheduler in three simple steps. Now, let’s figure out the process of editing, pausing, and deleting existing jobs in Quartz Scheduler.

### How to Edit an Existing Job?

* In order to edit/update an existing job in Quartz Scheduler developers need to:
  - Update the job details
  - Update the trigger details

Here’s the code, which would allow developers to complete this activity:

```java
  scheduler.addJob(updatedJobDetail, true, true); // 2nd parameter true means updating the existing job with the updated one.
  scheduler.rescheduleJob(oldTriggerKey, newTrigger);
```

### How to Pause/Resume an Existing Job?

In order to Pause/Resume an existing job in Quartz Scheduler, developers can use the below code:

```java
  scheduler.pauseJob(jobKey);
  scheduler.resumeJob(jobKey); // the job will be resumed based on the quartz misfire instructions.
```

### How to Delete an Existing Job?

* Deleting an existing job in Quartz Scheduler is a fairly easy task. Below is the code that would allow developers to delete an existing job:
  - Unschedule the job
  - Delete the job

```java
  scheduler.unscheduleJob(jobKey);
  scheduler.deleteJob(jobKey);
```

## Technology Stack

* To demonstrate the functioning of Quartz Scheduler, we have utilized the below tools and technologies:
  - Quartz 2.2.1
  - Spring Framework 3.2.12



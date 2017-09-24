package com.xplus.commons.batch.spring.single;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 6. Job监听。
 */
public class CsvJobListener implements JobExecutionListener {

	private final Logger logger = LoggerFactory.getLogger(CsvJobListener.class);

	private final JdbcTemplate jdbcTemplate;
	private long startTime = 0;
	private long endTime = 0;

	@Autowired
	public CsvJobListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = System.currentTimeMillis();
		logger.info("任务处理准备开始。");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		endTime = System.currentTimeMillis();
		logger.info("任务处理结束，耗时：{}ms，任务状态{}。", (endTime - startTime), jobExecution.getStatus());
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("!!! JOB FINISHED! Time to verify the results");

			List<String> results = jdbcTemplate.query("SELECT name FROM person", new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int row) throws SQLException {
					return rs.getString("name");
				}
			});

			for (String name : results) {
				logger.info("Found <" + name + "> in the database.");
			}

		}
	}

}

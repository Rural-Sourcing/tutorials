package org.rsi.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.rsi.model.Cereal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Job Finished: Verifying Results: ");

			List<Cereal> results = jdbcTemplate.query("SELECT name, num_of_boxes FROM cereal", new RowMapper<Cereal>() {
				public Cereal mapRow(ResultSet rs, int row) throws SQLException {
					return new Cereal(rs.getString(1), rs.getInt(2));
				}
			});

			for (Cereal cereal : results) {
				log.info("Found <" + cereal + "> in the database.");
			}

		}
	}
}

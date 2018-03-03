package org.rsi.config;

import javax.sql.DataSource;

import org.rsi.listener.JobCompletionNotificationListener;
import org.rsi.model.Cereal;
import org.rsi.processor.CerealItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
    
    private static final int BATCH_SIZE = 2;

    @Bean
    public FlatFileItemReader<Cereal> reader() {
        FlatFileItemReader<Cereal> reader = new FlatFileItemReader<Cereal>();
        reader.setResource(new ClassPathResource("sample-cereal.csv"));
        reader.setLineMapper(new DefaultLineMapper<Cereal>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "name", "numOfBoxes" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Cereal>() {{
                setTargetType(Cereal.class);
            }});
        }});
        return reader;
    }

    @Bean
    public CerealItemProcessor processor() {
        return new CerealItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Cereal> writer() {
        JdbcBatchItemWriter<Cereal> writer = new JdbcBatchItemWriter<Cereal>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Cereal>());
        writer.setSql("INSERT INTO cereal (name, num_of_boxes) VALUES (:name, :numOfBoxes)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importCerealJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Cereal, Cereal> chunk(BATCH_SIZE)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}

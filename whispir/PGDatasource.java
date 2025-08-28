@Configuration
@EnableTransactionManagement
public class PgHibernateConfig {

    @Bean(name = "pgDataSource")
    @ConfigurationProperties(prefix = "spring.pgdatasource")
    public DataSource pgDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pgJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("pgDataSource") DataSource pgDataSource) {
        return new JdbcTemplate(pgDataSource);
    }

    @Bean(name = "pgEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean pgEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("pgDataSource") DataSource pgDataSource,
            Environment env) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", env.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.jdbc.lob.non_contextual_creation", true);

        return builder
                .dataSource(pgDataSource)
                .packages("com.aia.pg") // <-- your Postgres entities
                .persistenceUnit("pg-db")
                .properties(properties)
                .build();
    }

    @Bean(name = "pgTransactionManager")
    public PlatformTransactionManager pgTransactionManager(
            @Qualifier("pgEntityManagerFactory") EntityManagerFactory pgEntityManagerFactory) {
        return new JpaTransactionManager(pgEntityManagerFactory);
    }
}

package com.packge.manager.tosam.br.libraryApi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
     String username;
    @Value("${spring.datasource.password}")
     String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;






    @Bean
    DataSource hikariDataSource(){
        HikariConfig config = new HikariConfig();

        config.setUsername(username);//seta o username
        config.setPassword(password);//seta o password
        config.setDriverClassName(driver);//seta o driver
        config.setJdbcUrl(url);//seta a url

        /**
         * OBS: TODOS OS VALORES DE TEMPO SÃO PASSADOS E TEM QUE SER PASSADOS NA MEDIDA DE MILISEGUNDOS*/

        config.setMaximumPoolSize(10);// Indica o maximo de conexões que podem estar ativas na aplicação
        config.setMinimumIdle(1);// Indica o minimo de conexões que a aplicação iara iniciar
        config.setPoolName("libraryapi-pool-db");// O nome da aplicção para aaprecer no console(opcional)
        config.setMaxLifetime(1800000);//Indica o maximo que a conexão pode ficar ativa depois desse tempo ela é derrubada e outra é criada
        config.setConnectionTimeout(120000);// Indica o tempo maximo que a aplicação tentara a conexao , após isso para de tentar e exibe o erro de Timeout
        config.setConnectionTestQuery("select 1");//Faz o teste selecionando a primentira conexao para saber se o banco esta ativo
        return new HikariDataSource(config);
    }



}

package com.example.bootweb.quartz.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import com.example.bootweb.quartz.profile.SpringBootPersistQuartzMybatis;

@Configuration
@MapperScan("com.example.bootweb.quartz.dao")
@SpringBootPersistQuartzMybatis
public class MapperDaoConfig {

}

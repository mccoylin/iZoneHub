package com.iZoneHub.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class IZoneHubApplication extends SpringBootServletInitializer
{

	public static void main(String[] args)
	{
		SpringApplication.run(IZoneHubApplication.class, args);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(IZoneHubApplication.class);
	}
	// 用於打包成 WAR 檔案的配置
	// 在根目錄下執行以下命令來清理並打包
	// ./gradlew clean bootWar

}

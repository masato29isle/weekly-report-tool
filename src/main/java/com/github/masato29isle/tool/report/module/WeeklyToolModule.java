package com.github.masato29isle.tool.report.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.masato29isle.tool.report.service.FileUploadService;
import com.github.masato29isle.tool.report.service.MessageSearchService;
import com.github.masato29isle.tool.report.service.MessageSearchServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.github.masato29isle.tool.report.service.FileUploadServiceImpl;
import okhttp3.OkHttpClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class WeeklyToolModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageSearchService.class).to(MessageSearchServiceImpl.class).in(Singleton.class);
        bind(FileUploadService.class).to(FileUploadServiceImpl.class).in(Singleton.class);
        bind(ObjectMapper.class).asEagerSingleton();
        bind(OkHttpClient.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    private TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".txt");
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}

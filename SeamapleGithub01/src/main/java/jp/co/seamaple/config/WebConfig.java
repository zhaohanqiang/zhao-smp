package jp.co.seamaple.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

// フォームクラスとメッセージの紐づけ
@Configuration
public class WebConfig {

    @Bean
    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();

        // メッセージプロパティファイル名(デフォルト)を指定
        // message.propertiesファイルをセット
        bean.setBasename("classpath:messages");

        // メッセージプロパティの文字コードを指定
        bean.setDefaultEncoding("UTF-8");

        return bean;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());

        return localValidatorFactoryBean;
    }
}

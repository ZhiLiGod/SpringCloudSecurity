package com.security.config;

import com.security.filter.ACLInterceptor;
import com.security.filter.AuditLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class SecurityConfig implements WebMvcConfigurer {

  @Autowired
  private AuditLogInterceptor auditLogInterceptor;

  @Autowired
  private ACLInterceptor aclInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(auditLogInterceptor);
    registry.addInterceptor(aclInterceptor);
  }

  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditorAware<String>() {
      @Override
      public Optional<String> getCurrentAuditor() {

        return Optional.of("Zhi");
      }
    };
  }

}

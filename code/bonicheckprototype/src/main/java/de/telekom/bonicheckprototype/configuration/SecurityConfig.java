package de.telekom.bonicheckprototype.configuration;

import de.telekom.certificate.CertificateForInterface;
import de.telekom.horizon.HorizonForInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Autowired
	CertificateForInterface certificateForInterface;

	@Autowired
	HorizonForInterface horizonForInterface;

	@Bean
	@ConditionalOnExpression("!('${FILE}'.equals('configmap-local-properties'))")
	public SecurityWebFilterChain certOnPostDecision(ServerHttpSecurity serverHttpSecurity){
		return certificateForInterface.createCertificateWebFilter(serverHttpSecurity, "/postdecision/**");
	}

	@Bean
	@ConditionalOnExpression("!('${FILE}'.equals('configmap-local-properties'))")
	public SecurityWebFilterChain certOnGetDecision(ServerHttpSecurity serverHttpSecurity){
		return certificateForInterface.createCertificateWebFilter(serverHttpSecurity, "/getdecision/**");
	}

}

package br.com.residencia.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
            
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.mvcMatchers(HttpMethod.POST, "/sgc/token").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/token/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/morador/**").permitAll()
        		.mvcMatchers(HttpMethod.PUT, "/sgc/morador/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/morador/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/visita/**").permitAll()
        		.mvcMatchers(HttpMethod.PUT, "/sgc/visita/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/visita/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/visitante/**").permitAll()
        		.mvcMatchers(HttpMethod.PUT, "/sgc/visitante/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/visitante/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/veiculo/**").permitAll()
        		.mvcMatchers(HttpMethod.PUT, "/sgc/veiculo/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/veiculo/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/residencia/**").permitAll()
        		.mvcMatchers(HttpMethod.PUT, "/sgc/residencia/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/residencia/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/access/**").permitAll()
        		.mvcMatchers(HttpMethod.PUT, "/sgc/access/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/access/**").permitAll()
        		.mvcMatchers(HttpMethod.POST, "/sgc/contribuicao/**").permitAll()
        		.mvcMatchers(HttpMethod.GET, "/sgc/contribuicao/**").permitAll()
        		.antMatchers(HttpMethod.GET, AUTH_WHITELIST).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                //.oauth2ResourceServer()
                //.jwt();
                //.addFilter(new JWTAuthenticationFilter(authenticationManager(), moradorRepository))
                //.addFilter(new JWTAuthorizationFilter(authenticationManager()))
                //.addFilter(new AuthenticationService(authenticationManager(), moradorRepository))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
    }
    
    private static final String[] AUTH_WHITELIST = {

            // for Swagger UI v2
            "/v2/api-docs",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",

            // for Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name()
        ));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
        return source;
    }
  
}

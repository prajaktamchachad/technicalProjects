package com.luv2code.springboot.cruddemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //this annotation is for security
public class DemoSecurityConfig {
	

	
		//add support for JDBC ... no more hardcoded users
        @Bean
		public UserDetailsManager userDetailsManager(DataSource dataSource)
        {
        	JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        	
        	//USing Custom Tables name
        	
        	//define a query to retrive a user by username
        	jdbcUserDetailsManager.setUsersByUsernameQuery("Select user_id , pw, active from members where user_id = ?");
        	
        	//define a query to retrive the authorities/roles by username 
        	jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("Select user_id, role from roles where user_id = ?");
        	
        return jdbcUserDetailsManager;
        }
	
	
	//Restricting Access BAsed on roles
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(configurer ->
		configurer
		.requestMatchers(HttpMethod.GET,"/api/employees").hasRole("EMPLOYEE")
		.requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
        .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")
		.requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT,"/api/employees").hasRole("MANAGER")
);

		//Use http basic authentication
		http.httpBasic(Customizer.withDefaults());
   
		//disable Cross Site Request Forgery (CSRF)
		//in general, not required for stateless REST APIs that use POST, PUT, DELETE, and/or PATCH
	    http.csrf(csrf -> csrf.disable());
	    
	    return http.build();
	}
}


/*
 * public InMemoryUserDetailsManager userDetailsManager()
	{
 * 		UserDetails john = User.builder()
.username("john")
.password("{noop}test123")
.roles("EMPLOYEE")
.build();

UserDetails mary = User.builder()
 .username("mary")
 .password("{noop}test123")
 .roles("EMPLOYEE","MANAGER")
 .build();

UserDetails prajakta = User.builder()
		 .username("prajakta")
		 .password("{noop}test123")
		 .roles("EMPLOYEE","MANAGER","ADMIN")
		 .build();

return new InMemoryUserDetailsManager(john, mary, prajakta);
*/		
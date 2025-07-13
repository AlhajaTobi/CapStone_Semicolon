//package com.antiTheftTracker.antiTheftTrackerApp.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//            .cors() // enable CORS
//            .and()
//            .csrf().disable() // disable CSRF for testing/local dev
//            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//
//    return http.build();
//}
//

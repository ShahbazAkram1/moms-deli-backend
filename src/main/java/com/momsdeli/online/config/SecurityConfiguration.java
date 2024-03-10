package com.momsdeli.online.config;

import com.momsdeli.online.repository.UserRepository;
import com.momsdeli.online.utils.Constants;
import com.momsdeli.online.utils.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtHelper jwtHelper;

    @Qualifier("userDetailsServiceImpl")
    private final UserDetailsService userDetailService;

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/auth/login", "/auth/register").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
//                        .anyRequest().authenticated()
                                .requestMatchers("/auth/*").permitAll()
                                .requestMatchers("/customer/*").hasRole("CUSTOMER")
                                .requestMatchers("/admin/*").hasRole("ADMIN")
                                .anyRequest().anonymous()
                )
                .logout(LogoutConfigurer::permitAll);

        UserDetails adminAndCustomer = User.withUsername("admincustomer@gmail.com")
                .password(new BCryptPasswordEncoder().encode("admincustomer"))
                .roles("ADMIN", "CUSTOMER")
                .build();

        // Use an InMemoryUserDetailsManager to store the dummy user
        return http.userDetailsService(new InMemoryUserDetailsManager(adminAndCustomer)).build();

    }
    // Dummy user with admin and customer roles




}
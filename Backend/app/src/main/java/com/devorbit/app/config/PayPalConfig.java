package com.devorbit.app.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        String clientId = "AbwzKZqWit84Kvq3mquF5oBL6kakyNz7s_Dih-oVXrN4AqSYQoWKPSZsaE8Eas0GmgCLqLv_TD7jx8w7";
        String clientSecret = "EMd0lSZgoqqXIUbUIQ0zIWwSCA37BYwabn5tadk7KufS8GvTxFOjgOo49vb2xliZqEOLzYm1RWKq-rMR";
        return new APIContext(clientId, clientSecret, "sandbox");
    }
}
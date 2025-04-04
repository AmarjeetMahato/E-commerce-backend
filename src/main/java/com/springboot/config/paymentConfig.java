package com.springboot.config;

import com.springboot.service.payment.CreditCardServiceImpl;
import com.springboot.service.payment.GpayServiceImpl;
import com.springboot.service.payment.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class paymentConfig {

      @Bean
      public PaymentService creditCardPaymentService(){
          return new CreditCardServiceImpl();
      }

      @Bean
      public PaymentService gpayPaymentService(){
          return  new GpayServiceImpl();
      }

}

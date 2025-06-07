package se.sowl.couponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import se.sowl.couponcore.CouponCoreConfiguration;

@Import(CouponCoreConfiguration.class)
@SpringBootApplication
public class CouponApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponApiApplication.class, args);
    }

}

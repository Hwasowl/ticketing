package se.sowl.ticketdomain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TicketDomainApplication {
    public static void main(String[] args) {
        System.out.println("Ticket Domain Application is running...");
    }
}

package dev.fwegrzyn.charity_fund_manager;

import dev.fwegrzyn.charity_fund_manager.model.FundraisingEvent;
import dev.fwegrzyn.charity_fund_manager.repository.FundraisingEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class CharityFundManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(CharityFundManagerApplication.class, args);

	}

	@Bean
	CommandLineRunner commandLineRunner(FundraisingEventRepository repository) {
		return args ->{
			repository.save(new FundraisingEvent(null, "Siepomaga", "PLN", BigDecimal.valueOf(1200.00)));
		};
	}
}

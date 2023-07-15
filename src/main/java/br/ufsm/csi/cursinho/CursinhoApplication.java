package br.ufsm.csi.cursinho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class CursinhoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursinhoApplication.class, args);
	}
}

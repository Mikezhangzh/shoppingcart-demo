package mikezhang.demo.shoppingcart;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Starting ...");
		run(DemoApplication.class, args);
	}

}

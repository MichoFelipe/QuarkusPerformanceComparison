package org.acme.factorialwebflux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Service
public class FactorialClientService {

	Logger LOGGER = LoggerFactory.getLogger(FactorialClientService.class);
	
	public Flux<Factorial> getFactorials() {
		var idProcess = UUID.randomUUID();
    	
		WebClient cliente = WebClient.create("http://localhost:8080/factorialExternalApi");
		int NUMBER_EXTERNAL_APIS = 3;
    	List<Flux<Factorial>> results = new ArrayList<>();
    	for(int i=1;i<=NUMBER_EXTERNAL_APIS;i++) {
    		int valueToCalculate = getRandomNumber(2,10);
    		LOGGER.info("getFactorials, START idProcess [{}] with valueToCalculate  [{}]", idProcess, valueToCalculate);
    		results.add(cliente.get().uri("/calculate/"+valueToCalculate+"/uuid/"+idProcess).retrieve().bodyToFlux(Factorial.class));
    	}
	    Flux<Factorial> webFluxResult=Flux.merge(results);
	    LOGGER.info("getFactorials, END idProcess [{}]", idProcess);
	    return webFluxResult;
	}
	
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

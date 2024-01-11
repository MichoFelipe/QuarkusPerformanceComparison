package org.acme.factorialwebflux;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/factorialWebFlux")
public class FactorialClientController {

	@Autowired
	FactorialClientService servicio;

	@RequestMapping("/list")
	public List<Factorial> getFactorialByWebFlux() {
		List<Factorial> lista = servicio.getFactorials().collectList().block();
		return lista;
	}
}
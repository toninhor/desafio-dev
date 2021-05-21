package br.com.jpinto.cadastroUsuariosApi.resource;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoResource {

	private String nomeApi = "Cadastro de Usuários API";

	private String versaoApi = "1.0.0";

	private Map<String, Object> info;

	@PostConstruct
	private void init() {
        info = new HashMap<>();
        info.put("nome", nomeApi);
        info.put("versao", versaoApi);
    }

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getInfo() {
		return info;
	}
}

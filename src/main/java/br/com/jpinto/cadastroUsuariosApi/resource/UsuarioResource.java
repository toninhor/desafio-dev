package br.com.jpinto.cadastroUsuariosApi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.jpinto.cadastroUsuariosApi.entity.Usuario;
import br.com.jpinto.cadastroUsuariosApi.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping
	public List<Usuario> listar(){
		return this.usuarioRepository.findAll();
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Usuario> carrega(@PathVariable("id") final Long id){
		try {
			return ResponseEntity.ok(this.usuarioRepository.findById(id)
					.orElseThrow(()->new Exception("Usuário não encontrado")));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(null);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario adicionar(@RequestBody Usuario usuario){
		return  usuarioRepository.save(usuario);
	}
}

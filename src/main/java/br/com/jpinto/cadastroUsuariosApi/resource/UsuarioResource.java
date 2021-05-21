package br.com.jpinto.cadastroUsuariosApi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jpinto.cadastroUsuariosApi.entity.Usuario;
import br.com.jpinto.cadastroUsuariosApi.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuario;

	@GetMapping
	public List<Usuario> listar(){
		return this.usuario.findAll();
	}
}

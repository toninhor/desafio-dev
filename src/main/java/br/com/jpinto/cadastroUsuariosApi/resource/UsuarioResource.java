package br.com.jpinto.cadastroUsuariosApi.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.jpinto.cadastroUsuariosApi.entity.Usuario;
import br.com.jpinto.cadastroUsuariosApi.repository.UsuarioRepository;

/**
*
* @author José Antonio Pinto
*
* Consulta, inclusão, alteração e exclusão básicas.
*/
@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

	final String USUARIO_NAO_ENCONTRADO_MESSAGE = "Usuário não encontrado";

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Carrega a lista de usuários
	 */
	@GetMapping
	public ResponseEntity<List<Usuario>> listar(){
		try {
			List<Usuario> usuarios = this.usuarioRepository.findAll();
			if(usuarios.isEmpty()){
				throw new Exception(USUARIO_NAO_ENCONTRADO_MESSAGE);
			}
			return ResponseEntity.ok(usuarios);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(null);
		}
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Usuario> carrega(@PathVariable("id") final Long id){
		return carregaUsuario(id);
	}

	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Método para centralizar todas as buscas por usuário
	 */
	private ResponseEntity<Usuario> carregaUsuario(final Long id){
		try {
			return ResponseEntity.ok(this.usuarioRepository.findById(id)
					.orElseThrow(()->new Exception(USUARIO_NAO_ENCONTRADO_MESSAGE)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(null);
		}
	}

	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Como estamos usando um banco de dados em memória, não é necessáro
	 * anotar a classe com o nome da tabela e a mesma é criada automaticamente
	 * quando a aplicação é iniciada.
	 *
	 * @returns Usuario
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario adicionar(@Validated @RequestBody Usuario usuario){
		return  usuarioRepository.save(usuario);
	}

	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Atualiza usuário
	 *
	 * @returns Usuario
	 */
	@PutMapping(path = "/{id}")
	public ResponseEntity<Usuario> atualizar(@Validated @RequestBody Usuario usuario,
			@PathVariable("id") Long id) {
		//return  usuarioRepository.save(usuarioCarregado);

		ResponseEntity<Usuario> usuarioCarregado = carregaUsuario(id);
		if(usuarioCarregado.getStatusCode() == HttpStatus.OK){
			usuarioCarregado.getBody().setCpf(usuario.getCpf());
			usuarioCarregado.getBody()
				.setDataDeNascimento(usuario.getDataDeNascimento());
			usuarioCarregado.getBody().setEmail(usuario.getEmail());
			usuarioCarregado.getBody()
				.setNacionalidade(usuario.getNacionalidade());
			usuarioCarregado.getBody()
				.setNaturalidade(usuario.getNaturalidade());
			usuarioCarregado.getBody().setNome(usuario.getNome());
			usuarioCarregado.getBody().setSexo(usuario.getSexo());

			this.usuarioRepository.save(usuarioCarregado.getBody());
			return ResponseEntity.ok(usuarioCarregado.getBody());
		}
		return ResponseEntity.status(usuarioCarregado.getStatusCode()).build();
	}


	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Exclusão de usuário
	 *
	 * @returns HttpStatus
	 */
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> exclui(@PathVariable("id") final Long id){

		ResponseEntity<Usuario> usuario = carregaUsuario(id);
		if(usuario.getStatusCode() == HttpStatus.OK){
			this.usuarioRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.GONE).build();
		}
		return ResponseEntity.status(usuario.getStatusCode()).build();
	}
}


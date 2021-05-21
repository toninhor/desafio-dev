package br.com.jpinto.cadastroUsuariosApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jpinto.cadastroUsuariosApi.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}

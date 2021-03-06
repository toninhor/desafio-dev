package br.com.jpinto.cadastroUsuariosApi.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

/**
 *
 * @author José Antonio Pinto
 *
 * Como estamos usando um banco de dados em memória, não é necessáro
 * anotar a classe com o nome da tabela e a mesma é criada automaticamente
 * quando a aplicação é iniciada.
 */
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	private String sexo;

	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Quando houver um problema de formatação, será gerado um erro 400.
	 */
	@Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
	private String email;

	@Column(nullable = false)
	private LocalDate dataDeNascimento;

	private String naturalidade;

	private String nacionalidade;

	/**
	 *
	 * @author José Antonio Pinto
	 *
	 * Na documentação, a validação é definida com as seguintes características
	 *
	 * CPF - obrigatório, deve ser validado (formato e não pode haver dois
	 * cadastros com mesmo cpf)
	 *
	 * Quando houver a tentativa de duplicidade de cpf, será gerado um erro 500
	 * relacionado a contraint.
	 *
	 * Quando houver um problema de formatação, será gerado um erro 400.
	 */
	@Validate()
	@Column(nullable = false, unique = true, length = 11,
	columnDefinition = "CHAR(11)")
	@Pattern(regexp = "^[0-9]{1,11}$")
	private String cpf;

	//---------------------------------------------

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(LocalDate dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

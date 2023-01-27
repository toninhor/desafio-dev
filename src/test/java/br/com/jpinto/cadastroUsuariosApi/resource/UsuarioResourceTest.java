package br.com.jpinto.cadastroUsuariosApi.resource;

import static br.com.jpinto.cadastroUsuariosApi.util.TestUtils.buildURL;
import static br.com.jpinto.cadastroUsuariosApi.util.TestUtils.jsonFromFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.jpinto.cadastroUsuariosApi.CadastroUsuariosApiApplication;


@SpringBootTest(classes = CadastroUsuariosApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.cloud.consul.config.enabled=false")
@DisplayName("Testes dos endpoints do resource Usuario")
@Sql("/sql/clean_up.sql")
public class UsuarioResourceTest {

		private static final String ENDPOINT_COLLECTION = "/usuario/";
		private static final String ENDPOINT_DOCUMENT = ENDPOINT_COLLECTION+"{id}";

		@LocalServerPort
		private int port;

		@Autowired
		private TestRestTemplate restTemplate;


		@Test
		@Sql("/sql/usuario/load_data.sql")
		@Sql(scripts = "/sql/clean_up.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@DisplayName("Deve retornar uma lista de usuarios | HTTP 200 ou 206")
		public void listarTest() {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			final HttpEntity<String> entity = new HttpEntity<String>(null, headers);

			final ResponseEntity<String> response = restTemplate.exchange(buildURL(port, ENDPOINT_COLLECTION), HttpMethod.GET, entity, String.class);

			assertThat(response.getStatusCode(), anyOf(is(HttpStatus.OK), is(HttpStatus.PARTIAL_CONTENT)));
			assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		}

		@Test
		@Sql("/sql/usuario/create.sql")
		@Sql(scripts = "/sql/clean_up.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@DisplayName("Deve retornar a representação de um usuario | HTTP 200")
		public void consultarTest() throws Exception {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			final HttpEntity<String> entity = new HttpEntity<String>(null, headers);

			final Map<String, String> param = new HashMap<String, String>();
			param.put("id", "1");

			final ResponseEntity<String> response = restTemplate.exchange(buildURL(port, ENDPOINT_DOCUMENT), HttpMethod.GET, entity, String.class, param);

			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		}

		@Test
		@DisplayName("Não deve retornar nenhum usuario | HTTP 404")
		public void consultarNotFoundTest() throws Exception {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			final HttpEntity<String> entity = new HttpEntity<String>(null, headers);

			final Map<String, String> param = new HashMap<String, String>();
			param.put("id", "1");

			final ResponseEntity<String> response = restTemplate.exchange(buildURL(port, ENDPOINT_DOCUMENT), HttpMethod.GET, entity, String.class, param);

			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		}

		@Test
		@Sql("/sql/autor/create.sql")
		@Sql(scripts = "/sql/clean_up.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@DisplayName("Deve criar um novo usuario | HTTP 201")
		public void incluirTest() throws Exception {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			final String json = jsonFromFile("classpath:json/usuario/post-payload-valido.json");

			final HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			final ResponseEntity<String> response = restTemplate.postForEntity(buildURL(port, ENDPOINT_COLLECTION), entity, String.class);

			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

			assertThat(response.getHeaders(), hasKey("Location"));

			ResponseEntity<String> responseGet = restTemplate.exchange(response.getHeaders().getLocation(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

			assertEquals(HttpStatus.OK, responseGet.getStatusCode());
		}

		@Test
		@DisplayName("Deve rejeitar um payload inválido na criação de um novo usuario | HTTP 422")
		public void incluirInvalidoTest() throws Exception {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			final String json = jsonFromFile("classpath:json/usuario/post-payload-invalido.json");

			final HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			final ResponseEntity<String> response = restTemplate.postForEntity(buildURL(port, ENDPOINT_COLLECTION), entity, String.class);

			assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
		}

		@Test
		@Sql("/sql/usuario/create.sql")
		@Sql(scripts = "/sql/clean_up.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@DisplayName("Deve rejeitar um payload de um usuario com um cpf já existente | HTTP 422")
		public void incluirNomeDuplicadoTest() throws Exception {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			final String json = jsonFromFile("classpath:json/usuario/post-payload-valido.json");

			final HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			final ResponseEntity<String> response = restTemplate.postForEntity(buildURL(port, ENDPOINT_COLLECTION), entity, String.class);

			assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
		}


		@Test
		@Sql("/sql/usuario/create.sql")
		@Sql(scripts = "/sql/clean_up.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		@DisplayName("Deve excluir um usuario | HTTP 204")
		public void excluirTest() throws Exception {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			final Map<String, String> param = new HashMap<String, String>();
			param.put("id", "1");

			final ResponseEntity<String> response = restTemplate.exchange(buildURL(port, ENDPOINT_DOCUMENT), HttpMethod.DELETE, HttpEntity.EMPTY, String.class, param);

			assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

			ResponseEntity<String> responseGet = restTemplate.exchange(buildURL(port, ENDPOINT_DOCUMENT), HttpMethod.GET, new HttpEntity<>(headers), String.class, param);

			assertEquals(HttpStatus.NOT_FOUND, responseGet.getStatusCode());
		}
}

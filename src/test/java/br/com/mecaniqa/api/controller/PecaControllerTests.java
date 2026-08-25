package br.com.mecaniqa.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.mecaniqa.api.exception.GlobalExceptionHandler;
import br.com.mecaniqa.api.model.CategoriaPeca;
import br.com.mecaniqa.api.model.Peca;
import br.com.mecaniqa.api.repository.PecaRepository;

class PecaControllerTests {

	private final PecaRepository repository = PecaRepository.getInstance();
	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		limparRepository();
		mockMvc = MockMvcBuilders
				.standaloneSetup(new PecaController())
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@AfterEach
	void limpar() {
		limparRepository();
	}

	@Test
	void deveCadastrarPecaValida() throws Exception {
		mockMvc.perform(post("/api/pecas")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "codigoBarras": "7890000000001",
						  "fornecedorMarca": "Marca Teste",
						  "quantidadeEstoque": 10,
						  "precoCusto": 80.00,
						  "precoVenda": 120.00,
						  "categoria": "MOTOR"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("/api/pecas/\\d+")))
				.andExpect(jsonPath("$.codigo").isNumber())
				.andExpect(jsonPath("$.codigoBarras").value("7890000000001"))
				.andExpect(jsonPath("$.categoria").value("MOTOR"))
				.andExpect(jsonPath("$.dataCadastro").exists())
				.andExpect(jsonPath("$.dataUltimaAtualizacao").exists());
	}

	@Test
	void deveAceitarCamposOpcionaisAusentesEValoresLimite() throws Exception {
		mockMvc.perform(post("/api/pecas")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "codigoBarras": "7890000000002",
						  "fornecedorMarca": "Marca Limite",
						  "quantidadeEstoque": 0,
						  "precoCusto": 0,
						  "precoVenda": 0,
						  "categoria": "ACESSORIOS"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.quantidadeEstoque").value(0))
				.andExpect(jsonPath("$.precoCusto").value(0))
				.andExpect(jsonPath("$.precoVenda").value(0));
	}

	@Test
	void deveRejeitarPecaComDadosInvalidos() throws Exception {
		mockMvc.perform(post("/api/pecas")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "codigoBarras": "",
						  "fornecedorMarca": "",
						  "quantidadeEstoque": -1,
						  "precoCusto": -0.01,
						  "precoVenda": -1
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.campos.codigoBarras").exists())
				.andExpect(jsonPath("$.campos.fornecedorMarca").exists())
				.andExpect(jsonPath("$.campos.quantidadeEstoque").exists())
				.andExpect(jsonPath("$.campos.precoCusto").exists())
				.andExpect(jsonPath("$.campos.precoVenda").exists())
				.andExpect(jsonPath("$.campos.categoria").exists());
	}

	@Test
	void deveListarBuscarAtualizarEExcluirPeca() throws Exception {
		Peca peca = repository.salvar(novaPeca());
		Long codigo = peca.getCodigo();

		mockMvc.perform(get("/api/pecas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].codigo").value(codigo));

		mockMvc.perform(get("/api/pecas/{codigo}", codigo))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.codigo").value(codigo));

		mockMvc.perform(put("/api/pecas/{codigo}", codigo)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "quantidadeEstoque": 20,
						  "precoCusto": 95.00,
						  "precoVenda": 150.00
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.quantidadeEstoque").value(20))
				.andExpect(jsonPath("$.precoVenda").value(150.0));

		mockMvc.perform(delete("/api/pecas/{codigo}", codigo))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));
	}

	@Test
	void deveListarCatalogoVazio() throws Exception {
		mockMvc.perform(get("/api/pecas"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void deveRejeitarAtualizacaoInvalidaSemAlterarAPeca() throws Exception {
		Peca peca = repository.salvar(novaPeca());

		mockMvc.perform(put("/api/pecas/{codigo}", peca.getCodigo())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "quantidadeEstoque": -1,
						  "precoCusto": -1,
						  "precoVenda": -1
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.quantidadeEstoque").exists())
				.andExpect(jsonPath("$.campos.precoCusto").exists())
				.andExpect(jsonPath("$.campos.precoVenda").exists());

		Peca preservada = repository.buscarPorCodigo(peca.getCodigo()).orElseThrow();
		org.junit.jupiter.api.Assertions.assertEquals(10, preservada.getQuantidadeEstoque());
		org.junit.jupiter.api.Assertions.assertEquals(new BigDecimal("80.00"), preservada.getPrecoCusto());
	}

	@Test
	void deveRetornar404AoAtualizarPecaInexistente() throws Exception {
		mockMvc.perform(put("/api/pecas/{codigo}", 999999)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "quantidadeEstoque": 1,
						  "precoCusto": 1,
						  "precoVenda": 1
						}
						"""))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Peça não encontrada"));
	}

	@Test
	void deveRetornarErrosControladosParaPecaInexistenteEParametrosInvalidos() throws Exception {
		mockMvc.perform(get("/api/pecas/{codigo}", 999999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Peça não encontrada"));

		mockMvc.perform(delete("/api/pecas/{codigo}", 999999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));

		mockMvc.perform(get("/api/pecas/invalido"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.codigo").exists());

		mockMvc.perform(post("/api/pecas")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "codigoBarras": "7890000000001",
						  "fornecedorMarca": "Marca Teste",
						  "quantidadeEstoque": 10,
						  "precoCusto": 80,
						  "precoVenda": 120,
						  "categoria": "INVALIDA"
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Corpo da requisição inválido"));
	}

	@Test
	void deveRejeitarCorpoAusenteOuJsonMalformado() throws Exception {
		mockMvc.perform(post("/api/pecas")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Corpo da requisição inválido"));

		mockMvc.perform(post("/api/pecas")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"codigoBarras\":"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	private Peca novaPeca() {
		return new Peca(
				"7890000000001",
				"Marca Teste",
				10,
				new BigDecimal("80.00"),
				new BigDecimal("120.00"),
				null,
				null,
				CategoriaPeca.MOTOR);
	}

	private void limparRepository() {
		repository.listarTodos().forEach(peca -> repository.excluir(peca.getCodigo()));
	}
}

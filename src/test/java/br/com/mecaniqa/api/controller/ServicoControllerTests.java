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
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.repository.ServicoRepository;

class ServicoControllerTests {

	private final ServicoRepository repository = ServicoRepository.getInstance();
	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		limparRepository();
		mockMvc = MockMvcBuilders
				.standaloneSetup(new ServicoController())
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@AfterEach
	void limpar() {
		limparRepository();
	}

	@Test
	void deveCadastrarServicoValido() throws Exception {
		mockMvc.perform(post("/api/servicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "nome": "Alinhamento",
						  "duracaoEstimadaMinutos": 45,
						  "custoTabelado": 100.00
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("/api/servicos/\\d+")))
				.andExpect(jsonPath("$.codigo").isNumber())
				.andExpect(jsonPath("$.nome").value("Alinhamento"))
				.andExpect(jsonPath("$.dataCriacao").exists())
				.andExpect(jsonPath("$.dataUltimaAtualizacao").exists());
	}

	@Test
	void deveAceitarDuracaoMinimaECustoZero() throws Exception {
		mockMvc.perform(post("/api/servicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "nome": "Avaliação rápida",
						  "duracaoEstimadaMinutos": 1,
						  "custoTabelado": 0
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.duracaoEstimadaMinutos").value(1))
				.andExpect(jsonPath("$.custoTabelado").value(0));
	}

	@Test
	void deveRejeitarServicoComDadosInvalidos() throws Exception {
		mockMvc.perform(post("/api/servicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "nome": "",
						  "duracaoEstimadaMinutos": 0,
						  "custoTabelado": -1
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.nome").exists())
				.andExpect(jsonPath("$.campos.duracaoEstimadaMinutos").exists())
				.andExpect(jsonPath("$.campos.custoTabelado").exists());
	}

	@Test
	void deveListarBuscarAtualizarEExcluirServico() throws Exception {
		Servico servico = repository.salvar(novoServico());
		Long codigo = servico.getCodigo();

		mockMvc.perform(get("/api/servicos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].codigo").value(codigo));

		mockMvc.perform(get("/api/servicos/{codigo}", codigo))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.codigo").value(codigo));

		mockMvc.perform(put("/api/servicos/{codigo}", codigo)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "duracaoEstimadaMinutos": 60,
						  "custoTabelado": 130.00
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.duracaoEstimadaMinutos").value(60))
				.andExpect(jsonPath("$.custoTabelado").value(130.0));

		mockMvc.perform(delete("/api/servicos/{codigo}", codigo))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));
	}

	@Test
	void deveListarCatalogoVazio() throws Exception {
		mockMvc.perform(get("/api/servicos"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void deveRejeitarAtualizacaoInvalidaSemAlterarOServico() throws Exception {
		Servico servico = repository.salvar(novoServico());

		mockMvc.perform(put("/api/servicos/{codigo}", servico.getCodigo())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "duracaoEstimadaMinutos": 0,
						  "custoTabelado": -1
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.duracaoEstimadaMinutos").exists())
				.andExpect(jsonPath("$.campos.custoTabelado").exists());

		Servico preservado = repository.buscarPorCodigo(servico.getCodigo()).orElseThrow();
		org.junit.jupiter.api.Assertions.assertEquals(45, preservado.getDuracaoEstimadaMinutos());
		org.junit.jupiter.api.Assertions.assertEquals(new BigDecimal("100.00"), preservado.getCustoTabelado());
	}

	@Test
	void deveRetornarErrosControladosParaServicoInexistente() throws Exception {
		mockMvc.perform(get("/api/servicos/{codigo}", 999999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Serviço não encontrado"));

		mockMvc.perform(put("/api/servicos/{codigo}", 999999)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "duracaoEstimadaMinutos": 60,
						  "custoTabelado": 130.00
						}
						"""))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));

		mockMvc.perform(delete("/api/servicos/{codigo}", 999999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Serviço não encontrado"));

		mockMvc.perform(get("/api/servicos/invalido"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.codigo").exists());
	}

	@Test
	void deveRejeitarCorpoAusenteETipoIncorreto() throws Exception {
		mockMvc.perform(post("/api/servicos")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Corpo da requisição inválido"));

		mockMvc.perform(post("/api/servicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "nome": "Alinhamento",
						  "duracaoEstimadaMinutos": "quarenta",
						  "custoTabelado": 100
						}
						"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	private Servico novoServico() {
		return new Servico("Alinhamento", 45, new BigDecimal("100.00"));
	}

	private void limparRepository() {
		repository.listarTodos().forEach(servico -> repository.excluir(servico.getCodigo()));
	}
}

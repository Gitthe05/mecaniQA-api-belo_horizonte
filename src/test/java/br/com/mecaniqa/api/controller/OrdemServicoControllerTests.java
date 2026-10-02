package br.com.mecaniqa.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import br.com.mecaniqa.api.model.Servico;
import br.com.mecaniqa.api.repository.OrdemServicoRepository;
import br.com.mecaniqa.api.repository.PecaRepository;
import br.com.mecaniqa.api.repository.ServicoRepository;
import br.com.mecaniqa.api.service.OrdemDeServicoService;

class OrdemServicoControllerTests {
	private final OrdemServicoRepository repository = OrdemServicoRepository.getInstance();
	private final PecaRepository pecas = PecaRepository.getInstance();
	private final ServicoRepository servicos = ServicoRepository.getInstance();
	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		limpar();
		mockMvc = MockMvcBuilders.standaloneSetup(new OrdemServicoController(new OrdemDeServicoService()))
				.setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@AfterEach
	void limpar() {
		repository.listarTodos().forEach(o -> repository.excluir(o.getCodigo()));
		pecas.listarTodos().forEach(p -> pecas.excluir(p.getCodigo()));
		servicos.listarTodos().forEach(s -> servicos.excluir(s.getCodigo()));
	}

	@Test
	void deveCriarConsultarEAtualizarStatusDaOrdemComBuilderEDtos() throws Exception {
		Peca peca = pecas.salvar(new Peca("789", "Bosch", 2, BigDecimal.TEN,
				new BigDecimal("15"), null, null, CategoriaPeca.FREIOS));
		Servico servico = servicos.salvar(new Servico("Troca", 30, new BigDecimal("80")));

		String localizacao = mockMvc.perform(post("/api/ordens-servico").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"cliente":"Ana","veiculo":"ABC-1234","descricao":"Troca de pastilhas",
						 "codigosPecas":[%d],"codigosServicos":[%d]}
						""".formatted(peca.getCodigo(), servico.getCodigo())))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("ABERTO"))
				.andExpect(jsonPath("$.pecas[0].codigo").value(peca.getCodigo()))
				.andExpect(jsonPath("$.servicos[0].codigo").value(servico.getCodigo()))
				.andReturn().getResponse().getHeader("Location");

		long codigo = Long.parseLong(localizacao.substring(localizacao.lastIndexOf('/') + 1));
		mockMvc.perform(get("/api/ordens-servico/{codigo}", codigo)).andExpect(status().isOk());
		mockMvc.perform(patch("/api/ordens-servico/{codigo}/status", codigo)
				.contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"EM_EXECUCAO\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status").value("EM_EXECUCAO"));
	}

	@Test
	void deveValidarDadosEReferencias() throws Exception {
		mockMvc.perform(post("/api/ordens-servico").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cliente\":\"\",\"veiculo\":\"\",\"descricao\":\"\"}"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.cliente").exists());
		mockMvc.perform(post("/api/ordens-servico").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cliente\":\"Ana\",\"veiculo\":\"ABC\",\"descricao\":\"Reparo\",\"codigosPecas\":[999]}"))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Peça não encontrada: 999"));
	}
}

package br.com.mecaniqa.api.controller;

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
import br.com.mecaniqa.api.repository.PecaRepository;
import br.com.mecaniqa.api.repository.PedidoPecasRepository;
import br.com.mecaniqa.api.service.PedidoPecasService;

class PedidoPecasControllerTests {
	private final PedidoPecasRepository repository = PedidoPecasRepository.getInstance();
	private final PecaRepository pecas = PecaRepository.getInstance();
	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		limpar();
		mockMvc = MockMvcBuilders.standaloneSetup(new PedidoPecasController(new PedidoPecasService()))
				.setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@AfterEach
	void limpar() {
		repository.listarTodos().forEach(p -> repository.excluir(p.getCodigo()));
		pecas.listarTodos().forEach(p -> pecas.excluir(p.getCodigo()));
	}

	@Test
	void deveCriarAdicionarPecasSomarQuantidadeEAtualizarStatus() throws Exception {
		Peca peca = pecas.salvar(new Peca("123", "NGK", 0, BigDecimal.TEN,
				new BigDecimal("20"), null, null, CategoriaPeca.MOTOR));
		String localizacao = mockMvc.perform(post("/api/pedidos-pecas").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fornecedor\":\"Distribuidora X\",\"itens\":[{\"codigoPeca\":"
						+ peca.getCodigo() + ",\"quantidade\":2}]}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("ORCANDO"))
				.andExpect(jsonPath("$.itens[0].quantidade").value(2))
				.andReturn().getResponse().getHeader("Location");
		long codigo = Long.parseLong(localizacao.substring(localizacao.lastIndexOf('/') + 1));

		mockMvc.perform(post("/api/pedidos-pecas/{codigo}/itens", codigo)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"codigoPeca\":" + peca.getCodigo() + ",\"quantidade\":3}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.itens[0].quantidade").value(5));
		mockMvc.perform(patch("/api/pedidos-pecas/{codigo}/status", codigo)
				.contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"PAGO_FATURADO\"}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PAGO_FATURADO"));
	}

	@Test
	void deveRejeitarQuantidadeInvalidaEPecaInexistente() throws Exception {
		mockMvc.perform(post("/api/pedidos-pecas").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fornecedor\":\"X\",\"itens\":[{\"codigoPeca\":1,\"quantidade\":0}]}"))
				.andExpect(status().isBadRequest());
		mockMvc.perform(post("/api/pedidos-pecas").contentType(MediaType.APPLICATION_JSON)
				.content("{\"fornecedor\":\"X\",\"itens\":[{\"codigoPeca\":999,\"quantidade\":1}]}"))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Peça não encontrada: 999"));
	}
}

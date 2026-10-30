-- Banco oficial da aplicação: MySQL 8.0+
CREATE DATABASE IF NOT EXISTS mecaniqa
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE mecaniqa;

CREATE TABLE IF NOT EXISTS pecas (
  codigo BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  codigo_barras VARCHAR(64) NOT NULL UNIQUE,
  fornecedor_marca VARCHAR(160) NOT NULL,
  quantidade_estoque INT NOT NULL DEFAULT 0,
  preco_custo DECIMAL(12,2) NOT NULL,
  preco_venda DECIMAL(12,2) NOT NULL,
  data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_ultima_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  tamanho VARCHAR(40),
  cor VARCHAR(60),
  categoria VARCHAR(30) NOT NULL,
  CONSTRAINT chk_pecas_estoque CHECK (quantidade_estoque >= 0),
  CONSTRAINT chk_pecas_categoria CHECK (categoria IN ('MOTOR','SUSPENSAO','FREIOS','ELETRICA','ACESSORIOS'))
);

CREATE TABLE IF NOT EXISTS servicos (
  codigo BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(160) NOT NULL,
  duracao_estimada_minutos INT NOT NULL,
  custo_tabelado DECIMAL(12,2) NOT NULL,
  data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_ultima_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_servicos_duracao CHECK (duracao_estimada_minutos > 0),
  CONSTRAINT chk_servicos_custo CHECK (custo_tabelado >= 0)
);

CREATE TABLE IF NOT EXISTS ordens_servico (
  codigo BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  cliente VARCHAR(160) NOT NULL,
  veiculo VARCHAR(160) NOT NULL,
  descricao TEXT NOT NULL,
  status VARCHAR(40) NOT NULL DEFAULT 'ABERTO',
  data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_ultima_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_os_status CHECK (status IN ('ABERTO','PENDENTE_DE_PAGAMENTO','PAGO','EM_EXECUCAO','EXECUTADO'))
);

CREATE TABLE IF NOT EXISTS ordem_servico_pecas (
  ordem_codigo BIGINT NOT NULL,
  peca_codigo BIGINT NOT NULL,
  PRIMARY KEY (ordem_codigo, peca_codigo),
  FOREIGN KEY (ordem_codigo) REFERENCES ordens_servico(codigo) ON DELETE CASCADE,
  FOREIGN KEY (peca_codigo) REFERENCES pecas(codigo)
);

CREATE TABLE IF NOT EXISTS ordem_servico_servicos (
  ordem_codigo BIGINT NOT NULL,
  servico_codigo BIGINT NOT NULL,
  PRIMARY KEY (ordem_codigo, servico_codigo),
  FOREIGN KEY (ordem_codigo) REFERENCES ordens_servico(codigo) ON DELETE CASCADE,
  FOREIGN KEY (servico_codigo) REFERENCES servicos(codigo)
);

CREATE TABLE IF NOT EXISTS pedidos_pecas (
  codigo BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  fornecedor VARCHAR(160) NOT NULL,
  status VARCHAR(40) NOT NULL DEFAULT 'ORCANDO',
  data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_ultima_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_pedido_status CHECK (status IN ('ORCANDO','PENDENTE_DE_PAGAMENTO','PAGO_FATURADO','ENTREGUE'))
);

CREATE TABLE IF NOT EXISTS itens_pedido_pecas (
  pedido_codigo BIGINT NOT NULL,
  peca_codigo BIGINT NOT NULL,
  quantidade INT NOT NULL,
  PRIMARY KEY (pedido_codigo, peca_codigo),
  CONSTRAINT chk_item_quantidade CHECK (quantidade > 0),
  FOREIGN KEY (pedido_codigo) REFERENCES pedidos_pecas(codigo) ON DELETE CASCADE,
  FOREIGN KEY (peca_codigo) REFERENCES pecas(codigo)
);

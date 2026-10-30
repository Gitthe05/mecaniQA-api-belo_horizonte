package br.com.mecaniqa.api.repository;

import java.util.List;
import java.util.Optional;

/** Contrato comum para repositórios JDBC da aplicação. */
public interface Repositorio<T, ID> {
  T salvar(T entidade);

  Optional<T> buscarPorCodigo(ID codigo);

  List<T> listarTodos();

  Optional<T> atualizar(ID codigo, T entidade);

  boolean excluir(ID codigo);
}

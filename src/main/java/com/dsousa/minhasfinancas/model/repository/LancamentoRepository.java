package com.dsousa.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dsousa.minhasfinancas.model.entity.Lancamento;
import com.dsousa.minhasfinancas.model.enuns.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query(value = 
			  " select sum(l.valor) from Lancamento l join l.usuario u "
			+ " where u.id = :idUsuario and l.tipo = :tipo group by u ")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoLancamento tipo);

}

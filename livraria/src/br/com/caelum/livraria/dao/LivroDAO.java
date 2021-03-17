package br.com.caelum.livraria.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.tx.Log;

public class LivroDAO implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager manager;
	
	private DAO<Livro> dao;
	
	@PostConstruct
	void init() {
		this.dao = new DAO<Livro>(this.manager, Livro.class);
	}

	public void adiciona(Livro t) {
		dao.adiciona(t);
	}

	public void remove(Livro t) {
		dao.remove(t);
	}

	public void atualiza(Livro t) {
		dao.atualiza(t);
	}

	@Log
	public List<Livro> listaTodos() {
		return dao.listaTodos();
	}

	@Log
	public Livro buscaPorId(Integer id) {
		return dao.buscaPorId(id);
	}
	
	@Log
	public List<Livro> listaTodosPaginada(int firstResult, int maxResults, String coluna, String valor) {
		return dao.listaTodosPaginada(firstResult, maxResults, coluna, valor);
	}

	@Log
	public int quantidadeDeElementos() {
		return dao.quantidadeDeElementos();
	}

	@Log
	public int contaTodos() {
		return dao.contaTodos();
	}
	
	
}

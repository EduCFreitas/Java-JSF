package br.com.caelum.livraria.modelo;

public class Venda {

	public Livro livro;
	public Integer quantidade;
	
	public Venda(Livro livro, Integer quantidade) {
		this.livro = livro;
		this.quantidade = quantidade;
	}
	
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	
}

package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.RollbackException;

import br.com.caelum.livraria.dao.AutorDAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.tx.Transacional;
import br.com.caelum.livraria.util.RedirectView;

@Named
@ViewScoped
public class AutorBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Autor autor = new Autor();
	
	@Inject
	private AutorDAO dao; //CDI faz new AutorDAO() e injeta
	
	private Integer autorId;

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	public void carregarAutorPelaId() {
		this.autor = this.dao.buscaPorId(autorId);
	}

	public Autor getAutor() {
		return autor;
	}
	
	public List<Autor> getAutores(){
		return this.dao.listaTodos();
	}

	@Transacional
	public RedirectView gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());

		if(this.autor.getId() == null) {
			this.dao.adiciona(this.autor);
		}else {
			this.dao.atualiza(this.autor);
		}
		this.autor = new Autor();
		
		return new RedirectView("livro");
	}
	
	@Transacional
	public void remover(Autor autor) {
		System.out.println("Removendo autor " + autor.getNome());
		try {
			this.dao.remove(autor);
		}catch (RollbackException e) {
			FacesContext.getCurrentInstance().addMessage("autor", 
					new FacesMessage("Autor não pode ser excluído por estar associado a um livro."));
		}

	}
	
	public void carregar(Autor autor) {
		System.out.println("Carregando autor");
		this.autor = autor;
	}
}

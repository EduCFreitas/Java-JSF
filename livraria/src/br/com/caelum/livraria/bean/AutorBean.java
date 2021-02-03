package br.com.caelum.livraria.bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.RollbackException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.util.RedirectView;

@ManagedBean
@ViewScoped
public class AutorBean {

	private Autor autor = new Autor();

	public Autor getAutor() {
		return autor;
	}
	
	public List<Autor> getAutores(){
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public RedirectView gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());

		if(this.autor.getId() == null) {
			new DAO<Autor>(Autor.class).adiciona(this.autor);
		}else {
			new DAO<Autor>(Autor.class).atualiza(this.autor);
		}
		this.autor = new Autor();
		
		return new RedirectView("livro");
	}
	
	public void remover(Autor autor) {
		System.out.println("Removendo autor " + autor.getNome());
		try {
			new DAO<Autor>(Autor.class).remove(autor);
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

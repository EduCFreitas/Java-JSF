package br.com.caelum.livraria.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.UsuarioDAO;
import br.com.caelum.livraria.modelo.Usuario;
import br.com.caelum.livraria.util.RedirectView;

@Named
@ViewScoped
public class LoginBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario = new Usuario();
	
	@Inject
	UsuarioDAO dao;
	
	@Inject
	FacesContext context;

	public Usuario getUsuario() {
		return usuario;
	}
	
	public RedirectView efetuaLogin() {
		System.out.println("Fazendo login do usu�rio " + this.usuario.getEmail());
		
		boolean existe = dao.existe(this.usuario);
		if(existe) {
			context.getExternalContext().getSessionMap().put("usuarioLogado", this.usuario);
			return new RedirectView("livro");
		}
		
		context.getExternalContext().getFlash().setKeepMessages(true);;
		context.addMessage(null, new FacesMessage("Usu�rio n�o encontrado"));
		
		return new RedirectView("login");
	}
	
	public RedirectView deslogar() {
		context.getExternalContext().getSessionMap().remove("usuarioLogado");
		return new RedirectView("login");
	}

}

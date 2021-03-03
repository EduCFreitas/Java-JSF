package br.com.caelum.livraria.bean;

import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.util.RedirectView;

@ManagedBean
@ViewScoped
public class LivroBean {

	private Integer livroId;
	private Livro livro = new Livro();
	private Integer autorId;
	private List<Livro> livros;
	
	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}
	
	public void carregaPelaId() {
		this.livro = new DAO<Livro>(Livro.class).buscaPorId(this.livroId);
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	public Integer getAutorId() {
		return autorId;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public List<Livro> getLivros() {
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		if(this.livros == null) {
			this.livros = dao.listaTodos();
		}
		return livros;
	}
	
	public List<Autor> getAutores(){
		return new DAO<Autor>(Autor.class).listaTodos();
	}
	
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}
	
	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Escrito por: " + autor.getNome());
	}
	
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			//throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));
			return;
		}
		
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		if(this.livro.getId() == null) {
			dao.adiciona(this.livro);
			this.livros = dao.listaTodos();
		}else {
			dao.atualiza(this.livro);
		}
		
		this.livro = new Livro();
	}
	
	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		new DAO<Livro>(Livro.class).remove(livro);
	}
	
	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}
	
	//livro.xhtml:
	//<h:commandLink value="Altera" action="#{livroBean.carregar(livro)}"/>
//	public void carregar(Livro livro) {
//		System.out.println("Carregando livro");
//		this.livro = livro;
//	}
	
	public RedirectView formAutor() {
		System.out.println("Chamando o formul�rio do Autor");
		return new RedirectView("autor");
	}
	
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if(!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("Deveria come�ar com 1"));
		}
	}
	
	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) {
		//Tirando espa�os do filtro
		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();
		
		System.out.println("Filtrando pelo " + textoDigitado.equals(""));
		
		//O filtro � nulo ou vazio?
		if(textoDigitado == null || textoDigitado.equals("")) {
			return true;
		}
		
		//Elemento da tabela � nulo?
		if(valorColuna == null) {
			return false;
		}
		
		try {
			//Fazendo o parsing do filtro para converter para Double
			Double precoDigitado = Double.valueOf(textoDigitado);
			Double precoColuna = (Double) valorColuna;
			
			//Comparando os valores, compareTo devolve um valor negativo se o value � menor do que o filtro
			return precoColuna.compareTo(precoDigitado) < 0;
		}catch(NumberFormatException e) {
			//Usu�rio n�o digitou um n�mero
			return false;
		}
	}

}

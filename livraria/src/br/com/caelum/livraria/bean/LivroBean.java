package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDAO;
import br.com.caelum.livraria.dao.LivroDAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.LivroDataModel;
import br.com.caelum.livraria.tx.Transacional;
import br.com.caelum.livraria.util.RedirectView;

@Named
@ViewScoped
public class LivroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer livroId;
	private Livro livro = new Livro();
	private Integer autorId;
	private List<Livro> livros;
	
	@Inject
	private LivroDataModel livroDataModel;
	private List<String> generos = Arrays.asList("Romance", "Drama", "Ação");
	
	@Inject
	LivroDAO livroDao;
	
	@Inject
	AutorDAO autorDao;
	
	@Inject
	FacesContext context;
	
	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}
	
	public void carregaPelaId() {
		this.livro = livroDao.buscaPorId(this.livroId);
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
	
	public List<String> getGeneros() {
		return generos;
	}

	public LivroDataModel getLivroDataModel() {
		return livroDataModel;
	}

	public List<Livro> getLivros() {
		if(this.livros == null) {
			this.livros = livroDao.listaTodos();
		}
		return livros;
	}
	
	public List<Autor> getAutores(){
		return autorDao.listaTodos();
	}
	
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}
	
	public void gravarAutor() {
		Autor autor = autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Escrito por: " + autor.getNome());
	}
	
	@Transacional
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			//throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			context.addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));
			return;
		}
		
		if(this.livro.getId() == null) {
			livroDao.adiciona(this.livro);
			this.livros = livroDao.listaTodos();
		}else {
			livroDao.atualiza(this.livro);
		}
		
		this.livro = new Livro();
	}
	
	@Transacional
	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		livroDao.remove(livro);
		this.livros = livroDao.listaTodos();
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
		System.out.println("Chamando o formulário do Autor");
		return new RedirectView("autor");
	}
	
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if(!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("Deveria começar com 1"));
		}
	}
	
	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) {
		//Tirando espaços do filtro
		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();
		
		System.out.println("Filtrando pelo " + textoDigitado.equals(""));
		
		//O filtro é nulo ou vazio?
		if(textoDigitado == null || textoDigitado.equals("")) {
			return true;
		}
		
		//Elemento da tabela é nulo?
		if(valorColuna == null) {
			return false;
		}
		
		try {
			//Fazendo o parsing do filtro para converter para Double
			Double precoDigitado = Double.valueOf(textoDigitado);
			Double precoColuna = (Double) valorColuna;
			
			//Comparando os valores, compareTo devolve um valor negativo se o value é menor do que o filtro
			return precoColuna.compareTo(precoDigitado) < 0;
		}catch(NumberFormatException e) {
			//Usuário não digitou um número
			return false;
		}
	}

}

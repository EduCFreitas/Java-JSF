package br.com.caelum.livraria.tx;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

@Transacional
@Interceptor
public class GerenciadorDeTransacao implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	EntityManager manager;
	
	@AroundInvoke
	public Object executaTX(InvocationContext contexto) throws Exception {
		// abre transacao
		manager.getTransaction().begin();
		System.out.println("Abrindo tx");
		
		// chama os DAO's que precisam de um TX
		Object resultado = contexto.proceed();
		
		// commita a transacao
		manager.getTransaction().commit();
		System.out.println("Fechando tx");
		
		return resultado;
	}

}

package exceptions;


public class ValidationException extends RuntimeException{
	
	/**
	 * EXCEÇÃO PARA ERRO DE VALIDAÇÃO DE DADOS(esse pode ser uma exceção genérica ou você pode manter as exceções do próprion Spring para validação de parametros
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(String message){
		super(message);
	}
	}
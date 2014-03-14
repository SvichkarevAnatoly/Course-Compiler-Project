package main.ru.svichkarev.compiler.lexer;

public class Token<T>{
	private TokenType type;
	private T value;
	
	public Token( TokenType type1, T value ){
		this.type = type1;
		this.value = value; 
	}

	public T getTokenValue(){
		return value;
	}
	
	public TokenType getTokenType(){
		return type;
	}
}

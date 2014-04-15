package main.ru.svichkarev.compiler.lexer;

public class Token<T>{
	private TokenType type;
	private T value;
	
	public Token( TokenType type, T value ){
		this.type = type;
		this.value = value; 
	}

	// TODO: заменить на упрощённые
	// упрощённый токен
	public Token( TokenType type ){
		this.type = type;
		this.value = null; 
	}
	
	public T getTokenValue(){
		return value;
	}

	public TokenType getTokenType(){
		return type;
	}
	
	// метод проверки типа
	public boolean match( TokenType type ){
		return (this.getTokenType() == type);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Token)) {
			return false;
		}
		
		Token<?> other = (Token<?>) obj;
		if (type != other.type) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) { // TODO:
			return false;
		}
		return true;
	}
}

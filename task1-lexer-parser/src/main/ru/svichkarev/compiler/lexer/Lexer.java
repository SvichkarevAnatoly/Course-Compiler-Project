package main.ru.svichkarev.compiler.lexer;

import main.ru.svichkarev.compiler.buffer.Buffer;

public class Lexer {
	private Buffer buffer;
	
	private Token<?> currentToken = null;
	
	// TODO: куча всяких флажков
	private boolean isEndSourseCode = false;
	
	public Lexer( Buffer buffer ) {
		this.buffer = buffer;
	}
	
	public Token<?> peekToken() {
		if( currentToken == null ){
			makeToken();
		}
		
		return currentToken;
	}
	
	public Token<?> getToken(){
		if( currentToken == null ){
			makeToken();
		}
		Token<?> result = currentToken;
		makeToken();
		
		return result;
	}

	// сравнивает токен с известным типом
	public static boolean match( Token<?> token, TokenType etalon ){
		return (token.getTokenType() == etalon);
	}
	
	// считывает токен и пишет в поле текущего токена
	private void makeToken(){
		if( isEndSourseCode ){
			currentToken = new Token<String>( TokenType.END , "End" );
			return;
		}
		
		readThroughSpacesAndComments();
		char curChar = buffer.getChar();
		switch (curChar) {
		case '+':
			currentToken = new Token<String>( TokenType.PLUS, "+" ); // TODO: почему так?
			break;
		case '-':
			currentToken = new Token<String>( TokenType.MINUS, "-" ); // TODO: почему так?
			break;
		case '*':
			currentToken = new Token<String>( TokenType.MULTIPLICATION, "*" ); // TODO: почему так?
			break;
		case '/':
			currentToken = new Token<String>( TokenType.DIVISION, "/" ); // TODO: почему так?
			break;
		case '^':
			currentToken = new Token<String>( TokenType.EXPONENTIATION, "^" ); // TODO: почему так?
			break;
		default:
			// цифра или число возможно
			if( Character.isDigit( curChar ) ){
				currentToken = getNumberFromBuffer( curChar );
			}
			
			break;
		}
	}
	
	// если это разделительный символ, то проматываем
	private void readThroughSpacesAndComments(){
		while( Character.isSpaceChar( peekCharFromBuffer() ) ){
			buffer.getChar();
		}
	}
	
	private Token<Integer> getNumberFromBuffer( char curChar ) {
		int number = Character.getNumericValue( curChar );
		
		while( Character.isDigit( peekCharFromBuffer() ) ){
			number = 10 * number + Character.getNumericValue( buffer.getChar() );
		}
		
		return new Token<Integer>( TokenType.NUMBER, number );
	}
	
	private int peekCharFromBuffer(){
		int ch = buffer.peekChar();
		
		if( ch == -1 ){
			isEndSourseCode = true;
		}
		
		return ch;
	}
}

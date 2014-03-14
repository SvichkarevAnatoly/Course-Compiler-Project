package main.ru.svichkarev.compiler.lexer;

import main.ru.svichkarev.compiler.buffer.Buffer;

public class Lexer {
	private Buffer buffer;

	// TODO: аккумулятор текущей строки токена
	private String curTokenString;
	
	// TODO: куча всяких флажков
	private boolean isEndSourseCode = false;
	
	public Lexer( Buffer buffer ) {
		this.buffer = buffer;
	}
	
	public Token getToken(){
		Token result = null; // TODO: убрать null
		
		if( isEndSourseCode ){
			result = new Token<String>( TokenType.END , "End" );
			return result;
		}
		
		readThroughSpacesAndComments();
		char curChar = buffer.getChar();
		switch (curChar) {
		case '+':
			result = new Token<String>( TokenType.PLUS, "+" ); // TODO: почему так?
			break;
		case '-':
			result = new Token<String>( TokenType.MINUS, "-" ); // TODO: почему так?
		default:
			// цифра или число возможно
			if( Character.isDigit( curChar ) ){
				result = getNumberFromBuffer( curChar );
			}
			
			break;
		}
		
		return result;
	}

	// сравнивает токен с известным типом
	public static boolean match( Token token, TokenType etalon ){
		return (token.getTokenType() == etalon);
	}
	
	// если это разделительный символ, то проматываем
	private void readThroughSpacesAndComments(){
		// TODO: потом реализую
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
	
	/*
	// В идеале достаточно 2 символов для определения любого токена
	private static TokenType determineTokenType( char cur, int next ){
		
		
		return null;
	}*/
}

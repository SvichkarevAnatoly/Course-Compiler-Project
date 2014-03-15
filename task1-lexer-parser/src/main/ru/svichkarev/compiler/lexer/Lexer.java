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
		case '(':
			currentToken = new Token<String>( TokenType.BRACKET_OPEN, "(" ); // TODO: почему так?
			break;
		case ')':
			currentToken = new Token<String>( TokenType.BRACKET_CLOSE, ")" ); // TODO: почему так?
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
	
	private Token<?> getNumberFromBuffer( char curChar ) {
		int number = Character.getNumericValue( curChar );
		
		int shiftComma = -1;
		
		while( Character.isDigit( peekCharFromBuffer()) || (peekCharFromBuffer() == '.') ){
			// если встретили точку или сдвиг уже считается, то делаем инкремент
			if( (shiftComma > -1) || (peekCharFromBuffer() == '.') ){
				shiftComma++;
			}
			if( shiftComma != 0 ){ // если у нас не точка, то цифра
				number = 10 * number + Character.getNumericValue( buffer.getChar() );
			} else{
				buffer.getChar(); // проматываем точку, если встретили
			}
		}
		
		switch (shiftComma) {
		case -1: // int
			return new Token<Double>( TokenType.NUMBER, (double)number );	// TODO: потом поменять на Integer
		case 0:
			// число на точку не может заканчиваться
			// TODO: кидаем ошибку
			break;
		default: // double
			double doubleNumber = number;
			double tenPower = Math.pow( 10, shiftComma ); // TODO: корректно ли так делать?
			doubleNumber /= tenPower;
			
			return new Token<Double>( TokenType.NUMBER, doubleNumber ); // TODO: добавить типы int и double вместо number
		}
		
		return new Token<Integer>( TokenType.NUMBER, 999 ); // TODO: заглушка вместо ошибке в case
	}
	
	private int peekCharFromBuffer(){
		int ch = buffer.peekChar();
		
		if( ch == -1 ){
			isEndSourseCode = true;
		}
		
		return ch;
	}
}

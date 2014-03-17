package main.ru.svichkarev.compiler.lexer;

import main.ru.svichkarev.compiler.buffer.Buffer;

public class Lexer {
	private Buffer buffer;
	
	private Token<?> currentToken = null;
	
	// TODO: куча всяких флажков
	private boolean isEndSourceCode = false;
	
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
		if( isEndSourceCode ){
			currentToken = new Token<String>( TokenType.END , "End" );
			return;
		}
		
		boolean isDetectToken = false;
		while( !isDetectToken ){ //из-за постоянных комментарием придётся зациклить, как будет нормальный символ, вернём токен
			readThroughSpacesAndComments(); // TODO: если получится выпилить комментарии, то поменять название
			char curChar = buffer.getChar();
			switch (curChar) {
			case '+':
				currentToken = new Token<String>( TokenType.PLUS, "+" ); // TODO: почему так?
				isDetectToken = true;
				break;
			case '-':
				currentToken = new Token<String>( TokenType.MINUS, "-" ); // TODO: почему так?
				isDetectToken = true;
				break;
			case '*':
				currentToken = new Token<String>( TokenType.MULTIPLICATION, "*" ); // TODO: почему так?
				isDetectToken = true;
				break;
			case '/':
				/*Возможны 3 варианта:
				 * 1) это просто деление
				 * 2) это однострочный комментарий
				 * 3) это многострочный комментарий*/
				if( tryDetectingCommentAndRemove() ){
					break;
				}
				
				// значит это знак деления
				currentToken = new Token<String>( TokenType.DIVISION, "/" ); // TODO: почему так?
				isDetectToken = true;
				break;
			case '^':
				currentToken = new Token<String>( TokenType.EXPONENTIATION, "^" ); // TODO: почему так?
				isDetectToken = true;
				break;
			case '(':
				currentToken = new Token<String>( TokenType.BRACKET_OPEN, "(" ); // TODO: почему так?
				isDetectToken = true;
				break;
			case ')':
				currentToken = new Token<String>( TokenType.BRACKET_CLOSE, ")" ); // TODO: почему так?
				isDetectToken = true;
				break;
			default:
				// могут быть пробелы до конца
				if( isEndSourceCode ){
					currentToken = new Token<String>( TokenType.END , "End" );
					return;
				}
				// цифра или число возможно
				if( Character.isDigit( curChar ) ){
					currentToken = getNumberFromBuffer( curChar );
					isDetectToken = true;
				}
				// TODO: кинуть исключение если фигня
				
				break;
			}
		}
	}
	
	/* Пытаемся удалить комментарий,
	 * если выйдет, то вернуть true*/
	private boolean tryDetectingCommentAndRemove(){
		char nextChar = (char) peekCharFromBuffer(); // TODO: testing
		if( isEndSourceCode ){
			// TODO: буфер закончился, нужно кидать ошибку
		} else{
			switch (nextChar) {
			case '/': // однострочный комментарий
				// пока не встретим перевод строки, всё пропускаем
				while( buffer.getChar() != '\n' );
				
				// дальше нужно заного пытаться распознать символ
				return true;
			case '*': // это многострочный комментарий
				buffer.getChar(); // пропустили звёздочку
				// проматываем, пока не встретим комбинацию конца коментария
				boolean isEndMultilineComment = false;
				while( !isEndMultilineComment ){ // TODO: пооптимизировать
					peekCharFromBuffer();
					if( isEndSourceCode ){
						// TODO: кинуть исключение
						return true; // TODO: заглушка
					} else{
						char nextCommentChar = buffer.getChar();
						if( nextCommentChar == '*' ){
							// претендент на закрытие комментария
							if( peekCharFromBuffer() == '/' ){
								if( !isEndSourceCode ){
									// это оно - закрытие комментария!
									isEndMultilineComment = true;
									buffer.getChar();
								} else{
									// TODO: кидаем ошибку
									return true; // TODO: заглушка
								}
							}
						}
					}
				}
				return true;
			default:
				return false;
			}
		}
		
		return false;
	}

	// если это разделительный символ(конец строки), то проматываем
	private void readThroughSpacesAndComments(){
		while( Character.isWhitespace( peekCharFromBuffer() )){
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
			isEndSourceCode = true;
		}
		
		return ch;
	}
}

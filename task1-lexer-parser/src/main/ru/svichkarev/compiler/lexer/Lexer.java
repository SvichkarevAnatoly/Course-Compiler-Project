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
		
		readThroughSpacesAndComments();
		
		char curChar = buffer.getChar();
		switch (curChar) {
		case '+':
			currentToken = new Token<String>( TokenType.PLUS, "+" );
			break;
		case '-':
			currentToken = new Token<String>( TokenType.MINUS, "-" );
			break;
		case '*':
			currentToken = new Token<String>( TokenType.MULTIPLICATION, "*" );
			break;
		case '/':
			currentToken = new Token<String>( TokenType.DIVISION, "/" );
			break;
		case '^':
			currentToken = new Token<String>( TokenType.EXPONENTIATION, "^" );
			break;
		case '(':
			currentToken = new Token<String>( TokenType.BRACKET_OPEN, "(" );
			break;
		case ')':
			currentToken = new Token<String>( TokenType.BRACKET_CLOSE, ")" );
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
			}
			// TODO: кинуть исключение если фигня
			
			break;
		}
	}

	// выискиваем комментарии и разделительные символы и удаляем
	private void readThroughSpacesAndComments(){
		// пока не наткнёмся на не разделительный символ или не коментарий
		while( true ){
			// пропускаем разделительные символы
			while( Character.isWhitespace( peekCharFromBuffer(0) )){
				buffer.getChar();
			}
			
			// отсеивание комментариев
			int nextChar = peekCharFromBuffer(0);
			if( nextChar == '/' ){
				
				// взяли символ через 1
				nextChar = peekCharFromBuffer(1);
				
				switch (nextChar) {
				case '/': // однострочный комментарий
					// TODO: индусский код
					// пока не встретим перевод строки или конец потока, всё пропускаем
					while( ! isEndSourceCode ){
						if( '\n' != peekCharFromBuffer(0) ){
							buffer.getChar();
						}else{
							buffer.getChar();
							break;
						}
					}
					break;
				case '*': // это многострочный комментарий
					buffer.getChar(); // пропустили косую
					buffer.getChar(); // пропустили звёздочку
					// проматываем, пока не встретим комбинацию конца коментария
					boolean isEndMultilineComment = false;
					while( !isEndMultilineComment ){ // TODO: пооптимизировать
						peekCharFromBuffer(0); //TODO зачем это действие?
						if( isEndSourceCode ){
							// TODO: кинуть исключение
							return; // TODO: заглушка
						} else{
							char nextCommentChar = buffer.getChar();
							if( nextCommentChar == '*' ){
								// претендент на закрытие комментария
								if( peekCharFromBuffer(0) == '/' ){
									if( !isEndSourceCode ){
										// это оно - закрытие комментария!
										isEndMultilineComment = true;
										buffer.getChar();
									} else{
										// TODO: кидаем ошибку
										return; // TODO: заглушка
									}
								}
							}
						}
					}
					// очистили от коментария, запускаем снова
				default: // не комментарий
					return;
				}
			} else{
				// просеивание завершилось
				return;
			}
		}
	}
	
	private Token<?> getNumberFromBuffer( char curChar ) {
		int number = Character.getNumericValue( curChar );
		
		int shiftComma = -1;
		
		while( Character.isDigit( peekCharFromBuffer(0)) || (peekCharFromBuffer(0) == '.') ){
			// если встретили точку или сдвиг уже считается, то делаем инкремент
			if( (shiftComma > -1) || (peekCharFromBuffer(0) == '.') ){
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
			return new Token<Integer>( TokenType.NUMBER, number );
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
		
		return new Token<Integer>( TokenType.NUMBER, 999 ); // TODO: заглушка вместо ошибки в case
	}
	
	// функция обёртка, которая определяет конец потока символов
	// аргумент - {0,1} - какой символ вперёд хотим взять
	private int peekCharFromBuffer( int serialIndex ){
		int ch = buffer.peekChar();
		
		if( ch == -1 ){ // TODO константа
			isEndSourceCode = true;
			if( serialIndex == 1 ){
				// если хотим через один, когда на следующем закончился поток,
				//то кинуть ошибку
				// TODO
			}
		} else{
			// если хотим через 1
			if( serialIndex == 1 ){
				ch = buffer.peekSecondChar();
				if( ch == -1 ){
					isEndSourceCode = true;
				}
			}
		}
		
		return ch;
	}
}

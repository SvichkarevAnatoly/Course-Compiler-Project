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
	
	// считывает токен и пишет в поле текущего токена
	private void makeToken(){
		if( isEndSourceCode ){
			currentToken = new Token<String>( TokenType.END , "End" );
			return;
		}
		
		readThroughSpacesAndComments();
		
		if( isEndSourceCode ){
			currentToken = new Token<String>( TokenType.END , "End" );
			return;
		}
		
		char curChar = buffer.getChar();
		switch (curChar) {
		case '+':
			currentToken = new Token<Object>( TokenType.PLUS );
			break;
		case '-':
			currentToken = new Token<Object>( TokenType.MINUS );
			break;
		case '*':
			currentToken = new Token<Object>( TokenType.MULTIPLICATION );
			break;
		case '/':
			currentToken = new Token<Object>( TokenType.DIVISION );
			break;
		case '^':
			currentToken = new Token<Object>( TokenType.EXPONENTIATION );
			break;
		case '(':
			currentToken = new Token<Object>( TokenType.BRACKET_OPEN );
			break;
		case ')':
			currentToken = new Token<Object>( TokenType.BRACKET_CLOSE );
			break;
		case '{':
			currentToken = new Token<Object>( TokenType.BRACE_OPEN );
			break;
		case '}':
			currentToken = new Token<Object>( TokenType.BRACE_CLOSE );
			break;
		case ',':
			currentToken = new Token<Object>( TokenType.COMMA );
			break;
		case ';':
			currentToken = new Token<Object>( TokenType.SEMICOLON );
			break;
		case '=':
			currentToken = new Token<Object>( TokenType.ASSIGNMENT );
			break;
			
		default:
			// цифра или число возможно
			if( Character.isDigit( curChar ) ){
				currentToken = getNumberFromBuffer( curChar );
			} else{
				// идентификатор возможно или служебное слово
				if( Character.isAlphabetic( curChar ) || curChar == '_' ){
					currentToken = getIdentificatorFromBuffer( curChar );
				}else{
					// TODO: кинуть исключение если фигня
				}
			}
			
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
										//buffer.getChar(); // TODO: не должен быть
									} else{
										// TODO: кидаем ошибку
										return; // TODO: заглушка
									}
								}
							}
						}
					}
					// очистили от коментария, запускаем снова
					break;
				default: // не комментарий
					// TODO: похоже на костыль
					// возможно знак деления и конец строки
					if( isEndSourceCode ){
						// типо мы ничего не видели
						isEndSourceCode = false;
					}
					
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
	
	private Token<?> getIdentificatorFromBuffer( char curChar ) {
		// TODO: не очень оптимально так посимвольно собирать
		String ident = Character.toString( curChar );
		
		// если символ из алфавита, то добавляем к строке
		while( Character.isAlphabetic( peekCharFromBuffer(0) ) ){
			ident += buffer.getChar(); //TODO подозрительно
		}
		
		// может быть вплотную допустимый служебный символ, тогда всё нормально
		if( ! isEndSourceCode ){ // может быть окончание потока
			// TODO кинуть ошибку, такого не должно быть 
		}else{
			char nextChar = (char) peekCharFromBuffer( 0 );
			switch (nextChar) {
			case ' ':
			// сразу знак арифметического выражения
			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
			// всякая пунктуация
			case '(':
			case ')':
			case '{':
			case ',':
			case ';':
			case '=':
				break;
			default:
				// TODO: какая-то фигня впритык, кинуть ошибку
				break;
			}
		}
		
		// TODO: нужно для каждого служебного слова определить возможные впритык следующие символы(они разные)
		Token<?> result = null;
        // TODO: символьные константы вынести
		//определение служебное ли это слово
		if( ident.equals( "return" ) ){
			result = new Token<Object>( TokenType.RETURN );
		} else if( ident.equals( "int" ) ){
			result = new Token<String>( TokenType.INT, "I" );
		} else if( ident.equals( "double" ) ){
            // TODO: double не совсем double, а float ?
			result = new Token<String>( TokenType.DOUBLE, "F" );
		} else if( ident.equals( "print" ) ){
			result = new Token<Object>( TokenType.PRINT );
		} else{
			result = new Token<String>( TokenType.NAME, ident );
		}
		
		return result;
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

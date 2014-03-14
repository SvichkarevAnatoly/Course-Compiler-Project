package main.ru.svichkarev.compiler.parser;

import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public int parseExpr(){
		int result = parseTerm();
		
		int secondTerm = 0;
		Token<?> curToken = lexer.peekToken();
		while( Lexer.match( curToken, TokenType.PLUS ) ||
				Lexer.match( curToken, TokenType.MINUS ) ){
			
			lexer.getToken();
			secondTerm = parseTerm();
			switch( curToken.getTokenType() ) {
			case PLUS:
				result += secondTerm;
				break;
			case MINUS:
				result -= secondTerm;
				break;
			default:
				break;
			}
			
			curToken = lexer.peekToken();
		}
		
		return result;
	}
	
	public int parseTerm(){
		int result = parseFactor();
		
		int secondFactor = 0;
		Token<?> curToken = lexer.peekToken();
		while( Lexer.match( curToken, TokenType.MULTIPLICATION ) ||
				Lexer.match( curToken, TokenType.DIVISION ) ){
			
			lexer.getToken();
			secondFactor = parseFactor();
			switch( curToken.getTokenType() ) {
			case MULTIPLICATION:
				result *= secondFactor;
				break;
			case DIVISION:
				result /= secondFactor;
				break;
			default:
				break;
			}
			
			curToken = lexer.peekToken();
		}
		
		return result;
	}
	
	public int parseFactor(){
		int result = parsePower();
		
		Token<?> curToken = lexer.peekToken();
		if( Lexer.match( curToken, TokenType.EXPONENTIATION ) ){
			lexer.getToken(); // пропускаем знак ^
			int secondPower = parseFactor();
			result = (int) Math.pow( result, secondPower ); // TODO: Так нельзя! нужна нормальная целочисленная функция
		}
		
		return result;
	}
	
	public int parsePower(){
		int result;
		int sign = 1;
		
		Token<?> curToken = lexer.peekToken();
		// если унарный минус
		if( Lexer.match( curToken, TokenType.MINUS ) ){
			lexer.getToken(); // пропускаем -
			sign = -1;
		}
		result = parseAtom();
		
		return result * sign;
	}
	
	public int parseAtom(){
		//TODO: Простой вариант - только int
		int result = 0; // TODO: убрать инициализацию
		
		Token<?> token = lexer.getToken();
		if( Lexer.match( token, TokenType.BRACKET_OPEN ) ){
			token = lexer.getToken();
			if( Lexer.match( token, TokenType.NUMBER ) ){
				token = lexer.getToken();
				result = (Integer) token.getTokenValue();
				if( Lexer.match( token, TokenType.BRACKET_CLOSE ) ){
					return result;
				}
			} else{
				// TODO: кинуть ошибку
			}
		} else{
			if( Lexer.match( token, TokenType.NUMBER ) ){
				result = (Integer) token.getTokenValue();
			} else{
				// TODO: кинуть ошибку
			}
		}
		
		return result;
	}
}

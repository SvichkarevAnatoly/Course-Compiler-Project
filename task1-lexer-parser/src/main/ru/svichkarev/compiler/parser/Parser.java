package main.ru.svichkarev.compiler.parser;

import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public double parseExpr(){
		double result = parseTerm();
		
		double secondTerm = 0;
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
	
	public double parseTerm(){
		double result = parseFactor();
		
		double secondFactor = 0;
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
	
	public double parseFactor(){
		double result = parsePower();
		
		Token<?> curToken = lexer.peekToken();
		if( Lexer.match( curToken, TokenType.EXPONENTIATION ) ){
			lexer.getToken(); // пропускаем знак ^
			double secondPower = parseFactor();
			result = Math.pow( result, secondPower ); // TODO: Так нельзя! нужна нормальная целочисленная функция
		}
		
		return result;
	}
	
	public double parsePower(){
		double result;
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
	
	public double parseAtom(){
		//TODO: Простой вариант - только int
		double result = 0; // TODO: убрать инициализацию
		
		Token<?> token = lexer.getToken();
		if( Lexer.match( token, TokenType.BRACKET_OPEN ) ){
			result = parseExpr();
			token = lexer.getToken();
			if( Lexer.match( token, TokenType.BRACKET_CLOSE ) ){
				return result;
			}else{
				// TODO: кинуть ошибку
			}
		} else{
			if( Lexer.match( token, TokenType.NUMBER ) ){
				result = (Double)token.getTokenValue();
			} else{
				// TODO: кинуть ошибку
			}
		}
		
		return result;
	}
}

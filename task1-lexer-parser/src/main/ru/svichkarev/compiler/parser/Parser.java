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
		Token<?> curToken = lexer.getToken();
		while( Lexer.match( curToken, TokenType.PLUS ) ||
				Lexer.match( curToken, TokenType.MINUS ) ){
			
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
			
			curToken = lexer.getToken();
		}
		
		return result;
	}
	
	public int parseTerm(){
		//TODO: Простой вариант сразу atom и только int
		int result = 0;
		Token<?> token = lexer.getToken();
		
		if( Lexer.match( token, TokenType.NUMBER ) ){
			result = (Integer) token.getTokenValue();
		} else{
			// TODO: кинуть ошибку
		}
		
		return result;
	}
}

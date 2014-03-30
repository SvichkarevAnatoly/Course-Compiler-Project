package main.ru.svichkarev.compiler.parser;

import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public Node parseExpr(){
		Node result = parseTerm();
		
		Token<?> curToken = lexer.peekToken();
		while( Lexer.match( curToken, TokenType.PLUS ) ||
				Lexer.match( curToken, TokenType.MINUS ) ){
			
			// пропускаем знак
			lexer.getToken();
			
			// комплектуем дерево
			Node sign = new Node( curToken );
			sign.setLeft( result );
			sign.setRight( parseTerm() );
			
			result = sign;
			
			curToken = lexer.peekToken();
		}
		
		return result;
	}
	
	public Node parseTerm(){
		Node result = parseFactor();
		
		Token<?> curToken = lexer.peekToken();
		while( Lexer.match( curToken, TokenType.MULTIPLICATION ) ||
				Lexer.match( curToken, TokenType.DIVISION ) ){
			
			lexer.getToken();
			
			// комплектуем дерево
			Node sign = new Node( curToken );
			sign.setLeft( result );
			sign.setRight( parseFactor() );
			
			result = sign;
			
			curToken = lexer.peekToken();
		}
		
		return result;
	}
	
	public Node parseFactor(){
		Node result = parsePower();
		
		Token<?> curToken = lexer.peekToken();
		if( Lexer.match( curToken, TokenType.EXPONENTIATION ) ){
			lexer.getToken(); // пропускаем знак ^
			
			Node exp = new Node( curToken );
			exp.setLeft( result );
			exp.setRight( parseFactor() );
			
			result = exp;
		}
		
		return result;
	}
	
	public Node parsePower(){
		Token<?> curToken = lexer.peekToken();
		// если унарный минус
		if( Lexer.match( curToken, TokenType.MINUS ) ){
			lexer.getToken(); // пропускаем -
			
			Node minus = new Node( curToken );
			minus.setLeft( parseAtom() ); // тут только левый ребёнок
			return minus;
		}
		
		return parseAtom();
	}
	
	public Node parseAtom(){
		Node result = null;
		
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
				result = new Node( token );
			} else{
				// TODO: кинуть ошибку
			}
		}
		
		return result;
	}
}

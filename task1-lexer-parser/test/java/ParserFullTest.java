package test.java;

import java.io.StringReader;

import junit.framework.Assert;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.parser.Parser;

import org.junit.Test;

// тестирование полной программы
public class ParserFullTest extends Assert {
	@Test
	public void testEmptyProgram() {
		Buffer buffer = new Buffer( new StringReader("") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> programToken = new Token<>( TokenType.PROGRAM );
		Node tree = new Node( programToken );
		tree.setLeft( new Node( TokenType.EMPTY ) );
		
		assertEquals( tree, parser.parseProgram() );
	}
	
	@Test
	public void testOneEmptyFunctionProgram() {
		Buffer buffer = new Buffer( new StringReader(
				"int main(){}" ) );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		Node realTree = parser.parseProgram();
		
		//собираем дерево
		Token<?> programToken = new Token<>( TokenType.PROGRAM );
		Node tree = new Node( programToken );
		
		Node functionNode = new Node( new Token<>( TokenType.FUNCTION, "main" ) );
		
		// возвращаемый тип
		Node typeNode = new Node( TokenType.TYPE );
		typeNode.setLeft( new Node( TokenType.INT ) );
		
		// список аргументов
		Node paramListNode = new Node( TokenType.PARAMS_LIST );
		paramListNode.setLeft( new Node( TokenType.EMPTY ) );
		
		// тело
		Node bodyNode = new Node( TokenType.BODY );
		bodyNode.setLeft( new Node( TokenType.EMPTY ) );
		
		// собираем функцию 
		functionNode.setLeft( typeNode );
		functionNode.setRight( paramListNode );
		functionNode.setRight( bodyNode );
		
		// добавляем к дереву
		tree.setLeft( functionNode );
		
		assertEquals( tree, realTree );
	}
	
	/*
	@Test
	public void testSimpleProgram() {
		Buffer buffer = new Buffer( new StringReader(
				"int main(){" +
				"	int a;" +
				"	a = 45;" +
				"}") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> programToken = new Token<>( TokenType.PROGRAM );
		Node tree = new Node( programToken );
		
		
		assertEquals( tree, parser.parseProgram() );
	}*/
}

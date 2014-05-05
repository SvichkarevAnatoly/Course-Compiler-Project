package test;

import junit.framework.TestCase;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.parser.Parser;
import org.junit.Test;

import java.io.StringReader;

// тестирование полной программы
public class ParserFullTest extends TestCase {
	@Test
	public void testEmptyProgram() {
		Buffer buffer = new Buffer( new StringReader("") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> programToken = new Token<Object>( TokenType.PROGRAM );
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
		Token<?> programToken = new Token<Object>( TokenType.PROGRAM );
		Node tree = new Node( programToken );
		
		Node functionNode = new Node( new Token<Object>( TokenType.FUNCTION, "main" ) );
		
		// возвращаемый тип
		Node typeNode = new Node( TokenType.TYPE );
		typeNode.setLeft( new Node( new Token<String>( TokenType.INT, "I" ) ) );
		
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
	
	@Test
	public void testSimpleProgram() {
		Buffer buffer = new Buffer( new StringReader(
				"int main(){" +
				"	int a;" +
				"	a = 45;" +
				"}") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		Node realTree = parser.parseProgram();
		
		//собираем дерево
		Token<?> programToken = new Token<Object>( TokenType.PROGRAM );
		Node tree = new Node( programToken );
		
		Node functionNode = new Node( new Token<Object>( TokenType.FUNCTION, "main" ) );
		
		// возвращаемый тип
		Node typeNode = new Node( TokenType.TYPE );
		typeNode.setLeft( new Node( new Token<String>( TokenType.INT, "I" ) ) );
		
		// список аргументов
		Node paramListNode = new Node( TokenType.PARAMS_LIST );
		paramListNode.setLeft( new Node( TokenType.EMPTY ) );
		
		// тело
		Node bodyNode = new Node( TokenType.BODY );
		// первая команда
		Node command1Node = new Node( TokenType.COMMAND );
		Node typeCommand1Node = new Node( TokenType.TYPE );
		typeCommand1Node.setLeft( new Node( new Token<String>( TokenType.INT, "I" ) ) );
		command1Node.setLeft( typeCommand1Node );
		command1Node.setRight( new Node( new Token<Object>( TokenType.NAME, "a" ) ) );
		// вторая команда
		Node command2Node = new Node( TokenType.COMMAND );
		command2Node.setLeft( new Node( new Token<Object>( TokenType.NAME, "a" ) ) );
		command2Node.setRight( new Node( TokenType.ASSIGNMENT ) );
		command2Node.setRight( new Node( new Token<Integer>( TokenType.NUMBER, 45 ) ) );
		
		bodyNode.setLeft( command1Node );
		bodyNode.setRight( command2Node );
		bodyNode.setRight( new Node( TokenType.EMPTY ) );
		
		// собираем функцию 
		functionNode.setLeft( typeNode );
		functionNode.setRight( paramListNode );
		functionNode.setRight( bodyNode );
		
		// добавляем к дереву
		tree.setLeft( functionNode );
		
		assertEquals( tree, realTree );
	}
	
	@Test
	public void testSimpleWithPrintProgram() {
		Buffer buffer = new Buffer( new StringReader(
				"int main(){" +
				"	int a;" +
				"	a = 45;" +
				"	print( a );" +
				"}") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		Node realTree = parser.parseProgram();
		
		//собираем дерево
		Token<?> programToken = new Token<Object>( TokenType.PROGRAM );
		Node tree = new Node( programToken );
		
		Node functionNode = new Node( new Token<Object>( TokenType.FUNCTION, "main" ) );
		
		// возвращаемый тип
		Node typeNode = new Node( TokenType.TYPE );
		typeNode.setLeft( new Node( new Token<String>( TokenType.INT, "I" ) ) );
		
		// список аргументов
		Node paramListNode = new Node( TokenType.PARAMS_LIST );
		paramListNode.setLeft( new Node( TokenType.EMPTY ) );
		
		// тело
		Node bodyNode = new Node( TokenType.BODY );
		// первая команда
		Node command1Node = new Node( TokenType.COMMAND );
		Node typeCommand1Node = new Node( TokenType.TYPE );
		typeCommand1Node.setLeft( new Node( new Token<String>( TokenType.INT, "I" ) ) );
		command1Node.setLeft( typeCommand1Node );
		command1Node.setRight( new Node( new Token<Object>( TokenType.NAME, "a" ) ) );
		// вторая команда
		Node command2Node = new Node( TokenType.COMMAND );
		command2Node.setLeft( new Node( new Token<Object>( TokenType.NAME, "a" ) ) );
		command2Node.setRight( new Node( TokenType.ASSIGNMENT ) );
		command2Node.setRight( new Node( new Token<Integer>( TokenType.NUMBER, 45 ) ) );
		// третья команда
		Node command3Node = new Node( TokenType.COMMAND );
		command3Node.setLeft( new Node( TokenType.PRINT ) );
		command3Node.setRight( new Node( new Token<Object>( TokenType.NAME, "a" ) ) );
		
		bodyNode.setLeft( command1Node );
		bodyNode.setRight( command2Node );
		bodyNode.setRight( command3Node );
		bodyNode.setRight( new Node( TokenType.EMPTY ) );
		
		// собираем функцию 
		functionNode.setLeft( typeNode );
		functionNode.setRight( paramListNode );
		functionNode.setRight( bodyNode );
		
		// добавляем к дереву
		tree.setLeft( functionNode );
		
		assertEquals( tree, realTree );
	}
}

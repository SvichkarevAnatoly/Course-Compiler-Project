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

public class ParserTest extends TestCase {
	@Test
	public void testNumber1() {
		Buffer buffer = new Buffer( new StringReader("5") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 5 );
		Node tree = new Node( num );
		
		assertEquals( tree, parser.parseExpr() );
	}

	@Test
	public void testNumber2() {
		Buffer buffer = new Buffer( new StringReader("54") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 54 );
		Node tree = new Node( num );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testNegativeNumber() {
		Buffer buffer = new Buffer( new StringReader("-5") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 5 );
		Token<?> minus = new Token<String>( TokenType.MINUS );
		Node numNode = new Node( num );
		
		Node tree = new Node( minus );
		tree.setLeft( numNode );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testSpacesNumber() {
		Buffer buffer = new Buffer( new StringReader("   5   ") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 5 );
		Node tree = new Node( num );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testMULTIPLICATION() {
		Buffer buffer = new Buffer( new StringReader("5*2") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 5 );
		Token<?> mul = new Token<String>( TokenType.MULTIPLICATION );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 2 );
		
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		
		Node tree = new Node( mul );
		tree.setLeft( numNode1 );
		tree.setRight( numNode2 );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testManyMULTIPLICATION() {
		Buffer buffer = new Buffer( new StringReader("5*2*3") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		// формируем токены
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 5 );
		Token<?> mul1 = new Token<String>( TokenType.MULTIPLICATION );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 2 );
		Token<?> mul2 = new Token<String>( TokenType.MULTIPLICATION );
		Token<?> num3 = new Token<Integer>( TokenType.NUMBER, 3 );
		
		
		//собираем дерево
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		Node numNode3 = new Node( num3 );
		
		Node tree = new Node( mul2 );
		Node subTree = new Node( mul1 );
		
		subTree.setLeft(numNode1);
		subTree.setRight(numNode2);
		
		tree.setLeft( subTree );
		tree.setRight( numNode3 );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testDIVISION() {
		Buffer buffer = new Buffer( new StringReader("20/2") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 20 );
		Token<?> div = new Token<String>( TokenType.DIVISION );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 2 );
		
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		
		Node tree = new Node( div );
		tree.setLeft( numNode1 );
		tree.setRight( numNode2 );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testManyDIVISION() {
		Buffer buffer = new Buffer( new StringReader("20/2/5") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		// формируем токены
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 20 );
		Token<?> div1 = new Token<String>( TokenType.DIVISION );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 2 );
		Token<?> div2 = new Token<String>( TokenType.DIVISION );
		Token<?> num3 = new Token<Integer>( TokenType.NUMBER, 5 );
		
		
		//собираем дерево
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		Node numNode3 = new Node( num3 );
		
		Node tree = new Node( div2 );
		Node subTree = new Node( div1 );
		
		subTree.setLeft(numNode1);
		subTree.setRight(numNode2);
		
		tree.setLeft( subTree );
		tree.setRight( numNode3 );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testEXPONENTIATION() {
		Buffer buffer = new Buffer( new StringReader("2^3") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 2 );
		Token<?> exp = new Token<String>( TokenType.EXPONENTIATION );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 3 );
		
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		
		Node tree = new Node( exp );
		tree.setLeft( numNode1 );
		tree.setRight( numNode2 );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testManyEXPONENTIATION() {
		Buffer buffer = new Buffer( new StringReader("2^3^4") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		// формируем токены
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 2 );
		Token<?> exp1 = new Token<String>( TokenType.EXPONENTIATION );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 3 );
		Token<?> exp2 = new Token<String>( TokenType.EXPONENTIATION );
		Token<?> num3 = new Token<Integer>( TokenType.NUMBER, 4 );
		
		
		//собираем дерево
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		Node numNode3 = new Node( num3 );
		
		Node tree = new Node( exp2 );
		Node subTree = new Node( exp1 );
		
		subTree.setLeft( numNode2 );
		subTree.setRight( numNode3 );
		
		tree.setLeft( numNode1 );
		tree.setRight( subTree );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testUnaryMINUS() {
		Buffer buffer = new Buffer( new StringReader("-2") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		// формируем токены
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 2 );
		Token<?> minus = new Token<String>( TokenType.MINUS );
		
		//собираем дерево
		Node numNode = new Node( num );
		Node tree = new Node( minus );
		tree.setLeft( numNode );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testBRACKET() {
		Buffer buffer = new Buffer( new StringReader("(2)") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 2 );
		Node tree = new Node( num );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testManyBRACKET() {
		Buffer buffer = new Buffer( new StringReader("(((2)))") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Integer>( TokenType.NUMBER, 2 );
		Node tree = new Node( num );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testBRACKETandMULTIPLICATION() {
		Buffer buffer = new Buffer( new StringReader("(2+3)*2") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		// формируем токены
		Token<?> num1 = new Token<Integer>( TokenType.NUMBER, 2 );
		Token<?> plus = new Token<String>( TokenType.PLUS );
		Token<?> num2 = new Token<Integer>( TokenType.NUMBER, 3 );
		Token<?> mul = new Token<String>( TokenType.MULTIPLICATION );
		Token<?> num3 = new Token<Integer>( TokenType.NUMBER, 2 );
		
		
		//собираем дерево
		Node numNode1 = new Node( num1 );
		Node numNode2 = new Node( num2 );
		Node numNode3 = new Node( num3 );
		
		Node tree = new Node( mul );
		Node subTree = new Node( plus );
		
		subTree.setLeft(numNode1);
		subTree.setRight(numNode2);
		
		tree.setLeft( subTree );
		tree.setRight( numNode3 );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	@Test
	public void testDoubleNumber() {
		Buffer buffer = new Buffer( new StringReader("1.0001") );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		//собираем дерево
		Token<?> num = new Token<Double>( TokenType.NUMBER, 1.0001 );
		Node tree = new Node( num );
		
		assertEquals( tree, parser.parseExpr() );
	}
	
	/*
	@Test
	public void testExpr1() {
		Buffer buffer = new Buffer( new StringReader("(2+3)*2 + 1"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 11.0, parser.parseExpr() );
	}*/
	
	/*
	@Test
	public void testExpr2() {
		Buffer buffer = new Buffer( new StringReader("(2.3+2.7)*2 + 1.00^2"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 11.0, parser.parseExpr() );
	}*/
	
	//Buffer buffer = new Buffer( new StringReader("(2.3+2.7)*2 +//345sgf\n/*fsftr*/ 1.00^2"), DEFAULT_SIZE );
}

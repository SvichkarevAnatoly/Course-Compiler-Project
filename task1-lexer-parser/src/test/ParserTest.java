package test;

import java.io.StringReader;

import junit.framework.Assert;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Parser;

import org.junit.Test;

public class ParserTest extends Assert {
	private final int DEFAULT_SIZE = 10;
	
	@Test
	public void testNumber1() {
		Buffer buffer = new Buffer( new StringReader("5"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 5.0, parser.parseExpr() );
	}

	@Test
	public void testNumber2() {
		Buffer buffer = new Buffer( new StringReader("54"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 54.0, parser.parseExpr() );
	}
	
	@Test
	public void testNegativeNumber() {
		Buffer buffer = new Buffer( new StringReader("-5"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( -5.0, parser.parseExpr() );
	}
	
	@Test
	public void testSpacesNumber() {
		Buffer buffer = new Buffer( new StringReader("   5   "), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 5.0, parser.parseExpr() );
	}
	
	@Test
	public void testMULTIPLICATION() {
		Buffer buffer = new Buffer( new StringReader("5*2"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 10.0, parser.parseExpr() );
	}
	
	@Test
	public void testManyMULTIPLICATION() {
		Buffer buffer = new Buffer( new StringReader("5*2*3"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 30.0, parser.parseExpr() );
	}
	
	@Test
	public void testDIVISION() {
		Buffer buffer = new Buffer( new StringReader("20/2"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 10.0, parser.parseExpr() );
	}
	
	@Test
	public void testManyDIVISION() {
		Buffer buffer = new Buffer( new StringReader("20/2/5"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 2.0, parser.parseExpr() );
	}
	
	@Test
	public void testEXPONENTIATION() {
		Buffer buffer = new Buffer( new StringReader("2^3"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 8.0, parser.parseExpr() );
	}
	
	@Test
	public void testManyEXPONENTIATION() {
		Buffer buffer = new Buffer( new StringReader("2^3^2"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 512.0, parser.parseExpr() );
	}
	
	@Test
	public void testBRACKET() {
		Buffer buffer = new Buffer( new StringReader("(2)"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 2.0, parser.parseExpr() );
	}
	
	@Test
	public void testManyBRACKET() {
		Buffer buffer = new Buffer( new StringReader("(((2)))"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 2.0, parser.parseExpr() );
	}
	
	@Test
	public void testBRACKETandMULTIPLICATION() {
		Buffer buffer = new Buffer( new StringReader("(2+3)*2"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 10.0, parser.parseExpr() );
	}
	
	@Test
	public void testDoubleNumber() {
		Buffer buffer = new Buffer( new StringReader("1.0001"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 1.0001, parser.parseExpr() );
	}
	
	@Test
	public void testExpr1() {
		Buffer buffer = new Buffer( new StringReader("(2+3)*2 + 1"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 11.0, parser.parseExpr() );
	}
	
	@Test
	public void testExpr2() {
		Buffer buffer = new Buffer( new StringReader("(2+3)*2 + 1^2"), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		assertEquals( 11.0, parser.parseExpr() );
	}
}

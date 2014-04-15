package test.java;

import java.io.StringReader;

import junit.framework.Assert;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

import org.junit.Test;

public class LexerTest extends Assert {
	@Test
	public void testNumber(){
		Buffer buffer = new Buffer( new StringReader( "42" ));
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 42, token1.getTokenValue() );
	}
	
	@Test
	public void testNumberDouble(){
		Buffer buffer = new Buffer( new StringReader( "42.042" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 42.042, ((Double)token1.getTokenValue()).doubleValue() );
	}
	
	@Test
	public void testPlus(){
		Buffer buffer = new Buffer( new StringReader( "+" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.PLUS, token1.getTokenType() );
	}
	
	@Test
	public void testMinus(){
		Buffer buffer = new Buffer( new StringReader( "-" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.MINUS, token1.getTokenType() );
	}
	
	@Test
	public void testMultiplication(){
		Buffer buffer = new Buffer( new StringReader( "*" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.MULTIPLICATION, token1.getTokenType() );
	}
	
	@Test
	public void testDivision(){
		Buffer buffer = new Buffer( new StringReader( "/" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.DIVISION, token1.getTokenType() );
	}
	
	@Test
	public void testExponential(){
		Buffer buffer = new Buffer( new StringReader( "^" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.EXPONENTIATION, token1.getTokenType() );
	}
	
	@Test
	public void testBracketOpen(){
		Buffer buffer = new Buffer( new StringReader( "(" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.BRACKET_OPEN, token1.getTokenType() );
	}
	
	@Test
	public void testBracketClose(){
		Buffer buffer = new Buffer( new StringReader( ")" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.BRACKET_CLOSE, token1.getTokenType() );
	}
	
	@Test
	public void testReturn(){
		Buffer buffer = new Buffer( new StringReader( "return;" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.RETURN, token1.getTokenType() );
	}
	
	@Test
	public void testInt(){
		Buffer buffer = new Buffer( new StringReader( "int " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.INT, token1.getTokenType() );
	}
	
	@Test
	public void testDouble(){
		Buffer buffer = new Buffer( new StringReader( "double " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.DOUBLE, token1.getTokenType() );
	}
	
	@Test
	public void testName1(){
		Buffer buffer = new Buffer( new StringReader( "foo " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NAME, token1.getTokenType() );
		assertEquals( "foo", token1.getTokenValue() );
	}
	
	@Test
	public void testName2(){
		Buffer buffer = new Buffer( new StringReader( "_bar " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NAME, token1.getTokenType() );
		assertEquals( "_bar", token1.getTokenValue() );
	}
	
	@Test
	public void testPrint(){
		Buffer buffer = new Buffer( new StringReader( "print " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.PRINT, token1.getTokenType() );
	}
	
	@Test
	public void testEnd(){
		Buffer buffer = new Buffer( new StringReader( "" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.END, token1.getTokenType() );
	}
	
	@Test
	public void testCommentSingleLine(){
		Buffer buffer = new Buffer( new StringReader( "//54 + 43" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.END, token1.getTokenType() );
	}
	
	@Test
	public void testCommentMultiplyLine(){
		Buffer buffer = new Buffer( new StringReader( "/*54 + 43*/  " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.END, token1.getTokenType() );
	}
	
	@Test
	public void testEmptyLine() {
		Buffer buffer = new Buffer( new StringReader( "  2  + 3  \n   " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 2, token1.getTokenValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 3, token3.getTokenValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
	}
	
	@Test
	public void testManyLines() {
		Buffer buffer = new Buffer( new StringReader( "  2 \n   +\n   3\n" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 2, token1.getTokenValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 3, token3.getTokenValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
	}

	@Test
	public void testCommentSingleLine2() {
		Buffer buffer = new Buffer( new StringReader( " 3 //2\n   + 4 " ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 3, token1.getTokenValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 4, token3.getTokenValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
	}
	
	@Test
	public void testCommentMultiplyLineComplex() {
		Buffer buffer = new Buffer( new StringReader( " /* 56  */  3  /* 123123 123123 1123 13123 1313 13131 13 131  3131 3123 112*/    /**//*2\n   */+ 4 /**/\n" ) );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 3, token1.getTokenValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 4, token3.getTokenValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
	}	
}

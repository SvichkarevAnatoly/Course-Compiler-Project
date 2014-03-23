package test.java;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import junit.framework.Assert;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

import org.junit.Test;

public class LexerTest extends Assert {
	private final int DEFAULT_SIZE = 5;
	
	@Test
	public void testNumber(){
		Buffer buffer = new Buffer( new StringReader( "42" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 42.0, ((Double)token1.getTokenValue()).doubleValue() );
	}
	
	@Test
	public void testNumberDouble(){
		Buffer buffer = new Buffer( new StringReader( "42.042" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 42.042, ((Double)token1.getTokenValue()).doubleValue() );
	}
	
	@Test
	public void testPlus(){
		Buffer buffer = new Buffer( new StringReader( "+" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.PLUS, token1.getTokenType() );
		assertEquals( "+", token1.getTokenValue() );
	}
	
	@Test
	public void testMinus(){
		Buffer buffer = new Buffer( new StringReader( "-" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.MINUS, token1.getTokenType() );
		assertEquals( "-", token1.getTokenValue() );
	}
	
	@Test
	public void testMultiplication(){
		Buffer buffer = new Buffer( new StringReader( "*" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.MULTIPLICATION, token1.getTokenType() );
		assertEquals( "*", token1.getTokenValue() );
	}
	
	@Test
	public void testDivision(){
		Buffer buffer = new Buffer( new StringReader( "/" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.DIVISION, token1.getTokenType() );
		assertEquals( "/", token1.getTokenValue() );
	}
	
	@Test
	public void testExponential(){
		Buffer buffer = new Buffer( new StringReader( "^" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.EXPONENTIATION, token1.getTokenType() );
		assertEquals( "^", token1.getTokenValue() );
	}
	
	@Test
	public void testBracketOpen(){
		Buffer buffer = new Buffer( new StringReader( "(" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.BRACKET_OPEN, token1.getTokenType() );
		assertEquals( "(", token1.getTokenValue() );
	}
	
	@Test
	public void testBracketClose(){
		Buffer buffer = new Buffer( new StringReader( ")" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.BRACKET_CLOSE, token1.getTokenType() );
		assertEquals( ")", token1.getTokenValue() );
	}
	
	@Test
	public void testEnd(){
		Buffer buffer = new Buffer( new StringReader( "" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.END, token1.getTokenType() );
		assertEquals( "End", token1.getTokenValue() );
	}
	
	@Test
	public void testFromFile1() throws FileNotFoundException {
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer1.myc" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 2.0, ((Double)token1.getTokenValue()).doubleValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		assertEquals( "+", token2.getTokenValue() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 3.0, ((Double)token3.getTokenValue()).doubleValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		assertEquals( "End", token4.getTokenValue() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
		assertEquals( "End", token5.getTokenValue() );
	}
	
	@Test
	public void testFromFile2() throws FileNotFoundException {
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer2.myc" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 2.0, ((Double)token1.getTokenValue()).doubleValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		assertEquals( "+", token2.getTokenValue() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 3.0, ((Double)token3.getTokenValue()).doubleValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		assertEquals( "End", token4.getTokenValue() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
		assertEquals( "End", token5.getTokenValue() );
	}

	@Test
	public void testFromFile3() throws FileNotFoundException {
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer3.myc" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 3.0, ((Double)token1.getTokenValue()).doubleValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		assertEquals( "+", token2.getTokenValue() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 4.0, ((Double)token3.getTokenValue()).doubleValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		assertEquals( "End", token4.getTokenValue() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
		assertEquals( "End", token5.getTokenValue() );
	}
	
	@Test
	public void testFromFile4() throws FileNotFoundException {
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer4.myc" ), DEFAULT_SIZE );
		Lexer lexer = new Lexer( buffer );
		
		Token<?> token1 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token1.getTokenType() );
		assertEquals( 3.0, ((Double)token1.getTokenValue()).doubleValue() );
		
		Token<?> token2 = lexer.getToken();
		assertEquals( TokenType.PLUS, token2.getTokenType() );
		assertEquals( "+", token2.getTokenValue() );
		
		Token<?> token3 = lexer.getToken();
		assertEquals( TokenType.NUMBER, token3.getTokenType() );
		assertEquals( 4.0, ((Double)token3.getTokenValue()).doubleValue() );
		
		Token<?> token4 = lexer.getToken();
		assertEquals( TokenType.END, token4.getTokenType() );
		assertEquals( "End", token4.getTokenValue() );
		
		Token<?> token5 = lexer.getToken();
		assertEquals( TokenType.END, token5.getTokenType() );
		assertEquals( "End", token5.getTokenValue() );
	}	
}

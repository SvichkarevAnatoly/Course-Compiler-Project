package test.java;

import java.io.FileNotFoundException;
import java.io.FileReader;

import junit.framework.Assert;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.lexer.Token;
import main.ru.svichkarev.compiler.lexer.TokenType;

import org.junit.Test;

public class LexerTest extends Assert {

	@Test
	public void testFromFile1() throws FileNotFoundException {
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer1.myc" ), 5 );
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
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer2.myc" ), 5 );
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
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer3.myc" ), 5 );
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
		Buffer buffer = new Buffer( new FileReader( "test/resources/testLexer4.myc" ), 5 );
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

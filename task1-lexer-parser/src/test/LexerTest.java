package test;

import static org.junit.Assert.*;

import java.io.StringReader;

import main.ru.svichkarev.compiler.buffer.Buffer;

import org.junit.Test;

public class LexerTest {

	@Test
	public void test() {
		Buffer buffer = new Buffer( new StringReader("AA"), 10 );
		
		assertEquals( 'A', buffer.getChar() );
	}

}

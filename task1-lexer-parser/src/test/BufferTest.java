package test;


import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import main.ru.svichkarev.compiler.buffer.Buffer;

import org.junit.Test;

public class BufferTest {
	private final int DEFAULT_SIZE = 10;
	
	@Test
	public void testEmptyBuffer() {
		Buffer buffer = new Buffer( new StringReader(""), DEFAULT_SIZE );
		assertEquals( 'A', buffer.getChar() );
	}

	@Test
	public void test2() {
		Buffer buffer = new Buffer( new StringReader("AA"), DEFAULT_SIZE );
		assertEquals( 'A', buffer.getChar() );
	}
	
}

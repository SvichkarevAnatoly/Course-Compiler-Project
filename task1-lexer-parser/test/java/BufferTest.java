package test.java;


import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import main.ru.svichkarev.compiler.buffer.Buffer;

import org.junit.Test;

public class BufferTest {
	private final int END_OF_SOURCE_CODE = -1;
	private final int DEFAULT_SIZE = 10;
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCapacity() {
		Buffer buffer = new Buffer( new StringReader("AA"), 0 );
		buffer.getChar();
	}
	
	@Test
	public void testEmptyBuffer() {
		Buffer buffer = new Buffer( new StringReader(""), DEFAULT_SIZE );
		assertEquals( END_OF_SOURCE_CODE, buffer.peekChar() );
	}

	@Test
	public void testGetCharOneChar() {
		Buffer buffer = new Buffer( new StringReader("A"), DEFAULT_SIZE );
		assertEquals( 'A', buffer.getChar() );
	}
	
	@Test
	public void testPeekCharOneChar() {
		Buffer buffer = new Buffer( new StringReader("A"), DEFAULT_SIZE );
		assertEquals( 'A', buffer.peekChar() );
	}
	
}

package test;

import junit.framework.TestCase;
import main.ru.svichkarev.compiler.buffer.Buffer;
import org.junit.Test;

import java.io.StringReader;

public class BufferTest extends TestCase {
	private final int END_OF_SOURCE_CODE = -1;
	private final int DEFAULT_SIZE = 10;

    /*
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCapacity() {
		Buffer buffer = new Buffer( new StringReader("AA"), 0 );
		buffer.getChar();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalCapacity2() {
		Buffer buffer = new Buffer( new StringReader("AA"), 1 );
		buffer.getChar();
	}*/
	
	@Test
	public void testTwoCapacity() {
		Buffer buffer = new Buffer( new StringReader("AB"), 2 );
		assertEquals( 'A', buffer.peekChar() );
		assertEquals( 'B', buffer.peekSecondChar() );
		
		assertEquals( 'A', buffer.getChar() );
		assertEquals( 'B', buffer.peekChar() );
		assertEquals( END_OF_SOURCE_CODE, buffer.peekSecondChar() );
		
		assertEquals( 'B', buffer.getChar() );
		assertEquals( END_OF_SOURCE_CODE, buffer.peekChar() );
		assertEquals( END_OF_SOURCE_CODE, buffer.peekSecondChar() );
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

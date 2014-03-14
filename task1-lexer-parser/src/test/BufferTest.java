package test;


import static org.junit.Assert.fail;

import java.io.StringReader;

import main.ru.svichkarev.compiler.buffer.Buffer;

import org.junit.Test;

import static org.junit.Assert.*;

public class BufferTest {

	@Test
	public void test() {
		
		Buffer buffer = new Buffer( new StringReader("SDFSDFFADFASDFASDF"), 10 ); 
		
		assertEquals( 'A', buffer.getChar() );
		
	}

}

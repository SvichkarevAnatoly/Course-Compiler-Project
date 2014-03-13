package test;


import static org.junit.Assert.fail;

import java.io.StringReader;

import main.Buffer;

import org.junit.Test;

import static org.junit.Assert.*;

public class BufferTest {

	@Test
	public void test() {
		
		Buffer buffer = new Buffer(new StringReader("SDFSDFFADFASDFASDF")); 
		
		assertEquals('A',buffer.getChar());
		
	}

}

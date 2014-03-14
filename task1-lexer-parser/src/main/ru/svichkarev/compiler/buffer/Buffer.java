package main.ru.svichkarev.compiler.buffer;

import java.io.Reader;

public class Buffer {

	private Reader reader;
	
	public Buffer( Reader reader ) {
		this.reader = reader;
	}
	
	public char getChar() {
		return 'A';
	}	
}
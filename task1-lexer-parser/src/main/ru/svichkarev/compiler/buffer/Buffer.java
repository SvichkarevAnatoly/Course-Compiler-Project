package main.ru.svichkarev.compiler.buffer;

import java.io.Reader;

public class Buffer {
	final static int END_OF_SOURCE_CODE = -1;
	
	private Reader reader;
	private int maxIndex = 0;
	private char localBuf[];
	
	private int indexBuf = 0;
	
	private boolean isEndSourseCode = false;
	
	public Buffer( Reader reader, int capacity ) {
		if( capacity < 1 ){
			throw new IllegalArgumentException( "capacity must be more than 0" );
		}
		
		this.reader = reader;
		localBuf = new char[ capacity ];
		
		writeInBuffer();
	}
	
	// TODO: может ли здесь быть конец файла?
	public char getChar() {
		if( isBufferEnded() ){
			writeInBuffer();
		}
		
		return localBuf[ indexBuf++ ];
	}
	
	// Не сдвигаем указатель
	public int peekChar(){
		// TODO: нужно ли каждый раз проверять?
		if( isBufferEnded() ){
			writeInBuffer();
		}
		if( isEndSourseCode ){
			return END_OF_SOURCE_CODE;
		}
		
		return localBuf[ indexBuf ];
	}

	private boolean isBufferEnded() {
		return (indexBuf == maxIndex);
	}
	
	private void writeInBuffer() {
		try {
			indexBuf = 0; // TODO: что раньше нужно?
			int numCharRead = reader.read( localBuf ); // TODO: посмотреть его поведение
			if( numCharRead == END_OF_SOURCE_CODE ){ // закончился исходник
				isEndSourseCode = true;
			} else{
				// новая граница буфера
				maxIndex = numCharRead;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
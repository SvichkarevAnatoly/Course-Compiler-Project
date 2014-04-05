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
		if( capacity < 2 ){
			throw new IllegalArgumentException( "capacity must be more than 1" );
		}
		
		this.reader = reader;
		localBuf = new char[ capacity ];
		
		writeInBuffer();
	}
	
	public char getChar() {
		if( isBufferEnded( 0 ) ){
			writeInBuffer();
		}
		
		return localBuf[ indexBuf++ ];
	}
	
	// Не сдвигаем указатель
	public int peekChar(){
		if( isBufferEnded( 0 ) ){
			writeInBuffer();
		}
		if( isEndSourseCode ){
			return END_OF_SOURCE_CODE;
		}
		
		return localBuf[ indexBuf ];
	}

	// просмотр на два символа вперёд без сдвига указателя
	public int peekSecondChar(){
		if( isBufferEnded( 1 ) ){
			writeInBuffer();
		}
		
		if( isEndSourseCode ){
			return END_OF_SOURCE_CODE;
		}
		
		return localBuf[ indexBuf + 1 ];
	}
	
	// проверка окончания буфера при смещении на shift == 0 или 1(при просмотре через 1)
	private boolean isBufferEnded( int shift ) {
		return (indexBuf + shift == maxIndex);
	}
	
	private void writeInBuffer() {
		// если мы выполняем обновление буфера из-за peekSecondChar,
		//то нужно скопировать последний символ вначало
		if( isBufferEnded( 1 ) && !isBufferEnded( 0 )  ){
			localBuf[ 0 ] = localBuf[ indexBuf ];
			indexBuf = 1;
		} else{
			indexBuf = 0;
		}
		
		try{
			int numCharRead = reader.read( localBuf, indexBuf, localBuf.length - indexBuf );
			if( numCharRead == END_OF_SOURCE_CODE ){ // закончился исходник
				isEndSourseCode = true;
			} else{
				// новая граница буфера
				maxIndex = numCharRead + indexBuf;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
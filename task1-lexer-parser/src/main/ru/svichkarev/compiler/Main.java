package main.ru.svichkarev.compiler;

import java.io.StringReader;

import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Parser;

public final class Main {
	public static void main(String[] args) {
		Buffer buffer = new Buffer( new StringReader("5+5+101-54-100"), 5 );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		int globResult = parser.parseExpr();
		
		System.out.println( globResult );
		
		System.out.println("hello");
	}
}
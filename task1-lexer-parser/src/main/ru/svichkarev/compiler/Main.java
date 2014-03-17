package main.ru.svichkarev.compiler;

import java.io.StringReader;

import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Parser;

public final class Main {
	public static void main(String[] args) {
		Buffer buffer = new Buffer( new StringReader("(1+-1) + (2+3)*2 + 2^2^(1*2) "), 5 );
		// TODO: при таком вводе выдаёт 10.0: "(1+-1) + (2+3)*2 + 2^2^(1*2) "
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		double globResult = parser.parseExpr();
		
		System.out.println( globResult );
		
		System.out.println("hello");
	}
}
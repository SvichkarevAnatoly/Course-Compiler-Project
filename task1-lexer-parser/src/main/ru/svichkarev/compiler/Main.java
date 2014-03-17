package main.ru.svichkarev.compiler;

import java.io.StringReader;
import java.util.Scanner;

import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Parser;

public final class Main {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		while( sc.hasNext() ){
			String str = sc.nextLine();
			if( str.compareTo("exit") == 0 || str.isEmpty()){
				break;
			}
			
			Buffer buffer = new Buffer( new StringReader( str ), 5 );
			Lexer lexer = new Lexer( buffer );
			Parser parser = new Parser( lexer );
			
			double globResult = parser.parseExpr();
			
			System.out.println( globResult );
		}
		
		sc.close();
		
		System.out.println("hello");
	}
}
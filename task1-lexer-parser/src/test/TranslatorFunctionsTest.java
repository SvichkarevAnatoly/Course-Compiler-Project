package test;

import junit.framework.TestCase;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.parser.Parser;
import main.ru.svichkarev.compiler.translator.Translator;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

public class TranslatorFunctionsTest extends TestCase {
	// пример простой программы
	final static String etalonStr =
            "int foo(){" +
            "   return 45;" +
            "}" +
			"int main(){" +
			"	int a;" +
			"	print( a );" +
			"}"; 
	
	// начало шаблона
	final static String outputEtalonBeginStr = 
			".class public Expr\n" +
			".super java/lang/Object\n" +

			".method public <init>()V\n" +
			"   .limit stack 1\n" +
			"   .limit locals 1\n" +
			"   aload_0\n" +
			"   invokespecial java/lang/Object/<init>()V\n" +
			"   return\n" +
			".end method\n" +

			".method public static main([Ljava/lang/String;)V\n" +
			"   .limit stack 10\n" +
			"   .limit locals 2\n";
	
	// конец шаблона
	final static String outputEtalonEndStr = 
			"   istore_1\n" +
			"   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
			"   iload_1\n" +
			"   invokevirtual java/io/PrintStream/println(I)V\n" +
			"   return\n" +
			".end method\n";

    @Ignore("Not Ready to Run")
	@Test
	public void testReturnNumberFunctionProgram() {
        String inputStr = etalonStr;
        String outputEtalonStr = outputEtalonBeginStr + outputEtalonEndStr;
		
		Buffer buffer = new Buffer( new StringReader( inputStr ) );
		Lexer lexer = new Lexer( buffer );
		Parser parser = new Parser( lexer );
		
		Node realTree = parser.parseProgram();
		
		Writer writer = new StringWriter();
		Translator translator = new Translator( realTree, writer );
		
		translator.translateProgram();
		String outputStr = writer.toString();

		assertEquals( outputEtalonStr, outputStr );
	}

    @Test
    public void testManyVariablesFunction() {
        // пример простой программы
        final String inputSource =
                "int main(){" +
                "	int a;" +
                "	int b;" +
                "	int c;" +
                "}";

        Buffer buffer = new Buffer( new StringReader( inputSource ) );
        Lexer lexer = new Lexer( buffer );
        Parser parser = new Parser( lexer );

        Node realTree = parser.parseProgram();

        Writer writer = new StringWriter();
        Translator translator = new Translator( realTree, writer );

        translator.translateProgram();
        String outputStr = writer.toString();

        //assertEquals( outputEtalonStr, outputStr );
    }
}
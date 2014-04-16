package test.java;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.Assert;
import main.ru.svichkarev.compiler.buffer.Buffer;
import main.ru.svichkarev.compiler.lexer.Lexer;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.parser.Parser;
import main.ru.svichkarev.compiler.translator.Translator;

import org.junit.Test;

public class TranslatorOneExprTest extends Assert {
	// начало шаблона нашего языка
	final static String inputEtalonBeginStr = 
			"int main(){" +
			"	int a;";
	// конец шаблона нашего языка
	final static String inputEtalonEndStr =
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
	
	@Test
	public void testNumberProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				outputEtalonEndStr;
		
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
	public void testPlusProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 + 7;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   iadd\n" +
				outputEtalonEndStr;            
		
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
	public void testMinusProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 - 7;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   isub\n" +
				outputEtalonEndStr;            
		
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
	public void testUnarMinusProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = - 42;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   ineg\n" +
				outputEtalonEndStr;
		
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
	public void testMultiplicationProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 * 7;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   imul\n" +
				outputEtalonEndStr;            
		
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
	public void testDivisionProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 / 7;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   idiv\n" +
				outputEtalonEndStr;
		
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
	public void testSimpleExprProgram() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 + 7 - 5;" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   iadd\n" +
				"   sipush 5\n" +
				"   isub\n" +
				outputEtalonEndStr;
		
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
	public void testSimpleExpr2Program() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 + (7 - 5);" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   sipush 5\n" +
				"   isub\n" +
				"   iadd\n" +
				outputEtalonEndStr;
		
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
	public void testSimpleExpr3Program() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 42 * (7 - 5);" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 42\n" +
				"   sipush 7\n" +
				"   sipush 5\n" +
				"   isub\n" +
				"   imul\n" +
				outputEtalonEndStr;
		
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
	public void testDifficultExpr1Program() {
		// собираем входную строку на нашем языке
		final String inputStr = 
				inputEtalonBeginStr +
				"	a = 126 / 42 * -(7 - 5);" +
				inputEtalonEndStr;
		
		// собираем эталонную строку
		final String outputEtalonStr =
				outputEtalonBeginStr +
				"   sipush 126\n" +
				"   sipush 42\n" +
				"   idiv\n" +
				"   sipush 7\n" +
				"   sipush 5\n" +
				"   isub\n" +
				"   ineg\n" +
				"   imul\n" +
				outputEtalonEndStr;
		
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
}
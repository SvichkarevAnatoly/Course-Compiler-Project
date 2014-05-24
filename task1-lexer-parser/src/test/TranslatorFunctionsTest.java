package test;

import junit.framework.TestCase;

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

    /*@Ignore("Not Ready to Run")
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
	}*/

    /*@Ignore("Not Ready to Run")
    @Test
    public void testManyVariablesFunction() {
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
    }*/

    /*
    @Test
    public void testParlistVariablesAddTableFunction() {
        final String inputSource =
                "int foo(int ar, double be, int as){" +
                "   return 45;" +
                "}" +
                "void main(){" +
                "	int a;" +
                "   a = 7;" +
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
    }*/

    /*@Ignore()
    @Test
    public void testInitializationVariableFunction() {
        final String inputSource =
                "int main(){" +
                "	int a;" +
                "   a = 7;" +
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
    }*/

    /*
    @Test
    public void testTwoVariablesPrintFunction() {
        final String inputSource =
                "void main(){\n"+
                "    int a;\n"+
                "    a = 5;\n"+
                "    int b;\n"+
                "    b = 3;\n"+
                "    a = a + b;\n"+
                "    print( a );\n"+
                "    print( b );\n"+
                "}";

        Buffer buffer = new Buffer( new StringReader( inputSource ) );
        Lexer lexer = new Lexer( buffer );
        Parser parser = new Parser( lexer );

        Node realTree = parser.parseProgram();

        Writer writer = new StringWriter();
        Translator translator = new Translator( realTree, writer );

        translator.translateProgram();
        String outputStr = writer.toString();

        final String outputSource =
                ".class public test\n" +
                ".super java/lang/Object\n" +
                ".method public <init>()V\n" +
                "   .limit stack 1\n" +
                "   .limit locals 1\n" +
                "   aload_0\n" +
                "   invokespecial java/lang/Object/<init>()V\n" +
                "   return\n" +
                ".end method\n" +
                "\n" +
                ".method public static main([Ljava/lang/String;)V\n" +
                "   .limit stack 100\n" +
                "   .limit locals 3\n" +
                "   sipush 5\n" +
                "   istore_1\n" +
                "   sipush 3\n" +
                "   istore_2\n" +
                "   iload_1\n" +
                "   iload_2\n" +
                "   iadd\n" +
                "   istore_1\n" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   iload_1\n" +
                "   invokevirtual java/io/PrintStream/println(I)V\n" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   iload_2\n" +
                "   invokevirtual java/io/PrintStream/println(I)V\n" +
                "   return\n" +
                ".end method";

        assertEquals( outputSource, outputStr );
    }
    */

    /*
    @Test
    public void testTwoVariablesPrintFunction() {
        final String inputSource =
                "void main(){\n"+
                "    int a;\n"+
                "    a = 5;\n"+
                "    int b;\n"+
                "    b = 3;\n"+
                "    a = a + b;\n"+
                "    print( a );\n"+
                "    print( b );\n"+
                "}";

        Buffer buffer = new Buffer( new StringReader( inputSource ) );
        Lexer lexer = new Lexer( buffer );
        Parser parser = new Parser( lexer );

        Node realTree = parser.parseProgram();

        Writer writer = new StringWriter();
        Translator translator = new Translator( realTree, writer );

        translator.translateProgram();
        String outputStr = writer.toString();

        final String outputSource =
                ".class public test\n" +
                ".super java/lang/Object\n" +
                ".method public <init>()V\n" +
                "   .limit stack 1\n" +
                "   .limit locals 1\n" +
                "   aload_0\n" +
                "   invokespecial java/lang/Object/<init>()V\n" +
                "   return\n" +
                ".end method\n" +
                "\n" +
                ".method public static main([Ljava/lang/String;)V\n" +
                "   .limit stack 100\n" +
                "   .limit locals 3\n" +
                "   sipush 5\n" +
                "   istore_1\n" +
                "   sipush 3\n" +
                "   istore_2\n" +
                "   iload_1\n" +
                "   iload_2\n" +
                "   iadd\n" +
                "   istore_1\n" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   iload_1\n" +
                "   invokevirtual java/io/PrintStream/println(I)V\n" +
                "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                "   iload_2\n" +
                "   invokevirtual java/io/PrintStream/println(I)V\n" +
                "   return\n" +
                ".end method";

        assertEquals( outputSource, outputStr );
    }
    */

    /*
    int foo(){
        int a;
        a = 3;
        return 7+a;
    }
    void main(){
        int a;
        a = foo()+foo();
        print( a );
    }
    */

    /*
    .class public test
.super java/lang/Object
.method public <init>()V
   .limit stack 1
   .limit locals 1
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

.method public static foo()I
   .limit stack 100
   .limit locals 1
   sipush 3
   istore_0
   sipush 7
   iload_0
   iadd
   ireturn
.end method

.method public static main([Ljava/lang/String;)V
   .limit stack 100
   .limit locals 2
   invokestatic test/foo()I
   invokestatic test/foo()I
   iadd
   istore_1
   getstatic java/lang/System/out Ljava/io/PrintStream;
   iload_1
   invokevirtual java/io/PrintStream/println(I)V
   return
.end method
     */

    /*
    int foo( int k, int kk ){
    return k + kk;
}
void main(){
    int a;
    a = foo( foo( 2, 5 ), 3 );
    print( a );
}
     */
}
package main.ru.svichkarev.compiler.translator;

import main.ru.svichkarev.compiler.parser.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class Translator {
	private Node tree;
	private Writer writer;

    // TODO: таблица функций
    // TODO: таблица переменных для каждой функции

	public Translator( Node inputTree, Writer writer ){
		tree = inputTree;
		this.writer = writer;
	}
	
	// главный метод - перевода программы в байт код
	public void translateProgram() {
        // TODO: делаем всякие подсчёты
        //вставим начало шаблона
        try {
            writer.write(
                    ".class public Expr\n" +
                            ".super java/lang/Object\n" +

                            ".method public <init>()V\n" +
                            "   .limit stack 1\n" +
                            "   .limit locals 1\n" +
                            "   aload_0\n" +
                            "   invokespecial java/lang/Object/<init>()V\n" +
                            "   return\n" +
                            ".end method\n"/* +

                            ".method public static main([Ljava/lang/String;)V\n" +
                            "   .limit stack 10\n" +
                            "   .limit locals 2\n"*/
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<Node> functions = tree.getChildren();
        for (Iterator<Node> it = functions.iterator(); it.hasNext(); ) {
            Node node = it.next();
            translateFunction(node);
        }

        // TODO: проверка, чтобы был метод main, иначе кидать ошибку

        /*
		// выделяем нужную команду
		Node neededCommand = null;
		neededCommand = tree.getChildren().get(0); // function
		neededCommand = neededCommand.getChildren().get(2); // body
		neededCommand = neededCommand.getChildren().get(1); // var = expr
		
		// часть необходимой функциональности
		translateCommand( neededCommand );
		
		// TODO: делаем ещё что-нибудь
		//вставляем конец шаблона
		
		try {
			writer.write( 
					"   istore_1\n" +
					"   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
					"   iload_1\n" +
					"   invokevirtual java/io/PrintStream/println(I)V\n" +
					"   return\n" +
					".end method\n"
					);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    }

    private void translateFunction(Node functionNode) {
        // разбираем возвращаемое значение и аргументы, выводим протатип функции
        Node returnTypeNode = functionNode.getChildren().get(0);
        Node parlistNode = functionNode.getChildren().get(1);
        Node bodyNode = functionNode.getChildren().get(2);

        // пишем заголовок метода
        String descriptorFunction = ".method public static ";
        descriptorFunction += functionNode.getValue().getTokenValue() + "(";

        // разбираем аргументы
        List<Node> params = parlistNode.getChildren();
        for (Iterator<Node> it = params.iterator(); it.hasNext(); ) {
            Node param = it.next();
            // получили название типа
            descriptorFunction += param.getChildren().get(0).getValue().getTokenValue();
        }

        descriptorFunction += ")" + returnTypeNode.getValue().getTokenValue() + "\n";
        try {
            writer.write( descriptorFunction );
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        // разбираем тело
        List<Node> commands = bodyNode.getChildren();
        for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
            Node command = iterator.next();
            translateCommand( command );
        }

        // закрываем метод
        try {
            writer.write(
                "   return\n" +
                ".end method\n"
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // транслирует только одну команду
	private void translateCommand( Node commandNode ){
		// определяем тип команды



		// var = expr;
		
		// дойдём до expr
		Node exprNode = commandNode.getChildren().get(2);
		
		// запускаем разбор поддерева выражения
		try {
			translateExpr( exprNode );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// TODO: простая реализация, без вызова методов
	//(и пока без возведения в степень, потому что нужно нужно функцию вызывать)
	// разбираем поддерево выражения
	private void translateExpr( Node exprNode ) throws IOException{
		// рекурсивная функция, работающая со стеком
		switch ( exprNode.getValue().getTokenType() ) {
		case PLUS:
			// рекурсивно вызываем для левого и правого поддерева
			translateExpr(exprNode.getChildren().get(0));
			translateExpr(exprNode.getChildren().get(1));
			writer.write( "   iadd\n" );
			break;
		case MINUS:
			// если унарный
			if( exprNode.getChildren().size() == 1 ){
				translateExpr(exprNode.getChildren().get(0));
				writer.write( "   ineg\n" );
			} else{
				translateExpr(exprNode.getChildren().get(0));
				translateExpr(exprNode.getChildren().get(1));
				writer.write( "   isub\n" );
			}
			break;
		case MULTIPLICATION:
			translateExpr(exprNode.getChildren().get(0));
			translateExpr(exprNode.getChildren().get(1));
			writer.write( "   imul\n" );
			break;
		case DIVISION:
			translateExpr(exprNode.getChildren().get(0));
			translateExpr(exprNode.getChildren().get(1));
			writer.write( "   idiv\n" );
			break;
		case NUMBER:
			//range -32768 to 32767
			writer.write( "   sipush " + exprNode.getValue().getTokenValue().toString() + "\n" );
			break;
			
		default:
			// TODO: кидаем ошибку
			break;
		}
	}
}

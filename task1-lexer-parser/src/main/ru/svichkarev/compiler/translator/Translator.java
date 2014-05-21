package main.ru.svichkarev.compiler.translator;

import main.ru.svichkarev.compiler.lexer.TokenType;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.translator.table.FunctionInfo;
import main.ru.svichkarev.compiler.translator.table.TableFunctions;
import main.ru.svichkarev.compiler.translator.table.TableVariables;
import main.ru.svichkarev.compiler.translator.table.VariableInfo;

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
        // создаём таблицу функций
        TableFunctions tf = new TableFunctions();

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
            Node function = it.next();
            translateFunction( function, tf );
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

    private void translateFunction(Node functionNode, TableFunctions tf) {
        // создаём таблицу переменных
        TableVariables tv = new TableVariables();

        // разбираем возвращаемое значение и аргументы, выводим протатип функции
        Node returnTypeNode = functionNode.getChildren().get(0).getChildren().get(0);
        Node parlistNode = functionNode.getChildren().get(1);
        Node bodyNode = functionNode.getChildren().get(2);

        // пишем заголовок метода
        String descriptorFunction = ".method public static ";
        descriptorFunction += functionNode.getValue().getTokenValue() + "(";

        // TODO: каждый аргумент - локальная переменная, добавить в таблицу переменных
        // разбираем аргументы
        List<Node> params = parlistNode.getChildren();
        if( ! params.get(0).match( TokenType.EMPTY ) ) {
            for (Iterator<Node> it = params.iterator(); it.hasNext(); ) {
                Node param = it.next();
                // получили название типа
                // TODO: нужно ставить запятую ещё
                descriptorFunction += param.getChildren().get(0).getChildren().get(0).getValue().getTokenValue();
            }
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
            translateCommand( command, tv, tf );
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

        // TODO: после успешной трансляции функции, добавляем его в таблицу функций
    }

    // транслирует команду
	private void translateCommand(Node commandNode, TableVariables tv, TableFunctions tf){
        List<Node> operands = commandNode.getChildren();

		// определяем тип команды:
        // TODO: добавить ещё if и while
        Node operand = operands.get(0);
        switch( operand.getTokenType() ){
            // объявление переменной
            case TYPE:
                // добавляем запись в таблицу переменных
                VariableInfo var = new VariableInfo( VariableInfo.VariableType.convertFromTokenType( operand.getFirstChildren().getTokenType() ),
                        tv.getLastLocalsIndex() );

                tv.add( (String)operands.get(1).getTokenValue(), var );

                break;
            // var = expr;
            case NAME:
                // проверка, что переменная объявлена
                // если объявлена, то в конце проставить инициализацию
                String varName = (String) operand.getTokenValue();
                if( ! tv.isDeclared(varName) ){
                    // должна быть объявлена
                    throw new RuntimeException("Variable must be declared");
                } else{
                    tv.setInitialization( varName );
                }

                // дойдём до expr
                Node exprNode = commandNode.getChildren().get(2);

                // запускаем разбор поддерева выражения
                try {
                    translateExpr( exprNode );
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // выполнить присвоение( снять со стека значение и положить в переменную )
                int index = tv.getLocalIndex(varName);
                try {
                    // TODO: здесь не должно этого быть, это синтаксис
                    if( index < 4 ){
                        writer.write( "   istore_" + index + "\n" );
                    } else {
                        writer.write("   istore " + index + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case RETURN:
                // проверка, какой тип должна вернуть функция( из таблицы функций )
                FunctionInfo.FunctionReturnType retType = tf.getCurrentFunctionReturnType();

                switch ( retType ){
                    case VOID:
                        // просто пишем возврат из функции
                        try {
                            writer.write( "   return\n" );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case DOUBLE:
                        VariableInfo.VariableType exprType;
                        // транслируем дальнейшее выражение
                        exprNode = commandNode.getChildren().get(1);

                        // запускаем разбор поддерева выражения
                        try {
                            exprType = translateExpr( exprNode );
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    case INT:
                        if( exprType == VariableInfo.VariableType.DOUBLE ){
                            try {
                                writer.write( "   dreturn\n" );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                writer.write( "   ireturn\n" );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                }

                break;
            case PRINT: // TODO: вспомнить как реализовывал
                break;
            // последняя пустая команда, просто пропускаем
            case EMPTY:
                break;
            default:
                // недопустимый тип команды
                throw new RuntimeException("Invalid type of command");
        }
	}
	
	// TODO: простая реализация, без вызова методов
	//(и пока без возведения в степень, потому что нужно нужно функцию вызывать)
	// разбираем поддерево выражения
	private VariableInfo.VariableType translateExpr( Node exprNode ) throws IOException{
        // как минимум будет INT, но возможно расширение
        VariableInfo.VariableType exprType = VariableInfo.VariableType.INT;

		// рекурсивная функция, работающая со стеком
		switch ( exprNode.getValue().getTokenType() ) {
		case PLUS:
			// рекурсивно вызываем для левого и правого поддерева
			exprType = translateExpr(exprNode.getChildren().get(0));
            if( exprType == VariableInfo.VariableType.INT ){
                exprType = translateExpr(exprNode.getChildren().get(1));
            } else {
                translateExpr(exprNode.getChildren().get(1));
            }

            // TODO: команда для нужного типа
            // TODO: сделать обёртку для строкового представления команд
			writer.write( "   iadd\n" );

            return exprType;
		case MINUS:
			// если унарный
			if( exprNode.getChildren().size() == 1 ){
				exprType = translateExpr(exprNode.getChildren().get(0));
				writer.write( "   ineg\n" );
                return exprType;
			} else{
				exprType = translateExpr(exprNode.getChildren().get(0));
				if( exprType == VariableInfo.VariableType.INT ){
                    exprType = translateExpr(exprNode.getChildren().get(1));
                } else {
                    translateExpr(exprNode.getChildren().get(1));
                }
				writer.write( "   isub\n" );
                return exprType;
			}
		case MULTIPLICATION:
			exprType = translateExpr(exprNode.getChildren().get(0));
            if( exprType == VariableInfo.VariableType.INT ) {
                exprType = translateExpr(exprNode.getChildren().get(1));
            } else {
                translateExpr(exprNode.getChildren().get(1));
            }
			writer.write( "   imul\n" );
			return exprType;
		case DIVISION:
			exprType = translateExpr(exprNode.getChildren().get(0));
            if( exprType == VariableInfo.VariableType.INT ) {
                exprType = translateExpr(exprNode.getChildren().get(1));
            } else {
                translateExpr(exprNode.getChildren().get(1));
            }
			writer.write( "   idiv\n" );
			return exprType;
        // TODO: нужно конкретно определить тип
		case NUMBER:
			//range -32768 to 32767
			writer.write( "   sipush " + exprNode.getValue().getTokenValue().toString() + "\n" );
			// TODO: исправить
			return exprType;
		default:
			throw new RuntimeException("TR: Невозможно распарсить выражение");
		}
	}
}

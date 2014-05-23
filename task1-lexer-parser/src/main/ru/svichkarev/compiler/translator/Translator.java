package main.ru.svichkarev.compiler.translator;

import main.ru.svichkarev.compiler.lexer.TokenType;
import main.ru.svichkarev.compiler.parser.Node;
import main.ru.svichkarev.compiler.translator.table.FunctionInfo;
import main.ru.svichkarev.compiler.translator.table.TableFunctions;
import main.ru.svichkarev.compiler.translator.table.TableVariables;
import main.ru.svichkarev.compiler.translator.table.VariableInfo;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Translator {
	private Node tree;
	private Writer writer;

    private Writer tmpWriter;
    private String className;

	public Translator( Node inputTree, Writer writer, String className ){
		tree = inputTree;
		this.writer = writer;
        if( className == null ){
            this.className = "out";
        }
        this.className = className;
        tmpWriter = new StringWriter();
	}

    // TODO: заменить всё на первый вариант
    public Translator( Node inputTree, Writer writer ){
        tree = inputTree;
        this.writer = writer;
        this.className = "test";
    }

	// главный метод - перевода программы в байт код
	public void translateProgram() {
        // создаём таблицу функций
        TableFunctions tf = new TableFunctions();

        // TODO: делаем всякие подсчёты
        //вставим начало шаблона
        try {
            writer.write(
                    ".class public " + className + "\n" +
                    ".super java/lang/Object\n" +

                    ".method public <init>()V\n" +
                    "   .limit stack 1\n" +
                    "   .limit locals 1\n" +
                    "   aload_0\n" +
                    "   invokespecial java/lang/Object/<init>()V\n" +
                    "   return\n" +
                    ".end method\n\n"/* +

                    ".method public static main([Ljava/lang/String;)V\n" +
                    "   .limit stack 10\n" +
                    "   .limit locals 2\n"*/
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<Node> functions = tree.getChildrens();
        for (Iterator<Node> it = functions.iterator(); it.hasNext(); ) {
            Node function = it.next();
            translateFunction( function, tf );
        }

        // если нет метода main, то кидать ошибку
        if( ! tf.hasMain() ){
            throw new RuntimeException( "TR: Нет метода main - точки входа" );
        }

        /*try {
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
        String functionName = (String) functionNode.getTokenValue();

        // разбираем возвращаемое значение и аргументы, выводим протатип функции
        FunctionInfo.FunctionReturnType returnType = FunctionInfo.FunctionReturnType.convertFromTokenType( functionNode.getFirstChildren().getFirstChildren().getTokenType() );
        Node parlistNode = functionNode.getChildren(1);
        Node bodyNode = functionNode.getChildren(2);

        // пишем заголовок метода
        String descriptorFunction = ".method public static ";
        descriptorFunction += functionNode.getTokenValue() + "(";

        // TODO: возможно неправильно разбираются имена переменных
        // разбираем аргументы
        // каждый аргумент - локальная переменная, добавить в таблицу переменных
        Vector<VariableInfo.VariableType> paramsVec = new Vector<VariableInfo.VariableType>();
        List<Node> params = parlistNode.getChildrens();
        if( ! params.get(0).match( TokenType.EMPTY ) ) {
            for (Iterator<Node> it = params.iterator(); it.hasNext(); ) {
                Node param = it.next();
                // получили название типа
                Node argType = param.getFirstChildren().getFirstChildren();
                descriptorFunction += argType.getTokenValue();

                // добавление к таблице переменных
                VariableInfo arg = new VariableInfo(argType.getTokenType());
                tv.add((String) param.getTokenValue(), arg );

                // заполнение вектора аргументов для таблицы функции
                paramsVec.add( VariableInfo.VariableType.convertFromTokenType( argType.getTokenType() ) );
            }
        }

        // Если прототип void main(), то эта точка входа,
        //нужно переписать по стандарту, подать String
        if( returnType == FunctionInfo.FunctionReturnType.VOID &&
            paramsVec.isEmpty() &&
            functionName.equals("main") ){

            descriptorFunction += "[Ljava/lang/String;";
            // заталкиваем фиктивный INT
            // TODO: может существует решение получше
            VariableInfo stringArgs = new VariableInfo( TokenType.INT );
            tv.add( "args", stringArgs );
        }

        descriptorFunction += ")" + returnType.toString() + "\n";

        try {
            writer.write( descriptorFunction );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // пишем в другой источник, чтобы потом вставить подсчёт размера стека и локальных переменных
        // разбираем тело
        List<Node> commands = bodyNode.getChildrens();
        for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
            Node command = iterator.next();
            translateCommand( command, tv, tf );
        }

        // TODO: подсчёт размера стека
        // подсчёт размера памяти под локальные переменные
        try {
            writer.write(
                    "   .limit stack 100\n" +
                    "   .limit locals " + tv.getLocalSpace() + "\n"
            );
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        // переписываем команды из вторичного writer
        try {
            writer.append(tmpWriter.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: проверка, если функция возвращает значение, то должна быть инструкция return
        // закрываем метод
        // если возвращаемое значение void, то добавить return,
        //иначе будет команда
        try {
            if( returnType == FunctionInfo.FunctionReturnType.VOID ){
                writer.write(
                    "   return\n" +
                    ".end method\n"
                );
            } else{
                writer.write(
                    ".end method\n"
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // добавляем вновь созданную функцию
        FunctionInfo curFunction = new FunctionInfo( returnType, paramsVec );
        tf.add( functionName, curFunction);
    }

    // транслирует команду
	private void translateCommand(Node commandNode, TableVariables tv, TableFunctions tf){
        // Если не пустышка
        if( commandNode.getTokenType() == TokenType.EMPTY ){
            return;
        }

        List<Node> operands = commandNode.getChildrens();

		// определяем тип команды:
        // TODO: добавить ещё if и while
        String varName = null;
        Node operand = operands.get(0);
        switch( operand.getTokenType() ){
            // объявление переменной
            case TYPE:
                // добавляем запись в таблицу переменных
                VariableInfo var = new VariableInfo( operand.getFirstChildren().getTokenType() );
                tv.add( (String)operands.get(1).getTokenValue(), var );

                break;
            // var = expr;
            case NAME:
                // проверка, что переменная объявлена
                // если объявлена, то в конце проставить инициализацию
                varName = (String) operand.getTokenValue();
                if( ! tv.isDeclared(varName) ){
                    // должна быть объявлена
                    throw new RuntimeException("Variable must be declared");
                } else{
                    tv.setInitialization( varName );
                }

                // дойдём до expr
                Node exprNode = commandNode.getChildrens().get(2);
                VariableInfo.VariableType exprType = VariableInfo.VariableType.INT;

                // запускаем разбор поддерева выражения
                try {
                    exprType = translateExpr( exprNode, tv, tf );
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // проверка возможности приведения типов( или расширения )
                if( exprType == VariableInfo.VariableType.DOUBLE &&
                        tv.getType(varName) == VariableInfo.VariableType.INT ){

                    throw new RuntimeException( "TR: Недопустимое приведение: double к int" );
                }
                // TODO: организовать приведение int к double

                // выполнить присвоение( снять со стека значение и положить в переменную )
                try {
                    tmpWriter.write( tv.getStrStore( varName ) );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case RETURN: // TODO
                /*// проверка, какой тип должна вернуть функция( из таблицы функций )
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
                        VariableInfo.VariableType exprType = VariableInfo.VariableType.INT;
                        // транслируем дальнейшее выражение
                        exprNode = commandNode.getChildrens().get(1);

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

                }*/

                break;
            case PRINT:
                // TODO: разница с append
                // TODO: сделать такую же обёртку для вызовов функции, как и у переменных
                // TODO: проверка правильной по типу println
                varName = (String) operands.get(1).getTokenValue();
                try {
                    tmpWriter.write(
                            "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                            tv.getStrLoad( varName ) +
                            "   invokevirtual java/io/PrintStream/println(I)V\n"
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            default:
                // недопустимый тип команды
                throw new RuntimeException("Недопустимый тип команды");
        }
	}
	
	// TODO: простая реализация, без вызова методов
	//(и пока без возведения в степень, потому что нужно нужно функцию вызывать)
	// разбираем поддерево выражения
	private VariableInfo.VariableType translateExpr( Node exprNode, TableVariables tv, TableFunctions tf ) throws IOException{
        // как минимум будет INT, но возможно расширение
        VariableInfo.VariableType exprType = VariableInfo.VariableType.INT;

		// рекурсивная функция, работающая со стеком
		switch ( exprNode.getValue().getTokenType() ) {
		case PLUS:
			// рекурсивно вызываем для левого и правого поддерева
			exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
            if( exprType == VariableInfo.VariableType.INT ){
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
            } else {
                translateExpr(exprNode.getChildrens().get(1), tv, tf);
            }

            // TODO: команда для нужного типа
            // TODO: сделать обёртку для строкового представления команд
			tmpWriter.write( "   iadd\n" );

            return exprType;
		case MINUS:
			// если унарный
			if( exprNode.getChildrens().size() == 1 ){
				exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
				tmpWriter.write( "   ineg\n" );
                return exprType;
			} else{
				exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
				if( exprType == VariableInfo.VariableType.INT ){
                    exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                } else {
                    translateExpr(exprNode.getChildrens().get(1), tv, tf);
                }
				tmpWriter.write( "   isub\n" );
                return exprType;
			}
		case MULTIPLICATION:
			exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
            if( exprType == VariableInfo.VariableType.INT ) {
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
            } else {
                translateExpr(exprNode.getChildrens().get(1), tv, tf);
            }
			tmpWriter.write( "   imul\n" );
			return exprType;
		case DIVISION:
			exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
            if( exprType == VariableInfo.VariableType.INT ) {
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
            } else {
                translateExpr(exprNode.getChildrens().get(1), tv, tf);
            }
			tmpWriter.write( "   idiv\n" );
			return exprType;
        // TODO: нужно конкретно определить тип
		case NUMBER:
			//range -32768 to 32767
			tmpWriter.write( "   sipush " + exprNode.getValue().getTokenValue().toString() + "\n" );
			// TODO: исправить определение типа
			return exprType;
        case NAME:
            String varName = (String) exprNode.getTokenValue();
            tmpWriter.write( tv.getStrLoad( varName ) );
            return tv.getType( varName );
        case CALL_FUNCTION:
            String funcName = (String) exprNode.getChildren(0).getTokenValue();

            // проверка, возвращает ли функция значение
            if( tf.getReturnType( funcName ) == FunctionInfo.FunctionReturnType.VOID ){
                throw new RuntimeException("TR: в выражении не может участвовать функция, возвращающая void");
            } else{
                // сверка фактических и формальных аргументов
                // TODO: доработать сверку
                if( !( tf.getAmountParameters( funcName ) == 0 &&
                        exprNode.getChildrens().get(1).getChildrenCount() == 1) ){ // TODO: всегда ли есть EMPTY?
                    throw new RuntimeException("TR: при вызове " + funcName + " несоответствие фактических и формальных аргументов" );
                }
            }

            return tf.getReturnType( funcName ).convertToVariableType();
		default:
            // TODO: не давать вызывать методы без возвращаемого значения
			throw new RuntimeException("TR: Невозможно распарсить выражение");
		}
	}
}

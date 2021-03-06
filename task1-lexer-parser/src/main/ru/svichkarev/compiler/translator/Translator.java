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
    private ControlGenerator cg = new ControlGenerator();

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
        TableFunctions tf = new TableFunctions( className );

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
        // максимальное смещение
        int maxLocalsShift = 0;

        // TODO: может можно переиспользовать?
        // очистить от прошлой функции временный буфер
        tmpWriter = new StringWriter();

        // разбираем возвращаемое значение и аргументы, выводим протатип функции
        FunctionInfo.FunctionReturnType returnType = FunctionInfo.FunctionReturnType.convertFromTokenType( functionNode.getFirstChildren().getFirstChildren().getTokenType() );
        Node parlistNode = functionNode.getChildren(1);
        Node bodyNode = functionNode.getChildren(2);

        // TODO: для этого можно использовать таблицу функций
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
                arg.setInitialization();
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

        // добавляем вновь созданную функцию
        FunctionInfo curFunction = new FunctionInfo( returnType, paramsVec );
        tf.add( functionName, curFunction);

        // пишем в другой источник, чтобы потом вставить подсчёт размера стека и локальных переменных
        // разбираем тело
        List<Node> commands = bodyNode.getChildrens();
        for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
            Node command = iterator.next();
            translateCommand( command, tv, tf, functionName );
        }

        // подсчёт размера стека
        int stackSize = tv.getMaxLocalsShift() * 4;
        // подсчёт размера памяти под локальные переменные
        try {
            writer.write(
                    "   .limit stack " + stackSize + "\n" +
                    "   .limit locals " + tv.getMaxLocalsShift() + "\n"
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
                    ".end method\n\n"
                );
            } else{
                writer.write(
                    ".end method\n\n"
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // транслирует команду
	private void translateCommand(Node commandNode, TableVariables tv, TableFunctions tf, String curFuncName){
        // Если не пустышка
        if( commandNode.getTokenType() == TokenType.EMPTY ){
            return;
        }

        List<Node> operands = commandNode.getChildrens();

		// определяем тип команды:
        // TODO: добавить ещё if и while
        String varName = null;
        VariableInfo.VariableType exprType = null;
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
                exprType = VariableInfo.VariableType.INT;

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

                // приведение int к double, если надо
                if( exprType == VariableInfo.VariableType.INT &&
                        tv.getType(varName) == VariableInfo.VariableType.DOUBLE ){

                    try {
                        tmpWriter.write( "   i2d\n" );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // выполнить присвоение( снять со стека значение и положить в переменную )
                try {
                    tmpWriter.write( tv.getStrStore( varName ) );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case RETURN:
                // проверка, какой тип должна вернуть функция( из таблицы функций )
                FunctionInfo.FunctionReturnType retType = tf.getReturnType( curFuncName );

                exprType = VariableInfo.VariableType.INT;
                // транслируем дальнейшее выражение
                exprNode = commandNode.getChildrens().get(1);

                // запускаем разбор поддерева выражения
                try {
                    exprType = translateExpr( exprNode, tv, tf );
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // TODO: расставлять return для каждой ветки, проверять это как-то
                try {
                    if (retType == FunctionInfo.FunctionReturnType.INT) {
                        if (exprType == VariableInfo.VariableType.INT) {
                            tmpWriter.write( "   ireturn\n" );
                        } else{
                            throw new RuntimeException("TR: недопустимое приведение типов");
                        }
                    } else{
                        if( exprType == VariableInfo.VariableType.DOUBLE ){
                            tmpWriter.write( "   dreturn\n" );
                        } else{
                            // TODO: приведение типов
                            tmpWriter.write(
                                    "   i2d\n" +
                                    "   dreturn\n"
                            );
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case PRINT:
                // TODO: разница с append
                // TODO: сделать такую же обёртку для вызовов функции, как и у переменных

                varName = (String) operands.get(1).getTokenValue();
                try {
                    tmpWriter.write(
                            "   getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
                            tv.getStrLoad( varName )
                    );

                    // проверка правильной по типу println
                    if( tv.getType( varName ) == VariableInfo.VariableType.INT ){
                        tmpWriter.write( "   invokevirtual java/io/PrintStream/println(I)V\n" );
                    } else{
                        tmpWriter.write( "   invokevirtual java/io/PrintStream/println(D)V\n" );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case IF:
                // похоже на реализацию функции, т.е.
                //свой цикл по командам, своя таблица переменных

                // TODO: нужно потом мержить только три таблицы,
                //чтобы поддерживать состояние инициализации

                // создаём копию таблицы переменных, чтобы таблица функции осталась цела
                TableVariables tvIf = new TableVariables( tv );

                // разбор условия
                Node condExpr1Node = commandNode.getChildren(1).getChildren(0);
                Node condExpr2Node = commandNode.getChildren(1).getChildren(2);

                // TODO: разобраться с типами в условии, пока без них
                try {
                    translateExpr(condExpr1Node, tv, tf);
                    translateExpr(condExpr2Node, tv, tf);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // вставка условия перехода
                String condStr = (String) commandNode.getChildren(1).getChildren(1).getTokenValue();

                // определение вида if: с веткой else или без
                if( commandNode.getChildrenCount() == 4 ){
                    // с else веткой

                    // копия таблицы переменных
                    TableVariables tvElse = new TableVariables( tv );

                    // вставка начала
                    try {
                        tmpWriter.write(
                                cg.getStrStatementPart(ControlGenerator.StatementWithCondition.IF_ELSE_BEGIN, condStr )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // разбор верной ветви
                    Node bodyIfNode = commandNode.getChildren(2);
                    List<Node> commands = bodyIfNode.getChildrens();
                    for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
                        Node command = iterator.next();
                        translateCommand( command, tvIf, tf, curFuncName );
                    }

                    // обновление максимума
                    tv.updateLocalSpace( tvIf.getMaxLocalsShift() );

                    // вставка середины
                    try {
                        tmpWriter.write(
                                cg.getStrStatementPart(ControlGenerator.Statement.IF_ELSE_MIDDLE )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // разбор ветви else
                    Node bodyElseNode = commandNode.getChildren(3);
                    commands = bodyElseNode.getChildrens();
                    for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
                        Node command = iterator.next();
                        translateCommand( command, tvElse, tf, curFuncName );
                    }

                    // обновление максимума
                    tv.updateLocalSpace( tvElse.getMaxLocalsShift() );

                    // вставка конца
                    try {
                        tmpWriter.write(
                                cg.getStrStatementPart(ControlGenerator.Statement.IF_ELSE_END )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    // просто ветка if
                    // вставка начала
                    try {
                        tmpWriter.write(
                                cg.getStrStatementPart(ControlGenerator.StatementWithCondition.IF_BEDIN, condStr )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // разбор верной ветви
                    Node bodyIfNode = commandNode.getChildren(2);
                    List<Node> commands = bodyIfNode.getChildrens();
                    for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
                        Node command = iterator.next();
                        translateCommand( command, tvIf, tf, curFuncName );
                    }

                    // обновление максимума
                    tv.updateLocalSpace( tvIf.getMaxLocalsShift() );

                    // вставка конца
                    try {
                        tmpWriter.write(
                                cg.getStrStatementPart(ControlGenerator.Statement.IF_END )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // TODO: сверка таблиц

                break;
            case WHILE:
                // создаём копию таблицы переменных, чтобы таблица функции осталась цела
                TableVariables tvWhile = new TableVariables( tv );

                // разбор условия
                condExpr1Node = commandNode.getChildren(1).getChildren(0);
                condExpr2Node = commandNode.getChildren(1).getChildren(2);

                // вставка условия перехода
                condStr = (String) commandNode.getChildren(1).getChildren(1).getTokenValue();

                // вставка начала(только метка)
                try {
                    tmpWriter.write(
                            cg.getStrStatementPart(ControlGenerator.Statement.WHILE_BEGIN_LABEL )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // TODO: разобраться с типами в условии, пока без них
                try {
                    translateExpr(condExpr1Node, tv, tf);
                    translateExpr(condExpr2Node, tv, tf);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // вставка начала(продолжение)
                try {
                    tmpWriter.write(
                            cg.getStrStatementPart(ControlGenerator.StatementWithCondition.WHILE_BEGIN, condStr )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // разбор верной ветви
                Node bodyWhileNode = commandNode.getChildren(2);
                List<Node> commands = bodyWhileNode.getChildrens();
                for (Iterator<Node> iterator = commands.iterator(); iterator.hasNext(); ) {
                    Node command = iterator.next();
                    translateCommand( command, tvWhile, tf, curFuncName );
                }

                // обновление максимума
                tv.updateLocalSpace( tvWhile.getMaxLocalsShift() );

                // вставка конца
                try {
                    tmpWriter.write(
                            cg.getStrStatementPart(ControlGenerator.Statement.WHILE_END )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // TODO: сверка таблиц

                break;
            default:
                // недопустимый тип команды
                throw new RuntimeException("Недопустимый тип команды");
        }
	}

	// TODO: пока без возведения в степень, потому что нужно нужно функцию вызывать
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
                if( exprType == VariableInfo.VariableType.INT ){
                    tmpWriter.write( "   iadd\n" );
                } else {
                    // перевернуть, привести, обратно перевернуть
                    tmpWriter.write(
                            "   \n" +
                            "   dup2_x1\n" +
                            "   pop2\n" +
                            "   i2d\n" +
                            "   \n" +
                            "   dup2_x2\n" +
                            "   pop2\n" +
                            "   dadd\n" +
                            "\n"
                    );
                }
            } else {
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                if( exprType == VariableInfo.VariableType.INT ){
                    // привести int к double
                    tmpWriter.write(
                            "   i2d\n" +
                            "   dadd\n"
                    );
                    exprType = VariableInfo.VariableType.DOUBLE;
                } else{
                    tmpWriter.write( "   dadd\n" );
                }
            }
            // TODO: сделать обёртку для строкового представления команд

            return exprType;
		case MINUS:
			// если унарный
			if( exprNode.getChildrens().size() == 1 ){
				exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);

                if( exprType == VariableInfo.VariableType.INT ){
                    tmpWriter.write( "   ineg\n" );
                } else{
                    tmpWriter.write( "   dneg\n" );
                }

                return exprType;
			} else{
				exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
                if( exprType == VariableInfo.VariableType.INT ){
                    exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                    if( exprType == VariableInfo.VariableType.INT ){
                        tmpWriter.write( "   isub\n" );
                    } else {
                        // перевернуть, привести, обратно перевернуть
                        tmpWriter.write(
                                "   \n" +
                                        "   dup2_x1\n" +
                                        "   pop2\n" +
                                        "   i2d\n" +
                                        "   \n" +
                                        "   dup2_x2\n" +
                                        "   pop2\n" +
                                        "   dsub\n" +
                                        "\n"
                        );
                    }
                } else {
                    exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                    if( exprType == VariableInfo.VariableType.INT ){
                        // привести int к double
                        tmpWriter.write(
                                "   i2d\n" +
                                "   dsub\n"
                        );
                        exprType = VariableInfo.VariableType.DOUBLE;
                    } else{
                        tmpWriter.write( "   dsub\n" );
                    }
                }

                return exprType;
			}
		case MULTIPLICATION:
			exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
            if( exprType == VariableInfo.VariableType.INT ){
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                if( exprType == VariableInfo.VariableType.INT ){
                    tmpWriter.write( "   imul\n" );
                } else {
                    // перевернуть, привести, обратно перевернуть
                    tmpWriter.write(
                            "   \n" +
                                    "   dup2_x1\n" +
                                    "   pop2\n" +
                                    "   i2d\n" +
                                    "   \n" +
                                    "   dup2_x2\n" +
                                    "   pop2\n" +
                                    "   dmul\n" +
                                    "\n"
                    );
                }
            } else {
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                if( exprType == VariableInfo.VariableType.INT ){
                    // привести int к double
                    tmpWriter.write(
                            "   i2d\n" +
                            "   dmul\n"
                    );
                    exprType = VariableInfo.VariableType.DOUBLE;
                } else{
                    tmpWriter.write( "   dmul\n" );
                }
            }
			return exprType;
		case DIVISION:
			exprType = translateExpr(exprNode.getChildrens().get(0), tv, tf);
            if( exprType == VariableInfo.VariableType.INT ){
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                if( exprType == VariableInfo.VariableType.INT ){
                    tmpWriter.write( "   idiv\n" );
                } else {
                    // перевернуть, привести, обратно перевернуть
                    tmpWriter.write(
                            "   \n" +
                                    "   dup2_x1\n" +
                                    "   pop2\n" +
                                    "   i2d\n" +
                                    "   \n" +
                                    "   dup2_x2\n" +
                                    "   pop2\n" +
                                    "   ddiv\n" +
                                    "\n"
                    );
                }
            } else {
                exprType = translateExpr(exprNode.getChildrens().get(1), tv, tf);
                if( exprType == VariableInfo.VariableType.INT ){
                    // привести int к double
                    tmpWriter.write(
                            "   i2d\n" +
                            "   ddiv\n"
                    );
                    exprType = VariableInfo.VariableType.DOUBLE;
                } else{
                    tmpWriter.write( "   ddiv\n" );
                }
            }
			return exprType;
        // TODO: нужно конкретно определить тип
		case NUMBER:
            // определение типа
            boolean isDouble = exprNode.getValue().getTokenValue().toString().contains(".");
            if( ! isDouble ){ // TODO: странное определение типа
                // тогда intf
                //range -32768 to 32767
                tmpWriter.write( "   sipush " + exprNode.getValue().getTokenValue().toString() + "\n" );
            } else{
                tmpWriter.write( "   ldc2_w " + exprNode.getValue().getTokenValue().toString() + "\n" );
                exprType = VariableInfo.VariableType.DOUBLE;
            }

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
                // перед вызовом функции вычисляем все выражения - фактические параметры
                int numParams = exprNode.getChildren( 1 ).getChildrenCount();
                if( numParams == tf.getAmountParameters( funcName ) ) {
                    for (int i = 0; i < numParams; i++) {
                        Node curActualArg = exprNode.getChildren(1).getChildren(i);
                        exprType = translateExpr(curActualArg, tv, tf);
                        // сверка фактических и формальных аргументов
                        //попытка приведения
                        String castStr = tf.castActualArg(funcName, i, exprType);
                        tmpWriter.write( castStr );
                    }
                } else{
                    throw new RuntimeException("TR: при вызове " + funcName + " несоответствие числа фактических и формальных аргументов" );
                }

                // вызов метода
                tmpWriter.write( "   invokestatic " + tf.getStrCall( funcName ) + "\n" );
            }

            return tf.getReturnType( funcName ).convertToVariableType();
		default:
            // TODO: не давать вызывать методы без возвращаемого значения
			throw new RuntimeException("TR: Невозможно распарсить выражение");
		}
	}
}

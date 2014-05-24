package main.ru.svichkarev.compiler.translator.table;

import java.util.HashMap;
import java.util.Map;

public class TableFunctions {
    private Map<String, FunctionInfo> functions = new HashMap<String, FunctionInfo>();
    // для указания класса метода при вызове
    private String className;

    public TableFunctions( String className ) {
        this.className = className;
    }

    // добавляет функцию в таблицу
    public void add( String functionName, FunctionInfo functionInfo ) {
        functions.put(functionName, functionInfo);
    }

    // проверяет, есть ли метод main
    public boolean hasMain() {
        FunctionInfo main = functions.get( "main" );
        return (main != null && main.getReturnType() == FunctionInfo.FunctionReturnType.VOID && main.getAmountParameters() == 0);
    }

    // получает возвращаемый тип указанной функции
    public FunctionInfo.FunctionReturnType getReturnType( String functionName ){
        if( ! functions.containsKey( functionName ) ){
            throw new RuntimeException("TR: не объявлена функция");
        }

        return functions.get( functionName ).getReturnType();
    }

    // получает число аргументов
    public int getAmountParameters(String functionName){
        if( ! functions.containsKey( functionName ) ){
            throw new RuntimeException("TR: не объявлена функция");
        }

        return functions.get( functionName ).getAmountParameters();
    }

    // вернуть строковое представление вызова функции
    public String getStrCall(String functionName) {
        String funcStr = functionName;
        if( functions.containsKey( functionName ) ) {
            FunctionInfo functionInfo = functions.get(functionName);
            funcStr += "(" + functionInfo.getStrParameters() + ")" + functionInfo.getReturnType().toString();

        } else{
            throw new RuntimeException("TR: не объявлена функция");
        }

        return className + "/" + funcStr;
    }

    // пробуем привести типы, возвращает приводящую строчку
    public String castActualArg( String functionName, int indexArg, VariableInfo.VariableType type ){
        FunctionInfo functionInfo = functions.get( functionName );
        if( functionInfo.isCast( indexArg, type ) ){
            return functionInfo.castStr( indexArg, type );
        } else{
            throw new RuntimeException("TR: при вызове " + functionName + " невозможно привети " + indexArg + " аргумент" );
        }
    }
}

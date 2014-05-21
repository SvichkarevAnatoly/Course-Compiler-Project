package main.ru.svichkarev.compiler.translator.table;

import java.util.HashMap;
import java.util.Map;

public class TableFunctions {
    private Map<String, FunctionInfo> functions = new HashMap<String, FunctionInfo>();

    private FunctionInfo currentFunction;

    // получить возвращаемый тип для текущей функции
    public FunctionInfo.FunctionReturnType getCurrentFunctionReturnType() {
        return currentFunction.getReturnType();
    }

    // добавляет функцию в таблицу
    public void addCurrentFunction( String functionName ) {
        functions.put(functionName, currentFunction);
    }
}

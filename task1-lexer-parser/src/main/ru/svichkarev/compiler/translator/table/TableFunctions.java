package main.ru.svichkarev.compiler.translator.table;

import java.util.HashMap;
import java.util.Map;

public class TableFunctions {
    private Map<String, FunctionInfo> functions = new HashMap<String, FunctionInfo>();

    // добавляет функцию в таблицу
    public void add( String functionName, FunctionInfo functionInfo ) {
        functions.put(functionName, functionInfo);
    }

    // проверяет, есть ли метод main
    public boolean hasMain() {
        FunctionInfo main = functions.get( "main" );
        if( main != null ){
            if( main.getReturnType() == FunctionInfo.FunctionReturnType.VOID &&
                main.getAmountParameters() == 0 ){

                return true;
            } else{
                return false;
            }
        }else{
            return false;
        }
    }
}

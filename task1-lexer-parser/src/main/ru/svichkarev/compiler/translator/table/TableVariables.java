package main.ru.svichkarev.compiler.translator.table;

import java.util.HashMap;
import java.util.Map;

public class TableVariables {
    private Map<String, VariableInfo> variables = new HashMap<String, VariableInfo>();
    // хранит последнее смещение для переменной
    private int lastLocalsShift = 0;

    // нужен обычный конструктор, если есть конструктор копирования
    public TableVariables(){}

    // конструктор копирования для веток в условных операторах
    public TableVariables( TableVariables parent ) {
        // TODO: полное копирование реализовать
    }

    // добавление в таблицу новой переменной
    public void add( String variableName, VariableInfo varInfo) {
        // проверить на повторное объявление переменной
        if( ! variables.containsKey( variableName ) ) {
            varInfo.setLocalsIndex( lastLocalsShift );
            // инкрементируем смещение для будущей новой переменной
            lastLocalsShift += varInfo.getType() == VariableInfo.VariableType.INT ? 1 : 2;
            variables.put(variableName, varInfo);
        } else {
            // уже объявлена эта переменная
            throw new RuntimeException("TR: Повторное объявление переменной");
        }
    }

    // проверка, объявлена ли переменная
    public boolean isDeclared( String variableName ) {
        return variables.containsKey(variableName);
    }

    // установить флаг инициализации
    public void setInitialization( String variableName ){
        variables.get( variableName ).setInitialization();
    }

    // получить номер локальной переменной
    public int getLocalIndex( String variableName ){
        return variables.get( variableName ).getLocalsIndex();
    }

    // получить тип указанной переменной
    public VariableInfo.VariableType getType( String variableName ){
        if( variables.containsKey( variableName ) ){
            return variables.get( variableName ).getType();
        } else {
            throw new RuntimeException("TR: не удалось определить тип переменной");
        }
    }

    // получить размер под локальные переменные
    public int getLocalSpace() {
        return lastLocalsShift;
    }
}


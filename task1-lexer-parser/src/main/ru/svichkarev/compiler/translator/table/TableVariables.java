package main.ru.svichkarev.compiler.translator.table;

import java.util.HashMap;
import java.util.Map;

public class TableVariables {
    private Map<String, VariableInfo> variables = new HashMap<String, VariableInfo>();

    // нужен обычный конструктор, если есть конструктор копирования
    public TableVariables(){}

    // конструктор копирования для веток в условных операторах
    public TableVariables( TableVariables parent ) {
        // TODO: полное копирование реализовать
    }

    // TODO: сделать удобные методы

    // TODO: проверить с какого номера идёт индексация
    public int getLastLocalsIndex(){
        return variables.size();
    }

    // добавление в таблицу новой переменной
    public void add( String variableName, VariableInfo varInfo) {
        // проверить на повторное объявление переменной
        if( ! variables.containsKey( variableName ) ) {
            variables.put(variableName, varInfo);
        } else {
            // уже объявлена эта переменная
            throw new RuntimeException("Variable has already declared");
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
}


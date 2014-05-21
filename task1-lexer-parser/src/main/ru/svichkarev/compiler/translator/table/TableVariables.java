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
    public int getLastLocalsNumber(){
        return variables.size();
    }

    // добавление в таблицу новой переменной
    public void add( String name, VariableInfo varInfo) {
        // проверить на повторное объявление переменной
        if( ! variables.containsKey( name ) ) {
            variables.put(name, varInfo);
        } else {
            // уже объявлена эта переменная
            throw new RuntimeException("Variable has already declared");
        }
    }
}


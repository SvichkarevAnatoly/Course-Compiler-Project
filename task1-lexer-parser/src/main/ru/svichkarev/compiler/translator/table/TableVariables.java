package main.ru.svichkarev.compiler.translator.table;

import java.util.HashMap;
import java.util.Map;

public class TableVariables {
    private Map<String, VariableInfo> variables = new HashMap<String, VariableInfo>();
    // хранит последнее смещение для переменной
    private int lastLocalsShift = 0;
    // максимальное смещение для переменных
    private int maxLocalsShift = 0;

    // нужен обычный конструктор, если есть конструктор копирования
    public TableVariables(){}

    // конструктор копирования для веток в условных операторах
    public TableVariables( TableVariables parent ) {
        for( Map.Entry<String, VariableInfo> entry : parent.variables.entrySet() ){
            VariableInfo curVarInf = new VariableInfo( entry.getValue() );
            this.variables.put( entry.getKey(), curVarInf );
        }

        this.lastLocalsShift = parent.lastLocalsShift;
    }

    // TODO: переменная может не использоваться(но это уже похоже на оптимизацию)
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

    // проверка, проинициализированна ли переменная
    private boolean isInitialized( String variableName ) {
        if( variables.containsKey(variableName) ){
            return variables.get( variableName ).isInitialized();
        } else{
            // кинуть ошибку, переменная не объявлена
            throw new RuntimeException( "TR: Переменная не объявлена" );
        }
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
    //подаём текущий размер, если локальный больше, вернуть его
    public void updateLocalSpace(int maxLocalsShift) {
        if( maxLocalsShift > this.maxLocalsShift ){
            this.maxLocalsShift = maxLocalsShift;
        }
    }

    // получить строку для загрузки значения из переменной на стек
    public String getStrLoad( String variableName ){
        // проверка, что такая переменная есть и проинициализированна
        if( ! variables.containsKey( variableName ) ){
            throw new RuntimeException("TR: не объявлена переменная");
        }
        if( ! isInitialized( variableName ) ){
            throw new RuntimeException("TR: не проинициализированная переменная");
        }

        int indexLocals = getLocalIndex(variableName);
        // тип переменной учитываем
        if( getType( variableName ) == VariableInfo.VariableType.INT ){
            // TODO: константу в константы
            if( indexLocals < 4 ){
                return "   iload_" + indexLocals + "\n";
            } else{
                return "   iload " + indexLocals + "\n";
            }
        } else{
            // TODO: константу в константы
            if( indexLocals < 4 ){
                return "   dload_" + indexLocals + "\n";
            } else{
                return "   dload " + indexLocals + "\n";
            }
        }
    }

    // получить строку для загрузки значения из переменной на стек
    public String getStrStore( String variableName ){
        // проверка, что такая переменная есть и проинициализированна
        if( ! variables.containsKey( variableName ) ){
            throw new RuntimeException("TR: не объявлена переменная");
        }

        int indexLocals = getLocalIndex(variableName);
        // тип переменной учитываем
        if( getType( variableName ) == VariableInfo.VariableType.INT ){
            // TODO: константу в константы
            if( indexLocals < 4 ){
                return "   istore_" + indexLocals + "\n";
            } else{
                return "   istore " + indexLocals + "\n";
            }
        } else{
            // TODO: константу в константы
            if( indexLocals < 4 ){
                return "   dstore_" + indexLocals + "\n";
            } else{
                return "   dstore " + indexLocals + "\n";
            }
        }
    }

    // возвращает максимальное смещение
    public int getMaxLocalsShift() {
        if( maxLocalsShift > lastLocalsShift ){
            return maxLocalsShift;
        } else{
            return lastLocalsShift;
        }
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.cpu.views.tapestry.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Вспомогательный класс для Edit и ListView. Содержит методы для переопределения
 * стандартных пакетов.
 * @author molodec
 */
public class EVTools {

    /**
     * Переменная, которая хранит наборы названий сущностей  для каждого своего
     * индекса, чтобы для этих наборов, с указанным индексом, можно было незначить
     * определенный пакет.
     */
    private static List<ArrayList> packages = new ArrayList();
    
    /**
     * Переменная хранит текущий набор названий сущностей. Используется для записи
     * массива названий сущностей в packages.
     */
    private static ArrayList<String> entities = new ArrayList<String>();

    /**
     * Метод для удобной записи новых значений в entities - сначала очищает, 
     * потом добавляет. 
     * @param entitiesForAdd названия сущностей с entity пакетом. 
     */
    private static void cleanAndAddToEntities(List<String> entitiesForAdd) {
        entities.clear();
        entities.addAll(entitiesForAdd);
    }
    
    /**
     * Метод для получения только имени сущности из строки, которая представляет
     * собою "entity-пакет.название сущности". Используется в случае 
     * переопределения пакетов. Если пакеты не были переопределены возвращает 
     * строку как есть.
     * @param packageWithEntity 
     * @param currentPackage 
     * @return Новое строка с названием сущности(исключенно имя entity-пакета). 
     */
    public static String getEntityByPackageWithEntity(String packageWithEntity, String currentPackage){
        String[] tempArray;
        String result=packageWithEntity;
        if (!"orion.cpu.entities".equals(currentPackage)){
        tempArray=packageWithEntity.split("\\.");
        result=tempArray[tempArray.length-1];
        }
        return result;
    }

    /**
     * Метод для получения имени пакета, для указанной сущности. Используется
     * в тех случаях, когда требуется переопределить стандартный путь,
     * записанный в символьной константе orion.root-package. Например, для
     * сущности User, данный метод возвращает br.com.arsmachina.authentication
     * @param context (Строка с названием сущности и entity пакетом. Например,
     * entity.User или entity.Role).
     * @return Название пакета
     */
    public static String getPackageByStringFromEventContext(String context) {
        String pack = null;
        //Добавляем необходимые сущности для пакета с индексом 0. Если необходимо
        //несколько несколько сущностей для нескольких пакетов, то добавляем по
        //тому же принципу - копируя данную строку и подставляя нужный массив.
        cleanAndAddToEntities(Arrays.asList("entity.User", "entity.Role"));
        packages.add(entities);
        for (int i = 0; i < packages.size(); i++) {
            ArrayList<String> row = new ArrayList<String>();
            row.addAll(packages.get(i));
            if (row.indexOf(context) > -1) {
                //Здесь необходимо перечислить, какие пакеты будут подставляться для
                //для наборов сущностей с указанными индексами - i.        
                switch (i) {
                    case 0:
                        pack = "br.com.arsmachina.authentication";
                        break;
                }
            }
        }
        return pack;
    }
}

package ru.avalon.java.ocpjp.labs;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

/**
 * Лабораторная работа №3
 * <p>
 * Курс: "DEV-OCPJP. Подготовка к сдаче сертификационных экзаменов серии Oracle Certified Professional Java Programmer"
 * <p>
 * Тема: "JDBC - Java Database Connectivity" 
 *
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class Main {
    public static final String PATH_DB = "src/resourses/db.properties";

    /**
     * Точка входа в приложение
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
         * TODO #01 Подключите к проекту все библиотеки, необходимые для соединения с СУБД.
         */
        try (Connection connection = getConnection()) {
            ProductCode code = new ProductCode("MO", 'N', "Movies");
            code.save(connection);                
            printAllCodes(connection);
            
            System.out.println("После изменения: ");
            code.setCode("MV");
            code.save(connection);
            printAllCodes(connection);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } 
        
        /*
         * TODO #14 Средствами отладчика проверьте корректность работы программы
         */
    }
    /**
     * Выводит в кодсоль все коды товаров
     * 
     * @param connection действительное соединение с базой данных
     * @throws SQLException 
     */    
    private static void printAllCodes(Connection connection) throws SQLException {
        Collection<ProductCode> codes = ProductCode.all(connection);
        for (ProductCode code : codes) {
            System.out.println(code);
        }
    }
    /**
     * Возвращает URL, описывающий месторасположение базы данных
     * 
     * @return URL в виде объекта класса {@link String}
     */
    private static String getUrl() {
        return getProperties().getProperty("host");
    }
    /**
     * Возвращает параметры соединения
     * 
     * @return Объект класса {@link Properties}, содержащий параметры user и 
     * password
     */
    private static Properties getProperties() {
        FileInputStream fis;
        Properties prpt = new Properties();
        try {
            fis = new FileInputStream(PATH_DB);
            prpt.load(fis);
                } catch (IOException e){
                    System.err.println("Файл свойств не найден!");
                }
            return prpt;
    }
    /**
     * Возвращает соединение с базой данных Sample
     * 
     * @return объект типа {@link Connection}
     * @throws SQLException 
     */
    private static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(getUrl(), getProperties());
    }
    
}

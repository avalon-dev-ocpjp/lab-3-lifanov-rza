package ru.avalon.java.ocpjp.labs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Класс описывает представление о коде товара и отражает соответствующую 
 * таблицу базы данных Sample (таблица PRODUCT_CODE).
 * 
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class ProductCode {
    /**
     * Код товара
     */
    private String code;
    /**
     * Кода скидки
     */
    private char discountCode;
    /**
     * Описание
     */
    private String description;
    /**
     * Основной конструктор типа {@link ProductCode}
     * 
     * @param code код товара
     * @param discountCode код скидки
     * @param description описание 
     */
    public ProductCode(String code, char discountCode, String description) {
        this.code = code;
        this.discountCode = discountCode;
        this.description = description;
    }
    /**
     * Инициализирует объект значениями из переданного {@link ResultSet}
     * 
     * @param set {@link ResultSet}, полученный в результате запроса, 
     * содержащего все поля таблицы PRODUCT_CODE базы данных Sample.
     */
    private ProductCode(ResultSet set) throws SQLException {
        code = set.getString("PROD_CODE");
        discountCode = set.getString("DISCOUNT_CODE").charAt(0);
        description = set.getString("DESCRIPTION");
    }
    /**
     * Возвращает код товара
     * 
     * @return Объект типа {@link String}
     */
    public String getCode() {
        return code;
    }
    /**
     * Устанавливает код товара
     * 
     * @param code код товара
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * Возвращает код скидки
     * 
     * @return Объект типа {@link String}
     */
    public char getDiscountCode() {
        return discountCode;
    }
    /**
     * Устанавливает код скидки
     * 
     * @param discountCode код скидки
     */
    public void setDiscountCode(char discountCode) {
        this.discountCode = discountCode;
    }
    /**
     * Возвращает описание
     * 
     * @return Объект типа {@link String}
     */
    public String getDescription() {
        return description;
    }
    /**
     * Устанавливает описание
     * 
     * @param description описание
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Хеш-функция типа {@link ProductCode}.
     * 
     * @return Значение хеш-кода объекта типа {@link ProductCode}
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.code);
        return hash;
    }
    /**
     * Сравнивает некоторый произвольный объект с текущим объектом типа 
     * {@link ProductCode}
     * 
     * @param obj Объект, скоторым сравнивается текущий объект.
     * @return true, если объект obj тождественен текущему объекту. В обратном 
     * случае - false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductCode other = (ProductCode) obj;
        return this.code.equals(other.code);
    }
    /**
     * Возвращает строковое представление кода товара.
     * 
     * @return Объект типа {@link String}
     */
    @Override
    public String toString() {
        return "Код товара: \t\t" + getCode() + 
               "\nКод скидки: \t\t" + getDiscountCode() + 
               "\nОписание товара: \t" + getDescription() + "\n";
    }
    /**
     * Возвращает запрос на выбор всех записей из таблицы PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getSelectQuery(Connection connection) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                                          "select * "
                                          + "from PRODUCT_CODE");
        return prepStatement;
        }
    /**
     * Возвращает запрос на добавление записи в таблицу PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getInsertQuery(Connection connection) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "insert into PRODUCT_CODE "
                        + "values (?,?,?)");
        return prepStatement;
    }
    /**
     * Возвращает запрос на обновление значений записи в таблице PRODUCT_CODE 
     * базы данных Sample
     * 
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getUpdateQuery(Connection connection) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "update PRODUCT_CODE set DISCOUNT_CODE = ?,"
              + "DESCRIPTION = ? where PROD_CODE = ?");
        return prepStatement;
    }
    /**
     * Преобразует {@link ResultSet} в коллекцию объектов типа {@link ProductCode}
     * 
     * @param set {@link ResultSet}, полученный в результате запроса, содержащего 
     * все поля таблицы PRODUCT_CODE базы данных Sample
     * @return Коллекция объектов типа {@link ProductCode}
     * @throws SQLException 
     */
    public static Collection<ProductCode> convert(ResultSet set) throws SQLException {
        Collection<ProductCode> col = new ArrayList();
        while (set.next()) col.add(new ProductCode(set));
        return col;
    }
    /**
     * Сохраняет текущий объект в базе данных. 
     * <p>
     * Если запись ещё не существует, то выполняется запрос типа INSERT.
     * <p>
     * Если запись уже существует в базе данных, то выполняется запрос типа UPDATE.
     * 
     * @param connection действительное соединение с базой данных
     */
    public void save(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        if (!(all(connection).contains(this))) {
        try(PreparedStatement statement = getInsertQuery(connection)) {
        statement.setString(1, code);
        statement.setString(2, "" + discountCode);
        statement.setString(3, description);
        statement.execute();
        connection.commit();
        }
        } else if ((this.equals(new ProductCode(code, 'X', description))) &&
                this.discountCode != 'X') {
            try (PreparedStatement statement = getUpdateQuery(connection)){
                statement.setString(2, code);
                statement.setString(3, description);
                statement.setString(1, "" + 'X');
                statement.execute();
                connection.commit();
            }
        }
    }
    /**
     * Возвращает все записи таблицы PRODUCT_CODE в виде коллекции объектов
     * типа {@link ProductCode}
     * 
     * @param connection действительное соединение с базой данных
     * @return коллекция объектов типа {@link ProductCode}
     * @throws SQLException 
     */
    public static Collection<ProductCode> all(Connection connection) throws SQLException {
        try (PreparedStatement statement = getSelectQuery(connection)) {
            try (ResultSet result = statement.executeQuery()) {
                return convert(result);
            }
        }
    }
}

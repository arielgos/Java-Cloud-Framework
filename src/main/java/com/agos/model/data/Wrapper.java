package com.agos.model.data;

import com.agos.model.entities.Action;
import com.agos.model.entities.Entity;
import com.agos.model.entities.annotations.Ignored;
import com.agos.model.entities.annotations.KeyField;
import com.agos.util.Log;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.joinFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.lang.reflect.Field;

/**
 * Created by arielgos on 12/30/16.
 */
abstract public class Wrapper<T> {

    private Class<T> type;
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;
    protected String tableName;
    protected List<Field> listFields;

    public Wrapper(Class<T> type, Connection connection) {
        this.connection = connection;
        this.type = type;
        this.tableName = this.getTableName(type);
        listFields = getListField(type);
    }

    private String getTableName(Class<T> cl) {
        String name = cl.getPackage().getName();
        int lastIndexOf = name.lastIndexOf(".");
        String substring = name.substring(lastIndexOf + 1, name.length());
        substring = substring + "_" + cl.getSimpleName();
        substring = substring.toLowerCase();
        return substring;
    }

    private List<Field> getListField(Class<T> type) {
        Field[] superFields = type.getSuperclass().getDeclaredFields();
        Field[] fields = type.getDeclaredFields();
        List<Field> listFields = new ArrayList<Field>();
        for (Field field : superFields) {
            if (!isIgnore(field)) {
                listFields.add(field);
            }
        }
        for (Field field : fields) {
            if (!isIgnore(field)) {
                listFields.add(field);
            }
        }
        return listFields;
    }

    private static boolean isIgnore(Field field) {
        return field.getAnnotation(Ignored.class) != null;
    }

    protected T search(long id) {
        String query = String.format("SELECT * FROM %s WHERE id = %d ", this.tableName, id);
        return this.search(query);
    }


    private T search(String strQuery) {
        ResultSet result = null;
        T entity = null;
        try {
            Log.d(strQuery);
            result = getList(strQuery);
            while (result.next()) {
                entity = (T) this.loadEntity(result);
            }
        } catch (Exception e) {
            Log.e("No se pudo ejecutrar la consulta : " + strQuery + "-->" + e, e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    Log.e("No se pudo cerrar conexion ResultSet:" + e, e);
                }
            }

        }
        return entity;
    }

    private ResultSet getList(String query) throws SQLException {
        statement = connection.createStatement();
        try {
            result = statement.executeQuery(query);
        } catch (Exception e) {
            Log.e("No se pudo ejecutrar la consulta : " + query + "-->" + e, e);
        }
        return result;
    }

    protected T loadEntity(ResultSet result) {
        T instance = null;
        try {
            instance = type.newInstance();
            for (Field field : listFields) {
                if (field.getAnnotation(Ignored.class) == null) {
                    Object value = result.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor;
                    try {
                        propertyDescriptor = new PropertyDescriptor(field.getName(), instance.getClass());
                    } catch (NullPointerException e) {
                        propertyDescriptor = new PropertyDescriptor(field.getName(), instance.getClass().getSuperclass());
                    }
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
            }
        } catch (Exception e) {
            Log.e(e.getMessage(), e);
            e.printStackTrace();
        }
        return instance;
    }

    protected List<T> list(String query) {
        List<T> Col = new ArrayList<T>();
        ResultSet result = null;
        try {
            result = getList(query);
            while (result.next()) {
                Col.add((T) this.loadEntity(result));
            }

        } catch (Exception e) {
            Log.e("No se pudo ejecutrar la consulta : " + query + "-->" + e, e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    Log.w("No se pudo cerrar conexion ResultSet:" + e, e);
                }
            }
        }
        return Col;
    }

    public boolean save(Entity entity) {
        try {
            if (entity == null) {
                Log.w("No se ejecutar la entidad es null", null);
                return false;
            }
            List<Column> columns = new ArrayList<Column>();
            String fieldAutoIncremental = "";

            for (Field field : listFields) {
                Annotation autoIncremental = field.getAnnotation(KeyField.class);
                if (entity.getAction() == Action.Insert && autoIncremental != null) {
                    fieldAutoIncremental = field.getName();
                    continue;
                }
                if (field.getAnnotation(Ignored.class) == null) {
                    PropertyDescriptor propertyDescriptor;
                    try {
                        propertyDescriptor = new PropertyDescriptor(field.getName(), entity.getClass());
                    } catch (NullPointerException e) {
                        propertyDescriptor = new PropertyDescriptor(field.getName(), entity.getClass().getSuperclass());
                    }

                    Method method = propertyDescriptor.getReadMethod();

                    Object value = method.invoke(entity);
                    if (value != null) {
                        Annotation a = field.getAnnotation(KeyField.class);
                        columns.add(new Column(field.getName(), value, a));
                    }
                }
            }

            if (columns.isEmpty()) {
                Log.w("No se puede " + entity.getAction().name() + " la entidad " + entity, null);
                return false;
            }
            if (entity.getAction() == Action.Insert)
                return insert(entity, columns, fieldAutoIncremental);

            if (entity.getAction() == Action.Update)
                return update(entity, columns);

            if (entity.getAction() == Action.Delete)
                return delete(columns);

            Log.e("No se pudo ejecutar la accion, entidad=" + entity, null);

        } catch (Exception e) {
            Log.e("No se pudo ejecutar la accion, entidad=" + entity, e);
        } finally {

        }
        return false;
    }

    private String getQuery(String nombre, List<Column> columns, Action action) {
        StringBuilder sb = new StringBuilder();
        if (action == Action.Insert) {
            sb.append("INSERT ");
            sb.append(nombre);
            sb.append(" (");
            sb.append(joinFrom(columns).getKey());
            sb.append(") VALUES (");
            sb.append(getSimple(columns));
            sb.append(")");
        }
        if (action == Action.Update) {
            List<Column> listKey = select(columns, having(on(Column.class).getAnnotation(), instanceOf(KeyField.class)));
            List<Column> listValue = select(columns, having(on(Column.class).getAnnotation(), is(nullValue())));
            sb.append("UPDATE ");
            sb.append(nombre);
            sb.append(" SET ");
            sb.append(joinFrom(listValue, Column.class, "=?,").getKey() + "=?");
            sb.append(" WHERE " + joinFrom(listKey, Column.class, "=?,").getKey() + "=?");
        }
        if (action == Action.Delete) {
            List<Column> listKey = select(columns, having(on(Column.class).getAnnotation(), instanceOf(KeyField.class)));
            sb.append("DELETE FROM ");
            sb.append(nombre);
            sb.append(" WHERE  ");
            sb.append(joinFrom(listKey, "=?,").getKey() + "=?");

        }
        return sb.toString();
    }

    private String getSimple(List<Column> columns) {
        StringBuilder sb = new StringBuilder();
        if (columns.isEmpty()) {
            return "";
        }
        sb.append("?");
        for (int i = 1; i < columns.size(); i++) {
            sb.append(",?");
        }
        return sb.toString();

    }

    private boolean insert(Entity entity, List<Column> columns, String fieldAutoIncremental) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = getQuery(this.tableName, columns, Action.Insert);
            Log.d("SQL:" + query);
            if (query.length() == 0) {
                return false;
            }
            if (fieldAutoIncremental.length() == 0) {
                preparedStatement = connection.prepareStatement(query);
                int i = 0;
                for (Column column : columns) {
                    preparedStatement.setObject(++i, column.getValue());
                }
                preparedStatement.execute();
            } else {
                preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                int i = 0;
                for (Column column : columns) {
                    preparedStatement.setObject(++i, column.getValue());
                }
                preparedStatement.execute();
                rs = preparedStatement.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldAutoIncremental, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(entity, rs.getLong(1));
                }

            }

            return true;
        } catch (Exception e) {
            Log.e("No se pudo ejecutar la consulta: " + query, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                Log.e("Cerrar la conexion base de datos", e);
            }
        }
        return false;
    }

    private boolean update(Entity entity, List<Column> columns) {
        List<Column> listKey = select(columns, having(on(Column.class).getAnnotation(), instanceOf(KeyField.class)));
        List<Column> listValue = select(columns, having(on(Column.class).getAnnotation(), is(nullValue())));
        if (listKey == null || listKey.isEmpty()) {
            return false;
        }
        String query = getQuery(this.tableName, columns, Action.Update);
        Log.d("SQL:" + query);
        if (query.length() == 0) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            int i = 0;
            for (Column column : listValue) {
                preparedStatement.setObject(++i, column.getValue());
            }
            for (Column column : listKey) {
                preparedStatement.setObject(++i, column.getValue());
            }
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean delete(List<Column> columns) {
        List<Column> listKey = select(columns, having(on(Column.class).getAnnotation(), instanceOf(KeyField.class)));
        if (listKey == null || listKey.isEmpty()) {
            return false;
        }
        PreparedStatement preparedStatement = null;
        String query = "";
        try {
            query = getQuery(this.tableName, listKey, Action.Delete);
            Log.d("SQL:" + query);
            if (query.length() == 0) {
                return false;
            }
            preparedStatement = connection.prepareStatement(query);
            int i = 0;
            for (Column column : listKey) {
                preparedStatement.setObject(++i, column.getValue());
            }
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            Log.e("Error al ejecutar la consulta, query: " + query, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                Log.e("Error al cerrar prepares statement", e);
            }
        }
        return false;
    }

}

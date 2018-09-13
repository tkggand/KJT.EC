package com.kjt.ec.data.imp;

import com.kjt.ec.data.DataCommand;
import com.kjt.ec.data.annontation.Transactional;
import com.kjt.ec.data.configuration.DataCommandSection;
import com.kjt.ec.data.configuration.DatabaseSection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCommandImp implements DataCommand {
    private String commandText;
    final DatabaseSection database;
    final DataCommandSection dataCommand;
    final Map<String,Object> parameter;
    final Map<Integer,String> paramPosition;

    public DataCommandImp(DatabaseSection database, DataCommandSection dataCommand){
        this.database=database;
        this.dataCommand=dataCommand;
        this.paramPosition=new HashMap<Integer,String>();
        this.parameter=new HashMap<String,Object>();
    }

    public final void setParameter(String name, Object value) {
        this.parameter.put(name,value);
    }

    public final <T> void setParameter(T value) {

    }

    public final int executeNonQuery(){
        ResultSet result=execute();
       try{
           return result.getRow();
       }catch (Exception e){
           e.printStackTrace();
       }
       return 0;
    }

    public final  <T> T executeScalar(Class<T> classType) {
        ResultSet result=execute();
        try{
            if(classType.isAssignableFrom(Number.class)){
                return (T)(Float)result.getFloat(0);
            }else if(classType.isAssignableFrom(String.class)){
                return (T)(String)result.getString(0);
            }else if(classType.isAssignableFrom(Boolean.class)){
                return (T)(Boolean)result.getBoolean(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public final  <T> T executeEntity(Class<T> classType) {
        ResultSet result= execute();
        Object instance=null;
        try {
            instance=classType.newInstance();
            Field[] fields= classType.getDeclaredFields();
            Map<String,Field> property=new HashMap<String,Field>();
            for(Field field:fields){
                field.setAccessible(true);
                String name=field.getName().toLowerCase();
                property.put(name,field);
            }
            if(result.next()){
                this.bindModel(instance,property,result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T)instance;
    }

    public final <T> List<T> executeList(Class<T> classType){
        ResultSet result= execute();
        List<T> list=new ArrayList<T>();
        try {
            Field[] fields= classType.getDeclaredFields();
            Map<String,Field> property=new HashMap<String,Field>();
            for(Field field:fields){
                field.setAccessible(true);
                String name=field.getName().toLowerCase();
                property.put(name,field);
            }
            while (result.next()){
                Object instance=classType.newInstance();
                this.bindModel(instance,property,result);
                list.add((T)instance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    protected ResultSet execute(){
        this.buildCommandText();
        ResultSet resultSet=null;
        PreparedStatement statement=createStatement();
        try{
            if(statement!=null){
                resultSet= statement.executeQuery();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    protected PreparedStatement createStatement(){

        Connection connection=prepareConnection();
        PreparedStatement statement=null;
        try{
            statement=connection.prepareStatement(commandText);
            preparedStatement(statement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return statement;
    }

    protected Connection prepareConnection(){
        Connection connection=ConnectionHolder.getConnection();
        try{
            if(connection==null){
                Map<String,String> datasource=database.getDatasource();
                String driver=datasource.get("driver");
                String url=datasource.get("url");
                String username=datasource.get("username");
                String password=datasource.get("password");
                Class.forName(driver);
                connection=DriverManager.getConnection(url,username,password);
                ConnectionHolder.setConnection(connection);
            }
            Transactional transactional=ConnectionHolder.getTransactional();
            if(transactional!=null&&connection.getAutoCommit()){
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(transactional.Isolation());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    protected void preparedStatement(PreparedStatement statement){
        for (Map.Entry<String,Object> param:parameter.entrySet()){
            String name= param.getKey();
            Object value=param.getValue();
            List<Integer> postions=this.findParamLocation(name);
            for(Integer pos:postions){
                try{
                    if(value==null){
                        statement.setNull(pos, Types.NULL);
                        continue;
                    }
                    if(value instanceof String){
                        statement.setString(pos,(String) value);
                    }else if(value instanceof Integer){
                        statement.setInt(pos,(Integer)value);
                    }else if(value instanceof Long){
                        statement.setLong(pos,(Long)value);
                    }else if(value instanceof Short){
                        statement.setShort(pos,(Short) value);
                    }else if(value instanceof Boolean){
                        statement.setBoolean(pos,(Boolean)value);
                    }else if(value instanceof Date){
                        statement.setDate(pos,(java.sql.Date) value);
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private List<Integer> findParamLocation(String name){
        name="@"+name;
        List<Integer> list=new LinkedList<Integer>();
        for (Map.Entry<Integer,String> param:paramPosition.entrySet()){
            if(param.getValue().equals(name)){
                list.add(param.getKey());
            }
        }
        return list;
    }

    private void bindModel(Object instance,Map<String,Field> property,ResultSet result){
        try{
            ResultSetMetaData metaData= result.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i=1;i<=columnCount;i++){
                String columnName = metaData.getColumnName(i);
                Object columnValue=result.getObject(columnName);
                columnName=columnName.replace("_","");
                Field field= property.get(columnName);
                if(field!=null){
                    field.set(instance,columnValue);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void buildCommandText(){
        paramPosition.clear();
        String exp="[?:@]\\w+";
        String command=dataCommand.getCommandText().evaluation(parameter);
        Pattern pattern = Pattern.compile(exp);
        Matcher matcher = pattern.matcher(command);
        int index=0;
        while (matcher.find()){
            index++;
            int start=matcher.start();
            int end=matcher.end();
            String name=command.substring(start,end);
            paramPosition.put(index,name);
        }
        this.commandText= command.replaceAll(exp,"?");
    }
}

package com.kjt.ec.data.imp;

import com.kjt.ec.data.DataAccess;
import com.kjt.ec.data.DataCommand;
import com.kjt.ec.data.configuration.DataCommandSection;
import com.kjt.ec.data.configuration.DatabaseSection;
import com.kjt.ec.data.configuration.ParameterSection;
import com.kjt.ec.data.expressions.*;
import com.kjt.ec.extension.StringExtension;
import org.w3c.dom.*;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccessImp implements DataAccess {
    volatile boolean initlized=false;
    static final String DEFAULT_COMMAND_TYPE="Text";
    static final int DEFAULT_TIME_OUT=7200;
    static final List<DatabaseSection> databaseSections=new ArrayList<DatabaseSection>();
    static final  List<DataCommandSection> commandSections=new ArrayList<DataCommandSection>();
    final HashMap<String,DataCommand> commandHashMap=new HashMap<String, DataCommand>();

    @PostConstruct
    private void initlize(){
        if(!initlized){
            this.parseDatabase();
            this.parseCommand("User.xml");
            initlized=true;
        }
    }

    public DataCommand createCommand(String name){
        this.initlize();
        DataCommand command=commandHashMap.get(name);
        if(command==null){
            synchronized (commandHashMap){
                try{
                    command=commandHashMap.get(name);
                    if(command==null){
                        command=GetCommandDefine(name);
                        if(command!=null){
                            commandHashMap.put(name,command);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return command;
    }

    private void parseDatabase(){
        try{
            InputStream input=this.getClass().getClassLoader().getResourceAsStream("configuration/database.xml");
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=factory.newDocumentBuilder();
            Document document= db.parse(input);
            NodeList nodeList=document.getElementsByTagName("datasource");
            for(int i=0;i<nodeList.getLength();i++) {
                DatabaseSection section = new DatabaseSection();
                Map<String,String> property=section.getDatasource();
                Element element =(Element) nodeList.item(i);
                String name = element.getAttribute("name");
                section.setName(name);

                NodeList childNodes = element.getChildNodes();
                for (int n=0;n<childNodes.getLength();n++){
                    Node node=childNodes.item(n);
                    if(node.getNodeType() != Node.ELEMENT_NODE){
                        continue;
                    }
                    Element propertyNode=(Element)node;
                    property.put(propertyNode.getAttribute("name"),propertyNode.getAttribute("value"));
                }
                databaseSections.add(section);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseCommand(String fileName){
        try{
            InputStream input=this.getClass().getClassLoader().getResourceAsStream("configuration/data/"+fileName);
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=factory.newDocumentBuilder();
            Document document= db.parse(input);
            NodeList nodeList=document.getElementsByTagName("dataCommand");
            for(int i=0;i<nodeList.getLength();i++) {
                DataCommandSection section=new DataCommandSection();
                Element commandNode=(Element)nodeList.item(i);
                String name=commandNode.getAttribute("name");
                String database=commandNode.getAttribute("database");
                String commandType=commandNode.getAttribute("commandType");
                String timeout=commandNode.getAttribute("timeout");
                section.setCommandName(name);
                section.setDatabase(database);
                section.setCommandType(StringExtension.isEmptyOrNull(commandType)?DEFAULT_COMMAND_TYPE:commandType);
                section.setTimeout(StringExtension.isEmptyOrNull(timeout)?DEFAULT_TIME_OUT:Integer.parseInt(timeout));

                NodeList commandChildNode= commandNode.getChildNodes();
                for (int m=0;m<commandChildNode.getLength();m++) {
                    Node child = commandChildNode.item(m);
                    if (child.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    Element element = (Element) child;
                    if (element.getTagName() == "commandText") {
                        section.setCommandText(this.parseCommandText(element.getChildNodes()));
                    } else if (element.getTagName() == "parameters") {
                        NodeList paramNode = element.getElementsByTagName("param");
                        List<ParameterSection> list=new ArrayList<ParameterSection>();
                        for (int n=0;n<paramNode.getLength();n++){
                            Element paramEle=(Element)paramNode.item(n);
                            ParameterSection param=this.parseCommandParameter(paramEle);
                            list.add(param);
                        }
                        section.setParameters(list);
                    }
                }
                commandSections.add(section);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private CommandExpression parseCommandText(NodeList nodeList){
        CommandExpression expression=new CommandExpression();
        List<Expression> expressions=new ArrayList<Expression>();
        expression.setExpressions(expressions);
        for (int m=0;m<nodeList.getLength();m++) {
            Node child = nodeList.item(m);
            if(child.getNodeType()==Node.TEXT_NODE){
                expressions.add(new LiteralExpression(child.getNodeValue()));
            }else if(child.getNodeType()==Node.ELEMENT_NODE) {
                switch (child.getNodeName().toLowerCase()){
                    case "where":
                        WhereExpression where=new WhereExpression();
                        this.parseWhere(child.getChildNodes(),where);
                        expressions.add(where);
                        break;
                    case "trim":
                        break;
                    case "foreach":
                        break;
                }
            }
        }
        return expression;
    }

    private void parseWhere(NodeList nodeList, WhereExpression where) {
        for (int m=0;m<nodeList.getLength();m++){
            Node child = nodeList.item(m);
            if(child.getNodeType()!=Node.ELEMENT_NODE){
                continue;
            }
            Element element=(Element)child;
            if(element.getTagName().equals("if")){
                String exp=element.getAttribute("test");
                String value=element.getTextContent();
                where.getExpressions().add(new IfExpression(exp,value));
            }
        }
    }

    private ParameterSection parseCommandParameter(Element element) {
        ParameterSection parameterSection = new ParameterSection();
        String name = element.getAttribute("name");
        String dbType = element.getAttribute("dbType");
        String size = element.getAttribute("size");
        String direction = element.getAttribute("direction");
        parameterSection.setName(name);
        parameterSection.setDbType(dbType);
        parameterSection.setDirection(StringExtension.isEmptyOrNull(direction)?"InputOutput":direction);
        parameterSection.setSize(StringExtension.isEmptyOrNull(size) ? 65536 : Integer.parseInt(size));
        return parameterSection;
    }

    protected DataCommand GetCommandDefine(String name) throws Exception{
        DataCommand command=null;
        for(DataCommandSection section:commandSections) {
            if (name != null && section.getCommandName().equals(name)) {
                if(section.getDatabase()==null||section.getDatabase().equals("")){
                    throw new Exception(String.format("command %s got a empty database",section.getCommandName()));
                }
                DatabaseSection database=null;
                for (DatabaseSection db:databaseSections){
                    if(db.getName().equals(section.getDatabase())){
                        database=db;
                        break;
                    }
                }
                if(database==null){
                    throw new Exception(String.format("can not find database %s",section.getDatabase()));
                }
                command=new DataCommandImp(database,section);
                break;
            }
        }
        return command;
    }
}

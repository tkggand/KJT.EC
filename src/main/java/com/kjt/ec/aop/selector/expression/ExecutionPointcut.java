package com.kjt.ec.aop.selector.expression;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExecutionPointcut extends AbstractExpression {
    private int position;
    private int total;
    private int tokenLen;
    private boolean parsed;
    private TokenType tokenType;
    final static AnyExpression Any = new AnyExpression();
    private List<AbstractExpression> expressions;

    enum TokenType
    {
        ReturnType,
        PackagName,
        MethodName,
        Argument,
        End
    }

    public ExecutionPointcut(String value)
    {
        super(value);
    }

    private void build()
    {
        if (this.parsed)
            return;
        this.tokenType=TokenType.ReturnType;
        this.parsed = true;
        this.expressions = new ArrayList<AbstractExpression>();
        this.tokenLen = this.getValue().split(" ").length;
        this.position = 0;
        this.total = this.getValue().length();
        this.parseExpression();
    }

    private void parseExpression()
    {
        AbstractExpression exp = readExpression();
        while (exp != null)
        {
            expressions.add(exp);
            exp = readExpression();
        }
    }

    private AbstractExpression readExpression()
    {
        if (position >= total)
            return null;
        switch (tokenType)
        {
            case ReturnType:
                tokenType = TokenType.PackagName;
                return readReturn();
            case PackagName:
                tokenType = TokenType.MethodName;
                return readPackag();
            case MethodName:
                tokenType = TokenType.Argument;
                return readMethod();
            case Argument:
                tokenType = TokenType.End;
                return readArgument();
            default:
            case End:
                return null;
        }
    }

    private AbstractExpression readReturn()
    {
        StringBuilder builder = new StringBuilder();
        do
        {
            char ch = this.getValue().charAt(position++);
            if (ch == ' ')
                break;
            builder.append(ch);
        } while (position < total);
        return new ReturnExpression(builder.toString());
    }

    private AbstractExpression readPackag() {
        StringBuilder builder = new StringBuilder();
        do
        {
            char ch = this.getValue().charAt(position++);
            if (ch == '(')
                break;
            builder.append(ch);
        } while (position < total);
        int len = builder.length();
        while (len > 0)
        {
            char ch = builder.charAt(--len);
            --position;
            if (ch == '.')
                break;
        }
        return new DeclaringExpression(builder.substring(0,len));
    }

    private AbstractExpression readMethod()
    {
        StringBuilder builder = new StringBuilder();
        do
        {
            char ch = this.getValue().charAt(position++);
            if (ch == '(')
                break;
            builder.append(ch);
        } while (position < total);
        return new MethodExpression(builder.toString());
    }

    private AbstractExpression readArgument()
    {
        StringBuilder builder = new StringBuilder();
        do
        {
            char ch = this.getValue().charAt(position++);
            if (ch == ')')
                break;
            builder.append(ch);
        } while (position < total);
        return new ParameterExpression(builder.toString());
    }

    @Override
    protected boolean doMatch(Method method) {
        this.build();
        for (AbstractExpression exp:expressions){
            if(!exp.match(method)){
                return false;
            }
        }
        return true;
    }
}

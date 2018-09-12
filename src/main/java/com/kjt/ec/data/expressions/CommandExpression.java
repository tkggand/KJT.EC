package com.kjt.ec.data.expressions;

import java.util.List;

public class CommandExpression extends Expression {
    private List<Expression> expressions;

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String evaluation(Object context) {
        if(expressions==null){
            return "";
        }
        StringBuilder builder=new StringBuilder();
        for(Expression exp:expressions){
            builder.append(exp.evaluation(context));
        }
        return builder.toString();
    }
}

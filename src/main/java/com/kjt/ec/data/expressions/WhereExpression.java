package com.kjt.ec.data.expressions;

import java.util.ArrayList;
import java.util.List;

public class WhereExpression extends Expression {
    private List<Expression> expressions;

    public WhereExpression(){
        this.expressions=new ArrayList<Expression>();
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public String evaluation(Object context) {
        StringBuilder builder=new StringBuilder();
        builder.append(" WHERE ");
        boolean matchNone=true;
        for(Expression exp:expressions){
            if(exp.match(context)){
                matchNone=false;
                builder.append(exp.evaluation(context));
            }
        }
        if(matchNone){
            return "";
        }
        return builder.toString();
    }
}

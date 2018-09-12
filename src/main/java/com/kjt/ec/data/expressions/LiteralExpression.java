package com.kjt.ec.data.expressions;

public class LiteralExpression extends Expression {

    public LiteralExpression(String value){
        super.value=value;
    }

    @Override
    public String evaluation(Object context) {
        return this.value;
    }
}

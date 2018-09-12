package com.kjt.ec.aop.selector.expression;

public class PointcutTokenlizer {
    int position;
    final int total;
    final String expression;
    static final AnyExpression Any=new AnyExpression();

    public PointcutTokenlizer(String expression)
    {
        this.expression = expression;
        this.position = 0;
        this.total = expression.length();
    }

    public AbstractExpression parser()
    {
        return this.parseExpression();
    }

    private AbstractExpression parseExpression() {
        if (position >= total)
            return null;
        boolean hasAnnontation = this.expression.charAt(0) == '@';
        if (hasAnnontation)
        {
            position++;
        }
        StringBuilder builder = new StringBuilder();
        do
        {
            char ch = expression.charAt(position++);
            if (ch == '(')
                break;
            builder.append(ch);
        } while (position < total);
        int end = total;
        do
        {
            char ch = expression.charAt(--end);
            if (ch == ')')
                break;
        }
        while (end > position);
        if (end == position)
        {
            return new NoneMatchPointcut();
        }

        String expName = builder.toString().toLowerCase();
        String exp = this.expression.substring(position, end);
        switch (expName)
        {
            case "this":
                return new OwnerPointcut(exp);
            case "args":
                return new ArgumentPointcut(hasAnnontation, exp);
            case "target":
                return new TargetPointcut(hasAnnontation, exp);
            case "within":
                return new WithinPointcut(hasAnnontation, exp);
            case "annotation":
                return new AnnotationPointcut(hasAnnontation, exp);
            case "execution":
                return new ExecutionPointcut(exp);
            default:
                return new NoneMatchPointcut();
        }
    }
}

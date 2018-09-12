package com.kjt.ec.data.evaluation;

import ognl.MemberAccess;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Map;

class OgnlMemberAccess implements MemberAccess {

    private final boolean canControlMemberAccessible;

    OgnlMemberAccess() {
        this.canControlMemberAccessible =true;
    }

    public Object setup(Map context, Object target, Member member, String propertyName) {
        Object result = null;
        if (isAccessible(context, target, member, propertyName)) {
            AccessibleObject accessible = (AccessibleObject) member;
            if (!accessible.isAccessible()) {
                result = Boolean.FALSE;
                accessible.setAccessible(true);
            }
        }
        return result;
    }

    public void restore(Map context, Object target, Member member, String propertyName,
                        Object state) {
        if (state != null) {
            ((AccessibleObject) member).setAccessible(((Boolean) state));
        }
    }

    public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
        return canControlMemberAccessible;
    }

}


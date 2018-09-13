package com.kjt.ec.data.imp;

import com.kjt.ec.aop.JoinPoint;
import com.kjt.ec.aop.annontation.Around;
import com.kjt.ec.aop.annontation.Aspect;
import com.kjt.ec.data.annontation.Transactional;
import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Aspect
@Service
@SuppressWarnings("unused")
public class TransactionManager {

    @Around("@annotation(com.kjt.ec.data.annontation.Transactional)")
    public void around(JoinPoint point){
        Transactional transactional= point.getMethod().getAnnotation(Transactional.class);
        ConnectionHolder.setTransactional(transactional);
        try {
            point.process();
            if(point.getException()!=null){
                throw point.getException();
            }
            this.commit();
        }catch (Exception ignore) {
            if(shouldRollback(transactional.rollbackFor(),ignore)){
                this.rollback();
            }
        }
    }

    private void commit() throws Exception{
        Connection connection=ConnectionHolder.getConnection();
        if(connection!=null&&!connection.getAutoCommit()){
            connection.commit();
        }
    }

    private void rollback(){
        Connection connection=ConnectionHolder.getConnection();
        try{
            if(connection!=null&&!connection.getAutoCommit()){
                connection.rollback();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean shouldRollback(Class [] rollback,Exception exception){
        if(rollback==null||rollback.length==0){
            return true;
        }
        for (Class classType:rollback){
            if(classType==exception.getClass()){
                return true;
            }
        }
        return false;
    }
}

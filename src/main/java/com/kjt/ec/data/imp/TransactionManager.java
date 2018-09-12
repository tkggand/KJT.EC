package com.kjt.ec.data.imp;

import com.kjt.ec.aop.JoinPoint;
import com.kjt.ec.aop.annontation.Around;
import com.kjt.ec.aop.annontation.Aspect;
import com.kjt.ec.ioc.annontation.Autowired;
import com.kjt.ec.ioc.annontation.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Aspect
@Service
public class TransactionManager {

    @Autowired
    private DataSourceManager manager;

    @Around("@annotation(com.kjt.ec.data.annontation.Transactional)")
    public void around(JoinPoint point){
        DataSource dataSource=manager.getDatasource();
        if(dataSource==null)
            return;
        Connection connection=null;
        boolean autoCommit=false;
        try {
            connection=dataSource.getConnection();
            if(connection.getAutoCommit()){
                connection.setAutoCommit(false);
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            point.process();
            connection.commit();
        }catch (Exception ignore){
            try{
                if(connection!=null&&!connection.getAutoCommit()){
                    connection.rollback();
                }
            }catch (Exception e){

            }
        }
        System.out.println("transaction injection");
    }
}

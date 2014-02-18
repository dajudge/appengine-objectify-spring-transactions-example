package com.example;

import com.googlecode.objectify.Work;
import org.aspectj.lang.ProceedingJoinPoint;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AppEngineTransactionManager {

    public Object transact(final ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            return ofy().transactNew(new Work<Object>() {
                @Override
                public Object run() {
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throw new ExceptionWrapper(throwable);
                    }
                }
            });
        } catch (ExceptionWrapper e) {
            throw e.getCause();
        } catch (Throwable ex) {
            throw new RuntimeException("Unexpected exception during transaction management", ex);
        }
    }

    private static class ExceptionWrapper extends RuntimeException {
        public ExceptionWrapper(Throwable cause) {
            super(cause);
        }
    }

}

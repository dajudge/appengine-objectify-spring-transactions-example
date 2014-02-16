package com.example;

import com.googlecode.objectify.Work;
import org.aspectj.lang.ProceedingJoinPoint;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by romanl on 03.02.14.
 */
public class AppEngineTransactionManager {

    public Object transact(final ProceedingJoinPoint joinpoint) throws Throwable {

        try {
            return ofy().transactNew(new Work<Object>() {
                @Override
                public Object run() {
                    try {
                        Object ret = joinpoint.proceed();
                        return ret;
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
        private static final long serialVersionUID = 1L;

        public ExceptionWrapper(Throwable cause) {
            super(cause);
        }

        /**
         * This makes the cost of using the ExceptionWrapper negligible
         */
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

}

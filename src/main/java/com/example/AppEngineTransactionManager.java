package com.example;

import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.Work;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by romanl on 03.02.14.
 */
public class AppEngineTransactionManager {

    private final static Logger LOG = LoggerFactory.getLogger(AppEngineTransactionManager.class);

    public Object transact(final ProceedingJoinPoint joinpoint) throws Throwable {

        try {
            return ofy().transactNew(new Work<Object>() {
                @Override
                public Object run() {
                    try {
                        LOG.debug("Entering ofy() transaction '" + this.hashCode() + "'");
                        Object ret = joinpoint.proceed();
                        LOG.debug("Committing ofy() transaction '" + this.hashCode() + "'");
                        return ret;
                    } catch (Throwable throwable) {
                        LOG.debug("Forwarding exception of ofy() transaction '" + this.hashCode() + "':" + throwable.getClass());
                        throw new ExceptionWrapper(throwable);
                    }
                }
            });
        } catch (ExceptionWrapper e) {
            LOG.debug("Transaction rolled back: " + e.getMessage());
            throw e.getCause();
        } catch (Throwable ex) {
            LOG.warn("Transaction rolled back: " + ex.getCause());
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

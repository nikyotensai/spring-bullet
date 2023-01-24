package com.github.nikyotensai.jdbc.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.lang.Nullable;
import org.springframework.transaction.interceptor.TransactionalProxy;


@SuppressWarnings("serial")
public class ConnectionProxyFactoryBean extends AbstractSingletonProxyFactoryBean {

    private final ConnectionInterceptor connectionInterceptor = new ConnectionInterceptor();

    @Nullable
    private Pointcut pointcut;


    /**
     * Set a pointcut, i.e a bean that can cause conditional invocation
     * of the TransactionInterceptor depending on method and attributes passed.
     * Note: Additional interceptors are always invoked.
     *
     * @see #setPreInterceptors
     * @see #setPostInterceptors
     */
    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }


    /**
     * Creates an advisor for this FactoryBean's TransactionInterceptor.
     */
    @Override
    protected Object createMainInterceptor() {
        if (this.pointcut != null) {
            return new DefaultPointcutAdvisor(this.pointcut, this.connectionInterceptor);
        }
        return null;
    }

    /**
     * As of 4.2, this method adds {@link TransactionalProxy} to the set of
     * proxy interfaces in order to avoid re-processing of transaction metadata.
     */
    @Override
    protected void postProcessProxyFactory(ProxyFactory proxyFactory) {
        proxyFactory.addInterface(TransactionalProxy.class);
    }

}

package com.algaworks.ecommerce.hibernate;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

// Ecm -ecomerce
public class EcmCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    //ThreadLocal para separar as requisições...
    private static ThreadLocal<String> tl = new ThreadLocal<>();

    public static void setTenantIdentifier(String tenantId) {
        tl.set(tenantId);
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        return tl.get();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}

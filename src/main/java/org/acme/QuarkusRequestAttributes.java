package org.acme;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.context.request.RequestAttributes;

public class QuarkusRequestAttributes implements RequestAttributes {

    private final Map<String, Object> requestAttributes = new ConcurrentHashMap<>();
    private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

    @Override
    public Object getAttribute(String name, int scope) {
        if (scope == SCOPE_REQUEST) {
            return requestAttributes.get(name);
        }
        if (scope == SCOPE_SESSION) {
            return null;
        }
        throw new IllegalArgumentException("Unknown scope: " + scope);
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        if (scope == SCOPE_REQUEST) {
            requestAttributes.put(name, value);
            return;
        }
        if (scope == SCOPE_SESSION) {
            throw new UnsupportedOperationException("Session scope is not supported in this implementation");
        }
        throw new IllegalArgumentException("Unknown scope: " + scope);
    }

    @Override
    public void removeAttribute(String name, int scope) {
        if (scope == SCOPE_REQUEST) {
            requestAttributes.remove(name);
            destructionCallbacks.remove(name);
            return;
        }
        if (scope == SCOPE_SESSION) {
            throw new UnsupportedOperationException("Session scope is not supported in this implementation");
        }
        throw new IllegalArgumentException("Unknown scope: " + scope);
    }

    @Override
    public String[] getAttributeNames(int scope) {
        if (scope == SCOPE_REQUEST) {
            return requestAttributes.keySet().toArray(String[]::new);
        }
        if (scope == SCOPE_SESSION) {
            return new String[0];
        }
        throw new IllegalArgumentException("Unknown scope: " + scope);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback, int scope) {
        if (scope == SCOPE_REQUEST) {
            destructionCallbacks.put(name, callback);
            return;
        }
        if (scope == SCOPE_SESSION) {
            throw new UnsupportedOperationException("Session scope is not supported in this implementation");
        }
        throw new IllegalArgumentException("Unknown scope: " + scope);
    }

    @Override
    public Object resolveReference(String key) {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Object getSessionMutex() {
        return this;
    }

    void requestCompleted() {
        destructionCallbacks.values().forEach(Runnable::run);
        destructionCallbacks.clear();
        requestAttributes.clear();
    }
}

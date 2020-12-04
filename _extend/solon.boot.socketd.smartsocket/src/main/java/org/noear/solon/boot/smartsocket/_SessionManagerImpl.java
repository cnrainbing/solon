package org.noear.solon.boot.smartsocket;

import org.noear.solon.core.message.Session;
import org.noear.solon.extend.socketd.SessionFactory;
import org.noear.solon.extend.socketd.SessionManager;
import org.smartboot.socket.transport.AioSession;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

class _SessionManagerImpl extends SessionManager {
    @Override
    protected Session getSession(Object conn) {
        if (conn instanceof AioSession) {
            return _SocketSession.get((AioSession) conn);
        } else {
            throw new IllegalArgumentException("This conn requires a smartsocket AioSession type");
        }
    }

    @Override
    protected Collection<Session> getOpenSessions() {
        return Collections.unmodifiableCollection(_SocketSession.sessions.values());
    }

    @Override
    protected void removeSession(Object conn) {
        if (conn instanceof AioSession) {
            _SocketSession.remove((AioSession) conn);
        } else {
            throw new IllegalArgumentException("This conn requires a smartsocket AioSession type");
        }
    }
}
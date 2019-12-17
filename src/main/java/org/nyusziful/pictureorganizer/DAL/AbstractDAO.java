package org.nyusziful.pictureorganizer.DAL;

import org.hibernate.Session;

public abstract class AbstractDAO {
    private Session currentSession;

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Session getCurrentSession() {
        return currentSession;
    }
}

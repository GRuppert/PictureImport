package org.nyusziful.pictureorganizer.DB;

import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class AbstractDAO {
    private Session currentSession;

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Session getCurrentSession() {
        return currentSession;
    }
}

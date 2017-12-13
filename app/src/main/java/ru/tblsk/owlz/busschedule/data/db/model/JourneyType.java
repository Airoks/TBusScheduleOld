package ru.tblsk.owlz.busschedule.data.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true)
public class JourneyType {
    @Id
    private long JourneyTypePK;

    @NotNull
    private String JourneyTypeName;

    @ToMany(referencedJoinProperty = "JourneyTypeFK")
    private List<Journey> journeys;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1215605968)
    private transient JourneyTypeDao myDao;

    @Generated(hash = 1020900786)
    public JourneyType(long JourneyTypePK, @NotNull String JourneyTypeName) {
        this.JourneyTypePK = JourneyTypePK;
        this.JourneyTypeName = JourneyTypeName;
    }

    @Generated(hash = 147593267)
    public JourneyType() {
    }

    public long getJourneyTypePK() {
        return this.JourneyTypePK;
    }

    public void setJourneyTypePK(long JourneyTypePK) {
        this.JourneyTypePK = JourneyTypePK;
    }

    public String getJourneyTypeName() {
        return this.JourneyTypeName;
    }

    public void setJourneyTypeName(String JourneyTypeName) {
        this.JourneyTypeName = JourneyTypeName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 220500115)
    public List<Journey> getJourneys() {
        if (journeys == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            JourneyDao targetDao = daoSession.getJourneyDao();
            List<Journey> journeysNew = targetDao
                    ._queryJourneyType_Journeys(JourneyTypePK);
            synchronized (this) {
                if (journeys == null) {
                    journeys = journeysNew;
                }
            }
        }
        return journeys;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1407158211)
    public synchronized void resetJourneys() {
        journeys = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 825192144)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getJourneyTypeDao() : null;
    }
}

package ru.tblsk.owlz.busschedule.data.db.model;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true)
public class Schedule {
    @SerializedName("id")
    @Id
    private long scheduleId;

    @SerializedName("stops_on_routs_fk")
    private long stopsOnRoutsId;

    @SerializedName("schedule_type_fk")
    private long scheduleTypeId;

    @SerializedName("schedule_json")
    @NotNull
    private String scheduleJson;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1493574644)
    private transient ScheduleDao myDao;

    @Generated(hash = 1917555176)
    public Schedule(long scheduleId, long stopsOnRoutsId, long scheduleTypeId,
            @NotNull String scheduleJson) {
        this.scheduleId = scheduleId;
        this.stopsOnRoutsId = stopsOnRoutsId;
        this.scheduleTypeId = scheduleTypeId;
        this.scheduleJson = scheduleJson;
    }

    @Generated(hash = 729319394)
    public Schedule() {
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

    public long getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getStopsOnRoutsId() {
        return this.stopsOnRoutsId;
    }

    public void setStopsOnRoutsId(long stopsOnRoutsId) {
        this.stopsOnRoutsId = stopsOnRoutsId;
    }

    public long getScheduleTypeId() {
        return this.scheduleTypeId;
    }

    public void setScheduleTypeId(long scheduleTypeId) {
        this.scheduleTypeId = scheduleTypeId;
    }

    public String getScheduleJson() {
        return this.scheduleJson;
    }

    public void setScheduleJson(String scheduleJson) {
        this.scheduleJson = scheduleJson;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 502317300)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getScheduleDao() : null;
    }
}
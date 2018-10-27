/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author frg
 */
@Entity
@Table(name = "step_count")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StepCount.findAll", query = "SELECT s FROM StepCount s"),
    @NamedQuery(name = "StepCount.findById", query = "SELECT s FROM StepCount s WHERE s.id = :id"),
    @NamedQuery(name = "StepCount.findByUserId", query = "SELECT s FROM StepCount s WHERE s.userId = :userId"),
    @NamedQuery(name = "StepCount.findByDay", query = "SELECT s FROM StepCount s WHERE s.day = :day"),
    @NamedQuery(name = "StepCount.findByTimeInterval", query = "SELECT s FROM StepCount s WHERE s.timeInterval = :timeInterval"),
    @NamedQuery(name = "StepCount.findByStepCount", query = "SELECT s FROM StepCount s WHERE s.stepCount = :stepCount"),
    @NamedQuery(name = "StepCount.findCurrentDayByUserId", query = "SELECT MAX(s.day) FROM StepCount s WHERE s.userId = :userId"),
    @NamedQuery(name = "StepCount.findStepSumByUserIdDay", query = "SELECT SUM(s.stepCount) FROM StepCount s WHERE s.userId = :userId and s.day = :day"),
    @NamedQuery(name = "StepCount.findRangeStepSumByUserId", query = "SELECT SUM(s.stepCount) FROM StepCount s WHERE s.userId = :userId and s.day >= :startDay and s.day < :endDay GROUP BY s.day")
        
})
public class StepCount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "day")
    private int day;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time_interval")
    private int timeInterval;
    @Basic(optional = false)
    @NotNull
    @Column(name = "step_count")
    private int stepCount;

    public StepCount() {
    }

    public StepCount(Integer id) {
        this.id = id;
    }

    public StepCount(Integer id, int userId, int day, int timeInterval, int stepCount) {
        this.id = id;
        this.userId = userId;
        this.day = day;
        this.timeInterval = timeInterval;
        this.stepCount = stepCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StepCount)) {
            return false;
        }
        StepCount other = (StepCount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.StepCount[ id=" + id + " ]";
    }
    
}

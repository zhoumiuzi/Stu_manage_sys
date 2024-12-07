package com.zyy.entity;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-07
 * @Description: 课程实体
 * @Version: 1.0
 */


public class Course {

    private Integer courseid;

    private String coursename;

    private Integer coursenum;

    private String coursetype;

    private Integer coursecredits;

    private Integer teacherid;

    private String teachername;

    private String coursedesc;

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public Integer getCoursenum() {
        return coursenum;
    }

    public void setCoursenum(Integer coursenum) {
        this.coursenum = coursenum;
    }

    public String getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype;
    }

    public Integer getCoursecredits() {
        return coursecredits;
    }

    public void setCoursecredits(Integer coursecredits) {
        this.coursecredits = coursecredits;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getCoursedesc() {
        return coursedesc;
    }

    public void setCoursedesc(String coursedesc) {
        this.coursedesc = coursedesc;
    }
}

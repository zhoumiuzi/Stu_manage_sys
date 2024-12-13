package com.zyy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zhouMiuzi
 * @CreateTime: 2024-12-13
 * @Description:
 * @Version: 1.0
 */


public class EnrollmentPageResult {

    private long total; // 总记录数
    private int totalElectiveCredits; // 全部选修课程总学分
    private int completedElectiveCredits; // 已完成选修课程总学分
    private List<Enrollment> list; // 当前页的选课列表

    // 构造器
    public EnrollmentPageResult() {}

    public EnrollmentPageResult(long total, int totalElectiveCredits, int completedElectiveCredits, List<Enrollment> list) {
        this.total = total;
        this.totalElectiveCredits = totalElectiveCredits;
        this.completedElectiveCredits = completedElectiveCredits;
        this.list = list;
    }

    // Getters and Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalElectiveCredits() {
        return totalElectiveCredits;
    }

    public void setTotalElectiveCredits(int totalElectiveCredits) {
        this.totalElectiveCredits = totalElectiveCredits;
    }

    public int getCompletedElectiveCredits() {
        return completedElectiveCredits;
    }

    public void setCompletedElectiveCredits(int completedElectiveCredits) {
        this.completedElectiveCredits = completedElectiveCredits;
    }

    public List<Enrollment> getList() {
        return list;
    }

    public void setList(List<Enrollment> list) {
        this.list = list;
    }
}

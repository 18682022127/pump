package com.itouch8.pump.core.util.page.impl;

import com.itouch8.pump.core.util.page.IPage;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 默认分页对象<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class BasePage implements IPage {

    private static final long serialVersionUID = -5436993928357442743L;

    // 是否需要计算总记录数
    private boolean needCalTotal = true;

    // 记录总条数
    private long totalRecords;

    // 每页记录条数
    private int pageSize;

    // 总页数
    private long totalPages;

    // 当前页数
    private long currentPage = 1;

    // 是否有下一页
    private boolean hasPrevPage = false;

    // 是否有上一页
    private boolean hasNextPage = false;

    // 是否是第一页
    private boolean firstPage = false;

    // 是否为最后一页
    private boolean lastPage = false;

    // 当前页的起始记录序号（偏移量，不含start位置）
    private long start;

    // 当前页的结束记录序号（结束，含end位置）
    private long end;

    
    @Override
    public void setPageProperty(long totalRecords) {
        setPageProperty(totalRecords, this.currentPage, this.pageSize);
    }

    
    @Override
    public void setPageProperty(long totalRecords, long currentPage, int pageSize) {
        this.totalRecords = totalRecords;// 总记录数
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        if (this.totalRecords >= 0) {
            this.totalPages = totalRecords / pageSize + (totalRecords % pageSize == 0 ? 0 : 1);// 总页数
            this.currentPage = Math.min(currentPage, totalPages);// 当前页数（不超过总页数）
            this.start = (0 >= this.currentPage ? 0 : pageSize * (this.currentPage - 1));// 当前页起始记录数，从0开始
            this.end = start + pageSize;// 当前页结束记录数
            this.hasPrevPage = this.totalPages >= 1 && this.currentPage > 1;
            this.hasNextPage = this.totalPages >= 1 && this.currentPage != this.totalPages;
            this.firstPage = this.totalPages >= 1 && this.currentPage == 1;
            this.lastPage = this.totalPages >= 1 && this.currentPage == this.totalPages;
            this.needCalTotal = false;// 将总记录数设置为不需重新计算
        } else {
            this.hasPrevPage = false;
            this.hasNextPage = false;
            this.firstPage = false;
            this.lastPage = false;
            this.needCalTotal = true;
        }
    }

    @Override
    public long getTotalRecords() {
        return this.totalRecords;
    }

    @Override
    public boolean isNeedCalTotal() {
        return this.needCalTotal;
    }

    @Override
    public long getTotalPages() {
        return this.totalPages;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public long getStart() {
        return this.start;
    }

    @Override
    public long getEnd() {
        return this.end;
    }

    @Override
    public boolean hasPrevPage() {
        return this.hasPrevPage;
    }

    @Override
    public boolean hasNextPage() {
        return this.hasNextPage;
    }

    @Override
    public boolean isFirstPage() {
        return this.firstPage;
    }

    @Override
    public boolean isLastPage() {
        return this.lastPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setNeedCalTotal(boolean needCalTotal) {
        this.needCalTotal = needCalTotal;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}

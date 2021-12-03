package com.qtec.unicom.pojo;

public class PageVo {
    private Integer PageNumber;
    private Integer PageSize;
    private Integer totalCount;

    public Integer getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        PageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return PageSize;
    }

    public void setPageSize(Integer pageSize) {
        PageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "PageNumber=" + PageNumber +
                ", PageSize=" + PageSize +
                ", totalCount=" + totalCount +
                '}';
    }
}

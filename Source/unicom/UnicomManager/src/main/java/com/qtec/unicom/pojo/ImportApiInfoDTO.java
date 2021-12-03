package com.qtec.unicom.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ImportApiInfoDTO {

    @Excel(name = "接口名称")
    private String apiName;
    @Excel(name = "链接")
    private String apiURL;
    @Excel(name = "上级")
    private Integer parentId;
    @Excel(name = "备注")
    private String comments;

}

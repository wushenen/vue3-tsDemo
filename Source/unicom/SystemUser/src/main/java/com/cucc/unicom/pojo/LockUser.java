package com.cucc.unicom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LockUser {

	private int id;

	private String userName;

	private Integer failureNum;

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private Date loginTime;

	private int isLock;
}

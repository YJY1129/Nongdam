package kr.co.ezen.entity;

import java.util.Date;

import lombok.Data;

@Data
public class GpUser {
	
	private int gp_idx;
	private int user_idx;
	private String gp_uid;
	private String gp_zipcode;
	private String gp_addr;
	private String gp_name;
	private String gp_email;
	private String gp_num;
	private String gp_total;

	
	private Date gp_date_start;
    private Date gp_date_last;
    private String gp_title;
    private String gp_price;
    
    private int gpUserCount;
}

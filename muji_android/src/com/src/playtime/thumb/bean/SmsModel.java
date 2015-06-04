package com.src.playtime.thumb.bean;

import java.util.Date;

import org.litepal.crud.DataSupport;

public class SmsModel extends DataSupport {

	private String sms_id; // 短信序号

	private String thread_id;// 对话的序号，如100，与同一个手机号互发的短信，其序号是相同的

	private String address;// 发件人地址，即手机号，如+8613811810000

	private String name;// 联系人名字

	private String date;// 日期，long型，如1256539465022，可以对日期显示格式进行设置

	private String protocol;// 协议0SMS_RPOTO短信，1MMS_PROTO彩信

	private String read;// 是否阅读0未读，1已读

	private String status;// 短信状态-1接收，0complete,64pending,128failed

	private String type;// 短信类型1是接收到的，2是已发出

	private String body;// 短信具体内容

    public boolean sel=false;//是否选中

    public boolean getSel() {
        return sel;
    }

    public void setSel(boolean sel) {
        this.sel = sel;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSms_id() {
		return sms_id;
	}

	public void set_Smsid(String _id) {
		this.sms_id = _id;
	}

	public String getThread_id() {
		return thread_id;
	}

	public void setThread_id(String thread_id) {
		this.thread_id = thread_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}

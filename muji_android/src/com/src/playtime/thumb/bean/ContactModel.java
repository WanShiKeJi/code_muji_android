package com.src.playtime.thumb.bean;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.src.playtime.thumb.utils.BaseUtil;

public class ContactModel extends DataSupport implements Serializable {
	/** 名字 */
	private String name = "";
	/** 电话号码 */
	private String telnum = "";
	/** 拼音名字 */
	private String pyname = "";
	/** 运营商 */
	private String operators = "";
	/** 归属地 */
	private String attribution = "";
	/***/
	public String group = "";
	/** 拼音对应的数字 */
	// public String pinyinsum;
	/** 联系人对应的ID */
	private String contactId = "";
    /**通话时间*/
	private String date = "";
    /**通话时长*/
    private String duration="";
    /**当前电话状态*/
    private String state="";
    /**名字多音字转化为数字的数组*/
    public List<String> pynameList;

	// public ContactModel(String name, String telnum, String contactId) {
	// this.name = name;
	// this.telnum = telnum;
	// // this.pinyinsum = pinyinsum;
	// this.group = "";
	// this.contactId = contactId;
	//
	// }


    public void setPynameList(List<String> pynameList) {
        this.pynameList = pynameList;
    }

    public List<String> getPynameList() {
        return pynameList;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getContactId() {
		return contactId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	// public String getPinyinsum() {
	// return pinyinsum;
	// }
	//
	// public void setPinyinsum(String pinyinsum) {
	// this.pinyinsum = pinyinsum;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public String getPyname() {
		return pyname;
	}

	public void setPyname(String pyname) {
		this.pyname = pyname;
	}

	public String getOperators() {
		return operators;
	}

	public void setOperators(String operators) {
		this.operators = operators;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}

package com.example.sign_online.Baseclass;

/**表示用户要接受的信息的一个mould
 * Created by 曾志强 on 2016/3/17.
 */
public class userim {
//发送方
    private String sendport=null;
    //接收方
    private String receiveport=null;
    //要传输的文件名
    private String filename=null;
    //备注
    private String remark=null;

    public String getSendport() {
        return sendport;
    }

    public void setSendport(String sendport) {
        this.sendport = sendport;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getReceiveport() {
        return receiveport;
    }

    public void setReceiveport(String receiveport) {
        this.receiveport = receiveport;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

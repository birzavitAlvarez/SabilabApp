package com.demo.sabilabapp.Compras;

public class ComprasResponse {
    private int status;
    private String statusMessage;
    private Data data;
    public ComprasResponse() {
    }

    public ComprasResponse(int status,String statusMessage,Data data) {
        this.status = status;
        this.statusMessage=statusMessage;
        this.data=data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

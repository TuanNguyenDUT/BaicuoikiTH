package model;

import java.io.Serializable;

public class PhancongGiamthi implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int stt;
    private String maCanBo;
    private String hoVaTen;
    private String giamthi1; // X hoặc trống
    private String giamthi2; // X hoặc trống
    private String phongThi;

    public PhancongGiamthi() {}

    public PhancongGiamthi(int stt, String maCanBo, String hoVaTen, 
                          String giamthi1, String giamthi2, String phongThi) {
        this.stt = stt;
        this.maCanBo = maCanBo;
        this.hoVaTen = hoVaTen;
        this.giamthi1 = giamthi1;
        this.giamthi2 = giamthi2;
        this.phongThi = phongThi;
    }

    // Getters and Setters
    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getMaCanBo() {
        return maCanBo;
    }

    public void setMaCanBo(String maCanBo) {
        this.maCanBo = maCanBo;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getGiamthi1() {
        return giamthi1;
    }

    public void setGiamthi1(String giamthi1) {
        this.giamthi1 = giamthi1;
    }

    public String getGiamthi2() {
        return giamthi2;
    }

    public void setGiamthi2(String giamthi2) {
        this.giamthi2 = giamthi2;
    }

    public String getPhongThi() {
        return phongThi;
    }

    public void setPhongThi(String phongThi) {
        this.phongThi = phongThi;
    }

    @Override
    public String toString() {
        return "PhancongGiamthi{" +
                "stt=" + stt +
                ", maCanBo='" + maCanBo + '\'' +
                ", hoVaTen='" + hoVaTen + '\'' +
                ", giamthi1='" + giamthi1 + '\'' +
                ", giamthi2='" + giamthi2 + '\'' +
                ", phongThi='" + phongThi + '\'' +
                '}';
    }
}

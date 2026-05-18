package model;

import java.io.Serializable;

public class CanBo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int stt;
    private String maCanBo;
    private String hoVaTen;
    private String ngaySinh;
    private String donViCongTac;

    public CanBo() {}

    public CanBo(int stt, String maCanBo, String hoVaTen, String ngaySinh, String donViCongTac) {
        this.stt = stt;
        this.maCanBo = maCanBo;
        this.hoVaTen = hoVaTen;
        this.ngaySinh = ngaySinh;
        this.donViCongTac = donViCongTac;
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

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDonViCongTac() {
        return donViCongTac;
    }

    public void setDonViCongTac(String donViCongTac) {
        this.donViCongTac = donViCongTac;
    }

    @Override
    public String toString() {
        return "CanBo{" +
                "stt=" + stt +
                ", maCanBo='" + maCanBo + '\'' +
                ", hoVaTen='" + hoVaTen + '\'' +
                ", ngaySinh='" + ngaySinh + '\'' +
                ", donViCongTac='" + donViCongTac + '\'' +
                '}';
    }
}

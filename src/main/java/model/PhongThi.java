package model;

import java.io.Serializable;

public class PhongThi implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int stt;
    private String maPhong;
    private String diaDiem;

    public PhongThi() {}

    public PhongThi(int stt, String maPhong, String diaDiem) {
        this.stt = stt;
        this.maPhong = maPhong;
        this.diaDiem = diaDiem;
    }

    // Getters and Setters
    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    @Override
    public String toString() {
        return "PhongThi{" +
                "stt=" + stt +
                ", maPhong='" + maPhong + '\'' +
                ", diaDiem='" + diaDiem + '\'' +
                '}';
    }
}

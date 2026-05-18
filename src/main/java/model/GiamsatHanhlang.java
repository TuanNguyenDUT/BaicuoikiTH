package model;

import java.io.Serializable;

public class GiamsatHanhlang implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int stt;
    private String maCanBo;
    private String hoVaTen;
    private String phongDuocGiamSat; // Từ C101 đến C110

    public GiamsatHanhlang() {}

    public GiamsatHanhlang(int stt, String maCanBo, String hoVaTen, String phongDuocGiamSat) {
        this.stt = stt;
        this.maCanBo = maCanBo;
        this.hoVaTen = hoVaTen;
        this.phongDuocGiamSat = phongDuocGiamSat;
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

    public String getPhongDuocGiamSat() {
        return phongDuocGiamSat;
    }

    public void setPhongDuocGiamSat(String phongDuocGiamSat) {
        this.phongDuocGiamSat = phongDuocGiamSat;
    }

    @Override
    public String toString() {
        return "GiamsatHanhlang{" +
                "stt=" + stt +
                ", maCanBo='" + maCanBo + '\'' +
                ", hoVaTen='" + hoVaTen + '\'' +
                ", phongDuocGiamSat='" + phongDuocGiamSat + '\'' +
                '}';
    }
}

-- SQL Setup Scripts for Exam Assignment System
-- PostgreSQL (Neon)

-- Tạo table Cán bộ
CREATE TABLE IF NOT EXISTS canbo (
    id SERIAL PRIMARY KEY,
    ma_canbo VARCHAR(20) UNIQUE NOT NULL,
    ho_va_ten VARCHAR(100) NOT NULL,
    ngay_sinh DATE,
    don_vi_cong_tac VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo table Phòng thi
CREATE TABLE IF NOT EXISTS phongthi (
    id SERIAL PRIMARY KEY,
    ma_phong VARCHAR(20) UNIQUE NOT NULL,
    dia_diem VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo table Phân công giám thị
CREATE TABLE IF NOT EXISTS phancong_giamthi (
    id SERIAL PRIMARY KEY,
    ma_canbo_1 VARCHAR(20),
    ten_canbo_1 VARCHAR(100),
    ma_canbo_2 VARCHAR(20),
    ten_canbo_2 VARCHAR(100),
    ma_phong VARCHAR(20),
    ca_thi INT DEFAULT 1,
    ngay_phan_cong TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_canbo_1) REFERENCES canbo(ma_canbo),
    FOREIGN KEY (ma_canbo_2) REFERENCES canbo(ma_canbo),
    FOREIGN KEY (ma_phong) REFERENCES phongthi(ma_phong),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo table Giám sát hành lang
CREATE TABLE IF NOT EXISTS giamsat_hanhlang (
    id SERIAL PRIMARY KEY,
    ma_canbo VARCHAR(20),
    ho_va_ten VARCHAR(100),
    phong_tu VARCHAR(20),
    phong_den VARCHAR(20),
    ca_thi INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_canbo) REFERENCES canbo(ma_canbo)
);

-- Tạo index để tăng tốc độ query
CREATE INDEX idx_canbo_ma ON canbo(ma_canbo);
CREATE INDEX idx_phongthi_ma ON phongthi(ma_phong);
CREATE INDEX idx_phancong_phong ON phancong_giamthi(ma_phong);
CREATE INDEX idx_phancong_ca ON phancong_giamthi(ca_thi);

-- Insert sample data (tùy chọn)
INSERT INTO canbo (ma_canbo, ho_va_ten, ngay_sinh, don_vi_cong_tac) VALUES
('GV001', 'Nguyễn Anh', '1961-04-09', 'ĐHBK'),
('GV002', 'Nguyễn Bê', '1979-09-07', 'ĐHKT'),
('GV003', 'Trần Hưng', '1985-06-15', 'ĐHSP'),
('GV004', 'Lê Văn A', '1990-03-22', 'ĐHXD'),
('GV005', 'Phạm Thị B', '1988-12-10', 'ĐHCN');

INSERT INTO phongthi (ma_phong, dia_diem) VALUES
('128', 'Đà Nẵng'),
('129', 'Huế'),
('130', 'Quảng Nam'),
('131', 'Quảng Bình'),
('132', 'Hà Tĩnh');

-- Procedure để có thể insert dữ liệu phân công từ Java
CREATE OR REPLACE FUNCTION insert_phancong(
    p_ma_canbo_1 VARCHAR,
    p_ten_canbo_1 VARCHAR,
    p_ma_canbo_2 VARCHAR,
    p_ten_canbo_2 VARCHAR,
    p_ma_phong VARCHAR,
    p_ca_thi INT
) RETURNS INT AS $$
DECLARE
    v_id INT;
BEGIN
    INSERT INTO phancong_giamthi (ma_canbo_1, ten_canbo_1, ma_canbo_2, ten_canbo_2, ma_phong, ca_thi)
    VALUES (p_ma_canbo_1, p_ten_canbo_1, p_ma_canbo_2, p_ten_canbo_2, p_ma_phong, p_ca_thi)
    RETURNING id INTO v_id;
    RETURN v_id;
END;
$$ LANGUAGE plpgsql;

-- Query để lấy danh sách phân công
-- SELECT * FROM phancong_giamthi WHERE ca_thi = 1 ORDER BY ma_phong;

-- Query để lấy danh sách giám sát
-- SELECT * FROM giamsat_hanhlang WHERE ca_thi = 1 ORDER BY phong_tu;

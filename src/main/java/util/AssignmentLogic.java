package util;

import model.CanBo;
import model.PhongThi;
import model.PhancongGiamthi;
import model.GiamsatHanhlang;

import java.util.*;

public class AssignmentLogic {
    
    private List<CanBo> allCanBo;
    private List<PhongThi> allPhongThi;
    private List<PhancongGiamthi> phancongList;
    private List<GiamsatHanhlang> giamsatList;
    
    // Lưu các giám thị đã được phân công cho mỗi phòng ở lần trước
    private Map<String, Set<String>> previousAssignments; // phongThi -> {maCanBo1, maCanBo2}

    public AssignmentLogic(List<CanBo> allCanBo, List<PhongThi> allPhongThi) {
        this.allCanBo = allCanBo;
        this.allPhongThi = allPhongThi;
        this.phancongList = new ArrayList<>();
        this.giamsatList = new ArrayList<>();
        this.previousAssignments = new HashMap<>();
    }

    /**
     * Thực hiện phân công giám thị
     */
    public void doAssignment() {
        phancongList.clear();
        giamsatList.clear();
        
        int totalCanBo = allCanBo.size();
        int totalPhongThi = allPhongThi.size();
        
        // Kiểm tra số lượng cán bộ có đủ không (ít nhất 2 người/phòng)
        if (totalCanBo < 2 * totalPhongThi) {
            throw new RuntimeException("Số cán bộ không đủ. Cần ít nhất " + (2 * totalPhongThi) + " người!");
        }
        
        // Số cán bộ dư (làm giám sát hành lang)
        int soCanBoDu = totalCanBo - (2 * totalPhongThi);
        int soCanBoGiamSat = soCanBoDu;
        int soPhongThi = allPhongThi.size();
        int soCanBoGiamSatTrenPhong = soCanBoGiamSat / soPhongThi; // Chia đều cho các phòng
        
        // Tạo danh sách cán bộ có sẵn
        List<CanBo> availableCanBo = new ArrayList<>(allCanBo);
        
        // Phân công giám thị
        int phancongStt = 1;
        Random rand = new Random();
        
        for (PhongThi phong : allPhongThi) {
            // Chọn 2 giám thị ngẫu nhiên
            Set<String> previousCanBo = previousAssignments.getOrDefault(phong.getMaPhong(), new HashSet<>());
            
            // Lọc các cán bộ có thể sử dụng (chưa được phân công, không trùng lần trước)
            List<CanBo> validCanBo = new ArrayList<>();
            for (CanBo canBo : availableCanBo) {
                if (!previousCanBo.contains(canBo.getMaCanBo())) {
                    validCanBo.add(canBo);
                }
            }
            
            // Nếu không có đủ, loại bỏ điều kiện "không trùng lần trước"
            if (validCanBo.size() < 2) {
                validCanBo = new ArrayList<>(availableCanBo);
            }
            
            // Chọn 2 giám thị
            if (validCanBo.size() < 2) {
                throw new RuntimeException("Không đủ cán bộ để phân công cho phòng: " + phong.getMaPhong());
            }
            
            CanBo giamthi1 = validCanBo.remove(rand.nextInt(validCanBo.size()));
            CanBo giamthi2 = validCanBo.remove(rand.nextInt(validCanBo.size()));
            
            // Loại bỏ khỏi danh sách có sẵn
            availableCanBo.remove(giamthi1);
            availableCanBo.remove(giamthi2);
            
            // Tạo bản ghi phân công
            PhancongGiamthi pc = new PhancongGiamthi(
                phancongStt++,
                giamthi1.getMaCanBo(),
                giamthi1.getHoVaTen(),
                "X",  // Giám thi 1
                "",   // Giám thi 2 để trống
                phong.getMaPhong()
            );
            phancongList.add(pc);
            
            pc = new PhancongGiamthi(
                phancongStt++,
                giamthi2.getMaCanBo(),
                giamthi2.getHoVaTen(),
                "",   // Giám thi 1 để trống
                "X",  // Giám thi 2
                phong.getMaPhong()
            );
            phancongList.add(pc);
            
            // Lưu phân công này cho lần sau
            Set<String> assigned = new HashSet<>();
            assigned.add(giamthi1.getMaCanBo());
            assigned.add(giamthi2.getMaCanBo());
            previousAssignments.put(phong.getMaPhong(), assigned);
        }
        
        // Phân công giám sát hành lang
        // Chỉ lấy số cán bộ bằng số phòng (hoặc ít hơn nếu không đủ)
        int numGiamsat = Math.min(availableCanBo.size(), allPhongThi.size());
        
        int giamsatStt = 1;
        for (int i = 0; i < numGiamsat; i++) {
            CanBo canBo = availableCanBo.get(i);
            
            // Mỗi cán bộ giám sát được giao 1 phòng tương ứng
            PhongThi phongGiamsat = allPhongThi.get(i);
            
            String phongRange = "Từ " + phongGiamsat.getMaPhong() + " đến " + phongGiamsat.getMaPhong();
            
            GiamsatHanhlang giamsat = new GiamsatHanhlang(
                giamsatStt++,
                canBo.getMaCanBo(),
                canBo.getHoVaTen(),
                phongRange
            );
            giamsatList.add(giamsat);
        }
    }

    public List<PhancongGiamthi> getPhancongList() {
        return phancongList;
    }

    public List<GiamsatHanhlang> getGiamsatList() {
        return giamsatList;
    }
}

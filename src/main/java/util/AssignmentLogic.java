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

    public AssignmentLogic(List<CanBo> allCanBo, List<PhongThi> allPhongThi) {
        this.allCanBo = allCanBo;
        this.allPhongThi = allPhongThi;
        this.phancongList = new ArrayList<>();
        this.giamsatList = new ArrayList<>();
    }

    /**
     * Thuật toán 4: Công thức tính Lmax - số lần phân công tối đa không xung đột.
     * Lmax = floor(N / (2 * M))
     * Ví dụ: N=20, M=10 → Lmax=1; N=40, M=10 → Lmax=2
     */
    public int tinhLmax() {
        int N = allCanBo.size();
        int M = allPhongThi.size();
        if (M == 0) return 0;
        return N / (2 * M);
    }

    /**
     * Gọi doAssignment với line mặc định = 0
     */
    public void doAssignment() {
        doAssignment(0);
    }

    /**
     * Thực hiện phân công theo 3 thuật toán:
     *   1. Invigilator Assignment  – phép tính mô-đun
     *   2. Room Partitioning       – chia phòng thành khối
     *   3. Greedy Selection        – chọn giám sát hành lang
     *
     * @param line lần tạo lịch (0-indexed, từ 0 đến Lmax-1)
     */
    public void doAssignment(int line) {
        phancongList.clear();
        giamsatList.clear();

        int N = allCanBo.size();
        int M = allPhongThi.size();

        if (N < 2 * M) {
            throw new RuntimeException("Số cán bộ không đủ. Cần ít nhất " + (2 * M) + " người!");
        }

        // S = N/2: nửa đầu là ứng viên GT1, nửa sau là ứng viên GT2
        int S = N / 2;

        // Chuẩn hóa line về [0, Lmax-1]
        int Lmax = tinhLmax();
        if (Lmax > 0) {
            line = ((line % Lmax) + Lmax) % Lmax;
        } else {
            line = 0;
        }

        // ----------------------------------------------------------------
        // Thuật toán 1: Phân công giám thị bằng phép tính số học mô-đun
        //   Phòng i, lần line:
        //     idx1 = (i + line * M) % S   → Giám thị 1
        //     idx2 = idx1 + S             → Giám thị 2
        //
        // Ví dụ: N=20, M=10, line=0, S=10
        //   Phòng 0: GT1=vị trí 0,  GT2=vị trí 10
        //   Phòng 1: GT1=vị trí 1,  GT2=vị trí 11
        // ----------------------------------------------------------------
        Set<Integer> usedIndices = new HashSet<>();
        int phancongStt = 1;

        for (int i = 0; i < M; i++) {
            int idx1 = (i + line * M) % S;
            int idx2 = idx1 + S;

            CanBo gt1 = allCanBo.get(idx1);
            CanBo gt2 = allCanBo.get(idx2);

            usedIndices.add(idx1);
            usedIndices.add(idx2);

            phancongList.add(new PhancongGiamthi(
                phancongStt++, gt1.getMaCanBo(), gt1.getHoVaTen(),
                "X", "", allPhongThi.get(i).getMaPhong()
            ));
            phancongList.add(new PhancongGiamthi(
                phancongStt++, gt2.getMaCanBo(), gt2.getHoVaTen(),
                "", "X", allPhongThi.get(i).getMaPhong()
            ));
        }

        // Thu thập cán bộ còn lại (chưa phân công làm giám thị)
        List<CanBo> remainingCanBo = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (!usedIndices.contains(i)) {
                remainingCanBo.add(allCanBo.get(i));
            }
        }

        if (remainingCanBo.isEmpty()) return;

        // ----------------------------------------------------------------
        // Thuật toán 2: Phân chia khối phòng thi (Room Partitioning)
        //   K = số cán bộ giám sát (không vượt quá số phòng)
        //   baseSize = M / K,  extra = M % K
        //   Khối 0..(K-extra-1): baseSize phòng
        //   Khối (K-extra)..(K-1): (baseSize+1) phòng
        //
        // Ví dụ: 13 phòng, 3 khối → baseSize=4, extra=1
        //   Khối 0: 4 phòng, Khối 1: 4 phòng, Khối 2: 5 phòng
        // ----------------------------------------------------------------
        int K = remainingCanBo.size();
        if (K > M) K = M;

        int baseSize = M / K;
        int extra    = M % K;

        List<int[]> blocks = new ArrayList<>();
        int start = 0;
        for (int k = 0; k < K; k++) {
            int size = baseSize + (k >= (K - extra) ? 1 : 0);
            blocks.add(new int[]{start, start + size - 1});
            start += size;
        }

        // ----------------------------------------------------------------
        // Thuật toán 3: Greedy Selection - chọn cán bộ giám sát cho mỗi khối
        //   Duyệt qua danh sách còn lại, chọn người đầu tiên thỏa coTheGiamSatKhoi()
        // ----------------------------------------------------------------
        Set<String> assignedGiamSat = new HashSet<>();
        int giamsatStt = 1;

        for (int k = 0; k < blocks.size(); k++) {
            int[] block = blocks.get(k);

            CanBo selected = null;
            for (CanBo cb : remainingCanBo) {
                if (coTheGiamSatKhoi(cb, assignedGiamSat)) {
                    selected = cb;
                    break;
                }
            }

            if (selected == null) break;

            assignedGiamSat.add(selected.getMaCanBo());
            remainingCanBo.remove(selected);

            String phongDau  = allPhongThi.get(block[0]).getMaPhong();
            String phongCuoi = allPhongThi.get(block[1]).getMaPhong();
            String range = phongDau.equals(phongCuoi)
                ? phongDau
                : "Từ " + phongDau + " đến " + phongCuoi;

            giamsatList.add(new GiamsatHanhlang(
                giamsatStt++, selected.getMaCanBo(), selected.getHoVaTen(), range
            ));
        }
    }

    /**
     * Kiểm tra ràng buộc: cán bộ có thể giám sát hành lang cho khối không.
     * Điều kiện: chưa được phân công làm giám sát trong lần này.
     */
    private boolean coTheGiamSatKhoi(CanBo canBo, Set<String> assignedGiamSat) {
        return !assignedGiamSat.contains(canBo.getMaCanBo());
    }

    public List<PhancongGiamthi> getPhancongList() {
        return phancongList;
    }

    public List<GiamsatHanhlang> getGiamsatList() {
        return giamsatList;
    }
}

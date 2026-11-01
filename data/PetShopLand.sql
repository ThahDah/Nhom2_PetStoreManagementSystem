-- =====================================================================
--  CSDL PetShop LAND (SQL Server) - phần phục vụ chức năng Lịch hẹn
-- =====================================================================
USE master;
GO
IF DB_ID('PetShopLand') IS NOT NULL
BEGIN
    ALTER DATABASE PetShopLand SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE PetShopLand;
END
GO
CREATE DATABASE PetShopLand;
GO
USE PetShopLand;
GO

CREATE TABLE KhachHang (
    maKH         VARCHAR(10)   NOT NULL PRIMARY KEY,
    tenKH        NVARCHAR(100) NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL UNIQUE,
    email        VARCHAR(100)  NULL,
    diemTichLuy  INT           NOT NULL DEFAULT 0,
    CONSTRAINT CK_KhachHang_SDT  CHECK (LEN(soDienThoai) = 10 AND soDienThoai NOT LIKE '%[^0-9]%'),
    CONSTRAINT CK_KhachHang_Diem CHECK (diemTichLuy >= 0)
);

CREATE TABLE NhanVien (
    maNV         VARCHAR(9)    NOT NULL PRIMARY KEY,
    tenNV        NVARCHAR(100) NOT NULL,
    soDienThoai  VARCHAR(10)   NOT NULL UNIQUE,
    email        VARCHAR(100)  NULL,
    trangThaiNV  VARCHAR(20)   NOT NULL DEFAULT 'CON_LAM',   -- tên hằng enum TrangThai
    hinhAnh      VARCHAR(255)  NULL
);

CREATE TABLE DichVu (
    maDV          VARCHAR(10)   NOT NULL PRIMARY KEY,
    tenDV         NVARCHAR(100) NOT NULL,
    giaTieuChuan  DECIMAL(18,0) NOT NULL,
    CONSTRAINT CK_DichVu_Gia CHECK (giaTieuChuan > 0)
);

CREATE TABLE ThuCung (
    maThuCung         VARCHAR(11)   NOT NULL PRIMARY KEY,
    tenThuCung        NVARCHAR(50)  NOT NULL,
    loai              VARCHAR(10)   NOT NULL,                -- enum Loai
    canNang           FLOAT         NOT NULL,
    ghiChu            NVARCHAR(255) NULL,
    trangThaiThuCung  VARCHAR(20)   NOT NULL DEFAULT 'BINH_THUONG',  -- enum TrangThai
    doHungDu          VARCHAR(10)   NOT NULL DEFAULT 'HIEN', -- enum DoHungDu
    maKH              VARCHAR(10)   NOT NULL,
    CONSTRAINT FK_ThuCung_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT CK_ThuCung_Loai     CHECK (loai IN ('CHO', 'MEO')),
    CONSTRAINT CK_ThuCung_CanNang  CHECK (canNang > 0),
    CONSTRAINT CK_ThuCung_HungDu   CHECK (doHungDu IN ('HIEN', 'NHAT', 'DU'))
);

CREATE TABLE LichHen (
    maLH          VARCHAR(11)   NOT NULL PRIMARY KEY,        -- LH + ddMMyy + 3 chữ số
    thoiGianHen   DATE          NOT NULL,
    ghiChu        NVARCHAR(255) NULL,
    trangThaiHen  VARCHAR(20)   NOT NULL DEFAULT 'CHO_DEN_HEN', -- enum TrangThai
    CONSTRAINT CK_LichHen_TrangThai CHECK (trangThaiHen IN ('CHO_DEN_HEN', 'DA_TIEP_NHAN', 'DA_HUY'))
);

CREATE TABLE ChiTietLichHen (
    maLH       VARCHAR(11)   NOT NULL,
    maThuCung  VARCHAR(11)   NOT NULL,
    maDV       VARCHAR(10)   NOT NULL,
    donGia     DECIMAL(18,0) NOT NULL,
    CONSTRAINT PK_ChiTietLichHen PRIMARY KEY (maLH, maThuCung, maDV),
    CONSTRAINT FK_CTLH_LichHen  FOREIGN KEY (maLH)      REFERENCES LichHen(maLH),
    CONSTRAINT FK_CTLH_ThuCung  FOREIGN KEY (maThuCung) REFERENCES ThuCung(maThuCung),
    CONSTRAINT FK_CTLH_DichVu   FOREIGN KEY (maDV)      REFERENCES DichVu(maDV),
    CONSTRAINT CK_CTLH_DonGia   CHECK (donGia > 0)
);
GO

-- ------------------------------- DỮ LIỆU MẪU -------------------------------
-- =====================================================================
--  DỮ LIỆU TEST - PetShop LAND (SQL Server)
--  Chạy SAU khi đã tạo bảng bằng PetShopLand.sql. Script xóa dữ liệu cũ rồi nạp lại.
--
--  Quy ước:
--   * Ngày hẹn tính theo "hôm nay" (GETDATE) nên luôn có lịch quá khứ và tương lai,
--     chạy vào ngày nào cũng dùng được.
--   * Đơn giá chi tiết lịch hẹn = giá dự kiến theo công thức:
--       phụ phí (cân nặng + hung dữ) chỉ tính MỘT LẦN cho mỗi thú cưng trong một lịch hẹn,
--       ở dòng dịch vụ đầu tiên (theo mã dịch vụ); các dòng sau chỉ tính giá tiêu chuẩn.
--
--  Gợi ý các ca để test trên giao diện (N = số ngày so với hôm nay):
--   1. Trùng lịch  : Rex (SĐT 0977333444) + Spa trọn gói + ngày hôm nay +1
--                    Lu  (SĐT 0987654321) + Tắm gội      + ngày hôm nay +2
--                    Milu / Mun (SĐT 0912345678) + Cạo vôi răng + ngày hôm nay +2
--   2. Được đặt    : Rex + Spa trọn gói + ngày hôm nay +4 (lịch cũ đã hủy nên không tính trùng)
--   3. Cùng thú cưng dùng nhiều dịch vụ : Rex + Tắm gội + Cắt tỉa lông (không trùng) 
--   4. Khách chưa có thú cưng : SĐT 0944888999
--   5. Không có khách         : SĐT 0900000000
--   6. Ghi chú dài            : mở chi tiết lịch LH311026003
--   7. Lịch có nhiều thú cưng : LH141026001, LH261026002, LH281026001
--   8. Lịch có ghi chú NULL   : LH151026001, LH171026001 ...
-- =====================================================================
USE PetShopLand;
GO

DELETE FROM ChiTietLichHen;
DELETE FROM LichHen;
DELETE FROM ThuCung;
DELETE FROM DichVu;
DELETE FROM NhanVien;
DELETE FROM KhachHang;
GO

-- ===================== KHÁCH HÀNG (10) =====================
INSERT INTO KhachHang (maKH, tenKH, soDienThoai, email, diemTichLuy) VALUES
 ('KH0001', N'Trần Văn An', '0912345678', 'an@gmail.com', 10),
 ('KH0002', N'Lê Thị Bình', '0987654321', 'binh@gmail.com', 0),
 ('KH0003', N'Phạm Minh Châu', '0905111222', 'chau@gmail.com', 25),
 ('KH0004', N'Nguyễn Quốc Dũng', '0938222333', 'dung@gmail.com', 5),
 ('KH0005', N'Hoàng Thị Em', '0977333444', 'em@gmail.com', 40),
 ('KH0006', N'Vũ Thanh Phong', '0966444555', 'phong@gmail.com', 0),
 ('KH0007', N'Đặng Mỹ Hạnh', '0911555666', 'hanh@gmail.com', 15),
 ('KH0008', N'Bùi Anh Khoa', '0922666777', 'khoa@gmail.com', 8),
 ('KH0009', N'Ngô Thị Lan', '0933777888', 'lan@gmail.com', 60),
 ('KH0010', N'Đỗ Hữu Nghĩa', '0944888999', 'nghia@gmail.com', 0);

-- ===================== NHÂN VIÊN (10) =====================
INSERT INTO NhanVien (maNV, tenNV, soDienThoai, email, trangThaiNV, hinhAnh) VALUES
 ('NV2601001', N'Nguyễn Thị Lan', '0901234567', 'lan@petland.vn', 'CON_LAM', NULL),
 ('NV2601002', N'Trần Minh Tuấn', '0901234568', 'tuan@petland.vn', 'CON_LAM', NULL),
 ('NV2601003', N'Lê Hoài Thu', '0901234569', 'thu@petland.vn', 'CON_LAM', NULL),
 ('NV2601004', N'Phạm Gia Bảo', '0901234570', 'bao@petland.vn', 'CON_LAM', NULL),
 ('NV2601005', N'Võ Ngọc Anh', '0901234571', 'anh@petland.vn', 'CON_LAM', NULL),
 ('NV2601006', N'Đinh Quang Huy', '0901234572', 'huy@petland.vn', 'CON_LAM', NULL),
 ('NV2601007', N'Cao Thị Mai', '0901234573', 'mai@petland.vn', 'CON_LAM', NULL),
 ('NV2601008', N'Lý Văn Sơn', '0901234574', 'son@petland.vn', 'DA_NGHI', NULL),
 ('NV2601009', N'Trương Bích Hà', '0901234575', 'ha@petland.vn', 'CON_LAM', NULL),
 ('NV2601010', N'Mai Xuân Khánh', '0901234576', 'khanh@petland.vn', 'DA_NGHI', NULL);

-- ===================== DỊCH VỤ (10) =====================
INSERT INTO DichVu (maDV, tenDV, giaTieuChuan) VALUES
 ('DV001', N'Tắm gội', 120000),
 ('DV002', N'Cắt tỉa lông', 180000),
 ('DV003', N'Spa trọn gói', 350000),
 ('DV004', N'Cạo vôi răng', 150000),
 ('DV005', N'Vệ sinh tai', 60000),
 ('DV006', N'Cắt móng', 50000),
 ('DV007', N'Nhuộm lông', 250000),
 ('DV008', N'Tiêm phòng', 200000),
 ('DV009', N'Khám tổng quát', 100000),
 ('DV010', N'Gửi thú cưng theo ngày', 150000);

-- ===================== THÚ CUNG (14) =====================
-- KH0010 cố ý không có thú cưng
INSERT INTO ThuCung (maThuCung, tenThuCung, loai, canNang, ghiChu, trangThaiThuCung, doHungDu, maKH) VALUES
 ('TCC10012601', N'Milu', 'CHO', 12.5, N'', 'BINH_THUONG', 'HIEN', 'KH0001'),
 ('TCM10012601', N'Mun', 'MEO', 4.5, N'Sợ tiếng ồn', 'BINH_THUONG', 'NHAT', 'KH0001'),
 ('TCC20012601', N'Lu', 'CHO', 22.0, N'Hay sủa người lạ', 'BINH_THUONG', 'DU', 'KH0002'),
 ('TCM05022601', N'Kitty', 'MEO', 3.8, N'', 'BINH_THUONG', 'HIEN', 'KH0003'),
 ('TCC05022602', N'Bông', 'CHO', 7.5, N'', 'BINH_THUONG', 'HIEN', 'KH0003'),
 ('TCC12022601', N'Bống', 'CHO', 8.2, N'Dị ứng dầu gội hương hoa', 'BINH_THUONG', 'NHAT', 'KH0004'),
 ('TCC18022601', N'Rex', 'CHO', 30.0, N'', 'DANG_CHAM_SOC', 'DU', 'KH0005'),
 ('TCM18022602', N'Luna', 'MEO', 6.5, N'', 'BINH_THUONG', 'NHAT', 'KH0005'),
 ('TCM01032601', N'Tom', 'MEO', 5.1, N'', 'BINH_THUONG', 'HIEN', 'KH0006'),
 ('TCC08032601', N'Nâu', 'CHO', 15.0, N'', 'BINH_THUONG', 'HIEN', 'KH0007'),
 ('TCM08032602', N'Mướp', 'MEO', 9.0, N'Hay cào người', 'BINH_THUONG', 'DU', 'KH0007'),
 ('TCM15032601', N'Cam', 'MEO', 4.2, N'Cắn khi bị chạm tai', 'BINH_THUONG', 'NHAT', 'KH0008'),
 ('TCC22032601', N'Bơ', 'CHO', 6.5, N'', 'BINH_THUONG', 'HIEN', 'KH0009'),
 ('TCC22032602', N'Gấu', 'CHO', 18.0, N'', 'BINH_THUONG', 'NHAT', 'KH0009');

-- ===================== LỊCH HẸN (30) =====================
-- Cột cuối là số ngày so với hôm nay của ngày hẹn
INSERT INTO LichHen (maLH, thoiGianHen, ghiChu, trangThaiHen) VALUES
 ('LH141026001', DATEADD(DAY, -14, CAST(GETDATE() AS DATE)), N'Khách quen', 'DA_TIEP_NHAN'),   -- -14 ngày
 ('LH151026001', DATEADD(DAY, -12, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),   -- -12 ngày
 ('LH161026001', DATEADD(DAY, -11, CAST(GETDATE() AS DATE)), N'Mang theo sổ tiêm phòng', 'DA_TIEP_NHAN'),   -- -11 ngày
 ('LH171026001', DATEADD(DAY, -9, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),   -- -9 ngày
 ('LH181026001', DATEADD(DAY, -8, CAST(GETDATE() AS DATE)), N'Khách đến sớm 15 phút', 'DA_TIEP_NHAN'),   -- -8 ngày
 ('LH191026001', DATEADD(DAY, -7, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),   -- -7 ngày
 ('LH201026001', DATEADD(DAY, -6, CAST(GETDATE() AS DATE)), N'Làm trọn gói chăm sóc', 'DA_TIEP_NHAN'),   -- -6 ngày
 ('LH211026001', DATEADD(DAY, -4, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),   -- -4 ngày
 ('LH221026001', DATEADD(DAY, -3, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),   -- -3 ngày
 ('LH231026001', DATEADD(DAY, -2, CAST(GETDATE() AS DATE)), N'Nhắc khách trước 1 ngày', 'DA_TIEP_NHAN'),   -- -2 ngày
 ('LH241026001', DATEADD(DAY, -1, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),   -- -1 ngày
 ('LH161026002', DATEADD(DAY, -10, CAST(GETDATE() AS DATE)), N'Khách báo bận', 'DA_HUY'),   -- -10 ngày
 ('LH201026002', DATEADD(DAY, -5, CAST(GETDATE() AS DATE)), N'Thú cưng bị ốm', 'DA_HUY'),   -- -5 ngày
 ('LH251026001', DATEADD(DAY, 4, CAST(GETDATE() AS DATE)), N'Khách đổi sang ngày khác', 'DA_HUY'),   -- +4 ngày
 ('LH251026002', DATEADD(DAY, 1, CAST(GETDATE() AS DATE)), N'Khách đến sớm 15 phút', 'CHO_DEN_HEN'),   -- +1 ngày
 ('LH261026001', DATEADD(DAY, 1, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +1 ngày
 ('LH261026002', DATEADD(DAY, 2, CAST(GETDATE() AS DATE)), N'Hai bé đi cùng nhau', 'CHO_DEN_HEN'),   -- +2 ngày
 ('LH271026001', DATEADD(DAY, 2, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +2 ngày
 ('LH271026002', DATEADD(DAY, 3, CAST(GETDATE() AS DATE)), N'Mang theo sổ tiêm phòng', 'CHO_DEN_HEN'),   -- +3 ngày
 ('LH281026001', DATEADD(DAY, 3, CAST(GETDATE() AS DATE)), N'Hai bé đi cùng nhau', 'CHO_DEN_HEN'),   -- +3 ngày
 ('LH281026002', DATEADD(DAY, 5, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +5 ngày
 ('LH291026001', DATEADD(DAY, 6, CAST(GETDATE() AS DATE)), N'Nhắc khách trước 1 ngày', 'CHO_DEN_HEN'),   -- +6 ngày
 ('LH291026002', DATEADD(DAY, 7, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +7 ngày
 ('LH301026001', DATEADD(DAY, 8, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +8 ngày
 ('LH301026002', DATEADD(DAY, 10, CAST(GETDATE() AS DATE)), N'Tiêm phòng và khám tổng quát cùng lượt', 'CHO_DEN_HEN'),   -- +10 ngày
 ('LH311026001', DATEADD(DAY, 12, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +12 ngày
 ('LH311026002', DATEADD(DAY, 14, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +14 ngày
 ('LH311026003', DATEADD(DAY, 18, CAST(GETDATE() AS DATE)), N'Khách yêu cầu nhân viên nữ phụ trách. Bé Rex rất sợ tiếng máy sấy nên cần sấy nhẹ và nói chuyện nhẹ nhàng trong suốt quá trình. Khách sẽ đưa bé đến lúc 8 giờ sáng và đón vào cuối giờ chiều, nhờ cửa hàng chụp ảnh gửi qua Zalo sau khi hoàn thành.', 'CHO_DEN_HEN'),   -- +18 ngày
 ('LH311026004', DATEADD(DAY, 21, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),   -- +21 ngày
 ('LH301026003', DATEADD(DAY, 30, CAST(GETDATE() AS DATE)), N'Đặt trước một tháng', 'CHO_DEN_HEN');   -- +30 ngày

-- ===================== CHI TIẾT LỊCH HẸN (44 dòng) =====================
-- Bước 1: nạp mỗi dòng với đơn giá = giá tiêu chuẩn của dịch vụ
INSERT INTO ChiTietLichHen (maLH, maThuCung, maDV, donGia)
SELECT v.maLH, v.maThuCung, v.maDV, d.giaTieuChuan
FROM (VALUES
  ('LH141026001', 'TCC10012601', 'DV001'),
  ('LH141026001', 'TCM10012601', 'DV001'),
  ('LH151026001', 'TCC20012601', 'DV003'),
  ('LH161026001', 'TCM05022601', 'DV001'),
  ('LH161026001', 'TCM05022601', 'DV006'),
  ('LH171026001', 'TCC12022601', 'DV002'),
  ('LH181026001', 'TCC18022601', 'DV003'),
  ('LH191026001', 'TCM01032601', 'DV005'),
  ('LH191026001', 'TCM01032601', 'DV006'),
  ('LH201026001', 'TCC08032601', 'DV001'),
  ('LH201026001', 'TCC08032601', 'DV002'),
  ('LH201026001', 'TCC08032601', 'DV004'),
  ('LH211026001', 'TCC22032601', 'DV009'),
  ('LH221026001', 'TCM15032601', 'DV001'),
  ('LH231026001', 'TCC05022602', 'DV001'),
  ('LH231026001', 'TCC05022602', 'DV006'),
  ('LH241026001', 'TCM18022602', 'DV002'),
  ('LH161026002', 'TCM08032602', 'DV007'),
  ('LH201026002', 'TCC22032602', 'DV001'),
  ('LH251026001', 'TCC18022601', 'DV003'),
  ('LH251026002', 'TCC18022601', 'DV003'),
  ('LH261026001', 'TCC10012601', 'DV002'),
  ('LH261026002', 'TCM10012601', 'DV004'),
  ('LH261026002', 'TCC10012601', 'DV004'),
  ('LH271026001', 'TCC20012601', 'DV001'),
  ('LH271026001', 'TCC20012601', 'DV002'),
  ('LH271026002', 'TCM05022601', 'DV003'),
  ('LH281026001', 'TCC08032601', 'DV003'),
  ('LH281026001', 'TCM08032602', 'DV003'),
  ('LH281026002', 'TCC22032602', 'DV001'),
  ('LH291026001', 'TCC12022601', 'DV001'),
  ('LH291026001', 'TCC12022601', 'DV005'),
  ('LH291026001', 'TCC12022601', 'DV006'),
  ('LH291026002', 'TCM01032601', 'DV002'),
  ('LH301026001', 'TCM15032601', 'DV003'),
  ('LH301026002', 'TCC22032601', 'DV008'),
  ('LH301026002', 'TCC22032601', 'DV009'),
  ('LH311026001', 'TCC05022602', 'DV002'),
  ('LH311026002', 'TCM18022602', 'DV001'),
  ('LH311026002', 'TCM18022602', 'DV005'),
  ('LH311026003', 'TCC18022601', 'DV001'),
  ('LH311026003', 'TCC18022601', 'DV002'),
  ('LH311026004', 'TCC20012601', 'DV003'),
  ('LH301026003', 'TCM08032602', 'DV002')
) AS v (maLH, maThuCung, maDV)
JOIN DichVu d ON d.maDV = v.maDV;
GO

-- Bước 2: cộng phụ phí vào dòng đầu tiên của mỗi thú cưng trong từng lịch hẹn
--   (phụ phí cân nặng theo bảng 1.3 / 1.4 + phụ phí hung dữ theo bảng 1.2)
UPDATE ct
SET ct.donGia = x.donGiaMoi
FROM ChiTietLichHen ct
JOIN (
    SELECT c.maLH, c.maThuCung, c.maDV,
           d.giaTieuChuan
           + CASE WHEN ROW_NUMBER() OVER (PARTITION BY c.maLH, c.maThuCung ORDER BY c.maDV) = 1
                  THEN
                      CASE
                          WHEN t.loai = 'CHO' THEN
                              CASE
                                  WHEN t.canNang <= 10 THEN 0
                                  WHEN t.canNang <= 15 THEN 30000
                                  WHEN t.canNang <= 20 THEN 60000
                                  ELSE 90000
                              END
                          ELSE
                              CASE
                                  WHEN t.canNang <= 4 THEN 0
                                  WHEN t.canNang <= 6 THEN 20000
                                  WHEN t.canNang <= 8 THEN 40000
                                  ELSE 60000
                              END
                      END
                      + CASE t.doHungDu
                            WHEN 'HIEN' THEN 0
                            WHEN 'NHAT' THEN 20000
                            ELSE 50000
                        END
                  ELSE 0
             END AS donGiaMoi
    FROM ChiTietLichHen c
    JOIN ThuCung t ON c.maThuCung = t.maThuCung
    JOIN DichVu d ON c.maDV = d.maDV
) x ON ct.maLH = x.maLH AND ct.maThuCung = x.maThuCung AND ct.maDV = x.maDV;
GO

-- ===================== KIỂM TRA NHANH =====================
SELECT 'KhachHang' AS bang, COUNT(*) AS soDong FROM KhachHang
UNION ALL SELECT 'NhanVien',       COUNT(*) FROM NhanVien
UNION ALL SELECT 'DichVu',         COUNT(*) FROM DichVu
UNION ALL SELECT 'ThuCung',        COUNT(*) FROM ThuCung
UNION ALL SELECT 'LichHen',        COUNT(*) FROM LichHen
UNION ALL SELECT 'ChiTietLichHen', COUNT(*) FROM ChiTietLichHen;

-- Danh sách lịch hẹn kèm tổng tiền (đối chiếu với màn hình)
SELECT lh.maLH, lh.thoiGianHen, lh.trangThaiHen, COUNT(*) AS soDong, SUM(ct.donGia) AS tongTien
FROM LichHen lh
JOIN ChiTietLichHen ct ON ct.maLH = lh.maLH
GROUP BY lh.maLH, lh.thoiGianHen, lh.trangThaiHen
ORDER BY lh.thoiGianHen DESC, lh.maLH DESC;
GO
USE PetShopLand;
GO

-- ===================== HÓA ĐƠN =====================
CREATE TABLE HoaDon (
    maHD VARCHAR(11) NOT NULL PRIMARY KEY,
    ngayLap DATE NOT NULL,
    donGia DECIMAL(18,0) NOT NULL,
    trangThaiHD VARCHAR(20) NOT NULL,
    phuongThucTT NVARCHAR(30) NULL,
    maKH VARCHAR(10) NOT NULL,

    CONSTRAINT FK_HoaDon_KhachHang
        FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),

    CONSTRAINT CK_HoaDon_DonGia
        CHECK (donGia >= 0)
);

-- ===================== CHI TIẾT HÓA ĐƠN =====================
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(11) NOT NULL,
    maSP VARCHAR(10) NOT NULL,
    maDV VARCHAR(10) NOT NULL,
    soLuong INT NOT NULL,

    CONSTRAINT PK_ChiTietHoaDon
        PRIMARY KEY (maHD, maSP, maDV),

    CONSTRAINT FK_ChiTietHoaDon_HoaDon
        FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),

    CONSTRAINT FK_ChiTietHoaDon_DichVu
        FOREIGN KEY (maDV) REFERENCES DichVu(maDV),

    CONSTRAINT CK_ChiTietHoaDon_SoLuong
        CHECK (soLuong > 0)
);
GO
INSERT INTO HoaDon
(maHD, ngayLap, donGia, trangThaiHD, phuongThucTT, maKH)
VALUES
('HD001', '2026-10-15', 120000, 'DA_THANH_TOAN', N'Tiền mặt', 'KH0001'),
('HD002', '2026-10-16', 350000, 'DA_THANH_TOAN', N'Chuyển khoản', 'KH0002'),
('HD003', '2026-10-17', 180000, 'DA_THANH_TOAN', N'Tiền mặt', 'KH0003'),
('HD004', '2026-10-18', 250000, 'DA_THANH_TOAN', N'Chuyển khoản', 'KH0004'),
('HD005', '2026-10-19', 450000, 'DA_THANH_TOAN', N'Tiền mặt', 'KH0005'),

('HD006', '2026-10-20', 150000, 'DA_THANH_TOAN', N'Chuyển khoản', 'KH0006'),
('HD007', '2026-10-21', 200000, 'DA_THANH_TOAN', N'Tiền mặt', 'KH0007'),
('HD008', '2026-10-22', 300000, 'DA_THANH_TOAN', N'Chuyển khoản', 'KH0008'),
('HD009', '2026-10-23', 100000, 'DA_THANH_TOAN', N'Tiền mặt', 'KH0009'),
('HD010', '2026-10-24', 500000, 'DA_THANH_TOAN', N'Chuyển khoản', 'KH0010'),

('HD011', '2026-10-25', 180000, 'CHUA_THANH_TOAN', NULL, 'KH0001'),
('HD012', '2026-10-26', 350000, 'CHUA_THANH_TOAN', NULL, 'KH0002'),
('HD013', '2026-10-27', 250000, 'CHUA_THANH_TOAN', NULL, 'KH0003'),
('HD014', '2026-10-28', 400000, 'DA_THANH_TOAN', NULL, 'KH0004'),
('HD015', '2026-11-01', 150000, 'CHUA_THANH_TOAN', NULL, 'KH0005');
GO
INSERT INTO ChiTietHoaDon
(maHD, maSP, maDV, soLuong)
VALUES
('HD001', 'SP001', 'DV001', 1),
('HD002', 'SP002', 'DV003', 1),
('HD003', 'SP003', 'DV002', 1),
('HD004', 'SP004', 'DV007', 1),
('HD005', 'SP005', 'DV003', 2),

('HD006', 'SP006', 'DV004', 1),
('HD007', 'SP007', 'DV008', 1),
('HD008', 'SP008', 'DV001', 2),
('HD009', 'SP009', 'DV009', 1),
('HD010', 'SP010', 'DV010', 2),

('HD011', 'SP001', 'DV002', 1),
('HD012', 'SP002', 'DV003', 1),
('HD013', 'SP003', 'DV007', 1),
('HD014', 'SP004', 'DV008', 2),
('HD015', 'SP005', 'DV004', 1);
GO
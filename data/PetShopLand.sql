-- =====================================================================

-- CSDL PetShop LAND (SQL Server) - phần phục vụ chức năng Lịch hẹn

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

    maKH VARCHAR(10) NOT NULL PRIMARY KEY,

    tenKH NVARCHAR(100) NOT NULL,

    soDienThoai VARCHAR(10) NOT NULL UNIQUE,

    email VARCHAR(100) NULL,

    diemTichLuy INT NOT NULL DEFAULT 0,

    CONSTRAINT CK_KhachHang_SDT CHECK (LEN(soDienThoai) = 10 AND soDienThoai NOT LIKE '%[^0-9]%'),

    CONSTRAINT CK_KhachHang_Diem CHECK (diemTichLuy >= 0)

);

CREATE TABLE NhanVien (

    maNV VARCHAR(9) NOT NULL PRIMARY KEY,

    tenNV NVARCHAR(100) NOT NULL,

    soDienThoai VARCHAR(10) NOT NULL UNIQUE,

    email VARCHAR(100) NULL,

    trangThaiNV VARCHAR(20) NOT NULL DEFAULT 'CON_LAM',

    hinhAnh VARCHAR(255) NULL,

    CONSTRAINT CHK_NhanVien_maNV CHECK (maNV LIKE 'NV[0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),

    CONSTRAINT CHK_NhanVien_soDienThoai CHECK (soDienThoai LIKE '0[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),

    CONSTRAINT CHK_NhanVien_email CHECK (email LIKE '%_@__%.__%'),

    CONSTRAINT CHK_NhanVien_trangThaiNV CHECK (trangThaiNV IN ('CON_LAM', 'DA_NGHI'))

);

CREATE TABLE DichVu (

    maDV VARCHAR(10) NOT NULL PRIMARY KEY,

    tenDV NVARCHAR(100) NOT NULL,

    giaTieuChuan DECIMAL(18,0) NOT NULL,

    CONSTRAINT CK_DichVu_Gia CHECK (giaTieuChuan > 0)

);

CREATE TABLE ThuCung (

    maThuCung VARCHAR(11) NOT NULL PRIMARY KEY,

    tenThuCung NVARCHAR(50) NOT NULL,

    loai VARCHAR(10) NOT NULL, -- enum Loai

    canNang FLOAT NOT NULL,

    ghiChu NVARCHAR(255) NULL,

    trangThaiThuCung VARCHAR(20) NOT NULL DEFAULT 'BINH_THUONG',

    doHungDu VARCHAR(10) NOT NULL DEFAULT 'HIEN',

    maKH VARCHAR(10) NOT NULL,

    CONSTRAINT FK_ThuCung_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),

    CONSTRAINT CK_ThuCung_Loai CHECK (loai IN ('CHO', 'MEO')),

    CONSTRAINT CK_ThuCung_CanNang CHECK (canNang > 0),

    CONSTRAINT CK_ThuCung_HungDu CHECK (doHungDu IN ('HIEN', 'NHAT', 'DU'))

);

CREATE TABLE LichHen (

    maLH VARCHAR(11) NOT NULL PRIMARY KEY,

    thoiGianHen DATE NOT NULL,

    ghiChu NVARCHAR(255) NULL,

    trangThaiHen VARCHAR(20) NOT NULL DEFAULT 'CHO_DEN_HEN',

    CONSTRAINT CK_LichHen_TrangThai CHECK (trangThaiHen IN ('CHO_DEN_HEN', 'DA_TIEP_NHAN', 'DA_HUY'))

);

CREATE TABLE ChiTietLichHen (

    maLH VARCHAR(11) NOT NULL,

    maThuCung VARCHAR(11) NOT NULL,

    maDV VARCHAR(10) NOT NULL,

    donGia DECIMAL(18,0) NOT NULL,

    CONSTRAINT PK_ChiTietLichHen PRIMARY KEY (maLH, maThuCung, maDV),

    CONSTRAINT FK_CTLH_LichHen FOREIGN KEY (maLH) REFERENCES LichHen(maLH),

    CONSTRAINT FK_CTLH_ThuCung FOREIGN KEY (maThuCung) REFERENCES ThuCung(maThuCung),

    CONSTRAINT FK_CTLH_DichVu FOREIGN KEY (maDV) REFERENCES DichVu(maDV),

    CONSTRAINT CK_CTLH_DonGia CHECK (donGia > 0)

);

CREATE TABLE TaiKhoan (

    tenTaiKhoan VARCHAR(50) NOT NULL PRIMARY KEY,

    matKhau VARCHAR(255) NOT NULL,

    vaiTroTK VARCHAR(20) NOT NULL,

    trangThaiTK VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG',

    maNV VARCHAR(9) NOT NULL UNIQUE,

    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),

    CONSTRAINT CK_TaiKhoan_TrangThai CHECK (trangThaiTK IN ('HOAT_DONG', 'KHOA'))

);

GO

-- ------------------------------- DỮ LIỆU MẪU -------------------------------

-- =====================================================================

-- DỮ LIỆU TEST - PetShop LAND (SQL Server)

-- Chạy SAU khi đã tạo bảng. Script xóa dữ liệu cũ rồi nạp lại.

--

-- Quy ước:

-- * Ngày hẹn tính theo "hôm nay" (GETDATE).

-- * Phụ phí (cân nặng + hung dữ) chỉ tính MỘT LẦN cho mỗi

--   thú cưng trong một lịch hẹn, ở dòng dịch vụ đầu tiên.

--

-- Gợi ý các ca để test trên giao diện:

-- 1. Trùng lịch: Rex + Spa + hôm nay +1

--    Lu + Tắm gội + hôm nay +2

--    Milu / Mun + Cạo vôi răng + hôm nay +2

-- 2. Được đặt: Rex + Spa + hôm nay +4 (lịch cũ đã hủy)

-- 3. Cùng thú cưng dùng nhiều dịch vụ: Rex + Tắm gội + Cắt tỉa

-- 4. Khách chưa có thú cưng: SĐT 0944888999

-- 5. Không có khách: SĐT 0900000000

-- 6. Ghi chú dài: LH311026003

-- 7. Lịch nhiều thú cưng: LH141026001, LH261026002, LH281026001

-- 8. Lịch ghi chú NULL: LH151026001, LH171026001

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

-- ===================== THÚ CƯNG (14) =====================

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

('LH141026001', DATEADD(DAY, -14, CAST(GETDATE() AS DATE)), N'Khách quen', 'DA_TIEP_NHAN'),

('LH151026001', DATEADD(DAY, -12, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),

('LH161026001', DATEADD(DAY, -11, CAST(GETDATE() AS DATE)), N'Mang theo sổ tiêm phòng', 'DA_TIEP_NHAN'),

('LH171026001', DATEADD(DAY, -9, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),

('LH181026001', DATEADD(DAY, -8, CAST(GETDATE() AS DATE)), N'Khách đến sớm 15 phút', 'DA_TIEP_NHAN'),

('LH191026001', DATEADD(DAY, -7, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),

('LH201026001', DATEADD(DAY, -6, CAST(GETDATE() AS DATE)), N'Làm trọn gói chăm sóc', 'DA_TIEP_NHAN'),

('LH211026001', DATEADD(DAY, -4, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),

('LH221026001', DATEADD(DAY, -3, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),

('LH231026001', DATEADD(DAY, -2, CAST(GETDATE() AS DATE)), N'Nhắc khách trước 1 ngày', 'DA_TIEP_NHAN'),

('LH241026001', DATEADD(DAY, -1, CAST(GETDATE() AS DATE)), NULL, 'DA_TIEP_NHAN'),

('LH161026002', DATEADD(DAY, -10, CAST(GETDATE() AS DATE)), N'Khách báo bận', 'DA_HUY'),

('LH201026002', DATEADD(DAY, -5, CAST(GETDATE() AS DATE)), N'Thú cưng bị ốm', 'DA_HUY'),

('LH251026001', DATEADD(DAY, 4, CAST(GETDATE() AS DATE)), N'Khách đổi sang ngày khác', 'DA_HUY'),

('LH251026002', DATEADD(DAY, 1, CAST(GETDATE() AS DATE)), N'Khách đến sớm 15 phút', 'CHO_DEN_HEN'),

('LH261026001', DATEADD(DAY, 1, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH261026002', DATEADD(DAY, 2, CAST(GETDATE() AS DATE)), N'Hai bé đi cùng nhau', 'CHO_DEN_HEN'),

('LH271026001', DATEADD(DAY, 2, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH271026002', DATEADD(DAY, 3, CAST(GETDATE() AS DATE)), N'Mang theo sổ tiêm phòng', 'CHO_DEN_HEN'),

('LH281026001', DATEADD(DAY, 3, CAST(GETDATE() AS DATE)), N'Hai bé đi cùng nhau', 'CHO_DEN_HEN'),

('LH281026002', DATEADD(DAY, 5, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH291026001', DATEADD(DAY, 6, CAST(GETDATE() AS DATE)), N'Nhắc khách trước 1 ngày', 'CHO_DEN_HEN'),

('LH291026002', DATEADD(DAY, 7, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH301026001', DATEADD(DAY, 8, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH301026002', DATEADD(DAY, 10, CAST(GETDATE() AS DATE)), N'Tiêm phòng và khám tổng quát cùng lượt', 'CHO_DEN_HEN'),

('LH311026001', DATEADD(DAY, 12, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH311026002', DATEADD(DAY, 14, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH311026003', DATEADD(DAY, 18, CAST(GETDATE() AS DATE)), N'Khách yêu cầu nhân viên nữ phụ trách. Bé Rex rất sợ tiếng máy sấy nên cần sấy nhẹ và nói chuyện nhẹ nhàng trong suốt quá trình. Khách sẽ đưa bé đến lúc 8 giờ sáng và đón vào cuối giờ chiều, nhờ cửa hàng chụp ảnh gửi qua Zalo sau khi hoàn thành.', 'CHO_DEN_HEN'),

('LH311026004', DATEADD(DAY, 21, CAST(GETDATE() AS DATE)), NULL, 'CHO_DEN_HEN'),

('LH301026003', DATEADD(DAY, 30, CAST(GETDATE() AS DATE)), N'Đặt trước một tháng', 'CHO_DEN_HEN');

-- ===================== CHI TIẾT LỊCH HẸN (44 dòng) =====================

-- Bước 1: nạp mỗi dòng với đơn giá = giá tiêu chuẩn

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

-- Bước 2: cộng phụ phí vào dòng đầu tiên của mỗi thú cưng

-- (phụ phí cân nặng + phụ phí hung dữ)

UPDATE ct

SET ct.donGia = x.donGiaMoi

FROM ChiTietLichHen ct

JOIN (

    SELECT c.maLH, c.maThuCung, c.maDV,

           d.giaTieuChuan

           + CASE WHEN ROW_NUMBER() OVER (

               PARTITION BY c.maLH, c.maThuCung ORDER BY c.maDV

             ) = 1

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

) x ON ct.maLH = x.maLH

   AND ct.maThuCung = x.maThuCung

   AND ct.maDV = x.maDV;

GO

-- ===================== KIỂM TRA NHANH =====================

SELECT 'KhachHang' AS bang, COUNT(*) AS soDong FROM KhachHang

UNION ALL SELECT 'NhanVien', COUNT(*) FROM NhanVien

UNION ALL SELECT 'DichVu', COUNT(*) FROM DichVu

UNION ALL SELECT 'ThuCung', COUNT(*) FROM ThuCung

UNION ALL SELECT 'LichHen', COUNT(*) FROM LichHen

UNION ALL SELECT 'ChiTietLichHen', COUNT(*) FROM ChiTietLichHen;

-- Danh sách lịch hẹn kèm tổng tiền

SELECT lh.maLH, lh.thoiGianHen, lh.trangThaiHen,

       COUNT(*) AS soDong, SUM(ct.donGia) AS tongTien

FROM LichHen lh

JOIN ChiTietLichHen ct ON ct.maLH = lh.maLH

GROUP BY lh.maLH, lh.thoiGianHen, lh.trangThaiHen

ORDER BY lh.thoiGianHen DESC, lh.maLH DESC;

GO

--Tao table HoaDon

USE PetShopLand;

GO

CREATE TABLE HoaDon (

    maHD VARCHAR(12) NOT NULL PRIMARY KEY,

    ngayLap DATETIME NOT NULL DEFAULT GETDATE(),

    tongTien DECIMAL(18,2) NOT NULL,

    -- THÊM CỘT GIẢM GIÁ (SỐ TIỀN)

    giamGia DECIMAL(18,2) NOT NULL DEFAULT 0,

    trangThaiHD VARCHAR(30) NOT NULL DEFAULT 'CHUA_THANH_TOAN',

    phuongThucTT VARCHAR(30) NULL,

    maKH VARCHAR(10) NOT NULL,

    maNV VARCHAR(9) NOT NULL, -- Khóa ngoại NhanVien

    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),

    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),

    CONSTRAINT CK_HoaDon_TongTien CHECK (tongTien > 0),

    -- GIẢM GIÁ KHÔNG ĐƯỢC ÂM

    CONSTRAINT CK_HoaDon_GiamGia CHECK (giamGia >= 0),

    CONSTRAINT CK_HoaDon_TrangThai CHECK (trangThaiHD IN ('CHUA_THANH_TOAN', 'DA_THANH_TOAN')),

    CONSTRAINT CK_HoaDon_PhuongThuc CHECK (

        phuongThucTT IS NULL OR

        phuongThucTT IN ('TIEN_MAT', 'THE_TIN_DUNG')

    )

);

GO

-- =====================================================================

-- 1. TẠO BẢNG (HoaDon, SanPham, ChiTietHoaDon)

-- =====================================================================

CREATE TABLE SanPham (

    maSP VARCHAR(8) NOT NULL PRIMARY KEY,

    tenSP NVARCHAR(100) NOT NULL,

    giaNhap DECIMAL(18,2) NOT NULL,

    giaBan DECIMAL(18,2) NOT NULL,

    soLuongTon INT NOT NULL,

    donViTinh VARCHAR(10) NOT NULL,

    CONSTRAINT CK_SanPham_Ma CHECK (

        maSP LIKE 'SPCC[0-9][0-9][0-9][0-9]' OR

        maSP LIKE 'SPCM[0-9][0-9][0-9][0-9]'

    ),

    CONSTRAINT CK_SanPham_GiaNhap CHECK (giaNhap > 0),

    CONSTRAINT CK_SanPham_GiaBan CHECK (giaBan >= giaNhap),

    CONSTRAINT CK_SanPham_SoLuongTon CHECK (soLuongTon >= 0),

    CONSTRAINT CK_SanPham_DonViTinh CHECK (

        donViTinh IN ('GOI', 'CHAI', 'CAI', 'TUI', 'HOP')

    )

);

GO

CREATE TABLE ChiTietHoaDon (

    maHD VARCHAR(12) NOT NULL,

    maSP VARCHAR(8) NULL,

    maDV VARCHAR(10) NULL,

    maThuCung VARCHAR(11) NULL, -- NULL đối với hóa đơn bán hàng

    soLuong INT NOT NULL,

    donGia DECIMAL(18,2) NOT NULL,

    thanhTien DECIMAL(18,2) NOT NULL,

    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),

    CONSTRAINT FK_CTHD_SanPham FOREIGN KEY (maSP) REFERENCES SanPham(maSP),

    CONSTRAINT FK_CTHD_DichVu FOREIGN KEY (maDV) REFERENCES DichVu(maDV),

    CONSTRAINT FK_CTHD_ThuCung FOREIGN KEY (maThuCung) REFERENCES ThuCung(maThuCung),

    CONSTRAINT CK_CTHD_SoLuong CHECK (soLuong > 0),

    CONSTRAINT CK_CTHD_DonGia CHECK (donGia > 0),

    CONSTRAINT CK_CTHD_ThanhTien CHECK (thanhTien > 0),

    CONSTRAINT CK_CTHD_Loai CHECK (

        (maSP IS NOT NULL AND maDV IS NULL AND maThuCung IS NULL)

        OR (maSP IS NULL AND maDV IS NOT NULL AND maThuCung IS NOT NULL)

    )

);

GO

CREATE UNIQUE INDEX UQ_CTHD_SanPham

ON ChiTietHoaDon(maHD, maSP)

WHERE maSP IS NOT NULL;

GO

CREATE UNIQUE INDEX UQ_CTHD_DichVu

ON ChiTietHoaDon(maHD, maThuCung, maDV)

WHERE maDV IS NOT NULL;

GO

-- =====================================================================

-- 2. THÊM DỮ LIỆU MẪU (Đúng thứ tự quan hệ khóa ngoại)

-- =====================================================================

-- Thêm tài khoản nhân viên trước

INSERT INTO TaiKhoan (tenTaiKhoan, matKhau, vaiTroTK, trangThaiTK, maNV) VALUES

('lan.nguyen', '123456', 'QUAN_LY', 'HOAT_DONG', 'NV2601001'),

('tuan.tran', '123456', 'NHAN_VIEN', 'HOAT_DONG', 'NV2601002'),

('thu.le', '123456', 'NHAN_VIEN', 'HOAT_DONG', 'NV2601003');

GO

-- Thêm Sản Phẩm

INSERT INTO SanPham (maSP, tenSP, giaNhap, giaBan, soLuongTon, donViTinh) VALUES

-- Sản phẩm dành cho chó

('SPCC0001', N'Thức ăn hạt cho chó 1kg', 105000, 150000, 50, 'GOI'),

('SPCC0002', N'Sữa tắm cho chó', 80000, 120000, 30, 'CHAI'),

('SPCC0003', N'Vòng cổ cho chó', 50000, 80000, 40, 'CAI'),

('SPCC0004', N'Bóng cao su cho chó', 30000, 50000, 60, 'CAI'),

('SPCC0005', N'Pate cho chó', 22000, 35000, 80, 'HOP'),

-- Sản phẩm dành cho mèo

('SPCM0001', N'Thức ăn hạt cho mèo 1kg', 95000, 140000, 45, 'GOI'),

('SPCM0002', N'Sữa tắm cho mèo', 75000, 110000, 25, 'CHAI'),

('SPCM0003', N'Cát vệ sinh cho mèo', 60000, 90000, 35, 'TUI'),

('SPCM0004', N'Pate cho mèo', 20000, 30000, 90, 'HOP'),

('SPCM0005', N'Đồ chơi cho mèo', 30000, 50000, 40, 'CAI');

GO

-- =====================================================================

-- SCRIPT SINH TỰ ĐỘNG HÓA ĐƠN & CHI TIẾT HÓA ĐƠN (01/01/2024 -> HÔM NAY)

-- =====================================================================

USE PetShopLand;

GO

SET NOCOUNT ON;

-- 1. Xóa dữ liệu Hóa đơn cũ để tránh trùng khóa chính

DELETE FROM ChiTietHoaDon;

DELETE FROM HoaDon;

BEGIN TRAN;

DECLARE @ngayHienTai DATE = '2024-01-01';

DECLARE @ngayKetThuc DATE = CAST(GETDATE() AS DATE);

DECLARE @soHDTrongNgay INT;

DECLARE @i INT;

DECLARE @loaiHD VARCHAR(4);

DECLARE @maHD VARCHAR(12);

DECLARE @ngayLap DATETIME;

DECLARE @maKH VARCHAR(10);

DECLARE @maThuCung VARCHAR(11);

DECLARE @maNV VARCHAR(9);

DECLARE @trangThaiHD VARCHAR(30);

DECLARE @phuongThucTT VARCHAR(30);

DECLARE @soMon INT;

-- 2. Vòng lặp duyệt qua từng ngày từ 01/01/2024 đến hôm nay

WHILE @ngayHienTai <= @ngayKetThuc

BEGIN

    SET @soHDTrongNgay = ABS(CHECKSUM(NEWID())) % 5 + 1;

    SET @i = 1;

    WHILE @i <= @soHDTrongNgay

    BEGIN

        IF (ABS(CHECKSUM(NEWID())) % 10) < 6

            SET @loaiHD = 'HDBH';

        ELSE

            SET @loaiHD = 'HDDV';

        -- Tạo mã HĐ: HDBH/HDDV + ddMMyy + 2 chữ số

        SET @maHD = @loaiHD

                    + FORMAT(@ngayHienTai, 'ddMMyy')

                    + RIGHT('00' + CAST(@i AS VARCHAR(2)), 2);

        -- Random giờ lập từ 08:00 đến 21:00

        SET @ngayLap = DATEADD(

            MINUTE,

            ABS(CHECKSUM(NEWID())) % 780 + 480,

            CAST(@ngayHienTai AS DATETIME)

        );

        -- Chọn ngẫu nhiên khách hàng và nhân viên còn làm

       -- HDBH: khách hàng bất kỳ; HDDV: chọn thú cưng trước để lấy đúng chủ

       IF @loaiHD = 'HDDV'

       BEGIN

           SELECT TOP 1 @maThuCung = maThuCung, @maKH = maKH

           FROM ThuCung ORDER BY NEWID();

       END

       ELSE

       BEGIN

           SET @maThuCung = NULL;

           SELECT TOP 1 @maKH = maKH

           FROM KhachHang ORDER BY NEWID();

       END;

        SELECT TOP 1 @maNV = maNV

        FROM NhanVien

        WHERE trangThaiNV = 'CON_LAM'

        ORDER BY NEWID();

        -- Hóa đơn hơn 3 ngày: đã thanh toán

        -- 3 ngày gần đây: có thể chưa thanh toán

        IF (@ngayHienTai >= DATEADD(DAY, -3, @ngayKetThuc))

           AND (ABS(CHECKSUM(NEWID())) % 10 < 3)

        BEGIN

            SET @trangThaiHD = 'CHUA_THANH_TOAN';

            SET @phuongThucTT = NULL;

        END

        ELSE

        BEGIN

            SET @trangThaiHD = 'DA_THANH_TOAN';

            IF (ABS(CHECKSUM(NEWID())) % 2 = 0)

                SET @phuongThucTT = 'TIEN_MAT';

            ELSE

                SET @phuongThucTT = 'THE_TIN_DUNG';

        END

        -- Thêm Hóa Đơn

        -- tongTien tạm = 1 để qua CHECK > 0

        -- giamGia tạm = 0, sẽ cập nhật ở dưới

        INSERT INTO HoaDon

            (maHD, ngayLap, tongTien, giamGia,

             trangThaiHD, phuongThucTT, maKH, maNV)

        VALUES

            (@maHD, @ngayLap, 1, 0,

             @trangThaiHD, @phuongThucTT, @maKH, @maNV);

        -- Random 1 đến 4 dòng chi tiết

        SET @soMon = ABS(CHECKSUM(NEWID())) % 4 + 1;

        IF @loaiHD = 'HDBH'

        BEGIN

            INSERT INTO ChiTietHoaDon (maHD, maSP, maDV, soLuong, donGia, thanhTien)

            SELECT TOP (@soMon) @maHD, maSP, NULL, ABS(CHECKSUM(NEWID())) % 3 + 1, giaBan, giaBan

            FROM SanPham ORDER BY NEWID();

        END

        ELSE

        BEGIN

           -- Mỗi HDDV mẫu phục vụ một thú cưng; số lượng dịch vụ = 1.

           -- Đơn giá lúc đầu là giá tiêu chuẩn, cộng phụ phí sau vòng lặp.

            INSERT INTO ChiTietHoaDon

               (maHD, maSP, maDV, maThuCung, soLuong, donGia, thanhTien)

            SELECT TOP (@soMon)

                @maHD,

                NULL,

                maDV,

               @maThuCung,

               1,

                giaTieuChuan,

                giaTieuChuan

            FROM DichVu

            ORDER BY NEWID();

        END

        SET @i = @i + 1;

    END

    -- Tăng lên 1 ngày

    SET @ngayHienTai = DATEADD(DAY, 1, @ngayHienTai);

END

-- 3. Cộng phụ phí cân nặng + hung dữ MỘT LẦN cho mỗi thú cưng/hóa đơn.

-- Chỉ cộng vào dịch vụ đầu tiên (theo maDV), giống ChiTietLichHen.

;WITH GiaDichVu AS (

    SELECT ct.maHD, ct.maDV, ct.maThuCung,

           ROW_NUMBER() OVER (

               PARTITION BY ct.maHD, ct.maThuCung ORDER BY ct.maDV

           ) AS thuTu,

           CASE WHEN tc.loai = 'CHO' THEN

               CASE WHEN tc.canNang <= 10 THEN 0

                    WHEN tc.canNang <= 15 THEN 30000

                    WHEN tc.canNang <= 20 THEN 60000

                    ELSE 90000 END

           ELSE

               CASE WHEN tc.canNang <= 4 THEN 0

                    WHEN tc.canNang <= 6 THEN 20000

                    WHEN tc.canNang <= 8 THEN 40000

                    ELSE 60000 END

           END

           + CASE tc.doHungDu

                 WHEN 'HIEN' THEN 0

                 WHEN 'NHAT' THEN 20000

                 ELSE 50000

             END AS phuPhi

    FROM ChiTietHoaDon ct

    JOIN ThuCung tc ON ct.maThuCung = tc.maThuCung

    WHERE ct.maDV IS NOT NULL

)

UPDATE ct

SET ct.donGia = ct.donGia + gd.phuPhi

FROM ChiTietHoaDon ct

JOIN GiaDichVu gd ON ct.maHD = gd.maHD

    AND ct.maDV = gd.maDV

    AND ct.maThuCung = gd.maThuCung

WHERE gd.thuTu = 1;

-- 4. Cập nhật thành tiền (HDBH vẫn là số lượng * đơn giá)

UPDATE ChiTietHoaDon

SET thanhTien = soLuong * donGia;

-- THÊM DỮ LIỆU MẪU GIẢM GIÁ

-- Các mức: 0, 10000, 20000, 30000 đồng

-- Dựa theo mã hóa đơn để phân bổ các mức giảm giá

UPDATE HoaDon

SET giamGia = (ABS(CONVERT(BIGINT, CHECKSUM(maHD))) % 4) * 10000;

-- 5. Cập nhật tổng tiền và điều chỉnh giảm giá nếu vượt quá tổng cộng

UPDATE hd

SET

    hd.giamGia = CASE

        WHEN hd.giamGia >= ct.tongCong THEN 0

        ELSE hd.giamGia

    END,

    hd.tongTien = CASE

        WHEN hd.giamGia >= ct.tongCong THEN ct.tongCong

        ELSE ct.tongCong - hd.giamGia

    END

FROM HoaDon hd

JOIN (

    SELECT maHD, SUM(thanhTien) AS tongCong

    FROM ChiTietHoaDon

    GROUP BY maHD

) ct ON hd.maHD = ct.maHD;

COMMIT TRAN;

SET NOCOUNT OFF;

GO

-- =====================================================================

-- KIỂM TRA KẾT QUẢ SAU KHI SINH DỮ LIỆU

-- =====================================================================

-- 1. Thống kê số lượng hóa đơn và doanh thu theo từng năm

SELECT

    YEAR(ngayLap) AS Nam,

    COUNT(*) AS TongSoHoaDon,

    SUM(tongTien) AS TongDoanhThu

FROM HoaDon

WHERE trangThaiHD = 'DA_THANH_TOAN'

GROUP BY YEAR(ngayLap)

ORDER BY Nam;

-- 2. Xem thử 20 hóa đơn mới nhất

SELECT TOP 20 *

FROM HoaDon

ORDER BY ngayLap DESC;

GO

---- Dữ liệu mẫu

--INSERT INTO HoaDon

--    (maHD, ngayLap, tongTien, trangThaiHD, phuongThucTT, maKH)

--VALUES

---- HÓA ĐƠN BÁN HÀNG

--('HDBH15102601', '2026-10-15T09:30:00', 350000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0001'),

--('HDBH16102601', '2026-10-16T10:15:00', 450000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0002'),

--('HDBH17102601', '2026-10-17T14:20:00', 210000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0003'),

--('HDBH18102601', '2026-10-18T08:45:00', 180000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0004'),

--('HDBH19102601', '2026-10-19T11:00:00', 250000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0005'),

--('HDBH20102601', '2026-10-20T15:30:00', 520000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0006'),

--('HDBH22102601', '2026-10-22T09:10:00', 175000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0007'),

--('HDBH25102601', '2026-10-25T13:40:00', 390000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0008'),

--('HDBH29102601', '2026-10-29T16:00:00', 120000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0009'),

--('HDBH02112601', '2026-11-02T09:00:00', 265000, 'CHUA_THANH_TOAN', NULL, 'KH0010'),

--('HDBH02112602', '2026-11-02T10:30:00', 430000, 'CHUA_THANH_TOAN', NULL, 'KH0001'),

--('HDBH02112603', '2026-11-02T14:00:00', 150000, 'CHUA_THANH_TOAN', NULL, 'KH0002'),

---- HÓA ĐƠN DỊCH VỤ

--('HDDV15102601', '2026-10-15T11:00:00', 180000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0003'),

--('HDDV17102601', '2026-10-17T15:00:00', 250000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0004'),

--('HDDV20102601', '2026-10-20T10:00:00', 320000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0005'),

--('HDDV23102601', '2026-10-23T14:30:00', 200000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0006'),

--('HDDV27102601', '2026-10-27T09:45:00', 280000, 'DA_THANH_TOAN', 'TIEN_MAT', 'KH0007'),

--('HDDV30102601', '2026-10-30T16:15:00', 350000, 'DA_THANH_TOAN', 'THE_TIN_DUNG', 'KH0008'),

--('HDDV02112601', '2026-11-02T11:15:00', 220000, 'CHUA_THANH_TOAN', NULL, 'KH0009'),

--('HDDV02112602', '2026-11-02T13:20:00', 170000, 'CHUA_THANH_TOAN', NULL, 'KH0010');

--GO

select * from HoaDon

SELECT * FROM HoaDon 

WHERE CAST(ngayLap AS DATE) BETWEEN '2026-11-02' AND '2026-11-06';

select count(*) from HoaDon

-- KIỂM TRA HDDV: Loài và phụ phí của từng dòng dịch vụ

SELECT TOP 30

    hd.maHD, hd.maKH, tc.maThuCung, tc.tenThuCung,

    tc.loai, dv.tenDV, dv.giaTieuChuan,

    ct.donGia - dv.giaTieuChuan AS phuPhi,

    ct.soLuong, ct.donGia, ct.thanhTien,

    hd.giamGia, hd.tongTien

FROM HoaDon hd

JOIN ChiTietHoaDon ct ON ct.maHD = hd.maHD

JOIN ThuCung tc ON tc.maThuCung = ct.maThuCung

JOIN DichVu dv ON dv.maDV = ct.maDV

WHERE hd.maHD LIKE 'HDDV%'

ORDER BY hd.maHD DESC, ct.maDV;

GO


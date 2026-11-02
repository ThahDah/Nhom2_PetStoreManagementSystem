-- =================================================================================
-- KỊCH BẢN KHỞI TẠO & NẠP DỮ LIỆU ĐỒ ÁN PETSHOP CHUYÊN CHÓ & MÈO (SQL SERVER)
-- Quy mô: 370 Khách hàng (70 gốc + 300 tự động) | 15 Nhân viên | 500 Sản phẩm (Chó & Mèo)
--         210 Dịch vụ (10 gốc + 200 tự động cho Chó & Mèo) | 120 Thú cưng (60 Chó, 60 Mèo)
--         520 Hóa đơn & 250 Lịch hẹn (Từ 01/01/2024 đến 31/10/2026)
-- Hướng dẫn: Bôi đen toàn bộ (Ctrl + A) và nhấn F5 (Execute) để Reset & Nạp lại
-- =================================================================================

USE master;
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'PetShopDB')
BEGIN
    CREATE DATABASE PetShopDB;
END
GO

USE PetShopDB;
GO

SET NOCOUNT ON;
GO

-- =================================================================================
-- 1. RESET DATABASE (XÓA CÁC BẢNG CŨ THEO THỨ TỰ TỪ BẢNG CON -> BẢNG CHA)
-- =================================================================================
-- Dùng OBJECT_ID thay cho DROP TABLE IF EXISTS để tương thích SQL Server 2008+
IF OBJECT_ID(N'dbo.ChiTietHoaDon', N'U') IS NOT NULL DROP TABLE dbo.ChiTietHoaDon;
IF OBJECT_ID(N'dbo.HoaDon', N'U') IS NOT NULL DROP TABLE dbo.HoaDon;
IF OBJECT_ID(N'dbo.ChiTietLichHen', N'U') IS NOT NULL DROP TABLE dbo.ChiTietLichHen;
IF OBJECT_ID(N'dbo.LichHen', N'U') IS NOT NULL DROP TABLE dbo.LichHen;
IF OBJECT_ID(N'dbo.TaiKhoan', N'U') IS NOT NULL DROP TABLE dbo.TaiKhoan;
IF OBJECT_ID(N'dbo.ThuCung', N'U') IS NOT NULL DROP TABLE dbo.ThuCung;
IF OBJECT_ID(N'dbo.SanPham', N'U') IS NOT NULL DROP TABLE dbo.SanPham;
IF OBJECT_ID(N'dbo.DichVu', N'U') IS NOT NULL DROP TABLE dbo.DichVu;
IF OBJECT_ID(N'dbo.NhanVien', N'U') IS NOT NULL DROP TABLE dbo.NhanVien;
IF OBJECT_ID(N'dbo.KhachHang', N'U') IS NOT NULL DROP TABLE dbo.KhachHang;
GO

-- =================================================================================
-- 2. TẠO BẢNG (CREATE TABLE) CHUẨN LƯỢC ĐỒ ERD
-- =================================================================================

CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    diemTichLuy INT DEFAULT 0
);

CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    trangThaiNV NVARCHAR(50) DEFAULT N'Đang làm việc',
    hinhAnh VARCHAR(255)
);

CREATE TABLE TaiKhoan (
    tenTaiKhoan VARCHAR(50) PRIMARY KEY,
    matKhau VARCHAR(255) NOT NULL,
    vaiTroTK NVARCHAR(50) NOT NULL,
    trangThaiTK NVARCHAR(50) DEFAULT N'Hoạt động',
    maNV VARCHAR(20) NOT NULL,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);

CREATE TABLE DichVu (
    maDV VARCHAR(20) PRIMARY KEY,
    tenDV NVARCHAR(150) NOT NULL,
    giaTieuChuan DECIMAL(18, 2) NOT NULL
);

CREATE TABLE SanPham (
    maSP VARCHAR(20) PRIMARY KEY,
    tenSP NVARCHAR(200) NOT NULL,
    giaNY DECIMAL(18, 2) NOT NULL,
    soLuongTon INT DEFAULT 0,
    donViTinh NVARCHAR(50)
);

CREATE TABLE ThuCung (
    maThuCung VARCHAR(20) PRIMARY KEY,
    tenThuCung NVARCHAR(100) NOT NULL,
    loai NVARCHAR(50) NOT NULL,
    canNang FLOAT,
    ghiChu NVARCHAR(255),
    trangThai NVARCHAR(50),
    doHungDu NVARCHAR(50),
    maKH VARCHAR(20) NOT NULL,
    CONSTRAINT FK_ThuCung_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH) ON DELETE CASCADE
);

CREATE TABLE LichHen (
    maLH VARCHAR(20) PRIMARY KEY,
    thoiGianHen DATETIME NOT NULL,
    ghiChu NVARCHAR(255),
    trangThaiHen NVARCHAR(50),
    maKH VARCHAR(20) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    CONSTRAINT FK_LichHen_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_LichHen_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

CREATE TABLE ChiTietLichHen (
    maLH VARCHAR(20) NOT NULL,
    maThuCung VARCHAR(20) NOT NULL,
    maDV VARCHAR(20) NOT NULL,
    donGiaDuKien DECIMAL(18, 2),
    canNangUocTinh FLOAT,
    doHungDuUocTinh NVARCHAR(50),
    CONSTRAINT PK_ChiTietLichHen PRIMARY KEY (maLH, maThuCung, maDV),
    CONSTRAINT FK_CTLH_LichHen FOREIGN KEY (maLH) REFERENCES LichHen(maLH) ON DELETE CASCADE,
    CONSTRAINT FK_CTLH_ThuCung FOREIGN KEY (maThuCung) REFERENCES ThuCung(maThuCung),
    CONSTRAINT FK_CTLH_DichVu FOREIGN KEY (maDV) REFERENCES DichVu(maDV)
);

CREATE TABLE HoaDon (
    maHD VARCHAR(20) PRIMARY KEY,
    ngayLap DATETIME NOT NULL,
    donGia DECIMAL(18, 2) DEFAULT 0,
    trangThaiHD NVARCHAR(50),
    phuongThucTT NVARCHAR(50),
    maKH VARCHAR(20) NOT NULL,
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);

CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(20) NOT NULL,
    maSP VARCHAR(20) NOT NULL,
    soLuong INT NOT NULL DEFAULT 1,
    maDV VARCHAR(20) NOT NULL,
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (maHD, maSP, maDV),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) ON DELETE CASCADE,
    CONSTRAINT FK_CTHD_SanPham FOREIGN KEY (maSP) REFERENCES SanPham(maSP),
    CONSTRAINT FK_CTHD_DichVu FOREIGN KEY (maDV) REFERENCES DichVu(maDV)
);
GO

-- =================================================================================
-- 3. NẠP DỮ LIỆU 15 NHÂN VIÊN & 15 TÀI KHOẢN
-- =================================================================================
INSERT INTO NhanVien (maNV, tenNV, soDienThoai, email, trangThaiNV, hinhAnh) VALUES
('NV001', N'Trần Phúc Thịnh', '0901112201', 'thinh.tp@petshop.vn', N'Đang làm việc', 'nv001.jpg'),
('NV002', N'Lê Thu Trang', '0901112202', 'trang.lt@petshop.vn', N'Đang làm việc', 'nv002.jpg'),
('NV003', N'Nguyễn Minh Quân', '0901112203', 'quan.nm@petshop.vn', N'Đang làm việc', 'nv003.jpg'),
('NV004', N'Phạm Thanh Thảo', '0901112204', 'thao.pt@petshop.vn', N'Đang làm việc', 'nv004.jpg'),
('NV005', N'Vũ Hoàng Long', '0901112205', 'long.vh@petshop.vn', N'Đang làm việc', 'nv005.jpg'),
('NV006', N'Đặng Ngọc Ánh', '0901112206', 'anh.dn@petshop.vn', N'Đang làm việc', 'nv006.jpg'),
('NV007', N'Bùi Quốc Bảo', '0901112207', 'bao.bq@petshop.vn', N'Đang làm việc', 'nv007.jpg'),
('NV008', N'Hoàng Phương Linh', '0901112208', 'linh.hp@petshop.vn', N'Đang làm việc', 'nv008.jpg'),
('NV009', N'Đỗ Đức Duy', '0901112209', 'duy.dd@petshop.vn', N'Đang làm việc', 'nv009.jpg'),
('NV010', N'Ngô Bích Phương', '0901112210', 'phuong.nb@petshop.vn', N'Đang làm việc', 'nv010.jpg'),
('NV011', N'Hồ Tuấn Kiệt', '0901112211', 'kiet.ht@petshop.vn', N'Đang làm việc', 'nv011.jpg'),
('NV012', N'Dương Khánh Ngân', '0901112212', 'ngan.dk@petshop.vn', N'Đang làm việc', 'nv012.jpg'),
('NV013', N'Lý Gia Huy', '0901112213', 'huy.lg@petshop.vn', N'Đang làm việc', 'nv013.jpg'),
('NV014', N'Phan Mai Hương', '0901112214', 'huong.pm@petshop.vn', N'Nghỉ phép', 'nv014.jpg'),
('NV015', N'Võ Thành Đạt', '0901112215', 'dat.vt@petshop.vn', N'Đã nghỉ việc', 'nv015.jpg');

INSERT INTO TaiKhoan (tenTaiKhoan, matKhau, vaiTroTK, trangThaiTK, maNV) VALUES
('admin', '123456', N'Quản lý', N'Hoạt động', 'NV001'),
('tranglt', '123456', N'Thu ngân', N'Hoạt động', 'NV002'),
('quannm', '123456', N'Bác sĩ thú y', N'Hoạt động', 'NV003'),
('thaopt', '123456', N'Nhân viên Spa', N'Hoạt động', 'NV004'),
('longvh', '123456', N'Nhân viên Spa', N'Hoạt động', 'NV005'),
('anhdn', '123456', N'Thu ngân', N'Hoạt động', 'NV006'),
('baobq', '123456', N'Bác sĩ thú y', N'Hoạt động', 'NV007'),
('linhhp', '123456', N'Nhân viên Spa', N'Hoạt động', 'NV008'),
('duydd', '123456', N'Nhân viên tư vấn', N'Hoạt động', 'NV009'),
('phuongnb', '123456', N'Thu ngân', N'Hoạt động', 'NV010'),
('kietht', '123456', N'Bác sĩ thú y', N'Hoạt động', 'NV011'),
('ngandk', '123456', N'Nhân viên Spa', N'Hoạt động', 'NV012'),
('huylg', '123456', N'Nhân viên tư vấn', N'Hoạt động', 'NV013'),
('huongpm', '123456', N'Nhân viên Spa', N'Tạm khóa', 'NV014'),
('datvt', '123456', N'Nhân viên tư vấn', N'Đã khóa', 'NV015');
GO

-- =================================================================================
-- 4. NẠP DỮ LIỆU KHÁCH HÀNG (70 KHÁCH HÀNG GỐC + SINH TỰ ĐỘNG 300 KHÁCH HÀNG = 370)
-- =================================================================================

-- 4.1. Nạp 70 khách hàng gốc (Mã CustomerID thực tế từ Online Retail.xlsx)
INSERT INTO KhachHang (maKH, tenKH, soDienThoai, email, diemTichLuy) VALUES
('KH17850', N'Nguyễn Văn An', '0912000000', 'an.nguyen17850@gmail.com', 10), ('KH13047', N'Trần Minh Hiếu', '0912112345', 'hieu.tran13047@gmail.com', 63),
('KH12583', N'Lê Thu Lâm', '0912224690', 'lam.le12583@gmail.com', 116), ('KH13748', N'Phạm Ngọc Nhung', '0912337035', 'nhung.pham13748@gmail.com', 169),
('KH15100', N'Hoàng Bích Tâm', '0912449380', 'tam.hoang15100@gmail.com', 222), ('KH15291', N'Huỳnh Bảo Toàn', '0912561725', 'toan.huynh15291@gmail.com', 275),
('KH14688', N'Phan Hữu Tùng', '0912674070', 'tung.phan14688@gmail.com', 328), ('KH17809', N'Vũ Thị Kiên', '0912786415', 'kien.vu17809@gmail.com', 381),
('KH15311', N'Võ Quốc Hải', '0912898760', 'hai.vo15311@gmail.com', 434), ('KH14527', N'Đặng Phương Khôi', '0913011105', 'khoi.dang14527@gmail.com', 487),
('KH16098', N'Bùi Tuấn Ngân', '0913123450', 'ngan.bui16098@gmail.com', 540), ('KH18074', N'Đỗ Hải Sơn', '0913235795', 'son.do18074@gmail.com', 593),
('KH17420', N'Hồ Xuân Thủy', '0913348140', 'thuy.ho17420@gmail.com', 646), ('KH16029', N'Ngô Mạnh Tuấn', '0913460485', 'tuan.ngo16029@gmail.com', 49),
('KH16250', N'Dương Hoàng Yến', '0913572830', 'yen.duong16250@gmail.com', 102), ('KH12431', N'Lý Thanh Giang', '0913685175', 'giang.ly12431@gmail.com', 155),
('KH17511', N'Đinh Gia Khoa', '0913797520', 'khoa.dinh17511@gmail.com', 208), ('KH17548', N'Nguyễn Đức Nam', '0913909865', 'nam.nguyen17548@gmail.com', 261),
('KH13705', N'Trần Khánh Quỳnh', '0914022210', 'quynh.tran13705@gmail.com', 314), ('KH13747', N'Lê Trọng Thư', '0914134555', 'thu.le13747@gmail.com', 367),
('KH13408', N'Phạm Văn Tú', '0914246900', 'tu.pham13408@gmail.com', 420), ('KH13767', N'Hoàng Minh Vy', '0914359245', 'vy.hoang13767@gmail.com', 473),
('KH17924', N'Huỳnh Thu Đạt', '0914471590', 'dat.huynh17924@gmail.com', 526), ('KH13448', N'Phan Ngọc Khánh', '0914583935', 'khanh.phan13448@gmail.com', 579),
('KH15862', N'Vũ Bích My', '0914696280', 'my.vu15862@gmail.com', 632), ('KH15513', N'Võ Bảo Quang', '0914808625', 'quang.vo15513@gmail.com', 35),
('KH12791', N'Đặng Hữu Thịnh', '0914920970', 'thinh.dang12791@gmail.com', 88), ('KH16218', N'Bùi Thị Trường', '0915033315', 'truong.bui16218@gmail.com', 141),
('KH14045', N'Đỗ Quốc Vinh', '0915145660', 'vinh.do14045@gmail.com', 194), ('KH14307', N'Hồ Phương Dũng', '0915258005', 'dung.ho14307@gmail.com', 247),
('KH17908', N'Ngô Tuấn Huy', '0915370350', 'huy.ngo17908@gmail.com', 300), ('KH17920', N'Dương Hải Mai', '0915482695', 'mai.duong17920@gmail.com', 353),
('KH12838', N'Lý Xuân Quân', '0915595040', 'quan.ly12838@gmail.com', 406), ('KH13255', N'Đinh Mạnh Thắng', '0915707385', 'thang.dinh13255@gmail.com', 459),
('KH16583', N'Nguyễn Hoàng Trung', '0915819730', 'trung.nguyen16583@gmail.com', 512), ('KH18085', N'Trần Thanh Việt', '0915932075', 'viet.tran18085@gmail.com', 565),
('KH13758', N'Lê Gia Châu', '0916044420', 'chau.le13758@gmail.com', 618), ('KH13694', N'Phạm Đức Hùng', '0916156765', 'hung.pham13694@gmail.com', 21),
('KH15983', N'Hoàng Khánh Long', '0916269110', 'long.hoang15983@gmail.com', 74), ('KH14849', N'Huỳnh Trọng Phúc', '0916381455', 'phuc.huynh14849@gmail.com', 127),
('KH17968', N'Phan Văn Thảo', '0916493800', 'thao.phan17968@gmail.com', 180), ('KH16210', N'Vũ Minh Trí', '0916606145', 'tri.vu16210@gmail.com', 233),
('KH17897', N'Võ Thu Vân', '0916718490', 'van.vo17897@gmail.com', 286), ('KH17377', N'Đặng Ngọc Bình', '0916830835', 'binh.dang17377@gmail.com', 339),
('KH16552', N'Bùi Bích Hòa', '0916943180', 'hoa.bui16552@gmail.com', 392), ('KH17181', N'Đỗ Bảo Linh', '0917055525', 'linh.do17181@gmail.com', 445),
('KH17951', N'Hồ Hữu Phong', '0917167870', 'phong.ho17951@gmail.com', 498), ('KH14729', N'Ngô Thị Tân', '0917280215', 'tan.ngo14729@gmail.com', 551),
('KH12748', N'Dương Quốc Trang', '0917392560', 'trang.duong12748@gmail.com', 604), ('KH15012', N'Lý Phương Uyên', '0917504905', 'uyen.ly15012@gmail.com', 657),
('KH12868', N'Đinh Tuấn An', '0917617250', 'an.dinh12868@gmail.com', 60), ('KH17572', N'Nguyễn Hải Hiếu', '0917729595', 'hieu.nguyen17572@gmail.com', 113),
('KH14078', N'Trần Xuân Lâm', '0917841940', 'lam.tran14078@gmail.com', 166), ('KH14001', N'Lê Mạnh Nhung', '0917954285', 'nhung.le14001@gmail.com', 219),
('KH12662', N'Phạm Hoàng Tâm', '0918066630', 'tam.pham12662@gmail.com', 272), ('KH15525', N'Hoàng Thanh Toàn', '0918178975', 'toan.hoang15525@gmail.com', 325),
('KH14237', N'Huỳnh Gia Tùng', '0918291320', 'tung.huynh14237@gmail.com', 378), ('KH17905', N'Phan Đức Kiên', '0918403665', 'kien.phan17905@gmail.com', 431),
('KH15485', N'Vũ Khánh Hải', '0918516010', 'hai.vu15485@gmail.com', 484), ('KH12433', N'Võ Trọng Khôi', '0918628355', 'khoi.vo12433@gmail.com', 537),
('KH16955', N'Đặng Văn Ngân', '0918740700', 'ngan.dang16955@gmail.com', 590), ('KH15350', N'Bùi Minh Sơn', '0918853045', 'son.bui15350@gmail.com', 643),
('KH15605', N'Đỗ Thu Thủy', '0918965390', 'thuy.do15605@gmail.com', 46), ('KH18144', N'Hồ Ngọc Tuấn', '0919077735', 'tuan.ho18144@gmail.com', 99),
('KH15922', N'Ngô Bích Yến', '0919190080', 'yen.ngo15922@gmail.com', 152), ('KH14594', N'Dương Bảo Giang', '0919302425', 'giang.duong14594@gmail.com', 205),
('KH15165', N'Lý Hữu Khoa', '0919414770', 'khoa.ly15165@gmail.com', 258), ('KH14911', N'Đinh Thị Nam', '0919527115', 'nam.dinh14911@gmail.com', 311),
('KH16456', N'Nguyễn Quốc Quỳnh', '0919639460', 'quynh.nguyen16456@gmail.com', 364), ('KH17841', N'Trần Phương Thư', '0919751805', 'thu.tran17841@gmail.com', 417);

-- 4.2. Sinh tự động thêm 300 Khách hàng (KH20001 -> KH20300)
DECLARE @khIdxAuto INT = 1;
WHILE @khIdxAuto <= 300
BEGIN
    DECLARE @maKHAuto VARCHAR(20) = 'KH' + CAST(20000 + @khIdxAuto AS VARCHAR(10));

    DECLARE @hoKH NVARCHAR(20) = CASE (@khIdxAuto % 15)
        WHEN 0 THEN N'Nguyễn' WHEN 1 THEN N'Trần' WHEN 2 THEN N'Lê' WHEN 3 THEN N'Phạm'
        WHEN 4 THEN N'Hoàng' WHEN 5 THEN N'Huỳnh' WHEN 6 THEN N'Phan' WHEN 7 THEN N'Vũ'
        WHEN 8 THEN N'Võ' WHEN 9 THEN N'Đặng' WHEN 10 THEN N'Bùi' WHEN 11 THEN N'Đỗ'
        WHEN 12 THEN N'Hồ' WHEN 13 THEN N'Ngô' ELSE N'Dương'
    END;

    DECLARE @demKH NVARCHAR(20) = CASE ((@khIdxAuto * 3) % 12)
        WHEN 0 THEN N'Văn' WHEN 1 THEN N'Thị' WHEN 2 THEN N'Minh' WHEN 3 THEN N'Hoàng'
        WHEN 4 THEN N'Ngọc' WHEN 5 THEN N'Thanh' WHEN 6 THEN N'Quốc' WHEN 7 THEN N'Gia'
        WHEN 8 THEN N'Phương' WHEN 9 THEN N'Đức' WHEN 10 THEN N'Bảo' ELSE N'Xuân'
    END;

    DECLARE @tenChinhKH NVARCHAR(20), @tenKhongDau VARCHAR(20);
    SELECT 
        @tenChinhKH = CASE ((@khIdxAuto * 7) % 20)
            WHEN 0 THEN N'An' WHEN 1 THEN N'Bình' WHEN 2 THEN N'Châu' WHEN 3 THEN N'Dũng'
            WHEN 4 THEN N'Đạt' WHEN 5 THEN N'Giang' WHEN 6 THEN N'Hải' WHEN 7 THEN N'Hiếu'
            WHEN 8 THEN N'Hùng' WHEN 9 THEN N'Huy' WHEN 10 THEN N'Khánh' WHEN 11 THEN N'Khoa'
            WHEN 12 THEN N'Lâm' WHEN 13 THEN N'Linh' WHEN 14 THEN N'Long' WHEN 15 THEN N'Mai'
            WHEN 16 THEN N'Nam' WHEN 17 THEN N'Ngân' WHEN 18 THEN N'Phúc' ELSE N'Quân'
        END,
        @tenKhongDau = CASE ((@khIdxAuto * 7) % 20)
            WHEN 0 THEN 'an' WHEN 1 THEN 'binh' WHEN 2 THEN 'chau' WHEN 3 THEN 'dung'
            WHEN 4 THEN 'dat' WHEN 5 THEN 'giang' WHEN 6 THEN 'hai' WHEN 7 THEN 'hieu'
            WHEN 8 THEN 'hung' WHEN 9 THEN 'huy' WHEN 10 THEN 'khanh' WHEN 11 THEN 'khoa'
            WHEN 12 THEN 'lam' WHEN 13 THEN 'linh' WHEN 14 THEN 'long' WHEN 15 THEN 'mai'
            WHEN 16 THEN 'nam' WHEN 17 THEN 'ngan' WHEN 18 THEN 'phuc' ELSE 'quan'
        END;

    DECLARE @sdtAuto VARCHAR(15) = '098' + RIGHT('0000000' + CAST(1000000 + @khIdxAuto * 23451 AS VARCHAR(10)), 7);
    DECLARE @emailAuto VARCHAR(100) = @tenKhongDau + '.kh' + CAST(20000 + @khIdxAuto AS VARCHAR(10)) + '@gmail.com';
    DECLARE @diemAuto INT = 15 + ((@khIdxAuto * 73) % 800);

    INSERT INTO KhachHang (maKH, tenKH, soDienThoai, email, diemTichLuy)
    VALUES (@maKHAuto, @hoKH + N' ' + @demKH + N' ' + @tenChinhKH, @sdtAuto, @emailAuto, @diemAuto);

    SET @khIdxAuto = @khIdxAuto + 1;
END;
GO

-- =================================================================================
-- 5. NẠP DỮ LIỆU DỊCH VỤ CHÓ & MÈO (10 DỊCH VỤ GỐC + SINH TỰ ĐỘNG 200 DỊCH VỤ = 210)
-- =================================================================================

-- 5.1. Nạp 10 dịch vụ gốc cho Chó & Mèo (DV000 -> DV009)
INSERT INTO DichVu (maDV, tenDV, giaTieuChuan) VALUES
('DV000', N'Không sử dụng dịch vụ (Chỉ mua sản phẩm)', 0),
('DV001', N'Tắm gội & Sấy khô khử mùi tiêu chuẩn cho Chó & Mèo', 150000),
('DV002', N'Cắt tỉa & Tạo kiểu lông thẩm mỹ (Grooming Chó & Mèo)', 250000),
('DV003', N'Khám sức khỏe tổng quát & Tư vấn dinh dưỡng Chó Mèo', 200000),
('DV004', N'Tiêm phòng Vaccine định kỳ & Tẩy giun Chó Mèo', 300000),
('DV005', N'Vệ sinh tai, cắt mài móng & Vắt tuyến hôi Chó Mèo', 120000),
('DV006', N'Trị liệu ve rận & Nấm da chuyên sâu cho Chó Mèo', 180000),
('DV007', N'Lưu trú khách sạn Chó Mèo phòng VIP (1 ngày)', 350000),
('DV008', N'Cạo vôi răng & Vệ sinh răng miệng Chó Mèo', 100000),
('DV009', N'Spa tắm thảo dược & Massage thư giãn Chó Mèo', 280000);

-- 5.2. Sinh tự động thêm 200 Dịch vụ chuyên biệt cho Chó và Mèo (DV010 -> DV209)
DECLARE @dvAuto INT = 10;
WHILE @dvAuto <= 209
BEGIN
    DECLARE @maDVAuto VARCHAR(20) = 'DV' + RIGHT('000' + CAST(@dvAuto AS VARCHAR(10)), 3);

    DECLARE @loaiDV NVARCHAR(60) = CASE (@dvAuto % 8)
        WHEN 0 THEN N'Gói tắm Spa thảo dược khử mùi'
        WHEN 1 THEN N'Cắt tỉa tạo kiểu lông nghệ thuật Grooming'
        WHEN 2 THEN N'Khám nội khoa & Siêu âm tổng quát'
        WHEN 3 THEN N'Tiêm phòng Vaccine & Xét nghiệm máu'
        WHEN 4 THEN N'Ủ dưỡng mượt lông Collagen & Vệ sinh tai móng'
        WHEN 5 THEN N'Điều trị nấm da, ve rận chuyên sâu'
        WHEN 6 THEN N'Lưu trú phòng điều hòa VIP & Chăm sóc dinh dưỡng'
        ELSE N'Vệ sinh nha khoa, lấy cao răng bằng sóng siêu âm'
    END;

    -- Chỉ dành riêng cho Chó hoặc Mèo
    DECLARE @thuDV NVARCHAR(30) = CASE (@dvAuto % 2)
        WHEN 0 THEN N'chó'
        ELSE N'mèo'
    END;

    DECLARE @coDV NVARCHAR(30) = CASE (@dvAuto % 5)
        WHEN 0 THEN N'cỡ nhỏ (S)'
        WHEN 1 THEN N'cỡ vừa (M)'
        WHEN 2 THEN N'cỡ lớn (L)'
        WHEN 3 THEN N'cỡ siêu nhỏ (XS)'
        ELSE N'cỡ đại (XL)'
    END;

    DECLARE @giaDVAuto DECIMAL(18, 2) = 80000 + ((@dvAuto * 17389) % 420) * 1000;

    INSERT INTO DichVu (maDV, tenDV, giaTieuChuan)
    VALUES (
        @maDVAuto,
        @loaiDV + N' cho ' + @thuDV + N' ' + @coDV + N' (#' + @maDVAuto + N')',
        @giaDVAuto
    );

    SET @dvAuto = @dvAuto + 1;
END;
GO

-- =================================================================================
-- 6. NẠP ĐÚNG 500 MÃ SẢN PHẨM CHỈ DÀNH CHO CHÓ & MÈO (SP4172 -> SP4671)
-- =================================================================================

-- 6.1. Nạp trực tiếp 30 mã sản phẩm Chó & Mèo đầu tiên (SP4172 -> SP4201) lọc từ pet_store_records_2020.csv
INSERT INTO SanPham (maSP, tenSP, giaNY, soLuongTon, donViTinh) VALUES
('SP4172', N'Thuốc thú y cho chó cỡ nhỏ (Medicine - #4172)', 118500, 210, N'Hộp'),
('SP4173', N'Thực phẩm bổ sung cho mèo cỡ vừa (Supplements - #4173)', 90740, 79, N'Hộp'),
('SP4174', N'Thiết bị chăm sóc cho chó cỡ vừa (Equipment - #4174)', 68960, 120, N'Cái'),
('SP4175', N'Áo quần thời trang cho mèo cỡ lớn (Clothes - #4175)', 179410, 93, N'Bộ'),
('SP4176', N'Thực phẩm bổ sung cho chó cỡ lớn (Supplements - #4176)', 102830, 138, N'Hộp'),
('SP4177', N'Dụng cụ Spa cho mèo cỡ siêu nhỏ (Grooming - #4177)', 63700, 109, N'Bộ'),
('SP4178', N'Áo quần thời trang cho chó cỡ siêu nhỏ (Clothes - #4178)', 45460, 57, N'Bộ'),
('SP4179', N'Nệm lót ổ nằm cho mèo cỡ đại (Bedding - #4179)', 176250, 160, N'Chiếc'),
('SP4180', N'Dụng cụ Spa cho chó cỡ đại (Grooming - #4180)', 58850, 121, N'Bộ'),
('SP4181', N'Phụ kiện cho mèo cỡ nhỏ (Accessory - #4181)', 24950, 90, N'Cái'),
('SP4182', N'Nệm lót ổ nằm cho chó cỡ nhỏ (Bedding - #4182)', 37660, 170, N'Chiếc'),
('SP4183', N'Chuồng lồng cho mèo cỡ vừa (Housing - #4183)', 79770, 194, N'Chuồng'),
('SP4184', N'Phụ kiện cho chó cỡ vừa (Accessory - #4184)', 61530, 218, N'Cái'),
('SP4185', N'Thức ăn dinh dưỡng cho mèo cỡ lớn (Food - #4185)', 11530, 192, N'Túi'),
('SP4186', N'Chuồng lồng cho chó cỡ lớn (Housing - #4186)', 66360, 172, N'Chuồng'),
('SP4187', N'Đồ chơi vận động cho mèo cỡ siêu nhỏ (Toys - #4187)', 120250, 178, N'Món'),
('SP4188', N'Thức ăn dinh dưỡng cho chó cỡ siêu nhỏ (Food - #4188)', 151750, 101, N'Túi'),
('SP4189', N'Bánh thưởng cho mèo cỡ đại (Snack - #4189)', 92370, 110, N'Gói'),
('SP4190', N'Đồ chơi vận động cho chó cỡ đại (Toys - #4190)', 127880, 11, N'Món'),
('SP4191', N'Thuốc thú y cho mèo cỡ nhỏ (Medicine - #4191)', 145580, 81, N'Hộp'),
('SP4192', N'Bánh thưởng cho chó cỡ nhỏ (Snack - #4192)', 148690, 208, N'Gói'),
('SP4193', N'Thiết bị chăm sóc cho mèo cỡ vừa (Equipment - #4193)', 123650, 108, N'Cái'),
('SP4194', N'Thuốc thú y cho chó cỡ vừa (Medicine - #4194)', 190820, 141, N'Hộp'),
('SP4195', N'Thực phẩm bổ sung cho mèo cỡ lớn (Supplements - #4195)', 179550, 93, N'Hộp'),
('SP4196', N'Thiết bị chăm sóc cho chó cỡ lớn (Equipment - #4196)', 144350, 31, N'Cái'),
('SP4197', N'Áo quần thời trang cho mèo cỡ siêu nhỏ (Clothes - #4197)', 16500, 31, N'Bộ'),
('SP4198', N'Thực phẩm bổ sung cho chó cỡ siêu nhỏ (Supplements - #4198)', 100110, 68, N'Hộp'),
('SP4199', N'Dụng cụ Spa cho mèo cỡ đại (Grooming - #4199)', 89270, 67, N'Bộ'),
('SP4200', N'Áo quần thời trang cho chó cỡ đại (Clothes - #4200)', 29960, 46, N'Bộ'),
('SP4201', N'Nệm lót ổ nằm cho mèo cỡ nhỏ (Bedding - #4201)', 114160, 91, N'Chiếc');

-- 6.2. Sinh tự động 470 mã sản phẩm Chó & Mèo tiếp theo (SP4202 -> SP4671) để đủ 500 mã
DECLARE @pid INT = 4202;
WHILE @pid <= 4671
BEGIN
    DECLARE @idx INT = @pid - 4172;
    DECLARE @catMod INT = @idx % 11;
    DECLARE @petMod INT = @idx % 2; -- Chỉ nhận 0 (Chó) hoặc 1 (Mèo)
    DECLARE @sizeMod INT = @idx % 5;

    DECLARE @catVN NVARCHAR(50), @catEN VARCHAR(30), @dvt NVARCHAR(20);
    SELECT 
        @catVN = CASE @catMod
            WHEN 0 THEN N'Thuốc thú y'
            WHEN 1 THEN N'Thực phẩm bổ sung'
            WHEN 2 THEN N'Dụng cụ Spa'
            WHEN 3 THEN N'Phụ kiện'
            WHEN 4 THEN N'Thức ăn dinh dưỡng'
            WHEN 5 THEN N'Bánh thưởng'
            WHEN 6 THEN N'Thiết bị chăm sóc'
            WHEN 7 THEN N'Áo quần thời trang'
            WHEN 8 THEN N'Nệm lót ổ nằm'
            WHEN 9 THEN N'Chuồng lồng'
            ELSE N'Đồ chơi vận động'
        END,
        @catEN = CASE @catMod
            WHEN 0 THEN 'Medicine'
            WHEN 1 THEN 'Supplements'
            WHEN 2 THEN 'Grooming'
            WHEN 3 THEN 'Accessory'
            WHEN 4 THEN 'Food'
            WHEN 5 THEN 'Snack'
            WHEN 6 THEN 'Equipment'
            WHEN 7 THEN 'Clothes'
            WHEN 8 THEN 'Bedding'
            WHEN 9 THEN 'Housing'
            ELSE 'Toys'
        END,
        @dvt = CASE @catMod
            WHEN 0 THEN N'Hộp'
            WHEN 1 THEN N'Hộp'
            WHEN 2 THEN N'Bộ'
            WHEN 3 THEN N'Cái'
            WHEN 4 THEN N'Túi'
            WHEN 5 THEN N'Gói'
            WHEN 6 THEN N'Cái'
            WHEN 7 THEN N'Bộ'
            WHEN 8 THEN N'Chiếc'
            WHEN 9 THEN N'Chuồng'
            ELSE N'Món'
        END;

    -- Chỉ có Chó hoặc Mèo
    DECLARE @petVN NVARCHAR(30) = CASE @petMod
        WHEN 0 THEN N'chó'
        ELSE N'mèo'
    END;

    DECLARE @sizeVN NVARCHAR(30) = CASE @sizeMod
        WHEN 0 THEN N'nhỏ'
        WHEN 1 THEN N'vừa'
        WHEN 2 THEN N'lớn'
        WHEN 3 THEN N'siêu nhỏ'
        ELSE N'đại'
    END;

    DECLARE @gia DECIMAL(18,2) = (1500 + ((@pid * 7919) % 18000)) * 10;
    DECLARE @ton INT = 10 + ((@pid * 104729) % 221);

    INSERT INTO SanPham (maSP, tenSP, giaNY, soLuongTon, donViTinh)
    VALUES (
        'SP' + CAST(@pid AS VARCHAR(10)),
        @catVN + N' cho ' + @petVN + N' cỡ ' + @sizeVN + N' (' + @catEN + N' - #' + CAST(@pid AS NVARCHAR(10)) + N')',
        @gia,
        @ton,
        @dvt
    );

    SET @pid = @pid + 1;
END;
GO

-- =================================================================================
-- 7. NẠP DỮ LIỆU 120 THÚ CƯNG (CHỈ GỒM 60 CHÓ & 60 MÈO PHÂN BỔ TRÊN 370 KHÁCH HÀNG)
-- =================================================================================
DECLARE @KhachHangList TABLE (stt INT IDENTITY(1,1), maKH VARCHAR(20));
INSERT INTO @KhachHangList (maKH) SELECT maKH FROM KhachHang ORDER BY maKH;
DECLARE @totalKH INT = (SELECT COUNT(*) FROM @KhachHangList);

DECLARE @tc INT = 1;
WHILE @tc <= 120
BEGIN
    DECLARE @maTC VARCHAR(20) = 'TC' + RIGHT('0000' + CAST(@tc AS VARCHAR(10)), 4);
    DECLARE @khIdx INT = ((@tc * 3 - 1) % @totalKH) + 1;
    DECLARE @maKH_TC VARCHAR(20);
    SELECT @maKH_TC = maKH FROM @KhachHangList WHERE stt = @khIdx;

    DECLARE @tenTC NVARCHAR(50) = CASE (@tc % 20)
        WHEN 0 THEN N'Milo' WHEN 1 THEN N'Miu' WHEN 2 THEN N'Bơ' WHEN 3 THEN N'Lu'
        WHEN 4 THEN N'Simba' WHEN 5 THEN N'Kiki' WHEN 6 THEN N'Rex' WHEN 7 THEN N'Mimi'
        WHEN 8 THEN N'Bông' WHEN 9 THEN N' Cam' WHEN 10 THEN N'Lucky' WHEN 11 THEN N'Coco'
        WHEN 12 THEN N'Đậu' WHEN 13 THEN N'Gấu' WHEN 14 THEN N'Mochi' WHEN 15 THEN N'Bella'
        WHEN 16 THEN N'Max' WHEN 17 THEN N'Na' WHEN 18 THEN N'Bim' ELSE N'Kem'
    END + N' #' + CAST(@tc AS NVARCHAR(10));

    -- Chỉ có Chó (chẵn) và Mèo (lẻ)
    DECLARE @loaiTC NVARCHAR(50) = CASE (@tc % 2)
        WHEN 0 THEN N'Chó'
        ELSE N'Mèo'
    END;

    -- Cân nặng thực tế cho Chó (2.0kg - 30.0kg) và Mèo (1.2kg - 8.7kg)
    DECLARE @canNang FLOAT = CASE (@tc % 2)
        WHEN 0 THEN ROUND(2.0 + ((@tc * 17) % 280) / 10.0, 1)
        ELSE ROUND(1.2 + ((@tc * 13) % 75) / 10.0, 1)
    END;

    -- Ghi chú giống Chó và giống Mèo thực tế
    DECLARE @ghiChuTC NVARCHAR(255) = CASE (@tc % 2)
        WHEN 0 THEN CASE ((@tc / 2) % 6)
            WHEN 0 THEN N'Giống chó Golden Retriever, hiền lành, thích vận động'
            WHEN 1 THEN N'Giống chó Poodle Tiny, lông xoăn cần cắt tỉa định kỳ'
            WHEN 2 THEN N'Giống chó Corgi chân ngắn, ham ăn bánh thưởng'
            WHEN 3 THEN N'Giống chó Husky Sibir, năng động, bộ lông dày'
            WHEN 4 THEN N'Giống chó Phốc Sóc (Pomeranian), nhỏ nhắn'
            ELSE N'Giống chó Shiba Inu, sạch sẽ, quấn chủ'
        END
        ELSE CASE ((@tc / 2) % 6)
            WHEN 0 THEN N'Giống mèo Anh lông ngắn (British Shorthair), má phệ'
            WHEN 1 THEN N'Giống mèo Anh lông dài, cần chải và dưỡng lông thường xuyên'
            WHEN 2 THEN N'Giống mèo Ba Tư (Persian), tính cách điềm tĩnh'
            WHEN 3 THEN N'Giống mèo Munchkin chân ngắn, tinh nghịch'
            WHEN 4 THEN N'Giống mèo Scottish Fold tai cụp, ngoan ngoãn'
            ELSE N'Giống mèo Maine Coon cỡ lớn, khung xương to'
        END
    END;

    DECLARE @trangThaiTC NVARCHAR(50) = CASE 
        WHEN @tc % 9 = 0 THEN N'Đang điều trị'
        WHEN @tc % 5 = 0 THEN N'Đang theo dõi'
        ELSE N'Khỏe mạnh'
    END;

    DECLARE @doHungDu NVARCHAR(50) = CASE (@tc % 5)
        WHEN 0 THEN N'Cao'
        WHEN 1 THEN N'Trung bình'
        ELSE N'Thấp'
    END;

    INSERT INTO ThuCung (maThuCung, tenThuCung, loai, canNang, ghiChu, trangThai, doHungDu, maKH)
    VALUES (
        @maTC, @tenTC, @loaiTC, @canNang,
        @ghiChuTC, @trangThaiTC, @doHungDu, @maKH_TC
    );

    SET @tc = @tc + 1;
END;
GO

-- =================================================================================
-- 8. NẠP 250 LỊCH HẸN & CHI TIẾT LỊCH HẸN (TỪ 01/01/2024 ĐẾN 31/10/2026)
-- =================================================================================
DECLARE @ThuCungList TABLE (stt INT IDENTITY(1,1), maThuCung VARCHAR(20), maKH VARCHAR(20), canNang FLOAT, doHungDu NVARCHAR(50));
INSERT INTO @ThuCungList (maThuCung, maKH, canNang, doHungDu)
SELECT maThuCung, maKH, canNang, doHungDu FROM ThuCung ORDER BY maThuCung;

DECLARE @lh INT = 1;
WHILE @lh <= 250
BEGIN
    DECLARE @maLH VARCHAR(20) = 'LH' + RIGHT('0000' + CAST(@lh AS VARCHAR(10)), 4);
    DECLARE @ngayHen DATETIME = DATEADD(HOUR, 8 + (@lh % 10), DATEADD(DAY, ((@lh - 1) * 1034) / 249, '2024-01-01'));
    
    DECLARE @tcIdx INT = ((@lh - 1) % 120) + 1;
    DECLARE @maTC_LH VARCHAR(20), @maKH_LH VARCHAR(20), @cn_LH FLOAT, @hd_LH NVARCHAR(50);
    SELECT @maTC_LH = maThuCung, @maKH_LH = maKH, @cn_LH = canNang, @hd_LH = doHungDu
    FROM @ThuCungList WHERE stt = @tcIdx;

    DECLARE @maNV_LH VARCHAR(20) = 'NV' + RIGHT('000' + CAST(((@lh % 13) + 1) AS VARCHAR(5)), 3);
    DECLARE @trangThaiHen NVARCHAR(50) = CASE 
        WHEN @ngayHen < '2026-10-25' THEN (CASE WHEN @lh % 12 = 0 THEN N'Đã hủy' ELSE N'Đã hoàn thành' END)
        WHEN @lh % 2 = 0 THEN N'Đã xác nhận'
        ELSE N'Chờ xác nhận'
    END;

    INSERT INTO LichHen (maLH, thoiGianHen, ghiChu, trangThaiHen, maKH, maNV)
    VALUES (@maLH, @ngayHen, N'Lịch hẹn chăm sóc & khám thú y Chó Mèo #' + CAST(@lh AS NVARCHAR(10)), @trangThaiHen, @maKH_LH, @maNV_LH);

    -- Gán dịch vụ từ 209 gói dịch vụ có phí (DV001 -> DV209)
    DECLARE @dvNum INT = (@lh % 209) + 1;
    DECLARE @maDV1 VARCHAR(20) = 'DV' + RIGHT('000' + CAST(@dvNum AS VARCHAR(5)), 3);
    DECLARE @giaDV1 DECIMAL(18,2);
    SELECT @giaDV1 = giaTieuChuan FROM DichVu WHERE maDV = @maDV1;

    INSERT INTO ChiTietLichHen (maLH, maThuCung, maDV, donGiaDuKien, canNangUocTinh, doHungDuUocTinh)
    VALUES (@maLH, @maTC_LH, @maDV1, @giaDV1, @cn_LH, @hd_LH);

    IF (@lh % 3 = 0)
    BEGIN
        DECLARE @dvNum2 INT = ((@lh + 17) % 209) + 1;
        IF @dvNum2 <> @dvNum
        BEGIN
            DECLARE @maDV2 VARCHAR(20) = 'DV' + RIGHT('000' + CAST(@dvNum2 AS VARCHAR(5)), 3);
            DECLARE @giaDV2 DECIMAL(18,2);
            SELECT @giaDV2 = giaTieuChuan FROM DichVu WHERE maDV = @maDV2;

            INSERT INTO ChiTietLichHen (maLH, maThuCung, maDV, donGiaDuKien, canNangUocTinh, doHungDuUocTinh)
            VALUES (@maLH, @maTC_LH, @maDV2, @giaDV2, @cn_LH, @hd_LH);
        END
    END

    SET @lh = @lh + 1;
END;
GO

-- =================================================================================
-- 9. NẠP 520 HÓA ĐƠN & ~1,040 CHI TIẾT HÓA ĐƠN (TỪ 01/01/2024 ĐẾN 31/10/2026)
-- =================================================================================
DECLARE @KHTable TABLE (stt INT IDENTITY(1,1), maKH VARCHAR(20));
INSERT INTO @KHTable (maKH) SELECT maKH FROM KhachHang ORDER BY maKH;
DECLARE @cntKH INT = (SELECT COUNT(*) FROM @KHTable);

DECLARE @hd INT = 1;
WHILE @hd <= 520
BEGIN
    DECLARE @offsetDays INT = ((@hd - 1) * 1034) / 519;
    DECLARE @ngayLap DATETIME = DATEADD(MINUTE, (@hd * 37) % 600, DATEADD(HOUR, 8, DATEADD(DAY, @offsetDays, '2024-01-01')));
    DECLARE @namPrefix VARCHAR(2) = RIGHT(CAST(YEAR(@ngayLap) AS VARCHAR(4)), 2);
    DECLARE @maHD VARCHAR(20) = 'HD' + @namPrefix + RIGHT('0000' + CAST(@hd AS VARCHAR(10)), 4);

    DECLARE @khIndex INT = ((@hd * 11) % @cntKH) + 1;
    DECLARE @maKH_HD VARCHAR(20);
    SELECT @maKH_HD = maKH FROM @KHTable WHERE stt = @khIndex;

    DECLARE @pttt NVARCHAR(50) = CASE (@hd % 4)
        WHEN 0 THEN N'Tiền mặt'
        WHEN 1 THEN N'Chuyển khoản'
        WHEN 2 THEN N'Thẻ tín dụng'
        ELSE N'Ví điện tử MoMo'
    END;

    DECLARE @ttHD NVARCHAR(50) = CASE
        WHEN @ngayLap >= '2026-10-28' AND (@hd % 3 = 0) THEN N'Chờ thanh toán'
        WHEN @hd % 25 = 0 THEN N'Đã hủy'
        ELSE N'Đã thanh toán'
    END;

    INSERT INTO HoaDon (maHD, ngayLap, donGia, trangThaiHD, phuongThucTT, maKH)
    VALUES (@maHD, @ngayLap, 0, @ttHD, @pttt, @maKH_HD);

    -- Mỗi hóa đơn có từ 1 đến 3 dòng ChiTietHoaDon kết nối với 500 mã sản phẩm Chó Mèo (SP4172 -> SP4671) và 210 mã dịch vụ (DV000 -> DV209)
    DECLARE @soDong INT = (@hd % 3) + 1;
    DECLARE @d INT = 1;
    WHILE @d <= @soDong
    BEGIN
        DECLARE @spNum INT = 4172 + ((@hd * 17 + @d * 83) % 500);
        DECLARE @maSP_CT VARCHAR(20) = 'SP' + CAST(@spNum AS VARCHAR(10));
        DECLARE @soLuong_CT INT = ((@hd + @d) % 5) + 1;
        DECLARE @maDV_CT VARCHAR(20) = CASE 
            WHEN @d = 1 AND (@hd % 2 = 0) THEN 'DV' + RIGHT('000' + CAST(((@hd % 209) + 1) AS VARCHAR(5)), 3)
            ELSE 'DV000'
        END;

        IF NOT EXISTS (SELECT 1 FROM ChiTietHoaDon WHERE maHD = @maHD AND maSP = @maSP_CT AND maDV = @maDV_CT)
        BEGIN
            INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong, maDV)
            VALUES (@maHD, @maSP_CT, @soLuong_CT, @maDV_CT);
        END

        SET @d = @d + 1;
    END;

    SET @hd = @hd + 1;
END;
GO

-- Cập nhật chính xác tổng tiền (donGia) cho toàn bộ 520 Hóa đơn dựa trên ChiTietHoaDon
UPDATE hd
SET hd.donGia = ISNULL(t.TongTien, 0)
FROM HoaDon hd
LEFT JOIN (
    SELECT 
        ct.maHD,
        SUM(ct.soLuong * sp.giaNY + dv.giaTieuChuan) AS TongTien
    FROM ChiTietHoaDon ct
    INNER JOIN SanPham sp ON ct.maSP = sp.maSP
    INNER JOIN DichVu dv ON ct.maDV = dv.maDV
    GROUP BY ct.maHD
) t ON hd.maHD = t.maHD;
GO

-- =================================================================================
-- 10. KIỂM TRA SỐ LƯỢNG BẢN GHI & PHÂN BỔ LOÀI THÚ CƯNG (CHỈ CHÓ & MÈO)
-- =================================================================================
SELECT '1. KhachHang (70 goc + 300 tu dong)' AS Bang, COUNT(*) AS SoLuongBanGhi FROM KhachHang
UNION ALL SELECT '2. NhanVien', COUNT(*) FROM NhanVien
UNION ALL SELECT '3. TaiKhoan', COUNT(*) FROM TaiKhoan
UNION ALL SELECT '4. DichVu (Chuyen Cho & Meo)', COUNT(*) FROM DichVu
UNION ALL SELECT '5. SanPham (Chuyen Cho & Meo)', COUNT(*) FROM SanPham
UNION ALL SELECT '6. ThuCung (Chi Cho & Meo)', COUNT(*) FROM ThuCung
UNION ALL SELECT '7. LichHen', COUNT(*) FROM LichHen
UNION ALL SELECT '8. ChiTietLichHen', COUNT(*) FROM ChiTietLichHen
UNION ALL SELECT '9. HoaDon (01/2024 - 10/2026)', COUNT(*) FROM HoaDon
UNION ALL SELECT '10. ChiTietHoaDon', COUNT(*) FROM ChiTietHoaDon;

-- Kiểm tra phân bổ loài trong bảng ThuCung
SELECT loai AS LoaiThuCung, COUNT(*) AS SoLuong, ROUND(AVG(canNang), 2) AS CanNangTrungBinh_Kg
FROM ThuCung
GROUP BY loai;
GO
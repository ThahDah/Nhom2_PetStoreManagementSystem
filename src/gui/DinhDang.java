package gui;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

/** Các định dạng dùng chung để hiển thị trên giao diện. */
public final class DinhDang {
	public static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static final DecimalFormat TIEN = new DecimalFormat("#,##0 'đ'");

	private DinhDang() {
	}
}

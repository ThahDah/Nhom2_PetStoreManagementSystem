package common;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/** Các định dạng dùng chung để hiển thị trên giao diện. */
public final class DinhDang {
	public static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public static final DecimalFormat TIEN = new DecimalFormat("#,##0 'đ'");
	// dung de doc ngay nhap vao la ngay khong ton tai
	public static final DateTimeFormatter NGAY_NHAP = DateTimeFormatter.ofPattern("dd/MM/uuuu")
			.withResolverStyle(ResolverStyle.STRICT);
	private DinhDang() {
	}
}


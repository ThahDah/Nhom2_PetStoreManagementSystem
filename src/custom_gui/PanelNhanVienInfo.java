package custom_gui;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.NhanVien;

/**
 * Component hiển thị avatar tròn + tên + chức vụ của nhân viên đăng nhập.
 * Dùng ở pnlTop của MainGUI.
 */
@SuppressWarnings("serial")
public class PanelNhanVienInfo extends JPanel {

    private static final int AVATAR_SIZE = 40;
    private static final String THU_MUC_ANH = "img/nhanvien/"; // thư mục ảnh nhân viên (sửa theo project)

    private final AvatarCircle avatar;
    private final JLabel lblTen;
    private final JLabel lblChucVu;

    public PanelNhanVienInfo(NhanVien nv) {
        this(nv, "Nhân viên");
    }

    public PanelNhanVienInfo(NhanVien nv, String chucVu) {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        avatar = new AvatarCircle(AVATAR_SIZE);

        lblTen = new JLabel();
        lblTen.setFont(new Font("Arial", Font.BOLD, 14));

        lblChucVu = new JLabel();
        lblChucVu.setFont(new Font("Arial", Font.ITALIC, 12));
        lblChucVu.setForeground(new Color(110, 110, 110));

        JPanel pnlText = new JPanel();
        pnlText.setOpaque(false);
        pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
        pnlText.add(lblTen);
        pnlText.add(Box.createVerticalStrut(2));
        pnlText.add(lblChucVu);

        add(avatar);
        add(pnlText);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        setNhanVien(nv, chucVu);
    }

    /** Cập nhật lại thông tin khi đổi nhân viên. */
    public void setNhanVien(NhanVien nv, String chucVu) {
        String ten = (nv != null && nv.getTenNV() != null) ? nv.getTenNV() : "Chưa đăng nhập";
        lblTen.setText(ten);
        lblChucVu.setText(chucVu != null ? chucVu : "");
        avatar.setData(loadAnh(nv != null ? nv.getHinhAnh() : null), ten);
        revalidate();
        repaint();
    }

    // Thử đọc ảnh theo đường dẫn gốc, nếu không có thì thử trong thư mục ảnh nhân viên
    private BufferedImage loadAnh(String path) {
        if (path == null || path.trim().isEmpty()) return null;
        String[] ungVien = { path, THU_MUC_ANH + path };
        for (String p : ungVien) {
            try {
                File f = new File(p);
                if (f.exists()) return ImageIO.read(f);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    // ====================================================================
    // Avatar hình tròn. Không có ảnh thì vẽ chữ cái đầu của tên.
    // ====================================================================
    private static class AvatarCircle extends JComponent {
        private final int size;
        private BufferedImage img;
        private String ten = "";

        AvatarCircle(int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
        }

        void setData(BufferedImage img, String ten) {
            this.img = img;
            this.ten = ten;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Ellipse2D circle = new Ellipse2D.Double(1, 1, size - 2, size - 2);

            if (img != null) {
                // cắt vuông ở giữa rồi bo tròn để ảnh không bị méo
                int side = Math.min(img.getWidth(), img.getHeight());
                int x = (img.getWidth() - side) / 2;
                int y = (img.getHeight() - side) / 2;
                g2.setClip(circle);
                g2.drawImage(img.getSubimage(x, y, side, side), 1, 1, size - 2, size - 2, null);
                g2.setClip(null);
            } else {
                g2.setColor(new Color(0, 102, 204));
                g2.fill(circle);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, size / 2));
                String chu = layChuCaiDau(ten);
                int tx = (size - g2.getFontMetrics().stringWidth(chu)) / 2;
                int ty = (size + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
                g2.drawString(chu, tx, ty);
            }

            g2.setColor(new Color(200, 200, 200));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(circle);
            g2.dispose();
        }

        // Lấy chữ cái đầu của từ cuối trong tên (tên người Việt): "Nguyễn Văn An" -> "A"
        private String layChuCaiDau(String s) {
            if (s == null || s.trim().isEmpty()) return "?";
            String[] parts = s.trim().split("\\s+");
            return parts[parts.length - 1].substring(0, 1).toUpperCase();
        }
    }
}
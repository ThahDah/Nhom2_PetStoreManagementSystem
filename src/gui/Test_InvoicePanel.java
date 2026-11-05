package gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Panel chính của màn hình lập hóa đơn: Hóa đơn sản phẩm / Hóa đơn dịch vụ.
 * Hai chế độ được chuyển đổi bằng CardLayout.
 */
public class Test_InvoicePanel extends JPanel {

    private static final String CARD_PRODUCT = "PRODUCT";
    private static final String CARD_SERVICE = "SERVICE";

    private static final Color COLOR_BG = new Color(0xF5F6FA);
    private static final Color COLOR_TEXT = new Color(0x1F2230);
    private static final Color COLOR_MUTED = new Color(0x6B7280);
    private static final Color COLOR_BORDER = new Color(0xE5E7EB);
    private static final Color COLOR_SOFT = new Color(0xF3F4F6);
    private static final Color COLOR_TOTAL_BG = new Color(0xFAFAFB);
    private static final Color COLOR_ORANGE = new Color(0xEE6C3B);
    private static final Color COLOR_ORANGE_LIGHT = new Color(0xFDEDE6);
    private static final Color COLOR_PURPLE = new Color(0x7455C4);
    private static final Color COLOR_PURPLE_LIGHT = new Color(0xEFEAFB);
    private static final Color COLOR_GREEN = new Color(0x15803D);
    private static final Color COLOR_GREEN_LIGHT = new Color(0xE3F6EA);
    private static final Color COLOR_RED = new Color(0xB91C1C);
    private static final Color COLOR_RED_LIGHT = new Color(0xFDE8E8);

    private static final long PRODUCT_DISCOUNT = 20000;
    private static final String[] STAFF = {"Trần Minh Anh", "Nguyễn Hoài Nam", "Lê Thu Trang"};

    private static final ServiceLine[] SERVICE_CATALOG = {
        new ServiceLine("Tắm vệ sinh", 45, "", 150000),
        new ServiceLine("Cắt tỉa lông", 60, "", 200000),
        new ServiceLine("Cắt móng", 15, "", 50000),
        new ServiceLine("Vệ sinh tai", 15, "", 60000),
        new ServiceLine("Spa dưỡng lông", 60, "", 250000)
    };

    private static final Product[] PRODUCT_CATALOG = {
        new Product("SP001", "Hạt Ganador Adult vị gà quay", 20000, 42),
        new Product("SP002", "Hạt cho mèo mọi lứa tuổi Catsrang", 60000, 18),
        new Product("SP003", "Pate Whiskas vị cá thu", 25000, 30),
        new Product("SP004", "Cát vệ sinh cho mèo 5L", 85000, 12)
    };

    private static final DecimalFormat MONEY_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('.');
        MONEY_FORMAT = new DecimalFormat("#,##0", symbols);
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final List<CustomerPanel> customerPanels = new ArrayList<>();
    private final Map<String, Customer> customers = new HashMap<>();
    private final ModeTab productTab;
    private final ModeTab serviceTab;
    private final ProductPage productPage;
    private final ServicePage servicePage;
    private Customer currentCustomer;

    public Test_InvoicePanel() {
        super(new BorderLayout(0, 16));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(24, 24, 24, 24));
        initSampleCustomers();

        productTab = new ModeTab(IconType.BAG, "Hóa đơn sản phẩm",
            "Bán hàng, kiểm tra và cập nhật tồn kho", COLOR_ORANGE, COLOR_ORANGE_LIGHT,
            () -> switchMode(CARD_PRODUCT));
        serviceTab = new ModeTab(IconType.BRUSH, "Hóa đơn dịch vụ",
            "Từ lịch hẹn, thú cưng và dịch vụ đã chọn", COLOR_PURPLE, COLOR_PURPLE_LIGHT,
            () -> switchMode(CARD_SERVICE));

        productPage = new ProductPage();
        servicePage = new ServicePage();

        cardPanel.setOpaque(false);
        cardPanel.add(productPage, CARD_PRODUCT);
        cardPanel.add(servicePage, CARD_SERVICE);

        add(buildModeBar(), BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        switchMode(CARD_PRODUCT);
        searchCustomer("090 123 4567");
    }

    // ------------------------------------------------------------------
    // Logic chung
    // ------------------------------------------------------------------

    private void initSampleCustomers() {
        customers.put("0901234567", new Customer("Nguyễn Văn An", "090 123 4567", 1000));
        customers.put("0912345678", new Customer("Trần Thu Hà", "091 234 5678", 250));
    }

    private JPanel buildModeBar() {
        RoundedPanel bar = new RoundedPanel(new GridLayout(1, 2, 8, 0), Color.WHITE, COLOR_BORDER, 18);
        bar.setBorder(new EmptyBorder(6, 6, 6, 6));
        bar.add(productTab);
        bar.add(serviceTab);
        return bar;
    }

    private void switchMode(String card) {
        cardLayout.show(cardPanel, card);
        productTab.setSelected(CARD_PRODUCT.equals(card));
        serviceTab.setSelected(CARD_SERVICE.equals(card));
    }

    private CustomerPanel createCustomerPanel() {
        CustomerPanel panel = new CustomerPanel(this::searchCustomer);
        customerPanels.add(panel);
        return panel;
    }

    private void searchCustomer(String phoneText) {
        currentCustomer = customers.get(phoneText.replaceAll("\\D", ""));
        for (CustomerPanel panel : customerPanels) {
            panel.showCustomer(currentCustomer, phoneText);
        }
        productPage.refreshSummary();
        servicePage.refreshSummary();
    }

    private void confirmInvoice(String type, PaymentPanel payment, int itemCount) {
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng tìm khách hàng trước khi tạo hóa đơn.",
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (itemCount == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn chưa có " + type + " nào.",
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String message = "Đã tạo hóa đơn " + type
            + "\nKhách hàng: " + currentCustomer.name
            + "\nTổng thanh toán: " + formatMoney(payment.getTotal())
            + "\nPhương thức: " + (payment.isCash() ? "Tiền mặt" : "Chuyển khoản");
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String formatMoney(long amount) {
        return MONEY_FORMAT.format(amount) + "đ";
    }

    private static Font font(int style, float size) {
        return new Font("Segoe UI", style, 13).deriveFont(size);
    }

    private static JLabel createLabel(String text, int style, float size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font(style, size));
        label.setForeground(color);
        return label;
    }

    private static JPanel titleBlock(JLabel title, JLabel subtitle) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 2));
        panel.setOpaque(false);
        panel.add(title);
        panel.add(subtitle);
        return panel;
    }

    private static JPanel wrap(Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel centered(Component component) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    private static JPanel stack(int gap, Component... components) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < components.length; i++) {
            Component c = components[i] instanceof JPanel ? components[i] : wrap(components[i]);
            panel.add(c);
            if (i < components.length - 1 && gap > 0) {
                panel.add(Box.createVerticalStrut(gap));
            }
        }
        return panel;
    }

    private static JPanel divider() {
        JPanel line = new JPanel();
        line.setBackground(COLOR_BORDER);
        line.setPreferredSize(new Dimension(1, 1));
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return line;
    }

    private static JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private static JButton createIconButton(IconType type, Color color) {
        JButton button = new JButton(new GlyphIcon(type, 16, color));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(4, 12, 4, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ------------------------------------------------------------------
    // Trang Hóa đơn sản phẩm
    // ------------------------------------------------------------------

    private final class ProductPage extends JPanel {

        private final List<CartItem> items = new ArrayList<>();
        private final JPanel itemRows = new JPanel();
        private final JLabel itemCountLabel = createLabel("", Font.PLAIN, 12, COLOR_MUTED);
        private final SearchField productSearch =
            new SearchField("Quét mã hoặc tìm tên sản phẩm...", "");
        private final PaymentPanel payment;

        ProductPage() {
            super(new BorderLayout(16, 0));
            setOpaque(false);

            payment = new PaymentPanel("HĐ mới · Sản phẩm", "Tổng số lượng", IconType.CART,
                COLOR_ORANGE_LIGHT, COLOR_ORANGE, this::refreshSummary);
            payment.addConfirmListener(e -> confirmInvoice("sản phẩm", payment, items.size()));

            items.add(new CartItem(PRODUCT_CATALOG[0], 1));
            items.add(new CartItem(PRODUCT_CATALOG[1], 2));

            itemRows.setOpaque(false);
            itemRows.setLayout(new BoxLayout(itemRows, BoxLayout.Y_AXIS));
            productSearch.getField().addActionListener(e -> addProduct(productSearch.getField().getText()));

            ScrollablePanel holder = new ScrollablePanel(new BorderLayout());
            holder.setOpaque(false);
            holder.add(stack(16, createCustomerPanel(), buildProductPanel()), BorderLayout.NORTH);

            JPanel side = new JPanel(new BorderLayout());
            side.setOpaque(false);
            side.add(payment, BorderLayout.NORTH);

            add(createScrollPane(holder), BorderLayout.CENTER);
            add(side, BorderLayout.EAST);
            refreshItems();
        }

        private JPanel buildProductPanel() {
            RoundedPanel panel = new RoundedPanel(new BorderLayout(), Color.WHITE, COLOR_BORDER, 18);

            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.add(titleBlock(createLabel("Sản phẩm trong hóa đơn", Font.BOLD, 15, COLOR_TEXT),
                itemCountLabel), BorderLayout.WEST);
            header.add(new Badge("Còn hàng", COLOR_ORANGE_LIGHT, COLOR_ORANGE), BorderLayout.EAST);

            JPanel top = new JPanel(new BorderLayout(0, 12));
            top.setOpaque(false);
            top.setBorder(new EmptyBorder(16, 16, 16, 16));
            top.add(header, BorderLayout.NORTH);
            top.add(productSearch, BorderLayout.CENTER);

            panel.add(top, BorderLayout.NORTH);
            panel.add(itemRows, BorderLayout.CENTER);
            return panel;
        }

        private void addProduct(String keyword) {
            String key = keyword.trim().toLowerCase();
            if (key.isEmpty()) {
                return;
            }
            for (Product product : PRODUCT_CATALOG) {
                if (product.code.toLowerCase().equals(key) || product.name.toLowerCase().contains(key)) {
                    CartItem existing = findItem(product);
                    if (existing == null) {
                        items.add(new CartItem(product, 1));
                    } else if (existing.quantity < product.stock) {
                        existing.quantity++;
                    }
                    productSearch.getField().setText("");
                    refreshItems();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm.", "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }

        private CartItem findItem(Product product) {
            for (CartItem item : items) {
                if (item.product == product) {
                    return item;
                }
            }
            return null;
        }

        private void refreshItems() {
            itemRows.removeAll();
            for (CartItem item : items) {
                itemRows.add(buildItemRow(item));
            }
            itemCountLabel.setText(items.size() + " mặt hàng đã chọn");
            itemRows.revalidate();
            itemRows.repaint();
            refreshSummary();
        }

        private JPanel buildItemRow(CartItem item) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, COLOR_BORDER),
                new EmptyBorder(12, 16, 12, 16)));

            IconBadge icon = new IconBadge(IconType.BOX, 36, COLOR_ORANGE, COLOR_ORANGE_LIGHT);
            icon.setBorder(new EmptyBorder(0, 0, 0, 12));
            JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
            text.setOpaque(false);
            text.add(createLabel(item.product.name, Font.PLAIN, 13, COLOR_TEXT));
            text.add(createLabel(item.product.code + " · Còn " + item.product.stock + " sản phẩm",
                Font.PLAIN, 11, COLOR_MUTED));
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            left.setOpaque(false);
            left.add(icon);
            left.add(text);

            RoundedButton minus = createQuantityButton("−");
            RoundedButton plus = createQuantityButton("+");
            JLabel quantity = createLabel(String.valueOf(item.quantity), Font.BOLD, 13, COLOR_TEXT);
            quantity.setHorizontalAlignment(SwingConstants.CENTER);
            quantity.setPreferredSize(new Dimension(26, 20));
            minus.addActionListener(e -> {
                if (item.quantity > 1) {
                    item.quantity--;
                    refreshItems();
                }
            });
            plus.addActionListener(e -> {
                if (item.quantity < item.product.stock) {
                    item.quantity++;
                    refreshItems();
                } else {
                    JOptionPane.showMessageDialog(this, "Vượt quá số lượng tồn kho.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            JPanel stepper = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            stepper.setOpaque(false);
            stepper.add(minus);
            stepper.add(quantity);
            stepper.add(plus);

            JLabel total = createLabel(formatMoney(item.product.price * item.quantity), Font.BOLD, 13, COLOR_TEXT);
            JLabel unit = createLabel(formatMoney(item.product.price) + " / SP", Font.PLAIN, 11, COLOR_MUTED);
            total.setHorizontalAlignment(SwingConstants.RIGHT);
            unit.setHorizontalAlignment(SwingConstants.RIGHT);
            JPanel price = new JPanel(new GridLayout(2, 1, 0, 2));
            price.setOpaque(false);
            price.setBorder(new EmptyBorder(0, 24, 0, 0));
            price.setPreferredSize(new Dimension(120, 34));
            price.add(total);
            price.add(unit);

            JButton delete = createIconButton(IconType.TRASH, COLOR_MUTED);
            delete.addActionListener(e -> {
                items.remove(item);
                refreshItems();
            });

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(stepper);
            right.add(price);
            right.add(delete);

            row.add(left, BorderLayout.WEST);
            row.add(right, BorderLayout.EAST);
            return row;
        }

        void refreshSummary() {
            int totalQuantity = 0;
            long subtotal = 0;
            for (CartItem item : items) {
                totalQuantity += item.quantity;
                subtotal += item.product.price * item.quantity;
            }
            long discount = items.isEmpty() ? 0 : Math.min(PRODUCT_DISCOUNT, subtotal);
            payment.update(totalQuantity, subtotal, discount, currentCustomer);
        }
    }

    private static RoundedButton createQuantityButton(String text) {
        RoundedButton button = new RoundedButton(text, Color.WHITE, COLOR_BORDER, COLOR_TEXT, 8);
        button.setPadding(2, 9);
        return button;
    }

    // ------------------------------------------------------------------
    // Trang Hóa đơn dịch vụ
    // ------------------------------------------------------------------

    private final class ServicePage extends JPanel {

        private final List<Appointment> appointments = createSampleAppointments();
        private final List<AppointmentCard> cards = new ArrayList<>();
        private final JPanel serviceRows = new JPanel();
        private final JLabel serviceTitle = createLabel("", Font.BOLD, 15, COLOR_TEXT);
        private final JLabel serviceSubtitle = createLabel("", Font.PLAIN, 12, COLOR_MUTED);
        private final PaymentPanel payment;
        private Appointment selected;

        ServicePage() {
            super(new BorderLayout(16, 0));
            setOpaque(false);

            payment = new PaymentPanel("HĐ mới · Dịch vụ", "Số dịch vụ", IconType.BRUSH,
                COLOR_PURPLE_LIGHT, COLOR_PURPLE, this::refreshSummary);
            payment.addConfirmListener(e -> confirmInvoice("dịch vụ", payment, selected.services.size()));

            serviceRows.setOpaque(false);
            serviceRows.setLayout(new BoxLayout(serviceRows, BoxLayout.Y_AXIS));

            ScrollablePanel holder = new ScrollablePanel(new BorderLayout());
            holder.setOpaque(false);
            holder.add(stack(16, createCustomerPanel(), buildAppointmentPanel(), buildServicePanel()),
                BorderLayout.NORTH);

            JPanel side = new JPanel(new BorderLayout());
            side.setOpaque(false);
            side.add(payment, BorderLayout.NORTH);

            add(createScrollPane(holder), BorderLayout.CENTER);
            add(side, BorderLayout.EAST);
            selectAppointment(0);
        }

        private JPanel buildAppointmentPanel() {
            RoundedPanel panel = new RoundedPanel(new BorderLayout(0, 12), Color.WHITE, COLOR_BORDER, 18);
            panel.setBorder(new EmptyBorder(16, 16, 16, 16));
            panel.add(titleBlock(createLabel("Chọn lịch hẹn", Font.BOLD, 15, COLOR_TEXT),
                createLabel("Các lịch hẹn đang chờ của khách hàng này", Font.PLAIN, 12, COLOR_MUTED)),
                BorderLayout.NORTH);

            JPanel grid = new JPanel(new GridLayout(1, appointments.size(), 12, 0));
            grid.setOpaque(false);
            for (int i = 0; i < appointments.size(); i++) {
                final int index = i;
                AppointmentCard card = new AppointmentCard(appointments.get(i));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectAppointment(index);
                    }
                });
                cards.add(card);
                grid.add(card);
            }
            panel.add(grid, BorderLayout.CENTER);
            return panel;
        }

        private JPanel buildServicePanel() {
            RoundedPanel panel = new RoundedPanel(new BorderLayout(), Color.WHITE, COLOR_BORDER, 18);

            RoundedButton addButton = new RoundedButton("Thêm dịch vụ", Color.WHITE, COLOR_BORDER, COLOR_TEXT, 12);
            addButton.setIcon(new GlyphIcon(IconType.PLUS, 14, COLOR_TEXT));
            addButton.setIconTextGap(8);
            addButton.addActionListener(e -> showAddServiceDialog());

            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(new EmptyBorder(16, 16, 14, 16));
            header.add(titleBlock(serviceTitle, serviceSubtitle), BorderLayout.WEST);
            header.add(centered(addButton), BorderLayout.EAST);

            panel.add(header, BorderLayout.NORTH);
            panel.add(serviceRows, BorderLayout.CENTER);
            return panel;
        }

        private void selectAppointment(int index) {
            selected = appointments.get(index);
            for (int i = 0; i < cards.size(); i++) {
                cards.get(i).setSelected(i == index);
            }
            serviceTitle.setText("Dịch vụ của " + selected.petName);
            serviceSubtitle.setText(selected.petInfo + " · Lịch hẹn " + selected.code);
            refreshServices();
        }

        private void refreshServices() {
            serviceRows.removeAll();
            for (ServiceLine line : selected.services) {
                serviceRows.add(buildServiceRow(line));
            }
            for (AppointmentCard card : cards) {
                card.refresh();
            }
            serviceRows.revalidate();
            serviceRows.repaint();
            refreshSummary();
        }

        private JPanel buildServiceRow(ServiceLine line) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, COLOR_BORDER),
                new EmptyBorder(12, 16, 12, 16)));

            IconBadge icon = new IconBadge(IconType.BRUSH, 36, COLOR_PURPLE, COLOR_PURPLE_LIGHT);
            icon.setBorder(new EmptyBorder(0, 0, 0, 12));
            JLabel duration = createLabel(line.minutes + " phút", Font.PLAIN, 11, COLOR_MUTED);
            duration.setIcon(new GlyphIcon(IconType.CLOCK, 12, COLOR_MUTED));
            duration.setIconTextGap(4);
            JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
            text.setOpaque(false);
            text.add(createLabel(line.name, Font.PLAIN, 13, COLOR_TEXT));
            text.add(duration);
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            left.setOpaque(false);
            left.add(icon);
            left.add(text);

            JPanel staff = new JPanel(new GridLayout(2, 1, 0, 2));
            staff.setOpaque(false);
            staff.setBorder(new EmptyBorder(0, 0, 0, 24));
            staff.add(createLabel("Nhân viên thực hiện", Font.PLAIN, 11, COLOR_MUTED));
            staff.add(createLabel(line.staff, Font.BOLD, 13, COLOR_TEXT));

            JLabel price = createLabel(formatMoney(line.price), Font.BOLD, 13, COLOR_TEXT);
            price.setHorizontalAlignment(SwingConstants.RIGHT);
            price.setPreferredSize(new Dimension(80, 20));

            JButton delete = createIconButton(IconType.TRASH, COLOR_MUTED);
            delete.addActionListener(e -> {
                selected.services.remove(line);
                refreshServices();
            });

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            right.setOpaque(false);
            right.add(staff);
            right.add(price);
            right.add(delete);

            row.add(left, BorderLayout.WEST);
            row.add(right, BorderLayout.EAST);
            return row;
        }

        private void showAddServiceDialog() {
            JComboBox<ServiceLine> serviceBox = new JComboBox<>(SERVICE_CATALOG);
            JComboBox<String> staffBox = new JComboBox<>(STAFF);
            JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
            form.add(new JLabel("Dịch vụ"));
            form.add(serviceBox);
            form.add(new JLabel("Nhân viên thực hiện"));
            form.add(staffBox);
            int result = JOptionPane.showConfirmDialog(this, form, "Thêm dịch vụ",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                ServiceLine template = (ServiceLine) serviceBox.getSelectedItem();
                selected.services.add(new ServiceLine(template.name, template.minutes,
                    (String) staffBox.getSelectedItem(), template.price));
                refreshServices();
            }
        }

        void refreshSummary() {
            if (selected == null) {
                return;
            }
            long subtotal = 0;
            for (ServiceLine line : selected.services) {
                subtotal += line.price;
            }
            long discount = selected.services.isEmpty() ? 0 : Math.min(selected.discount, subtotal);
            payment.update(selected.services.size(), subtotal, discount, currentCustomer);
        }
    }

    private static List<Appointment> createSampleAppointments() {
        Appointment milo = new Appointment("#LH0245", "09:30, hôm nay", "Milo", "Poodle · 6kg", 30000);
        milo.services.add(new ServiceLine("Tắm vệ sinh", 45, "Trần Minh Anh", 150000));
        milo.services.add(new ServiceLine("Cắt tỉa lông", 60, "Nguyễn Hoài Nam", 200000));
        Appointment miu = new Appointment("#LH0248", "14:00, hôm nay", "Miu", "Mèo Anh lông ngắn · 4kg", 15000);
        miu.services.add(new ServiceLine("Tắm vệ sinh", 45, "Lê Thu Trang", 150000));
        List<Appointment> list = new ArrayList<>();
        list.add(milo);
        list.add(miu);
        return list;
    }

    // ------------------------------------------------------------------
    // Dữ liệu
    // ------------------------------------------------------------------

    private static class Customer {
        final String name;
        final String phone;
        final long points;

        Customer(String name, String phone, long points) {
            this.name = name;
            this.phone = phone;
            this.points = points;
        }

        String initials() {
            String[] parts = name.trim().split("\\s+");
            String first = parts[0].substring(0, 1);
            String last = parts[parts.length - 1].substring(0, 1);
            return (first + last).toUpperCase();
        }
    }

    private static class Product {
        final String code;
        final String name;
        final long price;
        final int stock;

        Product(String code, String name, long price, int stock) {
            this.code = code;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }

    private static class CartItem {
        final Product product;
        int quantity;

        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    private static class ServiceLine {
        final String name;
        final int minutes;
        final String staff;
        final long price;

        ServiceLine(String name, int minutes, String staff, long price) {
            this.name = name;
            this.minutes = minutes;
            this.staff = staff;
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " (" + formatMoney(price) + ")";
        }
    }

    private static class Appointment {
        final String code;
        final String time;
        final String petName;
        final String petInfo;
        final long discount;
        final List<ServiceLine> services = new ArrayList<>();

        Appointment(String code, String time, String petName, String petInfo, long discount) {
            this.code = code;
            this.time = time;
            this.petName = petName;
            this.petInfo = petInfo;
            this.discount = discount;
        }
    }

    // ------------------------------------------------------------------
    // Thành phần giao diện dùng chung
    // ------------------------------------------------------------------

    private enum IconType { BAG, BRUSH, CART, BOX, TRASH, CLOCK, SEARCH, PLUS, RECEIPT }

    /** Panel bo góc có viền. */
    private static class RoundedPanel extends JPanel {
        private final int arc;
        private Color borderColor;
        private float borderWidth = 1f;

        RoundedPanel(LayoutManager layout, Color background, Color borderColor, int arc) {
            super(layout);
            this.arc = arc;
            this.borderColor = borderColor;
            setOpaque(false);
            setBackground(background);
        }

        void setBorderColor(Color color, float width) {
            this.borderColor = color;
            this.borderWidth = width;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape shape = new RoundRectangle2D.Float(borderWidth / 2, borderWidth / 2,
                getWidth() - borderWidth, getHeight() - borderWidth, arc, arc);
            g2.setColor(getBackground());
            g2.fill(shape);
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth));
                g2.draw(shape);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Nút bo góc. */
    private static class RoundedButton extends JButton {
        private final int arc;
        private Color fill;
        private Color line;

        RoundedButton(String text, Color fill, Color line, Color foreground, int arc) {
            super(text);
            this.fill = fill;
            this.line = line;
            this.arc = arc;
            setForeground(foreground);
            setFont(font(Font.BOLD, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPadding(9, 16);
        }

        void setPadding(int vertical, int horizontal) {
            setBorder(new EmptyBorder(vertical, horizontal, vertical, horizontal));
        }

        void setColors(Color fill, Color line, Color foreground) {
            this.fill = fill;
            this.line = line;
            setForeground(foreground);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.setColor(fill);
            g2.fill(shape);
            if (line != null) {
                g2.setColor(line);
                g2.draw(shape);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Nhãn dạng viên thuốc (badge). */
    private static class Badge extends RoundedPanel {
        private final JLabel label = createLabel("", Font.BOLD, 11, COLOR_TEXT);

        Badge(String text, Color background, Color foreground) {
            super(new FlowLayout(FlowLayout.CENTER, 10, 3), background, null, 20);
            add(label);
            set(text, background, foreground);
        }

        void set(String text, Color background, Color foreground) {
            label.setText(text);
            label.setForeground(foreground);
            setBackground(background);
            repaint();
        }
    }

    /** Ô nhập có placeholder. */
    private static class HintTextField extends JTextField {
        private final String hint;

        HintTextField(String hint, String text) {
            super(text);
            this.hint = hint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(0x9CA3AF));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(hint, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    /** Ô tìm kiếm có biểu tượng kính lúp. */
    private static class SearchField extends RoundedPanel {
        private final HintTextField field;

        SearchField(String hint, String text) {
            super(new BorderLayout(8, 0), Color.WHITE, COLOR_BORDER, 12);
            setBorder(new EmptyBorder(8, 12, 8, 12));
            field = new HintTextField(hint, text);
            field.setBorder(null);
            field.setOpaque(false);
            field.setFont(font(Font.PLAIN, 13));
            field.setForeground(COLOR_TEXT);
            add(centered(new IconBadge(IconType.SEARCH, 16, COLOR_MUTED, null)), BorderLayout.WEST);
            add(field, BorderLayout.CENTER);
        }

        JTextField getField() {
            return field;
        }
    }

    /** Biểu tượng vẽ bằng Java2D. */
    private static class GlyphIcon implements Icon {
        private final IconType type;
        private final int size;
        private final Color color;

        GlyphIcon(IconType type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.translate(x, y);
            g2.scale(size / 16.0, size / 16.0);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            draw(g2);
            g2.dispose();
        }

        private void draw(Graphics2D g2) {
            switch (type) {
                case BAG:
                    g2.draw(new RoundRectangle2D.Double(2.5, 5.5, 11, 9, 2, 2));
                    g2.draw(new Arc2D.Double(5, 1.5, 6, 7, 0, 180, Arc2D.OPEN));
                    break;
                case BRUSH:
                    g2.draw(new Line2D.Double(2.5, 13.5, 9.5, 6.5));
                    g2.draw(new Line2D.Double(12, 2, 12, 6));
                    g2.draw(new Line2D.Double(10, 4, 14, 4));
                    g2.draw(new Line2D.Double(13, 9, 13, 11));
                    break;
                case CART:
                    Path2D cart = new Path2D.Double();
                    cart.moveTo(1, 2.5);
                    cart.lineTo(3, 2.5);
                    cart.lineTo(4.5, 10.5);
                    cart.lineTo(12, 10.5);
                    cart.lineTo(13.5, 4.5);
                    cart.lineTo(3.4, 4.5);
                    g2.draw(cart);
                    g2.draw(new Ellipse2D.Double(5.2, 12.2, 1.6, 1.6));
                    g2.draw(new Ellipse2D.Double(10.2, 12.2, 1.6, 1.6));
                    break;
                case BOX:
                    Path2D box = new Path2D.Double();
                    box.moveTo(8, 1.5);
                    box.lineTo(14, 4.8);
                    box.lineTo(14, 11.2);
                    box.lineTo(8, 14.5);
                    box.lineTo(2, 11.2);
                    box.lineTo(2, 4.8);
                    box.closePath();
                    g2.draw(box);
                    Path2D inner = new Path2D.Double();
                    inner.moveTo(2, 4.8);
                    inner.lineTo(8, 8.2);
                    inner.lineTo(14, 4.8);
                    inner.moveTo(8, 8.2);
                    inner.lineTo(8, 14.5);
                    g2.draw(inner);
                    break;
                case TRASH:
                    g2.draw(new Line2D.Double(2.5, 4, 13.5, 4));
                    g2.draw(new Line2D.Double(6, 2.5, 10, 2.5));
                    g2.draw(new RoundRectangle2D.Double(4, 5.5, 8, 8.5, 2, 2));
                    g2.draw(new Line2D.Double(6.8, 7.5, 6.8, 12));
                    g2.draw(new Line2D.Double(9.2, 7.5, 9.2, 12));
                    break;
                case CLOCK:
                    g2.draw(new Ellipse2D.Double(2, 2, 12, 12));
                    g2.draw(new Line2D.Double(8, 4.8, 8, 8));
                    g2.draw(new Line2D.Double(8, 8, 10.2, 9.3));
                    break;
                case SEARCH:
                    g2.draw(new Ellipse2D.Double(2, 2, 9, 9));
                    g2.draw(new Line2D.Double(9.5, 9.5, 13.5, 13.5));
                    break;
                case PLUS:
                    g2.draw(new Line2D.Double(8, 3, 8, 13));
                    g2.draw(new Line2D.Double(3, 8, 13, 8));
                    break;
                case RECEIPT:
                    g2.draw(new RoundRectangle2D.Double(3, 1.5, 10, 13, 2, 2));
                    g2.draw(new Line2D.Double(5.5, 5.5, 10.5, 5.5));
                    g2.draw(new Line2D.Double(5.5, 8, 10.5, 8));
                    g2.draw(new Line2D.Double(5.5, 10.5, 8.5, 10.5));
                    break;
                default:
                    break;
            }
        }
    }

    /** Ô vuông bo góc chứa biểu tượng (background = null: chỉ vẽ biểu tượng). */
    private static class IconBadge extends JComponent {
        private final IconType type;
        private final int size;
        private Color foreground;
        private Color background;

        IconBadge(IconType type, int size, Color foreground, Color background) {
            this.type = type;
            this.size = size;
            this.foreground = foreground;
            this.background = background;
        }

        void setColors(Color foreground, Color background) {
            this.foreground = foreground;
            this.background = background;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            Insets in = getInsets();
            return new Dimension(size + in.left + in.right, size + in.top + in.bottom);
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Insets in = getInsets();
            if (background != null) {
                g2.setColor(background);
                g2.fillRoundRect(in.left, in.top, size, size, size / 3, size / 3);
            }
            int glyph = background == null ? size : (int) (size * 0.5);
            new GlyphIcon(type, glyph, foreground).paintIcon(this, g2,
                in.left + (size - glyph) / 2, in.top + (size - glyph) / 2);
            g2.dispose();
        }
    }

    /** Avatar tròn hiển thị chữ cái đầu. */
    private static class AvatarBadge extends JComponent {
        private String text = "";

        void setText(String text) {
            this.text = text;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(new Color(0xFDE7DD));
            g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.setColor(COLOR_ORANGE);
            g2.setFont(font(Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2,
                (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }

    /** Panel cuộn theo chiều dọc, luôn vừa chiều ngang viewport. */
    private static class ScrollablePanel extends JPanel implements Scrollable {

        ScrollablePanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
            return 64;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    /** Thẻ chọn chế độ ở thanh trên cùng. */
    private static class ModeTab extends RoundedPanel {
        private final IconBadge icon;
        private final JLabel title;
        private final JLabel subtitle;
        private final Badge badge = new Badge("Đang chọn", COLOR_ORANGE, Color.WHITE);
        private final Color accent;
        private final Color accentLight;

        ModeTab(IconType type, String titleText, String subtitleText,
                Color accent, Color accentLight, Runnable onClick) {
            super(new BorderLayout(12, 0), Color.WHITE, COLOR_BORDER, 14);
            this.accent = accent;
            this.accentLight = accentLight;
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            icon = new IconBadge(type, 40, accent, accentLight);
            title = createLabel(titleText, Font.BOLD, 14, COLOR_TEXT);
            subtitle = createLabel(subtitleText, Font.PLAIN, 11, COLOR_MUTED);
            JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
            text.setOpaque(false);
            text.add(title);
            text.add(subtitle);

            add(centered(icon), BorderLayout.WEST);
            add(text, BorderLayout.CENTER);
            add(centered(badge), BorderLayout.EAST);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }
            });
            setSelected(false);
        }

        void setSelected(boolean selected) {
            setBackground(selected ? accent : Color.WHITE);
            setBorderColor(selected ? accent : COLOR_BORDER, 1f);
            title.setForeground(selected ? Color.WHITE : COLOR_TEXT);
            subtitle.setForeground(selected ? new Color(255, 255, 255, 215) : COLOR_MUTED);
            icon.setColors(selected ? Color.WHITE : accent,
                selected ? new Color(255, 255, 255, 55) : accentLight);
            badge.set("Đang chọn", new Color(255, 255, 255, 70), Color.WHITE);
            badge.setVisible(selected);
        }
    }

    /** Thẻ lịch hẹn. */
    private static class AppointmentCard extends RoundedPanel {
        private final Appointment appointment;
        private final JLabel summaryLabel = createLabel("", Font.BOLD, 11, COLOR_TEXT);

        AppointmentCard(Appointment appointment) {
            super(new BorderLayout(0, 8), Color.WHITE, COLOR_BORDER, 14);
            this.appointment = appointment;
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JLabel time = createLabel(appointment.time, Font.PLAIN, 12, COLOR_MUTED);
            time.setIcon(new GlyphIcon(IconType.CLOCK, 13, COLOR_MUTED));
            time.setIconTextGap(5);
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(createLabel(appointment.code, Font.BOLD, 12, COLOR_PURPLE), BorderLayout.WEST);
            top.add(time, BorderLayout.EAST);

            JPanel info = new JPanel(new GridLayout(3, 1, 0, 3));
            info.setOpaque(false);
            info.add(createLabel(appointment.petName, Font.BOLD, 15, COLOR_TEXT));
            info.add(createLabel(appointment.petInfo, Font.PLAIN, 11, COLOR_MUTED));
            info.add(summaryLabel);

            add(top, BorderLayout.NORTH);
            add(info, BorderLayout.CENTER);
            refresh();
        }

        void refresh() {
            StringBuilder builder = new StringBuilder();
            for (ServiceLine line : appointment.services) {
                if (builder.length() > 0) {
                    builder.append(" · ");
                }
                builder.append(line.name);
            }
            summaryLabel.setText(builder.length() == 0 ? " " : builder.toString());
        }

        void setSelected(boolean selected) {
            setBackground(selected ? new Color(0xEEE9FB) : Color.WHITE);
            setBorderColor(selected ? COLOR_PURPLE : COLOR_BORDER, selected ? 1.5f : 1f);
        }
    }

    /** Khối thông tin khách hàng (dùng ở cả hai chế độ). */
    private static class CustomerPanel extends RoundedPanel {
        private final SearchField phoneField;
        private final Badge statusBadge = new Badge("Đã tìm thấy", COLOR_GREEN_LIGHT, COLOR_GREEN);
        private final AvatarBadge avatar = new AvatarBadge();
        private final JLabel nameValue = createLabel("", Font.BOLD, 13, COLOR_TEXT);
        private final JLabel phoneValue = createLabel("", Font.BOLD, 13, COLOR_TEXT);
        private final JLabel pointsValue = createLabel("", Font.BOLD, 13, COLOR_ORANGE);

        CustomerPanel(Consumer<String> onSearch) {
            super(new BorderLayout(0, 14), Color.WHITE, COLOR_BORDER, 18);
            setBorder(new EmptyBorder(16, 16, 16, 16));
            phoneField = new SearchField("Nhập số điện thoại", "");

            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.add(titleBlock(createLabel("Thông tin khách hàng", Font.BOLD, 15, COLOR_TEXT),
                createLabel("Tìm theo số điện thoại để áp dụng điểm và lịch sử giao dịch",
                    Font.PLAIN, 12, COLOR_MUTED)), BorderLayout.WEST);
            header.add(statusBadge, BorderLayout.EAST);

            RoundedButton searchButton = new RoundedButton("Tìm khách hàng", Color.WHITE,
                COLOR_BORDER, COLOR_TEXT, 12);
            searchButton.setFont(font(Font.PLAIN, 13));
            searchButton.addActionListener(e -> onSearch.accept(phoneField.getField().getText()));
            phoneField.getField().addActionListener(e -> onSearch.accept(phoneField.getField().getText()));
            JPanel searchRow = new JPanel(new BorderLayout(10, 0));
            searchRow.setOpaque(false);
            searchRow.add(phoneField, BorderLayout.CENTER);
            searchRow.add(searchButton, BorderLayout.EAST);

            RoundedPanel infoBox = new RoundedPanel(new FlowLayout(FlowLayout.LEFT, 24, 10),
                COLOR_SOFT, null, 14);
            infoBox.add(avatar);
            infoBox.add(fieldBlock("Khách hàng", nameValue));
            infoBox.add(fieldBlock("Số điện thoại", phoneValue));
            infoBox.add(fieldBlock("Điểm tích lũy", pointsValue));

            JPanel center = new JPanel(new BorderLayout(0, 12));
            center.setOpaque(false);
            center.add(searchRow, BorderLayout.NORTH);
            center.add(infoBox, BorderLayout.CENTER);

            add(header, BorderLayout.NORTH);
            add(center, BorderLayout.CENTER);
        }

        private JPanel fieldBlock(String caption, JLabel value) {
            JPanel panel = new JPanel(new GridLayout(2, 1, 0, 0));
            panel.setOpaque(false);
            panel.add(createLabel(caption, Font.PLAIN, 11, COLOR_MUTED));
            panel.add(value);
            return panel;
        }

        void showCustomer(Customer customer, String phoneText) {
            phoneField.getField().setText(phoneText);
            if (customer == null) {
                statusBadge.set("Không tìm thấy", COLOR_RED_LIGHT, COLOR_RED);
                avatar.setText("?");
                nameValue.setText("—");
                phoneValue.setText("—");
                pointsValue.setText("—");
            } else {
                statusBadge.set("Đã tìm thấy", COLOR_GREEN_LIGHT, COLOR_GREEN);
                avatar.setText(customer.initials());
                nameValue.setText(customer.name);
                phoneValue.setText(customer.phone);
                pointsValue.setText(MONEY_FORMAT.format(customer.points) + " điểm");
            }
        }
    }

    /** Khối chi tiết thanh toán bên phải. */
    private static class PaymentPanel extends RoundedPanel {
        private final JLabel countValue = createLabel("0", Font.BOLD, 13, COLOR_TEXT);
        private final JLabel subtotalValue = createLabel("0đ", Font.BOLD, 13, COLOR_TEXT);
        private final JLabel discountValue = createLabel("0đ", Font.BOLD, 13, COLOR_GREEN);
        private final JLabel totalValue = createLabel("0đ", Font.BOLD, 22, COLOR_TEXT);
        private final JLabel pointsHint = createLabel("", Font.PLAIN, 11, COLOR_MUTED);
        private final JCheckBox pointsCheck = new JCheckBox();
        private final RoundedButton cashButton =
            new RoundedButton("Tiền mặt", Color.WHITE, COLOR_BORDER, COLOR_TEXT, 12);
        private final RoundedButton transferButton =
            new RoundedButton("Chuyển khoản", Color.WHITE, COLOR_BORDER, COLOR_TEXT, 12);
        private final RoundedButton confirmButton =
            new RoundedButton("Xác nhận & tạo hóa đơn", COLOR_ORANGE, null, Color.WHITE, 12);
        private boolean cash = true;
        private long total;

        PaymentPanel(String subtitle, String countCaption, IconType iconType,
                     Color iconBackground, Color iconColor, Runnable onChange) {
            super(new BorderLayout(), Color.WHITE, COLOR_BORDER, 18);

            pointsCheck.setOpaque(false);
            pointsCheck.setFocusPainted(false);
            pointsCheck.addActionListener(e -> onChange.run());
            cashButton.addActionListener(e -> selectMethod(true));
            transferButton.addActionListener(e -> selectMethod(false));
            confirmButton.setIcon(new GlyphIcon(IconType.RECEIPT, 16, Color.WHITE));
            confirmButton.setIconTextGap(8);
            confirmButton.setPadding(12, 16);
            selectMethod(true);

            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(new EmptyBorder(16, 16, 16, 16));
            header.add(titleBlock(createLabel("Chi tiết thanh toán", Font.BOLD, 14, COLOR_TEXT),
                createLabel(subtitle, Font.PLAIN, 11, COLOR_MUTED)), BorderLayout.WEST);
            header.add(new IconBadge(iconType, 36, iconColor, iconBackground), BorderLayout.EAST);

            JPanel pointsBox = new RoundedPanel(new BorderLayout(), COLOR_SOFT, null, 12);
            pointsBox.setBorder(new EmptyBorder(10, 12, 10, 12));
            pointsBox.add(titleBlock(createLabel("Dùng điểm tích lũy", Font.BOLD, 12, COLOR_TEXT),
                pointsHint), BorderLayout.WEST);
            pointsBox.add(pointsCheck, BorderLayout.EAST);

            JPanel rows = stack(10, row(countCaption, countValue), row("Tạm tính", subtotalValue),
                row("Giảm giá", discountValue), pointsBox);
            rows.setBorder(new EmptyBorder(14, 16, 16, 16));

            JPanel totalRow = new JPanel(new BorderLayout());
            totalRow.setBackground(COLOR_TOTAL_BG);
            totalRow.setBorder(new EmptyBorder(18, 16, 18, 16));
            totalRow.add(createLabel("Tổng thanh toán", Font.BOLD, 13, COLOR_TEXT), BorderLayout.WEST);
            totalRow.add(totalValue, BorderLayout.EAST);

            JPanel methodGrid = new JPanel(new GridLayout(1, 2, 8, 0));
            methodGrid.setOpaque(false);
            methodGrid.add(cashButton);
            methodGrid.add(transferButton);
            JLabel hint = createLabel("Kiểm tra thông tin trước khi xác nhận", Font.PLAIN, 11, COLOR_MUTED);
            hint.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel methods = stack(10, createLabel("Phương thức thanh toán", Font.PLAIN, 11, COLOR_MUTED),
                methodGrid, confirmButton, hint);
            methods.setBorder(new EmptyBorder(14, 16, 16, 16));

            add(stack(0, header, divider(), rows, divider(), totalRow, divider(), methods),
                BorderLayout.CENTER);
        }

        private JPanel row(String caption, JLabel value) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            panel.add(createLabel(caption, Font.PLAIN, 13, COLOR_MUTED), BorderLayout.WEST);
            panel.add(value, BorderLayout.EAST);
            return panel;
        }

        private void selectMethod(boolean isCash) {
            cash = isCash;
            cashButton.setColors(isCash ? COLOR_ORANGE_LIGHT : Color.WHITE,
                isCash ? COLOR_ORANGE : COLOR_BORDER, isCash ? COLOR_ORANGE : COLOR_TEXT);
            transferButton.setColors(isCash ? Color.WHITE : COLOR_ORANGE_LIGHT,
                isCash ? COLOR_BORDER : COLOR_ORANGE, isCash ? COLOR_TEXT : COLOR_ORANGE);
        }

        void addConfirmListener(java.awt.event.ActionListener listener) {
            confirmButton.addActionListener(listener);
        }

        void update(int count, long subtotal, long discount, Customer customer) {
            long points = customer == null ? 0 : customer.points;
            pointsCheck.setEnabled(points > 0);
            if (points == 0) {
                pointsCheck.setSelected(false);
            }
            pointsHint.setText(customer == null ? "Chưa có khách hàng"
                : "Hiện có " + MONEY_FORMAT.format(points) + " điểm");

            long afterDiscount = Math.max(0, subtotal - discount);
            long usedPoints = pointsCheck.isSelected() ? Math.min(points, afterDiscount) : 0;
            total = afterDiscount - usedPoints;

            countValue.setText(String.valueOf(count));
            subtotalValue.setText(formatMoney(subtotal));
            discountValue.setText("− " + formatMoney(discount));
            totalValue.setText(formatMoney(total));
        }

        long getTotal() {
            return total;
        }

        boolean isCash() {
            return cash;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.width = 300;
            return size;
        }
    }

    // ------------------------------------------------------------------
    // Chạy thử
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lập hóa đơn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new Test_InvoicePanel());
            frame.setSize(1200, 780);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

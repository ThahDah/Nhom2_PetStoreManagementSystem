package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BanHangGUI extends JPanel implements ChangeListener {

	   private JTabbedPane switchPane;
	   private ProductPanel productPanel;
	   private ServicePanel servicePanel;
	   

	   public BanHangGUI() {
		   setLayout(new BorderLayout());
            switchPane = new JTabbedPane();
            
            productPanel = new ProductPanel();
            servicePanel = new ServicePanel();
           
            switchPane.addTab("Sản phẩm", productPanel);
            switchPane.addTab("Dịch vụ", servicePanel);
            
            add(switchPane, BorderLayout.NORTH);
            
            switchPane.addChangeListener(this);
       }

	   @Override
	   public void stateChanged(ChangeEvent e) {
		   switchPane.getTitleAt(switchPane.getSelectedIndex());
	   }
	   
	   
	   
	  // ========== TAB HÓA ĐƠN SẢN PHẨM ========== 
      private final class ProductPanel extends JPanel  {
    	  public ProductPanel() {
    		 setLayout(new BorderLayout());
    		 
    		 JPanel pnlMain = new JPanel();
    		 JPanel pnlPaymentDetail = new JPanel();
    		 add(pnlMain);
    		 add(pnlPaymentDetail, BorderLayout.EAST);
    		 
    		 // ====== PHẦN DANH SÁCH SẢN PHẨM (pnlMain) ======
    		 pnlMain.setBorder(BorderFactory.createLineBorder(new Color(220,220,220),1));
    		 
    		 
    		 // ======
    	  }
      }
      
      // ========== TAB HÓA ĐƠN DỊCH VỤ ==========
      private final class ServicePanel extends JPanel  {
		    
    	  public ServicePanel() {
		    	
		  }
		   
      }
    

}
package savasKartOyunu;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;  
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Oyun {
	private Oyuncu insan;
	private Oyuncu bilgisayar;
	private int maksimumHamleSayisi;
	private int mevcutHamle;
	private SavasAraci yeniKart;
	private boolean yeniKartSecilmeli = false;
	private JButton yeniKartButton;
	private JPanel yeniKartPanel;
	
	private JLabel hamleSayisiLabel; 
	private JFrame frame;
	private JPanel kartPanel;
	private JPanel bilgiPanel;
	private JTextArea bilgiAlani;
	private HashMap<String, ImageIcon> gorselCache;
	private ArrayList<JButton> secilenKartlar;
	private ArrayList<JButton> buHamledeSecilenKartlar;
	private JButton hamleButton;
	private JLabel hamleLabel; 
	private HashMap<String, ArrayList<SavasAraci>> gruplanmisKartlar;
	private JDialog kartSecimDialog;

    private boolean insanIcinGelistirilmisKartlarAcik = false;
    private boolean bilgisayarIcinGelistirilmisKartlarAcik = false;
    private PrintWriter logYazici;
    
    public Oyun(int maksimumHamleSayisi) {
        try {
            this.logYazici = new PrintWriter(new BufferedWriter(new FileWriter("oyun_kayit.txt", false)));
            logYazici.println("\n========== YENİ OYUN BAŞLADI ==========");
            logYazici.println("Maksimum Hamle Sayısı: " + maksimumHamleSayisi);
            logYazici.println("=======================================\n");
            logYazici.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Log dosyası oluşturulamadı!");
        }

        this.maksimumHamleSayisi = maksimumHamleSayisi;
        this.mevcutHamle = 0;
        this.gorselCache = new HashMap<>();
        this.secilenKartlar = new ArrayList<>();
        this.buHamledeSecilenKartlar = new ArrayList<>();
        this.gruplanmisKartlar = new HashMap<>();
        initGUI();
        hamleSecimGuncelle();
    }

	private void initGUI() {
	    
	    frame = new JFrame("Savaş Kart Oyunu");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(1200, 800);
	    frame.setLayout(new BorderLayout());

	   
	    kartPanel = new JPanel(new GridLayout(2, 1, 10, 10));
	    kartPanel.setBackground(new Color(53, 101, 77));
	    kartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    
	    bilgiPanel = new JPanel(new BorderLayout());
	    bilgiPanel.setBackground(new Color(53, 101, 77));
	    bilgiPanel.setPreferredSize(new Dimension(300, 800));
	    bilgiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	   
	    bilgiAlani = new JTextArea();
	    bilgiAlani.setEditable(false);
	    bilgiAlani.setLineWrap(true);
	    bilgiAlani.setWrapStyleWord(true);
	    bilgiAlani.setBackground(new Color(233, 237, 201));
	    bilgiAlani.setFont(new Font("Arial", Font.PLAIN, 14));
	    JScrollPane scrollPane = new JScrollPane(bilgiAlani);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    bilgiPanel.add(scrollPane, BorderLayout.CENTER);

	    
	    JPanel altPanel = new JPanel();
	    altPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
	    altPanel.setBackground(new Color(53, 101, 77));
	    altPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

	    hamleLabel = new JLabel("Seçilen: 0/3");
	    hamleLabel.setForeground(Color.WHITE);
	    hamleLabel.setFont(new Font("Arial", Font.BOLD, 16));

	    hamleButton = new JButton("Hamle Yap");
	    hamleButton.setFont(new Font("Arial", Font.BOLD, 14));
	    hamleButton.setBackground(new Color(70, 130, 180));
	    hamleButton.setForeground(Color.WHITE);
	    hamleButton.setFocusPainted(false);
	    hamleButton.addActionListener(e -> hamleYap());

	    altPanel.add(hamleLabel);
	    altPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
	    altPanel.add(hamleButton);

	    bilgiPanel.add(altPanel, BorderLayout.SOUTH);

	    yeniKartPanel = new JPanel();
	    yeniKartPanel.setBackground(new Color(53, 101, 77));
	    yeniKartPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
	    yeniKartPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
	    frame.add(yeniKartPanel, BorderLayout.NORTH);

	    JPanel skorPanel = new JPanel();
	    skorPanel.setBackground(new Color(53, 101, 77));
	    skorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

	    hamleSayisiLabel = new JLabel("Hamle: " + mevcutHamle + "/" + maksimumHamleSayisi);
	    hamleSayisiLabel.setForeground(Color.WHITE);
	    hamleSayisiLabel.setFont(new Font("Arial", Font.BOLD, 16));
	    skorPanel.add(hamleSayisiLabel);

	    frame.add(kartPanel, BorderLayout.CENTER);
	    frame.add(bilgiPanel, BorderLayout.EAST);
	    frame.add(skorPanel, BorderLayout.NORTH);

	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);

	    bilgiEkle("Oyun başladı! Lütfen 3 kart seçin ve 'Hamle Yap' butonuna tıklayın.");
	}

	private void hamleSecimGuncelle() {
		SwingUtilities.invokeLater(() -> {
			hamleLabel.setText("Seçilen: " + secilenKartlar.size() + "/3");
			hamleLabel.repaint();
		});
	}

	private void kartlariGrupla(Oyuncu oyuncu) {
		gruplanmisKartlar.clear();
		for (SavasAraci kart : oyuncu.getKartListesi()) {
			if (!oyuncu.getElenenKartlar().contains(kart)) {
				String kartTipi = kart.getAltSinif();
				gruplanmisKartlar.putIfAbsent(kartTipi, new ArrayList<>());
				gruplanmisKartlar.get(kartTipi).add(kart);
			}
		}
	}

	private JButton grupKartButtonOlustur(String kartTipi, ArrayList<SavasAraci> kartlar, boolean kapali) {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(100, 150));

		if (kapali) {
			button.setIcon(getKapaliKart());
		} else {
			button.setIcon(getGorsel(kartlar.get(0)));

			StringBuilder tooltip = new StringBuilder("<html>" + kartTipi + " (" + kartlar.size() + " adet)<br>");
			tooltip.append("Dayanıklılıklar: ");
			for (int i = 0; i < kartlar.size(); i++) {
			    tooltip.append(kartlar.get(i).getDayaniklilik());
			    if (i < kartlar.size() - 1) {
			        tooltip.append(", ");
			    }
			}
			tooltip.append("<br>Seviye Puanları: ");
			for (int i = 0; i < kartlar.size(); i++) {
			    tooltip.append(kartlar.get(i).getSeviyePuani());
			    if (i < kartlar.size() - 1) {
			        tooltip.append(", ");
			    }
			}
			tooltip.append("<br>Tıklayarak kartları görüntüle</html>");

			button.setToolTipText(tooltip.toString());
			button.addActionListener(e -> kartSecimDialogGoster(kartTipi, kartlar));
		}

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(new Color(0, 0, 0, 150));

		JLabel sayiLabel = new JLabel(String.valueOf(kartlar.size()));
		sayiLabel.setForeground(Color.WHITE);
		infoPanel.add(sayiLabel);

		if (!kapali) {
			int toplamDayaniklilik = kartlar.stream().mapToInt(SavasAraci::getDayaniklilik).sum();
			double ortDayaniklilik = (double) toplamDayaniklilik / kartlar.size();
			JLabel dayLabel = new JLabel(String.format("%.1f", ortDayaniklilik));
			dayLabel.setForeground(Color.YELLOW);
			infoPanel.add(dayLabel);
		}

		infoPanel.setBounds(5, 5, 30, kapali ? 20 : 40);
		button.setLayout(null);
		button.add(infoPanel);

		return button;
	}
	private void kartSecimDialogGoster(String kartTipi, ArrayList<SavasAraci> kartlar) {
	    if (kartSecimDialog != null && kartSecimDialog.isVisible()) {
	        kartSecimDialog.dispose();
	    }

	    kartSecimDialog = new JDialog(frame, kartTipi + " Kartları", true);
	    kartSecimDialog.setLayout(new FlowLayout());

	    int kullanilmamisKartSayisi = 0;
	    for (SavasAraci kart : kartlar) {
	        if (!insan.getKullanilmisKartlar().contains(kart)) {
	            kullanilmamisKartSayisi++;
	        }
	    }

	    int kalanSecimHakki = 3 - secilenKartlar.size();
	    
	    boolean tumKartlarSecilebilir = kullanilmamisKartSayisi < kalanSecimHakki;

	    for (SavasAraci kart : kartlar) {
	        JButton kartButton = new JButton();
	        kartButton.setPreferredSize(new Dimension(100, 150));
	        kartButton.setIcon(getGorsel(kart));
	        kartButton.setName("kart_" + System.identityHashCode(kart));

	       
	        StringBuilder cardInfo = new StringBuilder("<html>" + kart.getAltSinif() + 
	                                                "<br>Vuruş: " + kart.getVurus() + 
	                                                "<br>Dayanıklılık: " + kart.getDayaniklilik() +
	                                                "<br>Seviye: " + kart.getSeviyePuani());

	        if (kart instanceof Hava) {
	            cardInfo.append("<br>Kara Vuruş Avantajı: +").append(((Hava)kart).getKaraVurusAvantaji());
	        } else if (kart instanceof Kara) {
	            cardInfo.append("<br>Deniz Vuruş Avantajı: +").append(((Kara)kart).getDenizVurusAvantaji());
	        } else if (kart instanceof Deniz) {
	            cardInfo.append("<br>Hava Vuruş Avantajı: +").append(((Deniz)kart).getHavaVurusAvantaji());
	        }

	        if (kart == yeniKart) {
	            cardInfo.append("<br><font color='blue'><b>Yeni Kart - Seçilmesi Zorunlu!</b></font>");
	            kartButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
	        } 
	        else if (insan.getKullanilmisKartlar().contains(kart) && !tumKartlarSecilebilir) {
	            cardInfo.append("<br><font color='red'>Daha Önce Kullanıldı</font>");
	            kartButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
	            kartButton.setEnabled(false);
	        } else {
	            kartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	            kartButton.setEnabled(true);
	        }

	        cardInfo.append("</html>");
	        kartButton.setToolTipText(cardInfo.toString());

	        boolean kartZatenSecili = buHamledeSecilenKartlar.stream()
	                .anyMatch(b -> b.getName() != null && b.getName().equals(kartButton.getName()));

	        if (kartZatenSecili) {
	            kartButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
	            kartButton.setEnabled(true);
	        }

	        kartButton.addActionListener(e -> {
	            if (kartZatenSecili) {
	                secilenKartlar.removeIf(b -> b.getName().equals(kartButton.getName()));
	                buHamledeSecilenKartlar.removeIf(b -> b.getName().equals(kartButton.getName()));
	                kartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	                kartSecimDialog.dispose();
	                kartlariGuncelle();
	                hamleSecimGuncelle();
	            } else if (secilenKartlar.size() < 3) {
	                secilenKartlar.add(kartButton);
	                buHamledeSecilenKartlar.add(kartButton);
	                kartButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
	                kartSecimDialog.dispose();
	                kartlariGuncelle();
	                hamleSecimGuncelle();
	            }
	        });

	        kartSecimDialog.add(kartButton);
	    }

	    kartSecimDialog.pack();
	    kartSecimDialog.setLocationRelativeTo(frame);
	    kartSecimDialog.setVisible(true);
	}

	private JButton kartButtonOlustur(SavasAraci kart, boolean secilebilir, boolean kullanilmis) {
	    JButton kartButton = new JButton();
	    kartButton.setPreferredSize(new Dimension(100, 150));
	    kartButton.setIcon(getGorsel(kart));
	    kartButton.setName("kart_" + System.identityHashCode(kart));

	    String cardInfo = "<html>" + kart.getAltSinif() + 
	                     "<br>Vuruş: " + kart.getVurus() + 
	                     "<br>Dayanıklılık: " + kart.getDayaniklilik();

	    if (kart instanceof Hava) {
	        cardInfo += "<br>Kara Vuruş Avantajı: +" + ((Hava)kart).getKaraVurusAvantaji();
	    } else if (kart instanceof Kara) {
	        cardInfo += "<br>Deniz Vuruş Avantajı: +" + ((Kara)kart).getDenizVurusAvantaji();
	    } else if (kart instanceof Deniz) {
	        cardInfo += "<br>Hava Vuruş Avantajı: +" + ((Deniz)kart).getHavaVurusAvantaji();
	    }

	    if (kullanilmis) {
	        cardInfo += "<br><font color='red'>Daha önce kullanıldı</font>";
	    }
	    if (kart == yeniKart) {
	        cardInfo += "<br><font color='blue'>Yeni Kart!</font>";
	    }
	    cardInfo += "</html>";
	    kartButton.setToolTipText(cardInfo);

	    boolean kartZatenSecili = buHamledeSecilenKartlar.stream()
	            .anyMatch(b -> b.getName() != null && b.getName().equals(kartButton.getName()));

	    if (kartZatenSecili) {
	        kartButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
	        kartButton.setEnabled(false);
	    } else {
	        kartButton.setEnabled(secilebilir);
	        if (kullanilmis && !secilebilir) {
	            kartButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
	        } else {
	            kartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	        }
	    }

	    if (secilebilir) {
	        kartButton.addActionListener(e -> {
	            if (secilenKartlar.size() < 3) {
	                secilenKartlar.add(kartButton);
	                buHamledeSecilenKartlar.add(kartButton);
	                kartButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
	                kartButton.setEnabled(false);
	                kartSecimDialog.dispose();
	                kartlariGuncelle();
	                hamleSecimGuncelle();
	            }
	        });
	    }

	    return kartButton;
	}

	private void kartlariGuncelle() {
		kartPanel.removeAll();

		JPanel oyuncuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		oyuncuPanel.setBackground(new Color(53, 101, 77));
		kartlariGrupla(insan);

		for (Map.Entry<String, ArrayList<SavasAraci>> entry : gruplanmisKartlar.entrySet()) {
			JButton button = grupKartButtonOlustur(entry.getKey(), entry.getValue(), false);
			oyuncuPanel.add(button);
		}

		JPanel bilgisayarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		bilgisayarPanel.setBackground(new Color(53, 101, 77));
		kartlariGrupla(bilgisayar);

		for (Map.Entry<String, ArrayList<SavasAraci>> entry : gruplanmisKartlar.entrySet()) {
			JButton button = grupKartButtonOlustur(entry.getKey(), entry.getValue(), true);
			bilgisayarPanel.add(button);
		}

		kartPanel.add(bilgisayarPanel);
		kartPanel.add(oyuncuPanel);
		kartPanel.revalidate();
		kartPanel.repaint();
	}

	private ImageIcon getGorsel(SavasAraci kart) {
		String dosyaAdi = kart.getAltSinif().toLowerCase();
		if (dosyaAdi.equals("obus")) {
			dosyaAdi = "obüs";
		}

		String[] uzantilar = { ".png", ".webp" };

		return gorselCache.computeIfAbsent(dosyaAdi, k -> {
			try {
				File file = null;
				for (String uzanti : uzantilar) {
					String tamYol = "C:/java proje/SavasKartOyunu/src/resources/images/" + k + uzanti;
					file = new File(tamYol);
					if (file.exists()) {
						System.out.println("Dosya bulundu: " + tamYol); 
						ImageIcon icon = new ImageIcon(file.getAbsolutePath());
						Image img = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
						return new ImageIcon(img);
					}
				}

				BufferedImage img = new BufferedImage(100, 150, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = img.createGraphics();

				GradientPaint gradient = new GradientPaint(0, 0, new Color(70, 130, 180), 100, 150,
						new Color(25, 25, 112));
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, 100, 150);

				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(3));
				g2d.drawRect(3, 3, 93, 143);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Arial", Font.BOLD, 14));
				String kartTipi = kart.getAltSinif();
				FontMetrics fm = g2d.getFontMetrics();
				int textWidth = fm.stringWidth(kartTipi);
				g2d.drawString(kartTipi, (100 - textWidth) / 2, 75);

				g2d.dispose();
				return new ImageIcon(img);

			} catch (Exception e) {
				System.out.println("Görsel yükleme hatası: " + k);
				e.printStackTrace();

				BufferedImage img = new BufferedImage(100, 150, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = img.createGraphics();
				g2d.setColor(Color.GRAY);
				g2d.fillRect(0, 0, 100, 150);
				g2d.setColor(Color.WHITE);
				g2d.drawRect(5, 5, 89, 139);
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Arial", Font.BOLD, 12));
				g2d.drawString(kart.getAltSinif(), 10, 75);
				g2d.dispose();
				return new ImageIcon(img);
			}
		});
	}

	private ImageIcon getKapaliKart() {
		return gorselCache.computeIfAbsent("kapali", k -> {
			BufferedImage img = new BufferedImage(100, 150, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = img.createGraphics();

			Color bordoRenk = new Color(128, 0, 32); 
			Color acikBordoRenk = new Color(165, 42, 42);

			GradientPaint gradient = new GradientPaint(0, 0, bordoRenk, 100, 150, acikBordoRenk);
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, 100, 150);

			
			g2d.setColor(new Color(255, 215, 0)); 
			g2d.setStroke(new BasicStroke(4));
			g2d.drawRect(4, 4, 91, 141);

			// Süsleme deseni (altın sarısı)
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(new Color(255, 215, 0)); // Altın sarısı

			// Orta desen
			int centerX = 50;
			int centerY = 75;

			// İç içe kareler
			for (int i = 0; i < 3; i++) {
				int size = 40 - (i * 10);
				g2d.drawRect(centerX - size / 2, centerY - size / 2, size, size);
			}

			// Köşegen çizgiler
			g2d.drawLine(centerX - 25, centerY - 25, centerX + 25, centerY + 25);
			g2d.drawLine(centerX - 25, centerY + 25, centerX + 25, centerY - 25);

			g2d.dispose();
			return new ImageIcon(img);
		});
	}

	
	private boolean sonHamleMi = false; // Sınıf değişkeni olarak ekleyin

	private void karsilastirmaEkraniGoster(ArrayList<SavasAraci> insanKartlari, ArrayList<SavasAraci> bilgisayarKartlari) {
	    JDialog karsilastirmaDialog = new JDialog(frame, "Kart Karşılaştırmaları", true);
	    karsilastirmaDialog.setLayout(new BorderLayout());
	    karsilastirmaDialog.setSize(800, 600);
	    karsilastirmaDialog.setLocationRelativeTo(frame);

	    JPanel anaPanel = new JPanel();
	    anaPanel.setLayout(new BoxLayout(anaPanel, BoxLayout.Y_AXIS));
	    anaPanel.setBackground(new Color(53, 101, 77)); // Yeşil arka plan

	    Color altinRengi = new Color(255, 215, 0);

	    for (int i = 0; i < Math.min(insanKartlari.size(), bilgisayarKartlari.size()); i++) {
	        SavasAraci insanKarti = insanKartlari.get(i);
	        SavasAraci bilgisayarKarti = bilgisayarKartlari.get(i);

	        JPanel karsilastirmaPanel = new JPanel();
	        karsilastirmaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
	        karsilastirmaPanel.setBackground(new Color(53, 101, 77));
	        karsilastirmaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        JPanel insanKartPanel = new JPanel();
	        insanKartPanel.setLayout(new BoxLayout(insanKartPanel, BoxLayout.Y_AXIS));
	        insanKartPanel.setBackground(new Color(53, 101, 77));
	        
	        JButton insanKartButton = new JButton();
	        insanKartButton.setPreferredSize(new Dimension(120, 180));
	        insanKartButton.setIcon(getGorsel(insanKarti));
	        insanKartButton.setBorder(BorderFactory.createLineBorder(altinRengi, 3));
	        
	        JLabel insanBilgiLabel = new JLabel("<html>Vuruş: " + insanKarti.getVurus() + 
	                                          "<br>Dayanıklılık: " + insanKarti.getDayaniklilik() +
	                                          "<br>Seviye: " + insanKarti.getSeviyePuani() + "</html>");
	        insanBilgiLabel.setForeground(Color.WHITE);
	        insanBilgiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	        JLabel vsLabel = new JLabel("VS");
	        vsLabel.setFont(new Font("Arial", Font.BOLD, 24));
	        vsLabel.setForeground(Color.WHITE);

	        JPanel bilgisayarKartPanel = new JPanel();
	        bilgisayarKartPanel.setLayout(new BoxLayout(bilgisayarKartPanel, BoxLayout.Y_AXIS));
	        bilgisayarKartPanel.setBackground(new Color(53, 101, 77));
	        
	        JButton bilgisayarKartButton = new JButton();
	        bilgisayarKartButton.setPreferredSize(new Dimension(120, 180));
	        bilgisayarKartButton.setIcon(getGorsel(bilgisayarKarti));
	        bilgisayarKartButton.setBorder(BorderFactory.createLineBorder(altinRengi, 3));
	        
	        JLabel bilgisayarBilgiLabel = new JLabel("<html>Vuruş: " + bilgisayarKarti.getVurus() + 
	                                                "<br>Dayanıklılık: " + bilgisayarKarti.getDayaniklilik() +
	                                                "<br>Seviye: " + bilgisayarKarti.getSeviyePuani() + "</html>");
	        bilgisayarBilgiLabel.setForeground(Color.WHITE);
	        bilgisayarBilgiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	        insanKartPanel.add(insanKartButton);
	        insanKartPanel.add(Box.createVerticalStrut(10));
	        insanKartPanel.add(insanBilgiLabel);

	        bilgisayarKartPanel.add(bilgisayarKartButton);
	        bilgisayarKartPanel.add(Box.createVerticalStrut(10));
	        bilgisayarKartPanel.add(bilgisayarBilgiLabel);

	        karsilastirmaPanel.add(insanKartPanel);
	        karsilastirmaPanel.add(vsLabel);
	        karsilastirmaPanel.add(bilgisayarKartPanel);

	        anaPanel.add(karsilastirmaPanel);
	        anaPanel.add(Box.createVerticalStrut(20));
	    }

	    JButton devamButton = new JButton("Devam Et");
	    devamButton.setPreferredSize(new Dimension(120, 40));
	    devamButton.addActionListener(e -> karsilastirmaDialog.dispose());

	    JPanel butonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    butonPanel.setBackground(new Color(53, 101, 77));
	    butonPanel.add(devamButton);

	    JScrollPane scrollPane = new JScrollPane(anaPanel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.getViewport().setBackground(new Color(53, 101, 77));

	    karsilastirmaDialog.add(scrollPane, BorderLayout.CENTER);
	    karsilastirmaDialog.add(butonPanel, BorderLayout.SOUTH);
	    karsilastirmaDialog.setVisible(true);
	}
	private void hamleYap() {
	    try {
	        int insanKullanilabilirKartSayisi = insan.getAktifKartSayisi();
	        int bilgisayarKullanilabilirKartSayisi = bilgisayar.getAktifKartSayisi();

	        if (insanKullanilabilirKartSayisi == 0 || bilgisayarKullanilabilirKartSayisi == 0) {
	            bilgiEkle("\nBir oyuncunun kartları tükendi! Oyun sona eriyor...");
	            oyunSonucunuGoster();
	            hamleButton.setEnabled(false);
	            return;
	        }

	        if (secilenKartlar.size() != 3) {
	            JOptionPane.showMessageDialog(frame, "Lütfen 3 kart seçiniz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        if (yeniKartSecilmeli) {
	            boolean yeniKartSecilmis = false;
	            for (JButton button : secilenKartlar) {
	                if (button.getName().equals("kart_" + System.identityHashCode(yeniKart))) {
	                    yeniKartSecilmis = true;
	                    break;
	                }
	            }
	            if (!yeniKartSecilmis) {
	                JOptionPane.showMessageDialog(frame, "Yeni kartı seçmek zorundasınız!", "Uyarı", JOptionPane.WARNING_MESSAGE);
	                return;
	            }
	        }

	        mevcutHamle++;
	        hamleSayisiLabel.setText("Hamle: " + mevcutHamle + "/" + maksimumHamleSayisi);
	        bilgiEkle("\n=== HAMLE " + mevcutHamle + " ===");

	        if (mevcutHamle >= maksimumHamleSayisi || sonHamleMi) {
	            ArrayList<SavasAraci> insanKartlari = new ArrayList<>();
	            for (JButton button : secilenKartlar) {
	                String kartID = button.getName();
	                for (SavasAraci kart : insan.getKartListesi()) {
	                    if (kartID.equals("kart_" + System.identityHashCode(kart))) {
	                        insanKartlari.add(kart);
	                        insan.getKullanilmisKartlar().add(kart);
	                        break;
	                    }
	                }
	            }

	            ArrayList<SavasAraci> bilgisayarKartlari = bilgisayar.kartSec();

	            bilgiEkle("\nSon Hamle Karşılaştırmaları:");
	            karsilastirmaEkraniGoster(insanKartlari, bilgisayarKartlari);
	            for (int i = 0; i < Math.min(insanKartlari.size(), bilgisayarKartlari.size()); i++) {
	                karsilastir(insanKartlari.get(i), bilgisayarKartlari.get(i));
	            }

	            String sonlanmaSebebi = mevcutHamle >= maksimumHamleSayisi ? 
	                "Maksimum hamle sayısına ulaşıldı!" : 
	                "Son hamle tamamlandı!";
	            
	            bilgiEkle("\n" + sonlanmaSebebi + " Oyun sona eriyor...");
	            oyunSonucunuGoster();
	            hamleButton.setEnabled(false);
	            return;
	        }

	        ArrayList<SavasAraci> insanKartlari = new ArrayList<>();
	        for (JButton button : secilenKartlar) {
	            String kartID = button.getName();
	            for (SavasAraci kart : insan.getKartListesi()) {
	                if (kartID.equals("kart_" + System.identityHashCode(kart))) {
	                    insanKartlari.add(kart);
	                    insan.getKullanilmisKartlar().add(kart);
	                    break;
	                }
	            }
	        }

	        ArrayList<SavasAraci> bilgisayarKartlari = bilgisayar.kartSec();

	        bilgiEkle("\nKart Karşılaştırmaları:");
	        karsilastirmaEkraniGoster(insanKartlari, bilgisayarKartlari);
	        for (int i = 0; i < Math.min(insanKartlari.size(), bilgisayarKartlari.size()); i++) {
	            karsilastir(insanKartlari.get(i), bilgisayarKartlari.get(i));
	        }

	        insanKullanilabilirKartSayisi = insan.getAktifKartSayisi();
	        bilgisayarKullanilabilirKartSayisi = bilgisayar.getAktifKartSayisi();

	        if (insanKullanilabilirKartSayisi == 0 || bilgisayarKullanilabilirKartSayisi == 0) {
	            bilgiEkle("\nBir oyuncunun kartları tükendi! Oyun sona eriyor...");
	            oyunSonucunuGoster();
	            hamleButton.setEnabled(false);
	            return;
	        }

	        secilenKartlar.clear();
	        buHamledeSecilenKartlar.clear();
	        hamleSecimGuncelle();

	        insanKullanilabilirKartSayisi = insan.getAktifKartSayisi();
	        bilgisayarKullanilabilirKartSayisi = bilgisayar.getAktifKartSayisi();

	        bilgiEkle("\nKullanılabilir Kart Sayıları:");
	        bilgiEkle(insan.getOyuncuAdi() + ": " + insanKullanilabilirKartSayisi);
	        bilgiEkle("Bilgisayar: " + bilgisayarKullanilabilirKartSayisi);

	        if (insanKullanilabilirKartSayisi == 1 || bilgisayarKullanilabilirKartSayisi == 1) {
	            bilgiEkle("\nBir oyuncunun kartları tükeniyor! Son hamle başlıyor...");
	            
	            if (insanKullanilabilirKartSayisi == 1) {
	                SavasAraci kart1 = yeniKartOlustur(false);
	                SavasAraci kart2 = yeniKartOlustur(false);
	                insan.kartEkle(kart1);
	                insan.kartEkle(kart2);
	                bilgiEkle(insan.getOyuncuAdi() + "'a 2 yeni kart eklendi");
	                
	                bilgisayar.kartEkle(yeniKartOlustur(true));
	            } else {
	                bilgisayar.kartEkle(yeniKartOlustur(true));
	                bilgisayar.kartEkle(yeniKartOlustur(true));
	                bilgiEkle("Bilgisayara 2 yeni kart eklendi");
	                
	                yeniKart = yeniKartOlustur(false);
	                insan.kartEkle(yeniKart);
	                bilgiEkle(insan.getOyuncuAdi() + "'a yeni kart eklendi: " + yeniKart.getAltSinif());
	            }
	            
	            sonHamleMi = true;
	            bilgiEkle("\nSon hamle için kartlar hazır!");
	            
	            insan.kartlariSifirla();
	            bilgisayar.kartlariSifirla();
	            
	            kartlariGuncelle();
	            yeniKartPaneliGuncelle();
	            return;
	        }

	        if (!sonHamleMi) {
	            yeniKart = yeniKartOlustur(false);
	            yeniKartSecilmeli = true;
	            insan.kartEkle(yeniKart);
	            bilgisayar.kartEkle(yeniKartOlustur(true));
	            bilgiEkle("\nYeni kartlar eklendi:");
	            bilgiEkle(insan.getOyuncuAdi() + "'a " + yeniKart.getAltSinif() + " eklendi");
	        }

	        kartlariGuncelle();
	        yeniKartPaneliGuncelle();

	        bilgiEkle("\nMevcut Durum:");
	        bilgiEkle("Kalan Hamle: " + (maksimumHamleSayisi - mevcutHamle));
	        bilgiEkle(insan.getOyuncuAdi() + " Skor: " + insan.getSkor());
	        bilgiEkle("Bilgisayar Skor: " + bilgisayar.getSkor());

	        if (!insanIcinGelistirilmisKartlarAcik && insan.getSkor() >= 20) {
	            bilgiEkle(insan.getOyuncuAdi() + " için gelişmiş kartlar açık!");
	            insanIcinGelistirilmisKartlarAcik = true;
	        }
	        if (!bilgisayarIcinGelistirilmisKartlarAcik && bilgisayar.getSkor() >= 20) {
	            bilgiEkle("Bilgisayar için gelişmiş kartlar açık!");
	            bilgisayarIcinGelistirilmisKartlarAcik = true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        bilgiEkle("\nHATA: " + e.getMessage());
	        JOptionPane.showMessageDialog(frame, "Bir hata oluştu: " + e.getMessage(), 
	                                    "Hata", JOptionPane.ERROR_MESSAGE);
	    }
	}

	// Hamle yapma ve sonuç gösterme işlemlerini ayrı bir metoda aldım
	private void hamleYapVeSonucGoster() {
	    ArrayList<SavasAraci> insanKartlari = new ArrayList<>();
	    for (JButton button : secilenKartlar) {
	        String kartID = button.getName();
	        for (SavasAraci kart : insan.getKartListesi()) {
	            if (kartID.equals("kart_" + System.identityHashCode(kart))) {
	                insanKartlari.add(kart);
	                insan.getKullanilmisKartlar().add(kart);
	                break;
	            }
	        }
	    }

	    ArrayList<SavasAraci> bilgisayarKartlari = bilgisayar.kartSec();
	    
	    bilgiEkle("\nKart Karşılaştırmaları:");
	    for (int i = 0; i < Math.min(insanKartlari.size(), bilgisayarKartlari.size()); i++) {
	        karsilastir(insanKartlari.get(i), bilgisayarKartlari.get(i));
	    }
	}
	private void yeniKartPaneliGuncelle() {
	    if (yeniKartPanel == null) {
	        yeniKartPanel = new JPanel();
	        yeniKartPanel.setBackground(new Color(53, 101, 77));
	        frame.add(yeniKartPanel, BorderLayout.NORTH);
	    }
yeniKartPanel.removeAll();
    
    if (yeniKart != null) {
        JLabel bilgiLabel = new JLabel("Yeni Kart (Sonraki hamlede kullanılması zorunlu):");
        bilgiLabel.setForeground(Color.WHITE);
        yeniKartPanel.add(bilgiLabel);
        
        yeniKartButton = new JButton();
        yeniKartButton.setPreferredSize(new Dimension(100, 150));
        yeniKartButton.setIcon(getGorsel(yeniKart));
        yeniKartButton.setName("yeniKart_" + System.identityHashCode(yeniKart));
        
        String cardInfo = "<html>" + yeniKart.getAltSinif() + 
                         "<br>Vuruş: " + yeniKart.getVurus() + 
                         "<br>Dayanıklılık: " + yeniKart.getDayaniklilik();
        
        if (yeniKart instanceof Hava) {
            cardInfo += "<br>Kara Vuruş Avantajı: +" + ((Hava)yeniKart).getKaraVurusAvantaji();
        } else if (yeniKart instanceof Kara) {
            cardInfo += "<br>Deniz Vuruş Avantajı: +" + ((Kara)yeniKart).getDenizVurusAvantaji();
        } else if (yeniKart instanceof Deniz) {
            cardInfo += "<br>Hava Vuruş Avantajı: +" + ((Deniz)yeniKart).getHavaVurusAvantaji();
        }
        
        cardInfo += "</html>";
        yeniKartButton.setToolTipText(cardInfo);
        
        yeniKartPanel.add(yeniKartButton);
    }
    
    yeniKartPanel.revalidate();
    yeniKartPanel.repaint();
}
	
	private int saldiriHesapla(int Vurus, int avantajPuani) {
		return Vurus + avantajPuani;
	}
	
	private void karsilastir(SavasAraci insanKarti, SavasAraci bilgisayarKarti) {
		bilgiEkle("\n" + insan.getOyuncuAdi() + ": " + insanKarti.getAltSinif() + " (Dayanıklılık: "
				+ insanKarti.getDayaniklilik() + ", Seviye: " + insanKarti.getSeviyePuani() + ")" + " vs Bilgisayar: "
				+ bilgisayarKarti.getAltSinif() + " (Dayanıklılık: " + bilgisayarKarti.getDayaniklilik() + ", Seviye: "
				+ bilgisayarKarti.getSeviyePuani() + ")");

		int avantajPuani = 0;
		if (insanKarti instanceof Hava && bilgisayarKarti instanceof Kara) {
			avantajPuani = ((Hava) insanKarti).getKaraVurusAvantaji();
		} else if (insanKarti instanceof Kara && bilgisayarKarti instanceof Deniz) {
			avantajPuani = ((Kara) insanKarti).getDenizVurusAvantaji();
		} else if (insanKarti instanceof Deniz && bilgisayarKarti instanceof Hava) {
			avantajPuani = ((Deniz) insanKarti).getHavaVurusAvantaji();
		}
		
		int insanVurus = saldiriHesapla(insanKarti.getVurus(), avantajPuani);
		bilgiEkle("\n" + insan.getOyuncuAdi() + " Saldırı Detayı:");
		bilgiEkle("Temel Vuruş: " + insanKarti.getVurus());
		if (avantajPuani > 0) {
			bilgiEkle("Avantaj Puanı: +" + avantajPuani);
		}
		bilgiEkle("Toplam Saldırı: " + insanVurus);

		
		avantajPuani = 0;
		if (bilgisayarKarti instanceof Hava && insanKarti instanceof Kara) {
			avantajPuani = ((Hava) bilgisayarKarti).getKaraVurusAvantaji();
		} else if (bilgisayarKarti instanceof Kara && insanKarti instanceof Deniz) {
			avantajPuani = ((Kara) bilgisayarKarti).getDenizVurusAvantaji();
		} else if (bilgisayarKarti instanceof Deniz && insanKarti instanceof Hava) {
			avantajPuani = ((Deniz) bilgisayarKarti).getHavaVurusAvantaji();
		}

		int bilgisayarVurus = saldiriHesapla(bilgisayarKarti.getVurus(), avantajPuani);
		bilgiEkle("\nBilgisayar Saldırı Detayı:");
		bilgiEkle("Temel Vuruş: " + bilgisayarKarti.getVurus());
		if (avantajPuani > 0) {
			bilgiEkle("Avantaj Puanı: +" + avantajPuani);
		}
		bilgiEkle("Toplam Saldırı: " + bilgisayarVurus);
		
		int insanKartDayaniklilik = insanKarti.getDayaniklilik() - bilgisayarVurus;
		int bilgisayarKartDayaniklilik = bilgisayarKarti.getDayaniklilik() - insanVurus;

		insanKarti.setDayaniklilik(insanKartDayaniklilik);
		bilgisayarKarti.setDayaniklilik(bilgisayarKartDayaniklilik);

		bilgiEkle("\nKalan Dayanıklılıklar:");
		bilgiEkle(insan.getOyuncuAdi() + ": " + insanKarti.getDayaniklilik());
		bilgiEkle("Bilgisayar: " + bilgisayarKarti.getDayaniklilik());

		
		if (bilgisayarKartDayaniklilik <= 0 && insanKartDayaniklilik > 0) {
			bilgisayar.kartEle(bilgisayarKarti);
			
			insanKarti.setSeviyePuani(insanKarti.getSeviyePuani() + bilgisayarKarti.getSeviyePuani());
			
			insan.setSkor(insan.getSkor() + bilgisayarKarti.getSeviyePuani());
			bilgiEkle("\nBilgisayarın kartı elendi!");
			bilgiEkle(insan.getOyuncuAdi() + "'nın kartı " + bilgisayarKarti.getSeviyePuani()
					+ " seviye kazandı! Yeni seviye: " + insanKarti.getSeviyePuani());
			bilgiEkle(insan.getOyuncuAdi() + "'nın skoru " + bilgisayarKarti.getSeviyePuani() + " arttı! Yeni skor: "
					+ insan.getSkor());
		} else if (insanKartDayaniklilik <= 0 && bilgisayarKartDayaniklilik > 0) {
			insan.kartEle(insanKarti);
			
			bilgisayarKarti.setSeviyePuani(bilgisayarKarti.getSeviyePuani() + insanKarti.getSeviyePuani());
			
			bilgisayar.setSkor(bilgisayar.getSkor() + insanKarti.getSeviyePuani());
			bilgiEkle("\n" + insan.getOyuncuAdi() + "'nın kartı elendi!");
			bilgiEkle("Bilgisayarın kartı " + insanKarti.getSeviyePuani() + " seviye kazandı! Yeni seviye: "
					+ bilgisayarKarti.getSeviyePuani());
			bilgiEkle(
					"Bilgisayarın skoru " + insanKarti.getSeviyePuani() + " arttı! Yeni skor: " + bilgisayar.getSkor());
		} else if (insanKartDayaniklilik <= 0 && bilgisayarKartDayaniklilik <= 0) {
			
			insan.kartEle(insanKarti);
			bilgisayar.kartEle(bilgisayarKarti);

			
			insan.setSkor(insan.getSkor() + bilgisayarKarti.getSeviyePuani());
			bilgisayar.setSkor(bilgisayar.getSkor() + insanKarti.getSeviyePuani());

			bilgiEkle("\nBilgisayar kartı ve " + insan.getOyuncuAdi() + " kartı elendi");
			bilgiEkle(insan.getOyuncuAdi() + "'nın kartı " + bilgisayarKarti.getSeviyePuani()
					+ " seviye kazandı! Yeni seviye: " + insanKarti.getSeviyePuani());
			bilgiEkle("Bilgisayarın kartı " + insanKarti.getSeviyePuani() + " seviye kazandı! Yeni seviye: "
					+ bilgisayarKarti.getSeviyePuani());
			bilgiEkle(insan.getOyuncuAdi() + " skoru: " + insan.getSkor());
			bilgiEkle("Bilgisayar skoru: " + bilgisayar.getSkor());
		}
	}
	private void bilgiEkle(String mesaj) {
	   
	    bilgiAlani.append(mesaj + "\n");
	    bilgiAlani.setCaretPosition(bilgiAlani.getDocument().getLength());
	    
	  
	    if (logYazici != null) {
	        logYazici.println(mesaj);
	        logYazici.flush(); 
	    }
	}

	private void oyunSonucunuGoster() {
	    bilgiEkle("\n=== OYUN SONU ===");
	    bilgiEkle("Toplam Hamle: " + mevcutHamle);

	    
	    int insanKartSayisi = 0;
	    int bilgisayarKartSayisi = 0;
	    int insanToplamDayaniklilik = 0;
	    int bilgisayarToplamDayaniklilik = 0;

	    for (SavasAraci kart : insan.getKartListesi()) {
	        if (!insan.getElenenKartlar().contains(kart)) {
	            insanKartSayisi++;
	            insanToplamDayaniklilik += kart.getDayaniklilik();
	        }
	    }

	    for (SavasAraci kart : bilgisayar.getKartListesi()) {
	        if (!bilgisayar.getElenenKartlar().contains(kart)) {
	            bilgisayarKartSayisi++;
	            bilgisayarToplamDayaniklilik += kart.getDayaniklilik();
	        }
	    }

	   
	    bilgiEkle("\n================================");
	    bilgiEkle("OYUN SONU DETAYLI SONUÇLAR");
	    bilgiEkle("================================");
	    bilgiEkle("Toplam Yapılan Hamle: " + mevcutHamle);
	    bilgiEkle("\nKalan Kartlar:");
	    bilgiEkle(insan.getOyuncuAdi() + ": " + insanKartSayisi);
	    bilgiEkle("Bilgisayar: " + bilgisayarKartSayisi);

	    bilgiEkle("\nFinal Skorları:");
	    bilgiEkle(insan.getOyuncuAdi() + ": " + insan.getSkor());
	    bilgiEkle("Bilgisayar: " + bilgisayar.getSkor());

	  
	    String kazanan;
	    if (bilgisayarKartSayisi == 0) {
	        kazanan = insan.getOyuncuAdi();
	    } else if (insanKartSayisi == 0) {
	        kazanan = "Bilgisayar";
	    } else {
	        if (insan.getSkor() != bilgisayar.getSkor()) {
	            kazanan = (insan.getSkor() > bilgisayar.getSkor()) ? insan.getOyuncuAdi() : "Bilgisayar";
	        } else {
	            if (insanToplamDayaniklilik != bilgisayarToplamDayaniklilik) {
	                kazanan = (insanToplamDayaniklilik > bilgisayarToplamDayaniklilik) ? insan.getOyuncuAdi() : "Bilgisayar";
	                int fark = Math.abs(insanToplamDayaniklilik - bilgisayarToplamDayaniklilik);
	                bilgiEkle("\nDayanıklılık farkı (" + fark + ") kazananın skoruna eklendi!");
	            } else {
	                kazanan = "Berabere";
	            }
	        }
	    }

	   
	    bilgiEkle("\nSONUÇ:");
	    if (!kazanan.equals("Berabere")) {
	        bilgiEkle(kazanan + " KAZANDI!");
	    } else {
	        bilgiEkle("OYUN BERABERE BİTTİ!");
	    }
	    bilgiEkle("================================");

	    
	    if (logYazici != null) {
	        logYazici.flush();
	    }

	   
	    JDialog sonucDialog = new JDialog(frame, "Oyun Sonu", true);
	    sonucDialog.setLayout(new BorderLayout());
	    sonucDialog.setSize(400, 300);
	    sonucDialog.setLocationRelativeTo(frame);

	   
	    JPanel sonucPanel = new JPanel();
	    sonucPanel.setLayout(new BoxLayout(sonucPanel, BoxLayout.Y_AXIS));
	    sonucPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    sonucPanel.setBackground(new Color(233, 237, 201));

	   
	    JLabel sonucLabel = new JLabel(kazanan.equals("Berabere") ? "Oyun Berabere Bitti!" : 
	                                 kazanan + " Kazandı!");
	    sonucLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    sonucLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel skorLabel = new JLabel(String.format("Skorlar - %s: %d, Bilgisayar: %d", 
	                                 insan.getOyuncuAdi(), insan.getSkor(), bilgisayar.getSkor()));
	    skorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel kartLabel = new JLabel(String.format("Kalan Kartlar - %s: %d, Bilgisayar: %d", 
	                                 insan.getOyuncuAdi(), insanKartSayisi, bilgisayarKartSayisi));
	    kartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	   
	    JPanel butonPanel = new JPanel();
	    butonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
	    butonPanel.setBackground(new Color(233, 237, 201));

	    JButton yenidenOynaButton = new JButton("Yeniden Oyna");
	    yenidenOynaButton.setPreferredSize(new Dimension(120, 40));
	    yenidenOynaButton.addActionListener(e -> {
	        if (logYazici != null) {
	            logYazici.close(); 
	        }
	        sonucDialog.dispose();
	        frame.dispose();
	        main(new String[]{});
	    });

	    JButton bitirButton = new JButton("Oyunu Bitir");
	    bitirButton.setPreferredSize(new Dimension(120, 40));
	    bitirButton.addActionListener(e -> {
	        if (logYazici != null) {
	            logYazici.close();
	        }
	        sonucDialog.dispose();
	        frame.dispose();
	        System.exit(0);
	    });

	   
	    sonucPanel.add(Box.createVerticalStrut(20));
	    sonucPanel.add(sonucLabel);
	    sonucPanel.add(Box.createVerticalStrut(20));
	    sonucPanel.add(skorLabel);
	    sonucPanel.add(Box.createVerticalStrut(10));
	    sonucPanel.add(kartLabel);
	    sonucPanel.add(Box.createVerticalStrut(30));

	    butonPanel.add(yenidenOynaButton);
	    butonPanel.add(bitirButton);

	    
	    sonucDialog.add(sonucPanel, BorderLayout.CENTER);
	    sonucDialog.add(butonPanel, BorderLayout.SOUTH);

	  
	    sonucDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    sonucDialog.setVisible(true);
	}
	public void oyunuBaslat() {
	    frame.setVisible(true);
	    kartlariGuncelle();
	    yeniKartPaneliGuncelle(); 
	    bilgiEkle("Oyun başladı! Lütfen 3 kart seçin ve 'Hamle Yap' butonuna tıklayın.");
	}

	public static void main(String[] args) {
	    SwingUtilities.invokeLater(() -> {
	        String oyuncuAdi = JOptionPane.showInputDialog("Adınızı giriniz:");
	        if (oyuncuAdi == null || oyuncuAdi.trim().isEmpty()) {
	            oyuncuAdi = "Oyuncu";
	        }

	        int hamle = 0;
	        while (hamle < 1) {
	            try {
	                String hamleStr = JOptionPane.showInputDialog("Maksimum hamle sayısını giriniz (en az 1):");
	                if (hamleStr == null) {
	                    System.exit(0);
	                }
	                hamle = Integer.parseInt(hamleStr);
	                if (hamle < 1) {
	                    JOptionPane.showMessageDialog(null, "Lütfen 1 veya daha büyük bir sayı giriniz!");
	                }
	            } catch (NumberFormatException e) {
	                JOptionPane.showMessageDialog(null, "Geçerli bir sayı giriniz!");
	            }
	        }

	        int baslangicSeviyePuani = 0;
	        try {
	            String seviyeStr = JOptionPane.showInputDialog("Başlangıç seviye puanını giriniz (varsayılan: 0):");
	            if (seviyeStr != null && !seviyeStr.trim().isEmpty()) {
	                baslangicSeviyePuani = Integer.parseInt(seviyeStr);
	            }
	        } catch (NumberFormatException e) {
	            
	        }

	        Oyuncu insan = new Oyuncu(1, oyuncuAdi, baslangicSeviyePuani);
	        Oyuncu bilgisayar = new Oyuncu(2, "Bilgisayar", baslangicSeviyePuani);

	        kartlariDagit(insan, bilgisayar);

	        Oyun oyun = new Oyun(hamle);
	        oyun.insan = insan;
	        oyun.bilgisayar = bilgisayar;

	        
	        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	            if (oyun != null) {
	                oyun.kapat();
	            }
	        }));

	        oyun.oyunuBaslat();
	    });
	}

	private static void kartlariDagit(Oyuncu insan, Oyuncu bilgisayar) {
	    ArrayList<SavasAraci> kartHavuzu = new ArrayList<>();
	    
	    
	    boolean gelistirilmisKartlarAcik = (insan.getSkor() >= 20 || bilgisayar.getSkor() >= 20);

	  
	    for (int i = 0; i < 2; i++) {
	       
	        SavasAraci ucak1 = new Ucak();
	        SavasAraci ucak2 = new Ucak();
	        SavasAraci obus1 = new Obus();
	        SavasAraci obus2 = new Obus();
	        SavasAraci firkateyn1 = new Firkateyn();
	        SavasAraci firkateyn2 = new Firkateyn();
	        
	       
	        ucak1.setSeviyePuani(insan.getSeviyePuani());
	        ucak2.setSeviyePuani(insan.getSeviyePuani());
	        obus1.setSeviyePuani(insan.getSeviyePuani());
	        obus2.setSeviyePuani(insan.getSeviyePuani());
	        firkateyn1.setSeviyePuani(insan.getSeviyePuani());
	        firkateyn2.setSeviyePuani(insan.getSeviyePuani());
	        
	        kartHavuzu.add(ucak1);
	        kartHavuzu.add(ucak2);
	        kartHavuzu.add(obus1);
	        kartHavuzu.add(obus2);
	        kartHavuzu.add(firkateyn1);
	        kartHavuzu.add(firkateyn2);
	        
	       
	        if (gelistirilmisKartlarAcik) {
	            SavasAraci siha = new Siha();
	            SavasAraci kfs = new KFS();
	            SavasAraci sida = new Sida();
	            
	            siha.setSeviyePuani(insan.getSeviyePuani());
	            kfs.setSeviyePuani(insan.getSeviyePuani());
	            sida.setSeviyePuani(insan.getSeviyePuani());
	            
	            kartHavuzu.add(siha);
	            kartHavuzu.add(kfs);
	            kartHavuzu.add(sida);
	        }
	    }

	    Collections.shuffle(kartHavuzu);

	  
	    for (int i = 0; i < 12; i++) {
	        if (i < 6) {
	            insan.kartEkle(kartHavuzu.get(i));
	        } else {
	            bilgisayar.kartEkle(kartHavuzu.get(i));
	        }
	    }
	    
	    System.out.println("\nKart dağıtımı yapıldı:");
	    System.out.println("Geliştirilmiş kartlar: " + (gelistirilmisKartlarAcik ? "AÇIK" : "KAPALI"));
	}

	private SavasAraci yeniKartOlustur(boolean bilgisayarIcin) {
	    Random random = new Random();
	    SavasAraci yeniKart;
	    
	   
	    boolean gelistirilmisKartlarAcik = bilgisayarIcin ? 
                (bilgisayar.getSkor() >= 20) : 
                (insan.getSkor() >= 20);

	    
	    if (!gelistirilmisKartlarAcik) {
	     
	        int kartTipi = random.nextInt(3);
	        switch (kartTipi) {
	            case 0: yeniKart = new Ucak(); break;
	            case 1: yeniKart = new Obus(); break;
	            case 2: yeniKart = new Firkateyn(); break;
	            default: yeniKart = new Ucak(); break;
	        }
	    } else {
	        
	        int kartTipi = random.nextInt(6);
	        switch (kartTipi) {
	            case 0: yeniKart = new Ucak(); break;
	            case 1: yeniKart = new Obus(); break;
	            case 2: yeniKart = new Firkateyn(); break;
	            case 3: yeniKart = new Siha(); break;
	            case 4: yeniKart = new Sida(); break;
	            case 5: yeniKart = new KFS(); break;
	            default: yeniKart = new Ucak(); break;
	        }
	    }
	    
	    yeniKart.setSeviyePuani(bilgisayarIcin ? bilgisayar.getSeviyePuani() : insan.getSeviyePuani());
	    
	    return yeniKart;
	}
	public void kapat() {
	    if (logYazici != null) {
	        logYazici.close();
	    }
	}
}
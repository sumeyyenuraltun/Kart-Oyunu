package savasKartOyunu;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Oyuncu {
    private int oyuncuID;
    private String oyuncuAdi;
    private int skor;
    private int seviyePuani;
    private ArrayList<SavasAraci> kartListesi;
    private ArrayList<SavasAraci> kullanilmisKartlar;
    private ArrayList<SavasAraci> elenenKartlar;

    public Oyuncu(int oyuncuID, String oyuncuAdi, int baslangicSeviyePuani) {
        this.oyuncuID = oyuncuID;
        this.oyuncuAdi = oyuncuAdi;
        this.skor = 0;
        this.seviyePuani = baslangicSeviyePuani;
        this.kartListesi = new ArrayList<>();
        this.kullanilmisKartlar = new ArrayList<>();
        this.elenenKartlar = new ArrayList<>();
    }
    public Oyuncu() {
        this(0, "Bilinmeyen", 0);  
    }
   
    public String getOyuncuAdi() { return oyuncuAdi; }
    public int getSkor() { return skor; }
    public void setSkor(int skor) { this.skor = skor; }
    public int getSeviyePuani() { return seviyePuani; }
    public void setSeviyePuani(int seviyePuani) { this.seviyePuani = seviyePuani; }
    public ArrayList<SavasAraci> getKartListesi() { return kartListesi; }
    public ArrayList<SavasAraci> getElenenKartlar() { return elenenKartlar; }

    public void kartEkle(SavasAraci kart) {
        kartListesi.add(kart);
        System.out.println(oyuncuAdi + " yeni kart aldı: " + kart.getAltSinif());
    }

    public void kartEle(SavasAraci kart) {
        if (!elenenKartlar.contains(kart) && kart.getDayaniklilik() <= 0) {
            elenenKartlar.add(kart);
            System.out.println(kart.getAltSinif() + " kartı elendi!");
        }
    }

    public void skorGoster() {
        System.out.println(oyuncuAdi + " - Skor: " + skor + 
                         " - Seviye Puanı: " + seviyePuani + 
                         " - Aktif Kart: " + getAktifKartSayisi());
    }

    public ArrayList<SavasAraci> kartSec() {
        ArrayList<SavasAraci> secilenKartlar = new ArrayList<>();
        ArrayList<SavasAraci> secilebilirKartlar = getSecilebilirKartlar();
        
      
        int secilecekKartSayisi = 3;
        
        
        secilebilirKartlar.removeAll(elenenKartlar);
        secilebilirKartlar.removeAll(kullanilmisKartlar);
        
       
        if (secilebilirKartlar.size() < 3 && !kullanilmisKartlar.isEmpty()) {
            kullanilmisKartlar.clear();
            secilebilirKartlar = getSecilebilirKartlar(); 
            secilebilirKartlar.removeAll(elenenKartlar);
        }
        
       
        if (secilebilirKartlar.size() < 3) {
            return secilenKartlar; 
        }
        
        if (oyuncuAdi.equals("Bilgisayar")) {
           
            Random random = new Random();
            ArrayList<SavasAraci> geciciListe = new ArrayList<>(secilebilirKartlar);
            
            for (int i = 0; i < secilecekKartSayisi && !geciciListe.isEmpty(); i++) {
                int index = random.nextInt(geciciListe.size());
                SavasAraci secilenKart = geciciListe.get(index);
                secilenKartlar.add(secilenKart);
                kullanilmisKartlar.add(secilenKart);
                geciciListe.remove(index);
            }
        } else {
           
            return secilenKartlar;
        }
        
        return secilenKartlar;
    }

    
    public ArrayList<SavasAraci> getSecilebilirKartlar() {
        ArrayList<SavasAraci> secilebilirKartlar = new ArrayList<>();
        for (SavasAraci kart : kartListesi) {
            if (!elenenKartlar.contains(kart)) {
                secilebilirKartlar.add(kart);
            }
        }
        return secilebilirKartlar;
    }

   
    public ArrayList<SavasAraci> getKullanilmisKartlar() {
        return kullanilmisKartlar;
    }

    
    public void kartKullanildi(SavasAraci kart) {
        if (!kullanilmisKartlar.contains(kart)) {
            kullanilmisKartlar.add(kart);
        }
    }

   
    public void kartlariSifirla() {
        kullanilmisKartlar.clear();
    }
    
    private void kartlariGoster(ArrayList<SavasAraci> kartlar) {
        System.out.println("\nSeçilebilir Kartlar:");
        for (int i = 0; i < kartlar.size(); i++) {
            SavasAraci kart = kartlar.get(i);
            System.out.println((i+1) + ". " + kart.getSinif() + " - " + kart.getAltSinif() + 
                             " (Dayanıklılık: " + kart.getDayaniklilik() + 
                             ", Vuruş: " + kart.getVurus() + ")");
        }
    }

    public boolean kartlarTukendi() {
        return kartListesi.stream()
            .filter(kart -> !elenenKartlar.contains(kart))
            .allMatch(kart -> kart.getDayaniklilik() <= 0);
    }

    public int getAktifKartSayisi() {
        return (int) kartListesi.stream()
            .filter(kart -> !elenenKartlar.contains(kart) && kart.getDayaniklilik() > 0)
            .count();
    }

    public void kartDurumunuGoster() {
        System.out.println("\n" + oyuncuAdi + " Kart Durumu:");
        System.out.println("Toplam Kart: " + kartListesi.size());
        System.out.println("Aktif Kart: " + getAktifKartSayisi());
        System.out.println("Elenen Kart: " + elenenKartlar.size());
        System.out.println("Kullanılmış Kart: " + kullanilmisKartlar.size());
        
        System.out.println("\nAktif Kartlar:");
        kartListesi.stream()
            .filter(kart -> !elenenKartlar.contains(kart) && kart.getDayaniklilik() > 0)
            .forEach(kart -> System.out.println(kart.getAltSinif() + 
                           " (Dayanıklılık: " + kart.getDayaniklilik() + ")"));
    }
}
package savasKartOyunu;

public class Ucak extends Hava {
   
    private String altSinif;
    private int karaVurusAvantaji;
    
    // Constructor
    public Ucak() {
        super(20, 10, 10);  
        this.altSinif = "Ucak";
        this.karaVurusAvantaji = 10;
    }
    
    
    @Override
    protected String getAltSinif() {
        return altSinif;
    }
    
    @Override
	public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }
    
   
    @Override
    public void kartPuaniGoster() {
        System.out.println("\n=== Uçak Kartı Bilgileri ===");
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Kara Vuruş Avantajı: " + getKaraVurusAvantaji());
    }
    
    @Override
    public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
        setDayaniklilik(getDayaniklilik() - saldiriDegeri);
        setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
       
    }
}
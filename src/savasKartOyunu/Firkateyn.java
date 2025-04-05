package savasKartOyunu;

public class Firkateyn extends Deniz {
    private String altSinif;
    private int havaVurusAvantaji;

    public Firkateyn() {
        super(25, 10, 5);  
        this.altSinif = "Firkateyn";
        this.havaVurusAvantaji = 5;
    }

    @Override
    protected String getAltSinif() {
        return altSinif;
    }

    @Override
	public int getHavaVurusAvantaji() {
        return havaVurusAvantaji;
    }

    @Override
    public void kartPuaniGoster() {
        System.out.println("\n=== Fırkateyn Kartı Bilgileri ===");
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Hava Vuruş Avantajı: " + getHavaVurusAvantaji());
    }

    @Override
    public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
        setDayaniklilik(getDayaniklilik() - saldiriDegeri);
        setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
    }
}


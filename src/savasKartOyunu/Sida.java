package savasKartOyunu;

public class Sida extends Deniz {
    private String altSinif;
    private int havaVurusAvantaji;
    private int karaVurusAvantaji;

    public Sida() {
        super(15, 10, 10);  
        this.altSinif = "Sida";
        this.havaVurusAvantaji = 10;
        this.karaVurusAvantaji = 10;
    }

    @Override
    protected String getAltSinif() {
        return altSinif;
    }

    @Override
	public int getHavaVurusAvantaji() {
        return havaVurusAvantaji;
    }

    public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }

    @Override
    public void kartPuaniGoster() {
        System.out.println("\n=== Sida Kartı Bilgileri ===");
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Hava Vuruş Avantajı: " + getHavaVurusAvantaji());
        System.out.println("Kara Vuruş Avantajı: " + getKaraVurusAvantaji());
    }

    @Override
    public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
        setDayaniklilik(getDayaniklilik() - saldiriDegeri);
        setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
    }
}


package savasKartOyunu;

public class KFS extends Kara {
    private String altSinif;
    private int denizVurusAvantaji;
    private int havaVurusAvantaji;

    public KFS() {
        super(10, 10, 10); 
        this.altSinif = "KFS";
        this.denizVurusAvantaji = 10;
        this.havaVurusAvantaji = 20;
    }

    @Override
    protected String getAltSinif() {
        return altSinif;
    }

    @Override
	public int getDenizVurusAvantaji() {
        return denizVurusAvantaji;
    }

    public int getHavaVurusAvantaji() {
        return havaVurusAvantaji;
    }

    @Override
    public void kartPuaniGoster() {
        System.out.println("\n=== KFS Kartı Bilgileri ===");
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Deniz Vuruş Avantajı: " + getDenizVurusAvantaji());
        System.out.println("Hava Vuruş Avantajı: " + getHavaVurusAvantaji());
    }

    @Override
    public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
        setDayaniklilik(getDayaniklilik() - saldiriDegeri);
        setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
    }
}
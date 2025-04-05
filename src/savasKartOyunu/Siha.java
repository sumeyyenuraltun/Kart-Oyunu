package savasKartOyunu;

public class Siha extends Hava {
    private String altSinif;
    private int karaVurusAvantaji;
    private int denizVurusAvantaji;

    public Siha() {
        super(15, 10, 10);  
        this.altSinif = "Siha";
        this.karaVurusAvantaji = 10;
        this.denizVurusAvantaji = 10;
    }

    @Override
    protected String getAltSinif() {
        return altSinif;
    }

    @Override
	public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }

    public int getDenizVurusAvantaji() {
        return denizVurusAvantaji;
    }

    @Override
    public void kartPuaniGoster() {
        System.out.println("\n=== SİHA Kartı Bilgileri ===");
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Kara Vuruş Avantajı: " + getKaraVurusAvantaji());
        System.out.println("Deniz Vuruş Avantajı: " + getDenizVurusAvantaji());
    }

    @Override
    public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
        setDayaniklilik(getDayaniklilik() - saldiriDegeri);
        setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
    }
}
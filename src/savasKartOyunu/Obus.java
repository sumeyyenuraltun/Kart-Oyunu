package savasKartOyunu;

public class Obus extends Kara {
    private String altSinif;
    private int denizVurusAvantaji;

    public Obus() {
        super(20, 10, 5); 
        this.altSinif = "Obus";
        this.denizVurusAvantaji = 5;
    }

    @Override
    protected String getAltSinif() {
        return altSinif;
    }

    @Override
	public int getDenizVurusAvantaji() {
        return denizVurusAvantaji;
    }

    @Override
    public void kartPuaniGoster() {
        System.out.println("\n=== Obüs Kartı Bilgileri ===");
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Deniz Vuruş Avantajı: " + getDenizVurusAvantaji());
    }

    @Override
    public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
        setDayaniklilik(getDayaniklilik() - saldiriDegeri);
        setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
     
    }
}
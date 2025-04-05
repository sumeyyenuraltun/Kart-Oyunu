package savasKartOyunu;


public abstract class Hava extends SavasAraci {
 private int dayaniklilik;
 private String sinif;
 private int vurus;
 protected int karaVurusAvantaji;

 public Hava(int dayaniklilik, int vurus, int karaVurusAvantaji) {
     super();
     this.dayaniklilik = dayaniklilik;
     this.sinif = "Hava";
     this.vurus = vurus;
     this.karaVurusAvantaji = karaVurusAvantaji;
 }

 @Override
 protected int getDayaniklilik() {
     return dayaniklilik;
 }

 @Override
 protected void setDayaniklilik(int dayaniklilik) {
     this.dayaniklilik = dayaniklilik;
 }

 @Override
 protected String getSinif() {
     return sinif;
 }

 @Override
 protected int getVurus() {
     return vurus;
 }

 public int getKaraVurusAvantaji() {
     return karaVurusAvantaji;
 }
 
 @Override
    public void kartPuaniGoster() {
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş: " + getVurus());
        System.out.println("Seviye Puanı: " + getSeviyePuani());
        System.out.println("Kara Vuruş Avantajı: " + getKaraVurusAvantaji());
    }
    
 @Override
 public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
     this.dayaniklilik -= saldiriDegeri;
     setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
 }
}



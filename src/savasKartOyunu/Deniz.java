
package savasKartOyunu;


public abstract class Deniz extends SavasAraci {
private int dayaniklilik;
private String sinif;
private int vurus;
protected int havaVurusAvantaji;

public Deniz(int dayaniklilik, int vurus, int havaVurusAvantaji) {
   super();
   this.dayaniklilik = dayaniklilik;
   this.sinif = "Deniz";
   this.vurus = vurus;
   this.havaVurusAvantaji = havaVurusAvantaji;
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

public int getHavaVurusAvantaji() {
   return havaVurusAvantaji;
}

@Override
public void kartPuaniGoster() {
    System.out.println("Sınıf: " + getSinif());
    System.out.println("Alt Sınıf: " + getAltSinif());
    System.out.println("Dayanıklılık: " + getDayaniklilik());
    System.out.println("Vuruş: " + getVurus());
    System.out.println("Seviye Puanı: " + getSeviyePuani());
    System.out.println("Hava Vuruş Avantajı: " + getHavaVurusAvantaji());
}

@Override
public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
   this.dayaniklilik -= saldiriDegeri;
   setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
}
}
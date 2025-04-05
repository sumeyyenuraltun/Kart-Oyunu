package savasKartOyunu;


public abstract class Kara extends SavasAraci {
private int dayaniklilik;
private String sinif;
private int vurus;
protected int denizVurusAvantaji;

public Kara(int dayaniklilik, int vurus, int denizVurusAvantaji) {
   super();
   this.dayaniklilik = dayaniklilik;
   this.sinif = "Kara";
   this.vurus = vurus;
   this.denizVurusAvantaji = denizVurusAvantaji;
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

public int getDenizVurusAvantaji() {
   return denizVurusAvantaji;
}

@Override
public void kartPuaniGoster() {
    System.out.println("Sınıf: " + getSinif());
    System.out.println("Alt Sınıf: " + getAltSinif());
    System.out.println("Dayanıklılık: " + getDayaniklilik());
    System.out.println("Vuruş: " + getVurus());
    System.out.println("Seviye Puanı: " + getSeviyePuani());
    System.out.println("Deniz Vuruş Avantajı: " + getDenizVurusAvantaji());
}

@Override
public void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani) {
   this.dayaniklilik -= saldiriDegeri;
   setSeviyePuani(getSeviyePuani() + kazanilanSeviyePuani);
}
}

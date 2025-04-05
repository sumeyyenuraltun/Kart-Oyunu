package savasKartOyunu;

public abstract class SavasAraci {
   
    private int seviyePuani;

    
    protected abstract int getDayaniklilik();
    protected abstract void setDayaniklilik(int dayaniklilik);
    protected abstract String getSinif();
    protected abstract int getVurus();
    protected abstract String getAltSinif();

   
    public SavasAraci() {
        this.seviyePuani = 0; 
    }

  
    public int getSeviyePuani() {
        return seviyePuani;
    }

    public void setSeviyePuani(int seviyePuani) {
        this.seviyePuani = seviyePuani;
    }

   
    public void kartPuaniGoster() {
        System.out.println("Sınıf: " + getSinif());
        System.out.println("Alt Sınıf: " + getAltSinif());
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Seviye Puanı: " + seviyePuani);
        System.out.println("Vuruş: " + getVurus());
    }

   
    public abstract void durumGuncelle(int saldiriDegeri, int kazanilanSeviyePuani);
}
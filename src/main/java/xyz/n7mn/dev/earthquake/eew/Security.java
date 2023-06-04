package xyz.n7mn.dev.earthquake.eew;

public class Security {
    private String realm;
    private String hash;

    public Security(String realm, String hash) {
        this.realm = realm;
        this.hash = hash;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

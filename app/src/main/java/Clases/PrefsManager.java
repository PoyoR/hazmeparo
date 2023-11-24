package Clases;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class PrefsManager {

    private Activity activity;
    private MasterKey masterkey;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public PrefsManager(Activity activity){
        this.activity = activity;


        try {
            masterkey = new MasterKey.Builder(activity, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        try {
            prefs = EncryptedSharedPreferences.create(
                    activity,
                    "prefs",
                    masterkey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

    }

    public void logout(){
        prefs.edit().clear().apply();
    }

    public void guardarValorString(String clave, String valor){
        editor = prefs.edit();
        editor.putString(clave, valor);
        editor.apply();
    }

    public void guardarValorBoolean(String clave, Boolean valor){
        editor = prefs.edit();
        editor.putBoolean(clave, valor);
        editor.apply();
    }

    public String obtenerValorString(String clave){
        return prefs.getString(clave, "");
    }

    public Boolean obtenerValorBoolean(String clave){
        return prefs.getBoolean(clave, false);
    }
}

package Clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.hazmeparo.R;

public class DialogCargando{

    private Activity activity;
    private androidx.appcompat.app.AlertDialog dialog;

    public DialogCargando(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}

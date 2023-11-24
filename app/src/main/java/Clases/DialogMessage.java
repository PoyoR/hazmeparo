package Clases;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.hazmeparo.R;

public class DialogMessage {

    private Activity activity;
    private androidx.appcompat.app.AlertDialog dialog;

    public DialogMessage(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String msg){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.message_dialog, null);
        builder.setView(v);
        builder.setCancelable(false);

        TextView txt = v.findViewById(R.id.txtMsg);
        txt.setText(msg);
        MaterialButton btn = v.findViewById(R.id.btnCerrardialogo);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );

        dialog = builder.create();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }
}

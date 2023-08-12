package rahul.cyntech.mymall;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UpdatePasswordFragment extends Fragment {


    public UpdatePasswordFragment() {
        // Required empty public constructor
    }
    private EditText oldPassword,newPassword,confirmNewPassword;
    private Button updatePassBtn;
    private Dialog loadingDialog;
    private String email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        oldPassword=view.findViewById(R.id.old_password);
        newPassword=view.findViewById(R.id.new_password);
        confirmNewPassword=view.findViewById(R.id.confirm_new_password);
        updatePassBtn=view.findViewById(R.id.update_pass_btn);
//////////loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //////////loading dialog

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        email=getArguments().getString("Email");

        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPass();
            }
        });

        return view;
    }


    private void checkEmailandPass() {
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.custom_error_icon);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
            loadingDialog.show();
            AuthCredential credential= EmailAuthProvider.getCredential(email,oldPassword.getText().toString());
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    oldPassword.setText(null);
                                    newPassword.setText(null);
                                    confirmNewPassword.setText(null);
                                    getActivity().finish();

                                    Toast.makeText(getContext(), "Password successfully updated!", Toast.LENGTH_SHORT).show();
                                }else {
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                    }else {
                        loadingDialog.dismiss();
                        String error=task.getException().getMessage();
                        Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            confirmNewPassword.setError("Password doesn't match !");
        }

    }

    private void checkInputs() {

        if(!TextUtils.isEmpty(oldPassword.getText()) && oldPassword.length()>=8){
            if(!TextUtils.isEmpty(newPassword.getText()) && newPassword.length()>=8){
                if(!TextUtils.isEmpty(confirmNewPassword.getText()) && confirmNewPassword.length()>=8){
                    updatePassBtn.setEnabled(true);
                    updatePassBtn.setTextColor(Color.rgb(255,255,255));
                }else{
                    updatePassBtn.setEnabled(false);
                    updatePassBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                updatePassBtn.setEnabled(false);
                updatePassBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            updatePassBtn.setEnabled(false);
            updatePassBtn.setTextColor(Color.argb(50,255,255,255));
        }

    }

}
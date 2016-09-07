package agadi.enc.e_spectra;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Mr.Robot on 9/2/2016.
 */
public class Database extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

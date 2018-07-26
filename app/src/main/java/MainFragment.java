import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;

/**
 * Created by 171y005 on 2018/07/26.
 */

public class MainFragment extends Fragment {



    @CheckResult
    public static MainFragment createInstance(String name){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("keyname",name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String name ="";
        Bundle args = getArguments();
        if(args != null){
            name = args.getString("keyname");
        }
    }


}

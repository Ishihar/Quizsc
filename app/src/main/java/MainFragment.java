import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by 171y005 on 2018/07/26.
 */

public class MainFragment extends Fragment {



    @CheckResult
    public static void createInstance(String name){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String name ="";
        Bundle args = getArguments();
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);


    }


}

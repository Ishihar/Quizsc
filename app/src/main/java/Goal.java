import android.app.Application;
import java.util.ArrayList;

/**
 * Created by 171y005 on 2018/09/28.
 */
public class Goal extends Application {
        private static final String TAG ="Goal";
        private int Count;
        private ArrayList<String> list;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public void setGlobal(int addcnt){
        Count += addcnt;
    }

    public int getCount(){
        return Count;
    }
}


import java.util.Scanner;
import java.util.HashMap;
public class Ruff {
    int s;

    Ruff()
    {
        s = 10;
    }
    
    class Win{
        int t = 15;
        void update_t()
        {
            t = 2 *t;
        }
    }
    public static void main(String[] args) {
        HashMap<Integer,String> mp = new HashMap<>();
        Ruff r = new Ruff();
        System.out.println(r.s);
        Ruff.Win wi = r.new Win();

        System.out.println(wi.t);
        wi.update_t();

        System.out.println(wi.t);
        mp.put(1, "Tanmaysinh");
        mp.put(3, "Yash");
        mp.put(2, "Krish");
        System.out.println(mp.size());
        System.out.println(mp);

    }
}

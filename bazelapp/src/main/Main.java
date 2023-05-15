import greeting.Greeting;
import org.apache.commons.lang3.StringUtils;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello, World!");
        Greeting.greet();

        String myString = "notemptystring";
        boolean isEmpty = StringUtils.isEmpty(myString);
        System.out.println(isEmpty);
    }
}
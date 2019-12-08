import listeners.MessageListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main{

    public static void main(String[] args) throws LoginException {
        if(args != null && args.length > 0){
            System.out.println("Starting with token : "+args[0]);
            new JDABuilder(args[0])
                    .addEventListeners(new MessageListener())
                    .setActivity(Activity.playing("!team help"))
                    .build();
        }else{
            System.out.println("Token needed");
        }
    }

}
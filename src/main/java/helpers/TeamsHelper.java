package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamsHelper {

    public static List<String> getMembers(List<String> initial, List<String> added, List<String> excluded){
        initial.addAll(added);
        initial.removeAll(excluded);
        return initial;
    }

    public static List<List<String>> compose(List<String> members, int nTeam){
        List<List<String>> teams = new ArrayList<>(nTeam);
        for(int i= 0; i<nTeam; i++){
            teams.add(new ArrayList<>());
        }
        int i = 0;
        Collections.shuffle(members);
        for(String player : members){
            teams.get(i++%nTeam).add(player);
        }
        return teams;
    }

}

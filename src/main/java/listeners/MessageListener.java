package listeners;

import helpers.StringHelper;
import helpers.TeamsHelper;
import models.GenerateArg;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if(!msg.getAuthor().isBot()){
            if(msg.getContentRaw().startsWith("!team help")){
                MessageChannel channel = event.getChannel();
                sendHelpMessage(channel);
            }
            if (msg.getContentRaw().startsWith("!team generate"))
            {
                MessageChannel channel = event.getChannel();
                generateComposition(event, channel, msg);
            }
        }

    }

    private void sendHelpMessage(MessageChannel channel){
        channel.sendMessage("Hello,\n" +
                "This bot generates random teams with players in your vocal room." +
                "\n\n Usage : \n" +
                "!team generate <number of team>\n" +
                "Possible argument : \n" +
                "-e, --exclude <players> : players to exclude from team composition\n" +
                "-i, --include <players>: non-present players to add in team composition\n" +
                "All player in exclude and include arguments must be separated by a coma, and if player name contains space, please add double quote (\" \") around the whole list of player\n" +
                "Example : !team generate 2 --include \"The Dark Knight,Joker\" --exclude \"Delmort,Ytarion\"\n\n" +
                "-- Developed by Ytarion").queue(response -> {});
    }

    private void generateComposition(MessageReceivedEvent event, MessageChannel channel, Message msg){
        List<String> cmdArgs = StringHelper.splitCommand(msg.getContentRaw());
        String[] args = new String[cmdArgs.size()];
        args = StringHelper.splitCommand(msg.getContentRaw()).toArray(args);
        GenerateArg generateArg = new GenerateArg();
        int n;
        if(args.length < 3){
            channel.sendMessage("Some arguments are missing, please use !team help").queue(response -> {});
            return;
        }else if(args.length > 3){
            try{
                generateArg = GenerateArg.fromArgument(Arrays.copyOfRange(args, 3, args.length));
            }catch (Exception ex){
                channel.sendMessage("Invalid command format, please use !team help").queue(response -> {});
                return;
            }
        }
        try{
            n = Integer.parseInt(args[2]);
        }catch (Exception ex){
            channel.sendMessage("Invalid command format, please use !team help").queue(response -> {});
            return;
        }

        try{
            List<Member> members = event.getMember().getVoiceState().getChannel().getMembers();
            List<String> players = TeamsHelper.getMembers(
                    members.stream().map(x -> x.getUser().getName()).collect(Collectors.toList()),
                    generateArg.getInclude() != null ? Arrays.asList(generateArg.getInclude().split(",")) : new ArrayList<>(),
                    generateArg.getExclude() != null ? Arrays.asList(generateArg.getExclude().split(",")) : new ArrayList<>()

            );
            List<List<String>> teams = TeamsHelper.compose(players, n);
            sendTeamMessage(channel, teams);
        }catch(Exception ex){
            channel.sendMessage("You need to be in a Vocal channel to generate team").queue(response -> {});
        }
    }

    private void sendTeamMessage(MessageChannel channel, List<List<String>> teams) {
        int i = 1;
        StringBuilder builder = new StringBuilder();
        for(List<String> team : teams){
            builder.append(String.format("Team %d : %s\n", i++, String.join( ",", team)));
        }
        channel.sendMessage(builder.toString()).queue(r -> {});
    }

}

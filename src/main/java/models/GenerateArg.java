package models;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class GenerateArg {

    @Parameter(
            names = { "--include", "-i" },
            description = "players to includes",
            arity = 1
    )
    private String include;

    @Parameter(
            names = { "--exclude", "-e" },
            description = "players to exclude",
            arity = 1
    )

    private String exclude;

    public static GenerateArg fromArgument(String ...args){
        GenerateArg generateArg = new GenerateArg();
        JCommander cmd = JCommander.newBuilder()
                .addObject(generateArg)
                .build();
        cmd.parse(args);
        return generateArg;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
}

package engine.player;

import java.io.*;
import java.util.Scanner;

public class AgentSaver {

    public static void saveAgent(String path, GameAgent agent){
        try{
            File file = new File(path);
            if(!file.exists())
                file.createNewFile();
            FileWriter fos = new FileWriter(file,false);
            var qFunction = agent.getqFunction();
            qFunction.entrySet().forEach(element->{
                try {
                    fos.write(element.getKey()+" ");
                    for (int i = 0; i < 9; i++) {
                        fos.write(element.getValue()[i].toString()+",");
                    }
                    fos.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameAgent loadAgent(String filepath){
        var agent = new GameAgent();
        Scanner scanner = null;
        try{
            scanner = new Scanner(new FileInputStream(filepath));
            while (scanner.hasNext()){
                String[] states = scanner.nextLine().split(" ");
                var stateHash = Integer.parseInt(states[0]);
                String[] temp = states[1].split(",");
                Double[] turnProbs = new Double[9];
                for (int i = 0; i < turnProbs.length; i++) {
                    turnProbs[i] = Double.parseDouble(temp[i]);
                }
                agent.addState(stateHash, turnProbs);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return agent;
    }
}

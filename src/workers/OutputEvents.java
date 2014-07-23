/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package workers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import setWeightCover.finalSets;

/**
 *
 * @author bickhart
 */
public class OutputEvents {
    protected ArrayList<finalSets> sets;
    protected Path outfile;
    protected Path supportfile;
    protected final boolean debug;
    
    public OutputEvents(ArrayList<? extends finalSets> sets, String outfile, boolean debug){
        this.sets = (ArrayList<finalSets>) sets;
        this.outfile = Paths.get(outfile);
        this.supportfile = Paths.get(outfile + ".sup");
        this.debug = debug;
    }
    
    public void WriteOut (){
        try (BufferedWriter output = Files.newBufferedWriter(outfile, Charset.forName("UTF-8")) ){
            Collections.sort(sets);
            for(finalSets event : this.sets){
                String outLine = join(event.Chr(), String.valueOf(event.Start()), String.valueOf(event.InnerStart()),
                        String.valueOf(event.InnerEnd()), String.valueOf(event.End()), String.valueOf(event.svType),
                        String.valueOf(event.DiscSupport()), String.valueOf(event.SplitSupport()), String.valueOf(event.UnbalancedSplitSupport()),
                        String.valueOf(event.SumFullSupport()));
                output.write(outLine);
                //output.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(OutputEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(debug){
            try(BufferedWriter support = Files.newBufferedWriter(supportfile, Charset.defaultCharset())){
                for(finalSets event : this.sets){
                    
                    support.write(event.getSupportReadStr());
                    support.newLine();
                }
            }catch(IOException ex){
                Logger.getLogger(OutputEvents.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    protected String join (String ... t){
        StringBuilder holder = new StringBuilder();
        for(int i = 0; i < t.length; i++){
            if(i == 0){
                holder.append(t[i]);
            }else{
                holder.append("\t").append(t[i]);
            }
        }
        holder.append("\n");
        return holder.toString();        
    }
}

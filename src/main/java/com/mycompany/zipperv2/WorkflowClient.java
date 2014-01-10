package com.mycompany.zipperv2;

import ca.on.oicr.pde.utilities.workflows.OicrWorkflow;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.seqware.pipeline.workflowV2.model.Command;
import net.sourceforge.seqware.pipeline.workflowV2.model.Job;
import net.sourceforge.seqware.pipeline.workflowV2.model.SqwFile;

public class WorkflowClient extends OicrWorkflow {
    //Instance variables
    private static final Logger logger = Logger.getLogger(WorkflowClient.class.getName());
    
    //workflow parameters
    private String queue = null;
    private String inputFiles = null;
    private String binDir = null;
    private String dataDir = null;
    private Boolean manualOutput= false;
    private String outputZip = "zippedup";
    
    
    //Constructor
    private void WorkflowClient(){
        
        //creates the bin directory and intializs the data directory
        binDir = getWorkflowBaseDir() + "/bin/";
        dataDir = "data/";
        
        //Loads the workflow parameters from the .ini file
        try{
            manualOutput = Boolean.valueOf(getProperty("manual_output"));
            inputFiles = getProperty("input_Files");
            queue = getOptionalProperty("queue" , "");
            
        } catch (Exception ex) {
            
            logger.log(Level.SEVERE, "Expected parameter missing", ex);
        }
        
    }
    
    
    @Override
    public Map<String, SqwFile> setupFiles() {


        // register an input file
        int fileNumber = 0;
        
        //splits up the paths of the individual files and stores them (check config for details)
        for (String inputFilePath : inputFiles.split(",")) {
            SqwFile file = this.createFile("file_in_" + fileNumber++);
            file.setSourcePath(inputFilePath);
            file.setType("pleaseWork");
            file.setIsInput(true);
        }
        return this.getFiles();

      
    }
    
    @Override
    public void setupDirectory() {
        
        WorkflowClient(); //calls the constructor
        addDirectory(dataDir);
    }
    
    @Override
    public void buildWorkflow() {

        Job job = getZipJob();
        job.setMaxMemory("4000");
        job.setQueue(queue);
               

    }
    
    private Job getZipJob(){
        
        //runs the bash command to zip the files
        Job job = getWorkflow().createBashJob("Zip");
        Command command = job.getCommand();
        command.addArgument("zip");
        command.addArgument(dataDir + outputZip);
        
        //Adds the two files that must be zipped
        for ( Map.Entry<String, SqwFile> file : this.getFiles().entrySet() ) {
            command.addArgument(file.getValue().getProvisionedPath());
        }
        
        //creates the output file for the job
        SqwFile sqwOutputFile = createOutputFile(dataDir + outputZip + ".zip", "application/zip-report-bundle", manualOutput);
        job.addFile(sqwOutputFile);
        
        return job;
    }

}
Zipper-Workflow
===============
//Also does testing now too

My first workflow for Seqware. It zips two text files into one zip. Seqware is very confusing and by spending roughly 2 days just trying to write this simple workflow, I seem to have gained a lot of understanding. The two input files are "input1.txt" and "input2.txt" and the final output is in data/zippedup.zip. The file is also uploaded to seqware-resutls when actually run with seqware.

Important files:

WorkflowClient.java --The java file that contains the workflow. The code which knows which files to search for, which files to zip and what the name of the final file should be.

workflow.ini -- The configuration file for the Workflow. Fills in the parameters of the java file

The Java file works by executing bash scripts and a bunch of other seqware black magic.

This workflow is extremely simple, but it's my first so I decided to upload it. When I make more interesting workflows, I'll be sure to update them. Or just check out https://github.com/pipedev

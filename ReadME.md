#TF-IDF implementation in scala using spark

###Bharath Kumar Bandaru

This is a personal project.

Language: Scala
Dataset: 20newsgroup
Works with any dataset.

Assumptions:
1. The documents(multiple files) for the dataset should be in the folder.
2. The folder path is given as input parameter when runing the code.
3. Basic symbols and un-wanted characters were removed. 
 

Requirements:
1. Scala and spark needs to be installed before running the project.

Executing the code
1. Open the command line and go the root directory
2. In the shell enter the command :-  :load "Path to the Tf_Idf.scala file" Eg: :load src/main/scala/Tf_Idf.scala
3. Once the objects are created type the following command to run the program.
    Eg: Tf_idf.main(Array("src/main/resources/20news-18828/alt.atheism"))
4. The application finds the tf_idf factor for words for one class only. In the above command "alt.atheism" dataset is considered as an example
5. The output result is stored in the folder result/output folder.


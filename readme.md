### Options

`-d` or `--nunit-result-dir` is a mandatory parameter, and it should be the directory containing the nunit result xml.
`-o` or `--outputdir` is an optional parameter, and it should be the directory for storing the JSON reports. 

### Usage

Currently, the only option is to build the project yourself. It will be on maven soon.
- Checkout the project.
- Execute the command `mvn clean package` to generate the jar file
- Execute the following command to generate the JSON files:
```
java -jar target/donut-nunit-adapter-1.0-SNAPSHOT-jar-with-dependencies.jar
-d <path_of_nunit_xml_report>
-o <path_of_directory_to_store_JSON_files>
```
### Output Location
```
If the output directory is specified,
	 the JSON files are written in the folder `nunit-reports` in the specified directory
else		 
	if you're executing the jar at the project level
		 the JSON files are written in the folder `nunit-reports` in the current folder
	else
     	 the JSON files are written in the folder `nunit-reports` in the target folder
```



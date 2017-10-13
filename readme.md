![](http://donutreport.github.io/donut/img/Donut-05.png)

[![Build Status](https://travis-ci.org/DonutReport/donut-nunit-adapter.svg?branch=master)](https://travis-ci.org/DonutReport/donut-nunit-adapter)

Donut NUnit adapter is an open source adapter written for the open-source framework [donut](https://github.com/DonutReport/donut) by the teams at [MagenTys](https://magentys.io) & [Mechanical Rock](https://mechanicalrock.io) and is designed to generate gherkin jsons from NUnit xmls.
These gherkin jsons can be processed by donut.

### Options

`-p` or `--nunit-result-xml-path` is a mandatory parameter, and it should be the path of nunit result xml.<br>
`-o` or `--outputdir` is an optional parameter, and it should be the directory for storing the JSON reports. 

### Usage

Currently, the only option is to build the project yourself. It will be on maven soon.
- Checkout the project.
- Execute the command `mvn clean package` to generate the jar file
- Execute the following command to generate the JSON files:
```
java -jar target/donut-nunit-adapter-1.5.1-jar-with-dependencies.jar
-p <path_of_nunit_xml_report>
-o <path_of_directory_to_store_JSON_files>
```
### Output Location
```
If the output directory is specified,
	 the JSON files are written in the folder `nunit-reports` in the specified directory
else		 
	if you're executing the jar at the project level
		 the JSON files are written in the folder `nunit-reports` in the target folder
	else
		 the JSON files are written in the folder `nunit-reports` in the current folder     	 
```

## License

This project is under an MIT license

Powered by: [MagenTys](https://magentys.io) & [Mechanical Rock](https://www.mechanicalrock.io)

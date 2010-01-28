/************************************************************************************************
 * IF WANT TO RUN AGAINT MULTI ojb config file:
 * set resourceHome = '$resource_root' for your project
 * for example for rice: resourceHome = '/java/projects/play/rice-1.1.0/impl/src/main/resources
 * set the ojbMappingPattern to your project's ojb file name pattern
 **********************************************************************************************/

import java.util.ArrayList;

def ojbMappingPattern = ~/.*OJB.*repository.*xml/
def resourceHome = '/java/projects/play/rice-1.1.0/impl/src/main/resources/org/kuali/rice/ken'
def srcHome = '/java/projects/play/rice-1.1.0/impl/src/main/java'
def sourceDirectories = []
def repositories = []
def classes = []
def files = []

def CLEAN = false

def getRespositoryFiles(String resourcejHome, ojbMappingPattern, ArrayList repositories, ArrayList sourceDirectories){
	repositories.clear()
	sourceDirectories.clear()
	
	// local helpers
	def addRepository = { File f -> 
		repositories.add( f.getPath() );
		sourceDirectories.add( f.getParent() )
	}
	
	def dir = new File(resourcejHome)
	
	println 'directoryName='+dir.getPath()
	println 'ojbMappingPattern='+ojbMappingPattern
	
	dir.eachFileMatch(ojbMappingPattern, addRepository)
	dir.eachDirRecurse { File myFile ->
		myFile.eachFileMatch(ojbMappingPattern, addRepository)
	}
	
}

class ClassDescriptor {
	def compoundPrimaryKey = false
	def pkClassIdText = ""
	def cpkFilename = ""
	def tableName
	def className
	def primaryKeys = []
	def fields = [:]
	def referenceDescriptors = [:]
	def collectionDescriptors = [:]
}

def loadClasses(repositories, classes){
	
	repositories.each {
		repository -> 
		println 'Parsing repository file: '+repository.toString()
		def xml = new XmlParser().parse(new File(repository))
		def classDescriptors = xml['class-descriptor']
		
		classDescriptors.each { 
			cd -> 
			//def classDescriptor = new ClassDescriptor()
			println("********get class:\t" + cd.'@class')
			classes.add(cd.'@class')
		}
	}
		
}

def findExistingBOs(srcHome, classes, files){
	classes.each{
		cls->   def file = srcHome + '/'+ cls.replaceAll("\\.", "/") + ".java"
		
		println("************find file:\t" + file)
		
		files.add(file)
		}
	}

def removeAnnotatonLine(files){
	
	def javaFile
	def backupFile
	
	files.each{
		file-> println("************working on file:\t" + file)
		if (new File(file).exists()) {
			javaFile = new File(file)
			backupFile = new File(file + '.backup')		
			def text = ""
			def skip = "false"
			//scan file by line or convert file to list of lines....
			javaFile.eachLine{
				line->
				def cur = line.toString().trim();
				if(!cur.startsWith("@")){
					if(!skip.equals("true")){
						println("******get this line*****\t" + line)
						text = text + "\n" + line.toString();
						}
					skip = "false"
					}
				else{
					if(!cur.endsWith(")")|| cur.endsWith(","))//!Character.isLetter(cur.charAt(cur.length() - 1)))
					{skip = "true"}
					}
			}
			generateFile(file, text);
		}
	}
}

def generateFile(path, text){
	
	def persistFile = new File(path);
	def backupFile = new File(path + '.backup');
	if (persistFile.exists()){
		if (backupFile.exists()){
			backupFile.delete();
		}	
		persistFile.renameTo(backupFile);
		persistFile.delete();
	}
	
	persistFile << text
	persistFile << "\n"
	
}

def addTransient(files){
	
	def javaFile
	def backupFile
	
	files.each{
		file-> println("************working on file:\t" + file)
		if (new File(file).exists()) {
			javaFile = new File(file)
			backupFile = new File(file + '.backup')		
			ArrayList lines = new File(file).readLines()
			def text = ""
			
			lines.each{line->
				println(line.toString())
				def cur = line.toString().trim()
				if((cur.endsWith(";") )&&(cur.startsWith("private") || cur.startsWith("protected")))
				{
					def pre = (lines.get(lines.indexOf(line) - 1)).toString().trim();
					if(!pre.startsWith("@") && !pre.endsWith(")") && !pre.endsWith(",")){
						text = text + "\n" + "@Transient";
						}
				}
				text = text + "\n" + line; 
				}
			generateFile(file, text);
		}
	}
	
	}

getRespositoryFiles(resourceHome, ojbMappingPattern, repositories, sourceDirectories)

println 'Found '+repositories.size().toString()+' OJB mapping files:'
repositories.each {println it}

loadClasses(repositories, classes)

findExistingBOs(srcHome, classes, files )

if(CLEAN)
	removeAnnotatonLine(files)
	
else
	addTransient(files)








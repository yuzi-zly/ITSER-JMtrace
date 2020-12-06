JAVAC = javac
JAR = jar

JAVASRCFILES = $(shell find src -name "*.java")
TMPCLASSFILES = $(shell find com -name "*.class")
CLASSFILES = $(subst $$,\$$, $(TMPCLASSFILES))

JAR_PKG = jmtrace.jar
CLASSPATH = .:libs/asm-9.0-beta.jar 
RUN_PKG = $(shell find test -name "*.jar")

MANIFESTFILE = src/main/MANIFEST.MF
MANIFESTCONST = "Manifest-Version: 1.0\nPremain-Class: com.JMTRACE.Instrument.JMtraceAgent\nCan-Redefine-Classes: true\nCan-Retransform-Classes: true"

default:
	@echo "make compile: Compile source java files in directory 'src/main/java' into class files in directory 'com'"
	@echo "make jar: Pack class files in 'com' and manifest.mf in 'src/main' into $(JAR_PKG)"
	@echo "make run: Run the test jar in directory 'test' with javaagent $(JAR_PKG)"
	@echo "make clean: Remove $(JAR_PKG), directory 'com' , directory 'output', and manifest.mf in 'src/main'"

compile : 
	@echo "		/==============================		COMPILE		==============================/"
	$(JAVAC) -cp $(CLASSPATH) -d . $(JAVASRCFILES) 

manifest:
	@echo $(MANIFESTCONST) > $(MANIFESTFILE)
	@echo "Class-Path: "$(shell pwd)"/libs/asm-9.0-beta.jar" >> $(MANIFESTFILE)

Coutput:
	mkdir -pv output

jar : compile manifest
	@echo "		/==============================		JAR		==============================/"
	$(JAR) cvfm $(JAR_PKG) $(MANIFESTFILE) $(CLASSFILES)

run : Coutput jar
	@echo "		/==============================		RUN		==============================/"
	java -javaagent:$(JAR_PKG) -jar $(RUN_PKG)

clean:
	rm -f $(JAR_PKG)
	rm -rf com
	rm -rf output
	rm -f $(MANIFESTFILE)

.PHONY: compile clean run jar Coutput manifest